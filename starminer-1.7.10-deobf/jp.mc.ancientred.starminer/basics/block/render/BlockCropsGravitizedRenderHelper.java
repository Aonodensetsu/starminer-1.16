package jp.mc.ancientred.starminer.basics.block.render;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import jp.mc.ancientred.starminer.basics.block.DirectionConst;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

public class BlockCropsGravitizedRenderHelper
  implements ISimpleBlockRenderingHandler
{
  public static final int RENDER_TYPE = 4341800;
  
  public int getRenderId() {
    return 4341800;
  }

  public void renderInventoryBlock(Block par1Block, int metadata, int modelID, RenderBlocks renderer) {}

  public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
    Tessellator tessellator = Tessellator.instance;
    tessellator.setBrightness(block.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z));
    tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
    
    int dir = DirectionConst.getPlantGravityDirection(world, x, y, z);
    
    int meta = renderer.blockAccess.getBlockMetadata(x, y, z);
    IIcon icon = renderer.getBlockIconFromSideAndMetadata(block, 0, meta);
    if (renderer.hasOverrideBlockTexture())
    {
      icon = renderer.overrideBlockTexture;
    }
    double renX = x;
    double renY = y;
    double renZ = z;
    double verTop = renY + 1.0D - 0.0625D;
    double verBot = renY + 0.0D - 0.0625D;
    int renderType = 0;
    switch (dir) {
      case 1:
        renderType = 0;
        verTop = renY + 0.0D + 0.0625D;
        verBot = renY + 1.0D + 0.0625D;
        break;
      case 0:
        renderType = 0;
        verTop = renY + 1.0D - 0.0625D;
        verBot = renY + 0.0D - 0.0625D;
        break;
      case 3:
        renderType = 1;
        verTop = renX + 0.0D + 0.0625D;
        verBot = renX + 1.0D + 0.0625D;
        break;
      case 2:
        renderType = 1;
        verTop = renX + 1.0D - 0.0625D;
        verBot = renX + 0.0D - 0.0625D;
        break;
      case 5:
        renderType = 2;
        verTop = renZ + 0.0D + 0.0625D;
        verBot = renZ + 1.0D + 0.0625D;
        break;
      case 4:
        renderType = 2;
        verTop = renZ + 1.0D - 0.0625D;
        verBot = renZ + 0.0D - 0.0625D;
        break;
    } 
    
    double minU = icon.getMinU();
    double minV = icon.getMinV();
    double maxU = icon.getMaxU();
    double maxV = icon.getMaxV();

    if (renderType == 0) {
      double xMin = renX + 0.5D - 0.25D;
      double xMax = renX + 0.5D + 0.25D;
      double zMin = renZ + 0.5D - 0.5D;
      double zMax = renZ + 0.5D + 0.5D;
      
      tessellator.addVertexWithUV(xMin, verTop, zMin, minU, minV);
      tessellator.addVertexWithUV(xMin, verBot, zMin, minU, maxV);
      tessellator.addVertexWithUV(xMin, verBot, zMax, maxU, maxV);
      tessellator.addVertexWithUV(xMin, verTop, zMax, maxU, minV);
      
      tessellator.addVertexWithUV(xMin, verTop, zMax, minU, minV);
      tessellator.addVertexWithUV(xMin, verBot, zMax, minU, maxV);
      tessellator.addVertexWithUV(xMin, verBot, zMin, maxU, maxV);
      tessellator.addVertexWithUV(xMin, verTop, zMin, maxU, minV);
      
      tessellator.addVertexWithUV(xMax, verTop, zMax, minU, minV);
      tessellator.addVertexWithUV(xMax, verBot, zMax, minU, maxV);
      tessellator.addVertexWithUV(xMax, verBot, zMin, maxU, maxV);
      tessellator.addVertexWithUV(xMax, verTop, zMin, maxU, minV);
      
      tessellator.addVertexWithUV(xMax, verTop, zMin, minU, minV);
      tessellator.addVertexWithUV(xMax, verBot, zMin, minU, maxV);
      tessellator.addVertexWithUV(xMax, verBot, zMax, maxU, maxV);
      tessellator.addVertexWithUV(xMax, verTop, zMax, maxU, minV);
      xMin = renX + 0.5D - 0.5D;
      xMax = renX + 0.5D + 0.5D;
      zMin = renZ + 0.5D - 0.25D;
      zMax = renZ + 0.5D + 0.25D;
      tessellator.addVertexWithUV(xMin, verTop, zMin, minU, minV);
      tessellator.addVertexWithUV(xMin, verBot, zMin, minU, maxV);
      tessellator.addVertexWithUV(xMax, verBot, zMin, maxU, maxV);
      tessellator.addVertexWithUV(xMax, verTop, zMin, maxU, minV);
      
      tessellator.addVertexWithUV(xMax, verTop, zMin, minU, minV);
      tessellator.addVertexWithUV(xMax, verBot, zMin, minU, maxV);
      tessellator.addVertexWithUV(xMin, verBot, zMin, maxU, maxV);
      tessellator.addVertexWithUV(xMin, verTop, zMin, maxU, minV);
      
      tessellator.addVertexWithUV(xMax, verTop, zMax, minU, minV);
      tessellator.addVertexWithUV(xMax, verBot, zMax, minU, maxV);
      tessellator.addVertexWithUV(xMin, verBot, zMax, maxU, maxV);
      tessellator.addVertexWithUV(xMin, verTop, zMax, maxU, minV);
      
      tessellator.addVertexWithUV(xMin, verTop, zMax, minU, minV);
      tessellator.addVertexWithUV(xMin, verBot, zMax, minU, maxV);
      tessellator.addVertexWithUV(xMax, verBot, zMax, maxU, maxV);
      tessellator.addVertexWithUV(xMax, verTop, zMax, maxU, minV);
    } else if (renderType == 1) {
      double zMin = renZ + 0.5D - 0.25D;
      double zMax = renZ + 0.5D + 0.25D;
      double yMin = renY + 0.5D - 0.5D;
      double yMax = renY + 0.5D + 0.5D;
      
      tessellator.addVertexWithUV(verTop, yMin, zMin, minU, minV);
      tessellator.addVertexWithUV(verBot, yMin, zMin, minU, maxV);
      tessellator.addVertexWithUV(verBot, yMax, zMin, maxU, maxV);
      tessellator.addVertexWithUV(verTop, yMax, zMin, maxU, minV);
      
      tessellator.addVertexWithUV(verTop, yMax, zMin, minU, minV);
      tessellator.addVertexWithUV(verBot, yMax, zMin, minU, maxV);
      tessellator.addVertexWithUV(verBot, yMin, zMin, maxU, maxV);
      tessellator.addVertexWithUV(verTop, yMin, zMin, maxU, minV);
      
      tessellator.addVertexWithUV(verTop, yMax, zMax, minU, minV);
      tessellator.addVertexWithUV(verBot, yMax, zMax, minU, maxV);
      tessellator.addVertexWithUV(verBot, yMin, zMax, maxU, maxV);
      tessellator.addVertexWithUV(verTop, yMin, zMax, maxU, minV);
      
      tessellator.addVertexWithUV(verTop, yMin, zMax, minU, minV);
      tessellator.addVertexWithUV(verBot, yMin, zMax, minU, maxV);
      tessellator.addVertexWithUV(verBot, yMax, zMax, maxU, maxV);
      tessellator.addVertexWithUV(verTop, yMax, zMax, maxU, minV);
      
      zMin = renZ + 0.5D - 0.5D;
      zMax = renZ + 0.5D + 0.5D;
      yMin = renY + 0.5D - 0.25D;
      yMax = renY + 0.5D + 0.25D;
      tessellator.addVertexWithUV(verTop, yMin, zMin, minU, minV);
      tessellator.addVertexWithUV(verBot, yMin, zMin, minU, maxV);
      tessellator.addVertexWithUV(verBot, yMin, zMax, maxU, maxV);
      tessellator.addVertexWithUV(verTop, yMin, zMax, maxU, minV);
      
      tessellator.addVertexWithUV(verTop, yMin, zMax, minU, minV);
      tessellator.addVertexWithUV(verBot, yMin, zMax, minU, maxV);
      tessellator.addVertexWithUV(verBot, yMin, zMin, maxU, maxV);
      tessellator.addVertexWithUV(verTop, yMin, zMin, maxU, minV);
      
      tessellator.addVertexWithUV(verTop, yMax, zMax, minU, minV);
      tessellator.addVertexWithUV(verBot, yMax, zMax, minU, maxV);
      tessellator.addVertexWithUV(verBot, yMax, zMin, maxU, maxV);
      tessellator.addVertexWithUV(verTop, yMax, zMin, maxU, minV);
      
      tessellator.addVertexWithUV(verTop, yMax, zMin, minU, minV);
      tessellator.addVertexWithUV(verBot, yMax, zMin, minU, maxV);
      tessellator.addVertexWithUV(verBot, yMax, zMax, maxU, maxV);
      tessellator.addVertexWithUV(verTop, yMax, zMax, maxU, minV);
    } else if (renderType == 2) {
      double yMin = renY + 0.5D - 0.25D;
      double yMax = renY + 0.5D + 0.25D;
      double xMin = renX + 0.5D - 0.5D;
      double xMax = renX + 0.5D + 0.5D;
      
      tessellator.addVertexWithUV(xMin, yMin, verTop, minU, minV);
      tessellator.addVertexWithUV(xMin, yMin, verBot, minU, maxV);
      tessellator.addVertexWithUV(xMax, yMin, verBot, maxU, maxV);
      tessellator.addVertexWithUV(xMax, yMin, verTop, maxU, minV);
      
      tessellator.addVertexWithUV(xMax, yMin, verTop, minU, minV);
      tessellator.addVertexWithUV(xMax, yMin, verBot, minU, maxV);
      tessellator.addVertexWithUV(xMin, yMin, verBot, maxU, maxV);
      tessellator.addVertexWithUV(xMin, yMin, verTop, maxU, minV);
      
      tessellator.addVertexWithUV(xMax, yMax, verTop, minU, minV);
      tessellator.addVertexWithUV(xMax, yMax, verBot, minU, maxV);
      tessellator.addVertexWithUV(xMin, yMax, verBot, maxU, maxV);
      tessellator.addVertexWithUV(xMin, yMax, verTop, maxU, minV);
      
      tessellator.addVertexWithUV(xMin, yMax, verTop, minU, minV);
      tessellator.addVertexWithUV(xMin, yMax, verBot, minU, maxV);
      tessellator.addVertexWithUV(xMax, yMax, verBot, maxU, maxV);
      tessellator.addVertexWithUV(xMax, yMax, verTop, maxU, minV);
      
      yMin = renY + 0.5D - 0.5D;
      yMax = renY + 0.5D + 0.5D;
      xMin = renX + 0.5D - 0.25D;
      xMax = renX + 0.5D + 0.25D;
      tessellator.addVertexWithUV(xMin, yMin, verTop, minU, minV);
      tessellator.addVertexWithUV(xMin, yMin, verBot, minU, maxV);
      tessellator.addVertexWithUV(xMin, yMax, verBot, maxU, maxV);
      tessellator.addVertexWithUV(xMin, yMax, verTop, maxU, minV);
      
      tessellator.addVertexWithUV(xMin, yMax, verTop, minU, minV);
      tessellator.addVertexWithUV(xMin, yMax, verBot, minU, maxV);
      tessellator.addVertexWithUV(xMin, yMin, verBot, maxU, maxV);
      tessellator.addVertexWithUV(xMin, yMin, verTop, maxU, minV);
      
      tessellator.addVertexWithUV(xMax, yMax, verTop, minU, minV);
      tessellator.addVertexWithUV(xMax, yMax, verBot, minU, maxV);
      tessellator.addVertexWithUV(xMax, yMin, verBot, maxU, maxV);
      tessellator.addVertexWithUV(xMax, yMin, verTop, maxU, minV);
      
      tessellator.addVertexWithUV(xMax, yMin, verTop, minU, minV);
      tessellator.addVertexWithUV(xMax, yMin, verBot, minU, maxV);
      tessellator.addVertexWithUV(xMax, yMax, verBot, maxU, maxV);
      tessellator.addVertexWithUV(xMax, yMax, verTop, maxU, minV);
    } 
    return true;
  }

  public boolean shouldRender3DInInventory(int modelId) {
    return false;
  }
}
