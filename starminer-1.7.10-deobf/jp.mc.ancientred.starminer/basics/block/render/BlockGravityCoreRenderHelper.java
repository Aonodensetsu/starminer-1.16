package jp.mc.ancientred.starminer.basics.block.render;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import jp.mc.ancientred.starminer.basics.block.BlockGravityCore;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

public class BlockGravityCoreRenderHelper
  implements ISimpleBlockRenderingHandler
{
  public static final int RENDER_TYPE = 4341803;
  
  public int getRenderId() {
    return 4341803;
  }
  
  public void renderInventoryBlock(Block par1Block, int metadata, int modelID, RenderBlocks renderer) {
    IIcon coreItemIcon = ((BlockGravityCore)par1Block).getIcon(0, 0);
    Tessellator tessellator = Tessellator.instance;
    double lb = 0.40625D;
    double hb = 0.59375D;
    renderer.setOverrideBlockTexture(coreItemIcon);
    for (int k = 0; k < 8; k++) {
      
      if (k == 0) {
        
        renderer.setRenderBounds(0.0D, 0.0D, 0.0D, lb, lb, lb);
      }
      else if (k == 1) {
        
        renderer.setRenderBounds(hb, 0.0D, 0.0D, 1.0D, lb, lb);
      }
      else if (k == 2) {
        
        renderer.setRenderBounds(0.0D, hb, 0.0D, lb, 1.0D, lb);
      }
      else if (k == 3) {
        
        renderer.setRenderBounds(0.0D, 0.0D, hb, lb, lb, 1.0D);
      }
      else if (k == 4) {
        
        renderer.setRenderBounds(hb, hb, 0.0D, 1.0D, 1.0D, lb);
      
      }
      else if (k == 5) {
        
        renderer.setRenderBounds(hb, 0.0D, hb, 1.0D, lb, 1.0D);
      }
      else if (k == 6) {
        
        renderer.setRenderBounds(0.0D, hb, hb, lb, 1.0D, 1.0D);
      } else {
        
        renderer.setRenderBounds(hb, hb, hb, 1.0D, 1.0D, 1.0D);
      } 
      
      GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
      tessellator.startDrawingQuads();
      tessellator.setNormal(0.0F, -1.0F, 0.0F);
      renderer.renderFaceYNeg(par1Block, 0.0D, 0.0D, 0.0D, coreItemIcon);
      tessellator.draw();
      tessellator.startDrawingQuads();
      tessellator.setNormal(0.0F, 1.0F, 0.0F);
      renderer.renderFaceYPos(par1Block, 0.0D, 0.0D, 0.0D, coreItemIcon);
      tessellator.draw();
      tessellator.startDrawingQuads();
      tessellator.setNormal(0.0F, 0.0F, -1.0F);
      renderer.renderFaceZNeg(par1Block, 0.0D, 0.0D, 0.0D, coreItemIcon);
      tessellator.draw();
      tessellator.startDrawingQuads();
      tessellator.setNormal(0.0F, 0.0F, 1.0F);
      renderer.renderFaceZPos(par1Block, 0.0D, 0.0D, 0.0D, coreItemIcon);
      tessellator.draw();
      tessellator.startDrawingQuads();
      tessellator.setNormal(-1.0F, 0.0F, 0.0F);
      renderer.renderFaceXNeg(par1Block, 0.0D, 0.0D, 0.0D, coreItemIcon);
      tessellator.draw();
      tessellator.startDrawingQuads();
      tessellator.setNormal(1.0F, 0.0F, 0.0F);
      renderer.renderFaceXPos(par1Block, 0.0D, 0.0D, 0.0D, coreItemIcon);
      tessellator.draw();
      GL11.glTranslatef(0.5F, 0.5F, 0.5F);
    } 
    
    renderer.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
    renderer.clearOverrideBlockTexture();
  }

  public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
    renderer.renderAllFaces = true;
    double lb = 0.40625D;
    double hb = 0.59375D;
    renderer.renderAllFaces = false;
    renderer.setOverrideBlockTexture(((BlockGravityCore)block).getIcon(0, 0));
    renderer.setRenderBounds(0.0D, 0.0D, 0.0D, lb, lb, lb);
    renderer.renderStandardBlock(block, x, y, z);
    
    renderer.setRenderBounds(hb, 0.0D, 0.0D, 1.0D, lb, lb);
    renderer.renderStandardBlock(block, x, y, z);
    
    renderer.setRenderBounds(0.0D, hb, 0.0D, lb, 1.0D, lb);
    renderer.renderStandardBlock(block, x, y, z);
    
    renderer.setRenderBounds(0.0D, 0.0D, hb, lb, lb, 1.0D);
    renderer.renderStandardBlock(block, x, y, z);
    
    renderer.setRenderBounds(hb, hb, 0.0D, 1.0D, 1.0D, lb);
    renderer.renderStandardBlock(block, x, y, z);
    
    renderer.setRenderBounds(hb, 0.0D, hb, 1.0D, lb, 1.0D);
    renderer.renderStandardBlock(block, x, y, z);
    
    renderer.setRenderBounds(0.0D, hb, hb, lb, 1.0D, 1.0D);
    renderer.renderStandardBlock(block, x, y, z);
    
    renderer.setRenderBounds(hb, hb, hb, 1.0D, 1.0D, 1.0D);
    renderer.renderStandardBlock(block, x, y, z);
    
    renderer.setOverrideBlockTexture(Blocks.lava.getIcon(0, 0));
    renderer.setRenderBounds(0.1875D, 0.1875D, 0.1875D, 0.8125D, 0.875D, 0.8125D);
    renderer.renderStandardBlockWithColorMultiplier(block, x, y, z, 0.2F, 0.3F, 1.0F);
	
	renderer.renderAllFaces = false;
    renderer.clearOverrideBlockTexture();
    return true;
  }

  public boolean shouldRender3DInInventory(int modelId) {
    return true;
  }
}
