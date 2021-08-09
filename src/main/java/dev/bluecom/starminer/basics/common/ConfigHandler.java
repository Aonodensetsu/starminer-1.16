package dev.bluecom.starminer.basics.common;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.bluecom.starminer.api.GravityCapability;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.list.OptionsRowList;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class ConfigHandler extends Screen {
    private static final int TITLE_Y = 8;
    private static final int OPTIONS_TOP_Y = 24;
    private static final int OPTIONS_BOTTOM_Y = 32;
    private static final int OPTIONS_HEIGHT = 25;
    private static final int BUTTON_WIDTH = 200;
    private static final int BUTTON_HEIGHT = 20;
    private static final int DONE_BUTTON_TOP_OFFSET = 26;

    private OptionsRowList optionsRowList;

    @Override
    protected void init() {
        this.optionsRowList = new OptionsRowList(this.minecraft, this.width, this.height, OPTIONS_TOP_Y, this.height-OPTIONS_BOTTOM_Y, OPTIONS_HEIGHT);
        // add options here
        this.children.add(this.optionsRowList);
        this.addButton(new Button((this.width-BUTTON_WIDTH)/2, this.height-DONE_BUTTON_TOP_OFFSET, BUTTON_WIDTH, BUTTON_HEIGHT, new StringTextComponent(I18n.get("gui.done")), button -> this.onClose()));
    }

    public ConfigHandler() {
        super(new TranslationTextComponent("screen.starminer.config"));
    }

    @Override
    public void render(MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrix);
        drawCenteredString(matrix, this.font, this.title.getString(), this.width/2, TITLE_Y, 0xffffff);
        super.render(matrix, mouseX, mouseY, partialTicks);
    }

    // TODO list of options to be added
    public static double transitionAnimationRotationSpeed = 1.5; // from 1 to 1000
    public static double transitionAnimationRotationLength = GravityCapability.UPDATE_AFTER_TICKS / transitionAnimationRotationSpeed;
    public static double transitionAnimationRotationEnd = GravityCapability.UPDATE_AFTER_TICKS - transitionAnimationRotationLength;
}
