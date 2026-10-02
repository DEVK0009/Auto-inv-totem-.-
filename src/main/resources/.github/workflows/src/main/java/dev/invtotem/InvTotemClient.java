package dev.invtotem;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.item.Items;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.slot.SlotActionType;

public class InvTotemClient implements ClientModInitializer {
    public static boolean enabled = true;
    public static boolean autoClose = false;
    public static int delay = 0; // ticks (20 = 1 sec)

    private static boolean openGui = false;
    private int timer = 0;

    @Override
    public void onInitializeClient() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registry) ->
            dispatcher.register(ClientCommandManager.literal("invtotem").executes(ctx -> {
                openGui = true;
                return 1;
            })));
        ClientTickEvents.END_CLIENT_TICK.register(this::tick);
    }

    private void tick(MinecraftClient mc) {
        if (openGui) {
            openGui = false;
            mc.setScreen(new InvTotemScreen());
            return;
        }
        if (!enabled || mc.player == null || mc.interactionManager == null) return;

        if (!(mc.currentScreen instanceof InventoryScreen)) { timer = 0; return; }
        if (mc.player.getOffHandStack().isOf(Items.TOTEM_OF_UNDYING)) {
            timer = 0;
            if (autoClose) mc.player.closeHandledScreen();
            return;
        }
        if (timer++ < delay) return;

        PlayerScreenHandler h = mc.player.playerScreenHandler;
        int slot = -1;
        for (int i = 9; i <= 44; i++) {
            if (h.getSlot(i).getStack().isOf(Items.TOTEM_OF_UNDYING)) { slot = i; break; }
        }
        if (slot == -1) return;

        mc.interactionManager.clickSlot(h.syncId, slot, 40, SlotActionType.SWAP, mc.player);
        timer = 0;
    }
}
