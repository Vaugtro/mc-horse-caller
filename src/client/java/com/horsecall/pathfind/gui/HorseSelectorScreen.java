package com.horsecall.pathfind.gui;

import com.horsecall.pathfind.widget.ClickableIconWidget;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.*;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.client.gui.widget.IconButtonWidget;

import javax.swing.*;
import java.sql.SQLOutput;

// FIXME: If closed the client, it return ArithmeticException
@Environment(EnvType.CLIENT)
public class HorseSelectorScreen extends Screen {

    // Textures
    Identifier FOREGROUND_TEXTURE = new Identifier("textures/gui/widgets.png");
    Identifier PAGE_FLIP_TEXTURE = new Identifier("textures/gui/book.png");

    // Widgets
    ButtonWidget button1;
    ButtonWidget button2;
    ClickableIconWidget returnBtn, nextBtn;

    TextWidget typeCln, nameCln, distCln;

    // Variables

    float actualScale, maxClientScale;
    int centeredX, centeredY, drawTextureWidth, drawTextureHeight;

    public HorseSelectorScreen() {
        // The parameter is the title of the screen,
        // which will be narrated when you enter the screen.
        super(Text.literal("Horse Selector"));
    }

    @Override
    // TODO: Still need to create the GUI Widgets here
    // TODO: add sprite for each type of horse, donkey and camel. Create tooltip for sprite
    // TODO: Must be set a paginator and page for horses, max 9 instances per page
    protected void init() {

        assert this.client != null;
        // Instaciate variables to set widgets based on the foreground
        setup();

        typeCln = new TextWidget(24, 16, Text.literal("Type"), this.client.textRenderer);
        nameCln = new TextWidget(96, 16, Text.literal("Name"), this.client.textRenderer);
        distCln = new TextWidget(48, 16, Text.literal("Distance"), this.client.textRenderer);

        TextWidget tmpCln = new TextWidget(48, 16, Text.literal("Max"), this.client.textRenderer);
        ButtonWidget tmpBtn = ButtonWidget.builder(Text.literal("Poop"), action -> {
            System.out.println("ur mom");
        }).dimensions(0, 0, 96, 16).build();

        nextBtn = ClickableIconWidget.builder(
                Text.empty(),
                PAGE_FLIP_TEXTURE,
                action -> {
                    System.out.println("Clicou YAY");
                })
                .uv(27, 195)
                .iconSize(18, 12)
                .textureSize(256, 256)
                .hoveredVOffset(-2)
                .build();

        nextBtn.setWidth(18);

        SimplePositioningWidget layout = new SimplePositioningWidget(centeredX + 8, centeredY + 8, drawTextureWidth - 16, drawTextureHeight - 8);

        layout.add(typeCln, new Positioner.Impl().alignTop().alignLeft());
        layout.add(nameCln, new Positioner.Impl().alignTop().alignHorizontalCenter());
        layout.add(distCln, new Positioner.Impl().alignTop().alignRight());
        layout.add(tmpCln, new Positioner.Impl().alignTop().alignRight().relativeY(0.8f));
        layout.add(tmpBtn, new Positioner.Impl().alignTop().alignHorizontalCenter().relativeY(0.8f));
        layout.add(nextBtn, new Positioner.Impl().alignRight().alignBottom());


        layout.refreshPositions();
        layout.forEachChild(this::addDrawableChild);

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

    private void setupScaler(){
        assert this.client != null;

        // Get the max client scale for readapt the foreground
        this.maxClientScale = new SimpleOption.MaxSuppliableIntCallbacks(0, () -> {
            if (!this.client.isRunning()) {
                return 0x7FFFFFFE;
            }
            return client.getWindow().calculateScaleFactor(0, client.forcesUnicodeFont());
        }, 0x7FFFFFFE).maxInclusive();

        // Get the actual scale, if auto (in this case, zero), set to max scale
        this.actualScale = (client.options.getGuiScale().getValue() > 0 ) ? client.options.getGuiScale().getValue() : maxClientScale ;
    }

    private void setupCenterer(){
        this.drawTextureHeight = (int) (((this.height/5)*4) * (float) (this.actualScale/this.maxClientScale));
        this.drawTextureWidth = (int) ((this.width/2) * (float) (this.actualScale/this.maxClientScale));
        this.centeredX = (int) (this.width/2 - this.drawTextureWidth/2);
        this.centeredY = (int) (this.height/2 - this.drawTextureHeight/2);
    }

    private void setup(){
        setupScaler();
        setupCenterer();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {


        // Instaciate variables to centralize the foreground GUI and their respectives widgets
        setup();

        // Darkens the background with default method
        this.renderBackground(context);

        // Draw foreground GUI with 9-sliced mapping
        context.drawNineSlicedTexture(FOREGROUND_TEXTURE, centeredX, centeredY, drawTextureWidth, drawTextureHeight, 4, 4, 22, 22, 1, 23);
        context.drawNineSlicedTexture(FOREGROUND_TEXTURE, centeredX + 4, centeredY + 4, drawTextureWidth - 8, drawTextureHeight - 8, 2, 2, 198, 18, 1, 47);

        // Create shadow for text
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 15, 0xFFFFFF);
        super.render(context, mouseX, mouseY, delta);
    }
}
