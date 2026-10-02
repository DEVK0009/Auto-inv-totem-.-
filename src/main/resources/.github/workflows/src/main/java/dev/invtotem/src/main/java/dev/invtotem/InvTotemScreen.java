package dev.invtotem;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class InvTotemScreen extends Screen {
    private ButtonWidget toggle, close, delayBtn;

    public InvTotemScreen() { super(Text.literal("InvTotem")); }

    @Override
    protected void init() {
        int x = width / 2 - 100, y = height / 2 - 40;
        toggle = addDrawableChild(ButtonWidget.builder(Text.empty(), b -> {
            InvTotemClient.enabled = !InvTotemClient.enabled; refresh();
        }).dimensions(x, y, 200, 20).build());

        close = addDrawableChild(ButtonWidget.builder(Text.empty(), b -> {
            InvTotemClient.autoClose = !InvTotemClient.autoClose; refresh();
        }).dimensions(x, y + 25, 200, 20).build());

        addDrawableChild(ButtonWidget.builder(Text.literal("-"), b -> {
            InvTotemClient.delay = Math.max(0, InvTotemClient.delay - 1); refresh();
        }).dimensions(x, y + 50, 30, 20).build());

        delayBtn = addDrawableChild(ButtonWidget.builder(Text.empty(), b -> {})
            .dimensions(x + 35, y + 50, 130, 20).build());
        delayBtn.active = false;

        addDrawableChild(ButtonWidget.builder(Text.literal("+"), b -> {
            InvTotemClient.delay = Math.min(20, InvTotemClient.delay + 1); refresh();
        }).dimensions(x + 170, y + 50, 30, 20).build());

        addDrawableChild(ButtonWidget.builder(Text.literal("Done"), b -> close())
            .dimensions(x, y + 80, 200, 20).build());
        refresh();
    }

    private void refresh() {
        toggle.setMessage(Text.literal("InvTotem: " + (InvTotemClient.enabled ? "ON" : "OFF")));
        close.setMessage(Text.literal("Auto close inv: " + (InvTotemClient.autoClose ? "ON" : "OFF")));
        delayBtn.setMessage(Text.literal("Delay: " + InvTotemClient.delay + " ticks"));
    }

    @Override
    public void render(DrawContext ctx, int mx, int my, float delta) {
        super.render(ctx, mx, my, delta);
        ctx.drawCenteredTextWithShadow(textRenderer, title, width / 2, height / 2 - 65, 0xFFFFFF);
    }
}
