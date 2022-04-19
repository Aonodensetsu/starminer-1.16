package jp.mc.ancientred.starminer.basics.block.render;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

public class BlockGravityWallRenderHelper
  implements ISimpleBlockRenderingHandler
{
  public static final int RENDER_TYPE = 4341804;
  
  public int getRenderId() {
    return 4341804;
  }
  
  public void renderInventoryBlock(Block par1Block, int metadata, int modelID, RenderBlocks renderer) {
    IIcon icon = par1Block.getIcon(0, 0);
    GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
    GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
    double lw = 0.0625D;
    renderer.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, lw, 1.0D);
    Tessellator tessellator = Tessellator.instance;
    tessellator.startDrawingQuads();
    tessellator.setNormal(0.0F, -1.0F, 0.0F);
    renderer.renderFaceYNeg(par1Block, 0.0D, 0.0D, 0.0D, icon);
    tessellator.draw();
    tessellator.startDrawingQuads();
    tessellator.setNormal(0.0F, 1.0F, 0.0F);
    renderer.renderFaceYPos(par1Block, 0.0D, 0.0D, 0.0D, icon);
    tessellator.draw();
    tessellator.startDrawingQuads();
    tessellator.setNormal(0.0F, 0.0F, -1.0F);
    renderer.renderFaceZNeg(par1Block, 0.0D, 0.0D, 0.0D, icon);
    tessellator.draw();
    tessellator.startDrawingQuads();
    tessellator.setNormal(0.0F, 0.0F, 1.0F);
    renderer.renderFaceZPos(par1Block, 0.0D, 0.0D, 0.0D, icon);
    tessellator.draw();
    tessellator.startDrawingQuads();
    tessellator.setNormal(-1.0F, 0.0F, 0.0F);
    renderer.renderFaceXNeg(par1Block, 0.0D, 0.0D, 0.0D, icon);
    tessellator.draw();
    tessellator.startDrawingQuads();
    tessellator.setNormal(1.0F, 0.0F, 0.0F);
    renderer.renderFaceXPos(par1Block, 0.0D, 0.0D, 0.0D, icon);
    tessellator.draw();
    GL11.glTranslatef(0.5F, 0.5F, 0.5F);
  }

  public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
    double lw = 0.0625D;
    double hi = 0.9375D;
    int meta = world.getBlockMetadata(x, y, z);

    switch (meta) {
      case 0:
        renderer.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, lw, 1.0D);
        renderer.renderStandardBlock(block, x, y, z);
        break;
      case 1:
        renderer.setRenderBounds(0.0D, hi, 0.0D, 1.0D, 1.0D, 1.0D);
        renderer.renderStandardBlock(block, x, y, z);
        break;
      case 2:
        renderer.setRenderBounds(0.0D, 0.0D, 0.0D, lw, 1.0D, 1.0D);
        renderer.renderStandardBlock(block, x, y, z);
        break;
      case 3:
        renderer.setRenderBounds(hi, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
        renderer.renderStandardBlock(block, x, y, z);
        break;
      case 4:
        renderer.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, lw);
        renderer.renderStandardBlock(block, x, y, z);
        break;
      case 5:
        renderer.setRenderBounds(0.0D, 0.0D, hi, 1.0D, 1.0D, 1.0D);
        renderer.renderStandardBlock(block, x, y, z);
        break;
    } 

    renderer.clearOverrideBlockTexture();
    return true;
  }

  public boolean shouldRender3DInInventory(int modelId) {
    return true;
  }
}
