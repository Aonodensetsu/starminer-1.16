package dev.bluecom.starminer.basics.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.bluecom.starminer.basics.ModContainer;
import dev.bluecom.starminer.basics.common.CommonNetworkHandler;
import dev.bluecom.starminer.basics.common.PacketGravityCoreGUI;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class ScreenGravityCore extends ContainerScreen<ContainerGravityCore> {
	private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(ModContainer.MODID, "textures/gui/gui_star_core.png");
	private TileEntityGravityCore tileEntity;
	int edgeX;
	int edgeY;
	
	public ScreenGravityCore(ContainerGravityCore container, PlayerInventory inventory, ITextComponent title) {
		super(container, inventory, title);
		imageWidth = 176;
		imageHeight = 222;
		TileEntity tile = container.getTile();
		if (tile instanceof TileEntityGravityCore) {
			tileEntity = (TileEntityGravityCore) tile;
		}
	}
	
	@Override
	public void render(@NotNull MatrixStack matrix, int mouseX, int mouseY, float partial) {
		this.renderBackground(matrix);
		super.render(matrix, mouseX, mouseY, partial);
		this.renderTooltip(matrix, mouseX, mouseY);
	}
	
	@Override
	protected void renderLabels(MatrixStack matrix, int mouseX, int mouseY) {
		int displayTop = 8;
		int displayLeft = 44;
		int displayWidth = 54;
		int displayHeight = 50;
		float txtscale = 0.65F;
		
		matrix.scale(txtscale, txtscale, txtscale);
		int roundX = Math.round((displayLeft + (float) displayWidth / 2) / txtscale);
		drawCenteredString(matrix, font, new TranslationTextComponent("screen.starminer.gravityradius"), roundX, Math.round((displayTop+3)/txtscale), Color.WHITE.getRGB());
		drawCenteredString(matrix, font, new TranslationTextComponent("screen.starminer.starradius"), roundX, Math.round((displayTop+(float) displayHeight/2+3)/txtscale), Color.WHITE.getRGB());
		matrix.scale(1/txtscale, 1/txtscale, 1/txtscale);
		
		drawCenteredString(matrix, font, new StringTextComponent(String.valueOf(tileEntity.getGravityRadius())), displayLeft+displayWidth/2, displayTop+displayHeight/4, Color.WHITE.getRGB());
		drawCenteredString(matrix, font, new StringTextComponent(String.valueOf(tileEntity.getStarRadius())), displayLeft+displayWidth/2, displayTop+displayHeight/4*3, Color.WHITE.getRGB());
		this.font.draw(matrix, new TranslationTextComponent("screen.starminer.terraforming"), 8, 60, Color.DARK_GRAY.getRGB());
		this.font.draw(matrix, this.inventory.getDisplayName(), 8, 128, Color.DARK_GRAY.getRGB());
		int arrayY = edgeY+9;
		int leftX = edgeX+6;
		int rightX = leftX+93;
		int arrayX = 17;
		int buttonX = 32;
		int gapX = 2;
		int gapY = 5;
		
		addButton(new Button(leftX, arrayY, arrayX, 20, new TranslationTextComponent("screen.starminer.subtractfive"), button -> {
			PacketGravityCoreGUI packet = new PacketGravityCoreGUI(tileEntity.getBlockPos(), -5, 0, false, false);
			CommonNetworkHandler.sendToServer(packet);
			tileEntity.setChanged();
		}));
		addButton(new Button(leftX+arrayX+gapX, arrayY, arrayX, 20, new TranslationTextComponent("screen.starminer.subtractone"), button -> {
			PacketGravityCoreGUI packet = new PacketGravityCoreGUI(tileEntity.getBlockPos(), -1, 0, false, false);
			CommonNetworkHandler.sendToServer(packet);
			tileEntity.setChanged();
		}));
		addButton(new Button(leftX, arrayY+gapY+20, arrayX, 20, new TranslationTextComponent("screen.starminer.subtractfive"), button -> {
			PacketGravityCoreGUI packet = new PacketGravityCoreGUI(tileEntity.getBlockPos(), 0, -5, false, false);
			CommonNetworkHandler.sendToServer(packet);
			tileEntity.setChanged();
		}));
		addButton(new Button(leftX+arrayX+gapX, arrayY+gapY+20, arrayX, 20, new TranslationTextComponent("screen.starminer.subtractone"), button -> {
			PacketGravityCoreGUI packet = new PacketGravityCoreGUI(tileEntity.getBlockPos(), 0, -1, false, false);
			CommonNetworkHandler.sendToServer(packet);
			tileEntity.setChanged();
		}));
		addButton(new Button(rightX, arrayY, arrayX, 20, new TranslationTextComponent("screen.starminer.addone"), button -> {
			PacketGravityCoreGUI packet = new PacketGravityCoreGUI(tileEntity.getBlockPos(), 1, 0, false, false);
			CommonNetworkHandler.sendToServer(packet);
			tileEntity.setChanged();
		}));
		addButton(new Button(rightX+arrayX+gapX, arrayY, arrayX, 20, new TranslationTextComponent("screen.starminer.addfive"), button -> {
			PacketGravityCoreGUI packet = new PacketGravityCoreGUI(tileEntity.getBlockPos(), 5, 0, false, false);
			CommonNetworkHandler.sendToServer(packet);
			tileEntity.setChanged();
		}));
		addButton(new Button(rightX, arrayY+gapY+20, arrayX, 20, new TranslationTextComponent("screen.starminer.addone"), button -> {
			PacketGravityCoreGUI packet = new PacketGravityCoreGUI(tileEntity.getBlockPos(), 0, 1, false, false);
			CommonNetworkHandler.sendToServer(packet);
			tileEntity.setChanged();
		}));
		addButton(new Button(rightX+arrayX+gapX, arrayY+gapY+20, arrayX, 20, new TranslationTextComponent("screen.starminer.addfive"), button -> {
			PacketGravityCoreGUI packet = new PacketGravityCoreGUI(tileEntity.getBlockPos(), 0, 5, false, false);
			CommonNetworkHandler.sendToServer(packet);
			tileEntity.setChanged();
		}));
		addButton(new Button(rightX+2*arrayX+2*gapX, arrayY, buttonX, 20, null, button -> {
			PacketGravityCoreGUI packet = new PacketGravityCoreGUI(tileEntity.getBlockPos(), 0, 0, true, false);
			CommonNetworkHandler.sendToServer(packet);
			tileEntity.setChanged();
		}) {
			@Override
			public @NotNull ITextComponent getMessage() {
				return tileEntity.getGravityTypeMessage();
			}
		});
		addButton(new Button(rightX+2*arrayX+2*gapX, arrayY+gapY+20, buttonX, 20, null, button -> {
			PacketGravityCoreGUI packet = new PacketGravityCoreGUI(tileEntity.getBlockPos(), 0, 0, false, true);
			CommonNetworkHandler.sendToServer(packet);
			tileEntity.setChanged();
		}) {
			@Override
			public @NotNull ITextComponent getMessage() {
				return tileEntity.getInvTypeMessage();
			}
		});
	}
	
	@SuppressWarnings("deprecation")
	@Override
	protected void renderBg(@NotNull MatrixStack matrix, float partial, int mouseX, int mouseY) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.minecraft.getTextureManager().bind(BACKGROUND_TEXTURE);
		edgeX = (this.width - this.imageWidth) / 2;
		edgeY = (this.height - this.imageHeight) / 2;
		this.blit(matrix, edgeX, edgeY, 0, 0, this.imageWidth, this.imageHeight);
	}
}
