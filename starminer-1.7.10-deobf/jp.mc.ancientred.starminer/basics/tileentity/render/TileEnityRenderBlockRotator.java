package jp.mc.ancientred.starminer.basics.tileentity.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.mc.ancientred.starminer.api.GravityDirection;
import jp.mc.ancientred.starminer.basics.block.BlockRotator;
import jp.mc.ancientred.starminer.basics.tileentity.TileEntityBlockRotator;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class TileEnityRenderBlockRotator
  extends TileEntitySpecialRenderer
{
  private RenderBlocks renderBlocks;
  
  public void renderTileEntityAt(TileEntityBlockRotator tileEntity, double x, double y, double z, float partialTick) {
    Block storedBlock = tileEntity.getStoredBlock();

    if (storedBlock == null || storedBlock == Blocks.air) {
      return;
    }

    EntityClientPlayerMP entityClientPlayerMP = (Minecraft.getMinecraft()).thePlayer;
    ItemStack onHandItemStack = ((EntityPlayer)entityClientPlayerMP).inventory.getCurrentItem();
    if (onHandItemStack == null || !(onHandItemStack.getItem() instanceof jp.mc.ancientred.starminer.basics.item.block.ItemBlockForRotator)) {
      return;
    }
    
    Tessellator tessellatorOrg = Tessellator.instance;
    IBlockAccess blockAccessOrg = this.renderBlocks.blockAccess;
    
    GL11.glPushMatrix();

    GL11.glTranslatef((float)x, (float)y, (float)z);
    GL11.glTranslatef(0.5F, 0.5F, 0.5F);
    GravityDirection gravityDirection = tileEntity.getGravityDirection();
    GL11.glRotatef(-180.0F * gravityDirection.rotX, 1.0F, 0.0F, 0.0F);
    GL11.glRotatef(-180.0F * gravityDirection.rotZ, 0.0F, 0.0F, 1.0F);
    GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

    bindTexture(TextureMap.locationBlocksTexture);
    RenderHelper.disableStandardItemLighting();
    GL11.glBlendFunc(770, 771);
    GL11.glEnable(3042);
    
    if (Minecraft.isAmbientOcclusionEnabled()) {
      GL11.glShadeModel(7425);
    } else {
      GL11.glShadeModel(7424);
    } 
    tessellatorOrg.startDrawingQuads();
    tessellatorOrg.setColorOpaque_F(1.0F, 1.0F, 1.0F);

    tessellatorOrg.setTranslation(-tileEntity.xCoord, -tileEntity.yCoord, -tileEntity.zCoord);

    try {
      this.renderBlocks.setRenderBounds(-0.009999999776482582D, -0.009999999776482582D, -0.009999999776482582D, 1.0099999904632568D, 1.0099999904632568D, 1.0099999904632568D);

      BlockRotator blockSelf = (BlockRotator)tileEntity.getBlockType();
      blockSelf.rotateBlockSelfFlg = true;
      this.renderBlocks.renderFromInside = true;
      this.renderBlocks.renderStandardBlock((Block)blockSelf, tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
      this.renderBlocks.renderFromInside = false;
      this.renderBlocks.renderStandardBlock((Block)blockSelf, tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
      blockSelf.rotateBlockSelfFlg = false;
    }
    finally {
      
      this.renderBlocks.blockAccess = blockAccessOrg;
      tessellatorOrg.setTranslation(0.0D, 0.0D, 0.0D);
    } 
    
    tessellatorOrg.draw();
    RenderHelper.enableStandardItemLighting();
    GL11.glPopMatrix();
  }

  public void onWorldChange(World world) {
    this.renderBlocks = new RenderBlocks((IBlockAccess)world);
  }

  public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float partialTick) {
    renderTileEntityAt((TileEntityBlockRotator)tileEntity, x, y, z, partialTick);
  }
}
