package jp.mc.ancientred.starminer.basics.block.render;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

public class BlockDirtExRenderHelper
  implements ISimpleBlockRenderingHandler
{
  public static final int RENDER_TYPE = 4341802;
  
  public int getRenderId() {
    return 4341802;
  }
  
  public void renderInventoryBlock(Block par1Block, int metadata, int modelID, RenderBlocks renderer) {
    Tessellator tessellator = Tessellator.instance;
    par1Block.setBlockBoundsForItemRender();
    renderer.setRenderBoundsFromBlock(par1Block);
    GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
    GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
    tessellator.startDrawingQuads();
    tessellator.setNormal(0.0F, -1.0F, 0.0F);
    renderer.renderFaceYNeg(par1Block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(par1Block, 0, metadata));
    tessellator.draw();
    tessellator.startDrawingQuads();
    tessellator.setNormal(0.0F, 1.0F, 0.0F);
    renderer.renderFaceYPos(par1Block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(par1Block, 1, metadata));
    tessellator.draw();
    tessellator.startDrawingQuads();
    tessellator.setNormal(0.0F, 0.0F, -1.0F);
    renderer.renderFaceZNeg(par1Block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(par1Block, 2, metadata));
    tessellator.draw();
    tessellator.startDrawingQuads();
    tessellator.setNormal(0.0F, 0.0F, 1.0F);
    renderer.renderFaceZPos(par1Block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(par1Block, 3, metadata));
    tessellator.draw();
    tessellator.startDrawingQuads();
    tessellator.setNormal(-1.0F, 0.0F, 0.0F);
    renderer.renderFaceXNeg(par1Block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(par1Block, 4, metadata));
    tessellator.draw();
    tessellator.startDrawingQuads();
    tessellator.setNormal(1.0F, 0.0F, 0.0F);
    renderer.renderFaceXPos(par1Block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(par1Block, 5, metadata));
    tessellator.draw();
    GL11.glTranslatef(0.5F, 0.5F, 0.5F);
  }
  private static Vec3[] vec = new Vec3[60];
  
  public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
    int l, meta = world.getBlockMetadata(x, y, z);
    
    if ((meta & 0x8) == 0) {
      l = Blocks.grass.colorMultiplier(renderer.blockAccess, x, y, z);
    } else {
      l = block.colorMultiplier(renderer.blockAccess, x, y, z);
    } 
    float f = (l >> 16 & 0xFF) / 255.0F;
    float f1 = (l >> 8 & 0xFF) / 255.0F;
    float f2 = (l & 0xFF) / 255.0F;
    
    if (EntityRenderer.anaglyphEnable) {
      
      float f3 = (f * 30.0F + f1 * 59.0F + f2 * 11.0F) / 100.0F;
      float f4 = (f * 30.0F + f1 * 70.0F) / 100.0F;
      float f5 = (f * 30.0F + f2 * 70.0F) / 100.0F;
      f = f3;
      f1 = f4;
      f2 = f5;
    } 
    
    return renderer.renderStandardBlockWithColorMultiplier(block, x, y, z, f, f1, f2);
  }

  public boolean shouldRender3DInInventory(int modelId) {
    return true;
  }
}
