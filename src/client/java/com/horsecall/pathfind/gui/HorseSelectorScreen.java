package com.horsecall.pathfind.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.*;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class HorseSelectorScreen extends Screen {
    private int x;
    private int y;
    private int backgroundWidth;
    private int backgroundHeight;

    public HorseSelectorScreen() {
        // The parameter is the title of the screen,
        // which will be narrated when you enter the screen.
        super(Text.literal("Horse Selector"));
    }

    Identifier TEXTURE = new Identifier("textures/gui/widgets.png");
    public ButtonWidget button1;
    public ButtonWidget button2;

    @Override
    // TODO: Still need to create the GUI Widgets here
    protected void init() {
        button1 = ButtonWidget.builder(Text.literal("Button 1"), button -> {
                    System.out.println("You clicked button1!");
                })
                .tooltip(Tooltip.of(Text.literal("Tooltip of button1")))
                .build();
        button2 = ButtonWidget.builder(Text.literal("Button 2"), button -> {
                    System.out.println("You clicked button2!");
                })
                .tooltip(Tooltip.of(Text.literal("Tooltip of button2")))
                .build();

        /*
        GridWidget grid = new GridWidget(width/2, height/3);
        grid.getMainPositioner().marginX(5).marginBottom(4).alignHorizontalCenter();
        GridWidget.Adder gridAdder = grid.createAdder(4);

        gridAdder.add(button1,2);
        gridAdder.add(button2,2);

        grid.refreshPositions();
        grid.forEachChild(this::addDrawableChild);*/
        //WrapperWidget test = new WrapperWidget(100, 200, width/2, height/2);

    }

    @Override
    public boolean shouldPause(){
        return false;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {

        // Instaciate variables to centralize the foreground GUI
        int drawTextureHeight = (height/5)*4;
        int drawTextureWidth = width/3;
        int centeredX = (int) (width/2 - drawTextureWidth/2);
        int centeredY = (int) (height/2 - drawTextureHeight/2);

        // Darkens the background with default method
        this.renderBackground(context);

        // Draw foreground GUI with 9-sliced mapping
        context.drawNineSlicedTexture(TEXTURE, centeredX, centeredY, drawTextureWidth, drawTextureHeight, 4, 4, 22, 22, 1, 23);
        context.drawNineSlicedTexture(TEXTURE, centeredX + 4, centeredY + 4, drawTextureWidth - 8, drawTextureHeight - 8, 2, 2, 198, 18, 1, 47);

        // Create shadow for text
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 15, 0xFFFFFF);
        super.render(context, mouseX, mouseY, delta);
    }
}
