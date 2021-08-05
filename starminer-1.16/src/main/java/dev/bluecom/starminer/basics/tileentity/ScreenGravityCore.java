package dev.bluecom.starminer.basics.tileentity;

import java.awt.Color;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import dev.bluecom.starminer.basics.SMModContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.Button.IPressable;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class ScreenGravityCore extends ContainerScreen<ContainerGravityCore> {
	private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(SMModContainer.MODID, "textures/gui/gui_star_core.png");
	int edgeX;
	int edgeY;
	
	public ScreenGravityCore(ContainerGravityCore container, PlayerInventory inventory, ITextComponent title) {
		super(container, inventory, title);
		imageWidth = 176;
		imageHeight = 222;
	}
	
	@Override
	public void render(MatrixStack matrix, int mouseX, int mouseY, float partial) {
		this.renderBackground(matrix);
		super.render(matrix, mouseX, mouseY, partial);
		this.renderTooltip(matrix, mouseX, mouseY);
	}
	
	@Override
	protected void renderLabels(MatrixStack matrix, int mouseX, int mouseY) {
		this.font.draw(matrix, new TranslationTextComponent("screen.starminer.terraformingtab"), 8, 60, Color.DARK_GRAY.getRGB());
		this.font.draw(matrix, this.inventory.getDisplayName(), 8, 128, Color.DARK_GRAY.getRGB());
		int arrayY = edgeY+9; // top of buttons
		int leftX = edgeX+6; // left of left buttons
		int rightX = leftX+93; // left of right buttons
		int arrayX = 17; // width of number buttons
		int buttonX = 32; // width of function buttons
		int gapX = 2;
		int gapY = 5;
		// gravity subtract
		addButton(new Button(leftX, arrayY, arrayX, 20, new TranslationTextComponent("screen.starminer.subtractfive"), new IPressable() {
			@Override
			public void onPress(Button button) {}
		}));
		addButton(new Button(leftX+arrayX+gapX, arrayY, arrayX, 20, new TranslationTextComponent("screen.starminer.subtractone"), new IPressable() {
			@Override
			public void onPress(Button button) {}
		}));
		// radius subtract
		addButton(new Button(leftX, arrayY+gapY+20, arrayX, 20, new TranslationTextComponent("screen.starminer.subtractfive"), new IPressable() {
			@Override
			public void onPress(Button button) {}
		}));
		addButton(new Button(leftX+arrayX+gapX, arrayY+gapY+20, arrayX, 20, new TranslationTextComponent("screen.starminer.subtractone"), new IPressable() {
			@Override
			public void onPress(Button button) {}
		}));
		// gravity add
		addButton(new Button(rightX, arrayY, arrayX, 20, new TranslationTextComponent("screen.starminer.addone"), new IPressable() {
			@Override
			public void onPress(Button button) {}
		}));
		addButton(new Button(rightX+arrayX+gapX, arrayY, arrayX, 20, new TranslationTextComponent("screen.starminer.addfive"), new IPressable() {
			@Override
			public void onPress(Button button) {}
		}));
		// radius add
		addButton(new Button(rightX, arrayY+gapY+20, arrayX, 20, new TranslationTextComponent("screen.starminer.addone"), new IPressable() {
			@Override
			public void onPress(Button button) {}
		}));
		addButton(new Button(rightX+arrayX+gapX, arrayY+gapY+20, arrayX, 20, new TranslationTextComponent("screen.starminer.addfive"), new IPressable() {
			@Override
			public void onPress(Button button) {}
		}));
		// function buttons
		addButton(new Button(rightX+2*arrayX+2*gapX, arrayY, buttonX, 20, new TranslationTextComponent("screen.starminer.sphere"), new IPressable() {
			@Override
			public void onPress(Button button) {}
		}));
		addButton(new Button(rightX+2*arrayX+2*gapX, arrayY+gapY+20, buttonX, 20, new TranslationTextComponent("screen.starminer.terraforming"), new IPressable() {
			@Override
			public void onPress(Button button) {}
		}));
	}
	
	@SuppressWarnings("deprecation")
	@Override
	protected void renderBg(MatrixStack matrix, float partial, int mouseX, int mouseY) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.minecraft.getTextureManager().bind(BACKGROUND_TEXTURE);
		edgeX = (this.width - this.imageWidth) / 2;
		edgeY = (this.height - this.imageHeight) / 2;
		this.blit(matrix, edgeX, edgeY, 0, 0, this.imageWidth, this.imageHeight);
	}
}
