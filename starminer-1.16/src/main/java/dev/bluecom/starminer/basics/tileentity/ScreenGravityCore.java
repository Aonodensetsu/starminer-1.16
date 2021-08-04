package dev.bluecom.starminer.basics.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import dev.bluecom.starminer.basics.SMModContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class ScreenGravityCore extends ContainerScreen<ContainerGravityCore> {
	private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(SMModContainer.MODID, "textures/gui/gui_star_core.png");
	
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
	}
	
	@SuppressWarnings("deprecation")
	@Override
	protected void renderBg(MatrixStack matrix, float partial, int mouseX, int mouseY) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.minecraft.getTextureManager().bind(BACKGROUND_TEXTURE);
		int edgeSpacingX = (this.width - this.imageWidth) / 2;
		int edgeSpacingY = (this.height - this.imageHeight) / 2;
		this.blit(matrix, edgeSpacingX, edgeSpacingY, 0, 0, this.imageWidth, this.imageHeight);
	}
}
