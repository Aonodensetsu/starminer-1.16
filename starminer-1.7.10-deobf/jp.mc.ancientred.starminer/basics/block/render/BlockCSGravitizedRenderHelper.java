package jp.mc.ancientred.starminer.basics.block.render;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import jp.mc.ancientred.starminer.basics.block.DirectionConst;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

public class BlockCSGravitizedRenderHelper
  implements ISimpleBlockRenderingHandler
{
  public static final int RENDER_TYPE = 4341801;
  
  public int getRenderId() {
    return 4341801;
  }

  public void renderInventoryBlock(Block par1Block, int metadata, int modelID, RenderBlocks renderer) {}

  public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
    Tessellator tessellator = Tessellator.instance;
    tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));
    float f = 1.0F;
    int l = block.colorMultiplier(world, x, y, z);
    float f1 = (l >> 16 & 0xFF) / 255.0F;
    float f2 = (l >> 8 & 0xFF) / 255.0F;
    float f3 = (l & 0xFF) / 255.0F;
    
    if (EntityRenderer.anaglyphEnable) {
      
      float f4 = (f1 * 30.0F + f2 * 59.0F + f3 * 11.0F) / 100.0F;
      float f5 = (f1 * 30.0F + f2 * 70.0F) / 100.0F;
      float f6 = (f1 * 30.0F + f3 * 70.0F) / 100.0F;
      f1 = f4;
      f2 = f5;
      f3 = f6;
    } 
    
    tessellator.setColorOpaque_F(f * f1, f * f2, f * f3);
    double renX = x;
    double renY = y;
    double renZ = z;
    
    if (block == Blocks.tallgrass) {
      
      long i1 = (x * 3129871) ^ x * 116129781L ^ y;
      i1 = i1 * i1 * 42317861L + i1 * 11L;
      renX += (((float)(i1 >> 16L & 0xFL) / 15.0F) - 0.5D) * 0.5D;
      renY += (((float)(i1 >> 20L & 0xFL) / 15.0F) - 1.0D) * 0.2D;
      renZ += (((float)(i1 >> 24L & 0xFL) / 15.0F) - 0.5D) * 0.5D;
    } 
    int meta = world.getBlockMetadata(x, y, z);
    
    IIcon icon = renderer.getBlockIconFromSideAndMetadata(block, 0, meta);
    
    if (renderer.hasOverrideBlockTexture())
    {
      icon = renderer.overrideBlockTexture;
    }
    int renderType = 0;
    double verTop = renY + 1.0D;
    double verBot = renY + 0.0D;
    int dir = DirectionConst.getPlantGravityDirection(world, x, y, z);
    switch (dir) {
      case 1:
        renderType = 0;
        verTop = renY + 0.0D;
        verBot = renY + 1.0D;
        break;
      case 0:
        renderType = 0;
        verTop = renY + 1.0D;
        verBot = renY + 0.0D;
        break;
      case 3:
        renderType = 1;
        verTop = renX + 0.0D;
        verBot = renX + 1.0D;
        break;
      case 2:
        renderType = 1;
        verTop = renX + 1.0D;
        verBot = renX + 0.0D;
        break;
      case 5:
        renderType = 2;
        verTop = renZ + 0.0D;
        verBot = renZ + 1.0D;
        break;
      case 4:
        renderType = 2;
        verTop = renZ + 1.0D;
        verBot = renZ + 0.0D;
        break;
    } 
    double fix = 0.45D;
    double minU = icon.getMinU();
    double minV = icon.getMinV();
    double maxU = icon.getMaxU();
    double maxV = icon.getMaxV();
    
    if (renderType == 0) {
      double xMin = renX + 0.5D - fix;
      double xMax = renX + 0.5D + fix;
      double zMin = renZ + 0.5D - fix;
      double zMax = renZ + 0.5D + fix;
      tessellator.addVertexWithUV(xMin, verTop, zMin, minU, minV);
      tessellator.addVertexWithUV(xMin, verBot, zMin, minU, maxV);
      tessellator.addVertexWithUV(xMax, verBot, zMax, maxU, maxV);
      tessellator.addVertexWithUV(xMax, verTop, zMax, maxU, minV);
      tessellator.addVertexWithUV(xMax, verTop, zMax, minU, minV);
      tessellator.addVertexWithUV(xMax, verBot, zMax, minU, maxV);
      tessellator.addVertexWithUV(xMin, verBot, zMin, maxU, maxV);
      tessellator.addVertexWithUV(xMin, verTop, zMin, maxU, minV);
      tessellator.addVertexWithUV(xMin, verTop, zMax, minU, minV);
      tessellator.addVertexWithUV(xMin, verBot, zMax, minU, maxV);
      tessellator.addVertexWithUV(xMax, verBot, zMin, maxU, maxV);
      tessellator.addVertexWithUV(xMax, verTop, zMin, maxU, minV);
      tessellator.addVertexWithUV(xMax, verTop, zMin, minU, minV);
      tessellator.addVertexWithUV(xMax, verBot, zMin, minU, maxV);
      tessellator.addVertexWithUV(xMin, verBot, zMax, maxU, maxV);
      tessellator.addVertexWithUV(xMin, verTop, zMax, maxU, minV);
    } 
    if (renderType == 1) {
      double zMin = renZ + 0.5D - fix;
      double zMax = renZ + 0.5D + fix;
      double yMin = renY + 0.5D - fix;
      double yMax = renY + 0.5D + fix;
      tessellator.addVertexWithUV(verTop, yMin, zMin, minU, minV);
      tessellator.addVertexWithUV(verBot, yMin, zMin, minU, maxV);
      tessellator.addVertexWithUV(verBot, yMax, zMax, maxU, maxV);
      tessellator.addVertexWithUV(verTop, yMax, zMax, maxU, minV);
      tessellator.addVertexWithUV(verTop, yMax, zMax, minU, minV);
      tessellator.addVertexWithUV(verBot, yMax, zMax, minU, maxV);
      tessellator.addVertexWithUV(verBot, yMin, zMin, maxU, maxV);
      tessellator.addVertexWithUV(verTop, yMin, zMin, maxU, minV);
      tessellator.addVertexWithUV(verTop, yMax, zMin, minU, minV);
      tessellator.addVertexWithUV(verBot, yMax, zMin, minU, maxV);
      tessellator.addVertexWithUV(verBot, yMin, zMax, maxU, maxV);
      tessellator.addVertexWithUV(verTop, yMin, zMax, maxU, minV);
      tessellator.addVertexWithUV(verTop, yMin, zMax, minU, minV);
      tessellator.addVertexWithUV(verBot, yMin, zMax, minU, maxV);
      tessellator.addVertexWithUV(verBot, yMax, zMin, maxU, maxV);
      tessellator.addVertexWithUV(verTop, yMax, zMin, maxU, minV);
    } 
    if (renderType == 2) {
      double yMin = renY + 0.5D - fix;
      double yMax = renY + 0.5D + fix;
      double xMin = renX + 0.5D - fix;
      double xMax = renX + 0.5D + fix;
      tessellator.addVertexWithUV(xMin, yMin, verTop, minU, minV);
      tessellator.addVertexWithUV(xMin, yMin, verBot, minU, maxV);
      tessellator.addVertexWithUV(xMax, yMax, verBot, maxU, maxV);
      tessellator.addVertexWithUV(xMax, yMax, verTop, maxU, minV);
      tessellator.addVertexWithUV(xMax, yMax, verTop, minU, minV);
      tessellator.addVertexWithUV(xMax, yMax, verBot, minU, maxV);
      tessellator.addVertexWithUV(xMin, yMin, verBot, maxU, maxV);
      tessellator.addVertexWithUV(xMin, yMin, verTop, maxU, minV);
      tessellator.addVertexWithUV(xMax, yMin, verTop, minU, minV);
      tessellator.addVertexWithUV(xMax, yMin, verBot, minU, maxV);
      tessellator.addVertexWithUV(xMin, yMax, verBot, maxU, maxV);
      tessellator.addVertexWithUV(xMin, yMax, verTop, maxU, minV);
      tessellator.addVertexWithUV(xMin, yMax, verTop, minU, minV);
      tessellator.addVertexWithUV(xMin, yMax, verBot, minU, maxV);
      tessellator.addVertexWithUV(xMax, yMin, verBot, maxU, maxV);
      tessellator.addVertexWithUV(xMax, yMin, verTop, maxU, minV);
    } 
    
    return true;
  }
  
  public boolean shouldRender3DInInventory(int modelId) {
    return false;
  }
}
