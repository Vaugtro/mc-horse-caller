package com.horsecall.pathfind.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

/**
 * Reimplementation of IconButtonWidget, but without the default widget background on render, because override was not possible
 */
@Environment(value= EnvType.CLIENT)
public class ClickableIconWidget
        extends ButtonWidget {
    protected final Identifier iconTexture;
    protected final int iconU;
    protected final int iconV;
    protected final int iconHoveredVOffset;
    protected final int iconTextureWidth;
    protected final int iconTextureHeight;
    private final int iconXOffset;
    private final int iconYOffset;
    private final int iconWidth;
    private final int iconHeight;

    ClickableIconWidget(Text message, int iconU, int iconV, int iconXOffset, int iconYOffset, int iconHoveredVOffset, int iconWidth, int iconHeight, int iconTextureWidth, int iconTextureHeight, Identifier iconTexture, ButtonWidget.PressAction onPress) {
        super(0, 0, 150, 20, message, onPress, DEFAULT_NARRATION_SUPPLIER);
        this.iconTextureWidth = iconTextureWidth;
        this.iconTextureHeight = iconTextureHeight;
        this.iconU = iconU;
        this.iconV = iconV;
        this.iconHoveredVOffset = iconHoveredVOffset;
        this.iconTexture = iconTexture;
        this.iconXOffset = iconXOffset;
        this.iconYOffset = iconYOffset;
        this.iconWidth = iconWidth;
        this.iconHeight = iconHeight;
    }

    @Override
    public void renderButton(DrawContext context, int mouseX, int mouseY, float delta) {
        this.drawTexture(context, this.iconTexture, this.getIconX(), this.getIconY(), this.iconU, this.iconV, this.iconHoveredVOffset, this.iconWidth, this.iconHeight, this.iconTextureWidth, this.iconTextureHeight);
    }

    @Override
    public void drawMessage(DrawContext context, TextRenderer textRenderer, int color) {
        int j = this.getX() + 2;
        int k = this.getX() + this.getWidth() - this.iconWidth - 6;
        ClickableIconWidget.drawScrollableText(context, textRenderer, this.getMessage(), j, this.getY(), k, this.getY() + this.getHeight(), color);
    }

    private int getIconX() {
        return this.getX() + (this.width / 2 - this.iconWidth / 2) + this.iconXOffset;
    }

    private int getIconY() {
        return this.getY() + this.iconYOffset;
    }

    public static ClickableIconWidget.Builder builder(Text message, Identifier texture, ButtonWidget.PressAction pressAction) {
        return new ClickableIconWidget.Builder(message, texture, pressAction);
    }

    @Environment(value=EnvType.CLIENT)
    public static class Builder {
        private final Text message;
        private final Identifier texture;
        private final ButtonWidget.PressAction pressAction;
        private int u;
        private int v;
        private int hoveredVOffset;
        private int iconWidth;
        private int iconHeight;
        private int textureWidth;
        private int textureHeight;
        private int xOffset;
        private int yOffset;

        public Builder(Text message, Identifier texture, ButtonWidget.PressAction pressAction) {
            this.message = message;
            this.texture = texture;
            this.pressAction = pressAction;
        }

        public ClickableIconWidget.Builder uv(int u, int v) {
            this.u = u;
            this.v = v;
            return this;
        }

        public ClickableIconWidget.Builder xyOffset(int xOffset, int yOffset) {
            this.xOffset = xOffset;
            this.yOffset = yOffset;
            return this;
        }

        public ClickableIconWidget.Builder hoveredVOffset(int hoveredVOffset) {
            this.hoveredVOffset = hoveredVOffset;
            return this;
        }

        public ClickableIconWidget.Builder iconSize(int iconWidth, int iconHeight) {
            this.iconWidth = iconWidth;
            this.iconHeight = iconHeight;
            return this;
        }

        public ClickableIconWidget.Builder textureSize(int textureWidth, int textureHeight) {
            this.textureWidth = textureWidth;
            this.textureHeight = textureHeight;
            return this;
        }

        public ClickableIconWidget build() {
            return new ClickableIconWidget(this.message, this.u, this.v, this.xOffset, this.yOffset, this.hoveredVOffset, this.iconWidth, this.iconHeight, this.textureWidth, this.textureHeight, this.texture, this.pressAction);
        }
    }
}