package jp.mc.ancientred.starminer.basics.block.render;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fluids.BlockFluidBase;
import org.lwjgl.opengl.GL11;

public class BlockLiquieHookRenderHelper
  implements ISimpleBlockRenderingHandler
{
  public static final int RENDER_TYPE = 4341900;
  
  public int getRenderId() {
    return 4341900;
  }
  
  public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
    Tessellator tessellator = Tessellator.instance;
    block.setBlockBoundsForItemRender();
    renderer.setRenderBoundsFromBlock(block);
    GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
    GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
    tessellator.startDrawingQuads();
    tessellator.setNormal(0.0F, -1.0F, 0.0F);
    renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 0, metadata));
    tessellator.draw();
    tessellator.startDrawingQuads();
    tessellator.setNormal(0.0F, 1.0F, 0.0F);
    renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 1, metadata));
    tessellator.draw();
    tessellator.startDrawingQuads();
    tessellator.setNormal(0.0F, 0.0F, -1.0F);
    renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 2, metadata));
    tessellator.draw();
    tessellator.startDrawingQuads();
    tessellator.setNormal(0.0F, 0.0F, 1.0F);
    renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 3, metadata));
    tessellator.draw();
    tessellator.startDrawingQuads();
    tessellator.setNormal(-1.0F, 0.0F, 0.0F);
    renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 4, metadata));
    tessellator.draw();
    tessellator.startDrawingQuads();
    tessellator.setNormal(1.0F, 0.0F, 0.0F);
    renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 5, metadata));
    tessellator.draw();
    GL11.glTranslatef(0.5F, 0.5F, 0.5F);
  }
  private static Vec3[] vec = new Vec3[60];
  
  public boolean renderWorldBlock(IBlockAccess world, int parX, int parY, int parZ, Block block, int modelId, RenderBlocks renderer) {
    Tessellator tessellator = Tessellator.instance;
    int l = block.colorMultiplier(renderer.blockAccess, parX, parY, parZ);
    float f = (l >> 16 & 0xFF) / 255.0F;
    float f1 = (l >> 8 & 0xFF) / 255.0F;
    float f2 = (l & 0xFF) / 255.0F;
    boolean flag = block.shouldSideBeRendered(renderer.blockAccess, parX, parY + 1, parZ, 1);
    boolean flag1 = block.shouldSideBeRendered(renderer.blockAccess, parX, parY - 1, parZ, 0);
    boolean[] aboolean = { block.shouldSideBeRendered(renderer.blockAccess, parX, parY, parZ - 1, 2), block.shouldSideBeRendered(renderer.blockAccess, parX, parY, parZ + 1, 3), block.shouldSideBeRendered(renderer.blockAccess, parX - 1, parY, parZ, 4), block.shouldSideBeRendered(renderer.blockAccess, parX + 1, parY, parZ, 5) };

    if (!flag && !flag1 && !aboolean[0] && !aboolean[1] && !aboolean[2] && !aboolean[3])
    {
      return false;
    }

    boolean flag2 = false;
    float f3 = 0.5F;
    float f4 = 1.0F;
    float f5 = 0.8F;
    float f6 = 0.6F;
    double d0 = 0.0D;
    double d1 = 1.0D;
    int i1 = renderer.blockAccess.getBlockMetadata(parX, parY, parZ);
    double d2 = 1.0D;
    double d3 = 1.0D;
    double d4 = 1.0D;
    double d5 = 1.0D;
    double d6 = 0.0010000000474974513D;

    if (renderer.renderAllFaces || flag) {
      double d7, d8, d9, d10, d11, d12, d13, d14;
      flag2 = true;
      IIcon icon = renderer.getBlockIconFromSideAndMetadata(block, 1, i1);
      float f10 = (float)BlockFluidBase.getFlowDirection(renderer.blockAccess, parX, parY, parZ);

      if (f10 > -999.0F)
      {
        icon = renderer.getBlockIconFromSideAndMetadata(block, 1, i1);
      }
      
      d2 -= d6;
      d3 -= d6;
      d4 -= d6;
      d5 -= d6;

      if (f10 < -999.0F) {
        
        d8 = icon.getInterpolatedU(0.0D);
        d12 = icon.getInterpolatedV(0.0D);
        d7 = d8;
        d11 = icon.getInterpolatedV(16.0D);
        d10 = icon.getInterpolatedU(16.0D);
        d14 = d11;
        d9 = d10;
        d13 = d12;
      }
      else {
        
        float f11 = MathHelper.sin(f10) * 0.25F;
        float f8 = MathHelper.cos(f10) * 0.25F;
        float f7 = 8.0F;
        d8 = icon.getInterpolatedU((8.0F + (-f8 - f11) * 16.0F));
        d12 = icon.getInterpolatedV((8.0F + (-f8 + f11) * 16.0F));
        d7 = icon.getInterpolatedU((8.0F + (-f8 + f11) * 16.0F));
        d11 = icon.getInterpolatedV((8.0F + (f8 + f11) * 16.0F));
        d10 = icon.getInterpolatedU((8.0F + (f8 + f11) * 16.0F));
        d14 = icon.getInterpolatedV((8.0F + (f8 - f11) * 16.0F));
        d9 = icon.getInterpolatedU((8.0F + (f8 - f11) * 16.0F));
        d13 = icon.getInterpolatedV((8.0F + (-f8 - f11) * 16.0F));
      } 
      
      tessellator.setBrightness(block.getMixedBrightnessForBlock(renderer.blockAccess, parX, parY, parZ));
      float f9 = 1.0F;
      tessellator.setColorOpaque_F(f4 * f9 * f, f4 * f9 * f1, f4 * f9 * f2);
      tessellator.addVertexWithUV((parX + 0), parY + d2, (parZ + 0), d8, d12);
      tessellator.addVertexWithUV((parX + 0), parY + d3, (parZ + 1), d7, d11);
      tessellator.addVertexWithUV((parX + 1), parY + d4, (parZ + 1), d10, d14);
      tessellator.addVertexWithUV((parX + 1), parY + d5, (parZ + 0), d9, d13);
    } 
    
    if (renderer.renderAllFaces || flag1) {
      
      tessellator.setBrightness(block.getMixedBrightnessForBlock(renderer.blockAccess, parX, parY - 1, parZ));
      float f11 = 1.0F;
      tessellator.setColorOpaque_F(f3 * f11, f3 * f11, f3 * f11);
      renderer.renderFaceYNeg(block, parX, parY + d6, parZ, renderer.getBlockIconFromSide(block, 0));
      flag2 = true;
    } 
    
    for (int j1 = 0; j1 < 4; j1++) {
      
      int k1 = parX;
      int l1 = parZ;
      
      if (j1 == 0)
      {
        l1 = parZ - 1;
      }
      
      if (j1 == 1)
      {
        l1++;
      }
      
      if (j1 == 2)
      {
        k1 = parX - 1;
      }
      
      if (j1 == 3)
      {
        k1++;
      }
      
      IIcon icon1 = renderer.getBlockIconFromSideAndMetadata(block, 1, i1);
      
      if (renderer.renderAllFaces || aboolean[j1]) {
        double d15, d16, d17, d18, d19, d20;

        if (j1 == 0) {
          
          d15 = d2;
          d17 = d5;
          d16 = parX;
          d18 = (parX + 1);
          d19 = parZ + d6;
          d20 = parZ + d6;
        }
        else if (j1 == 1) {
          
          d15 = d4;
          d17 = d3;
          d16 = (parX + 1);
          d18 = parX;
          d19 = (parZ + 1) - d6;
          d20 = (parZ + 1) - d6;
        }
        else if (j1 == 2) {
          
          d15 = d3;
          d17 = d2;
          d16 = parX + d6;
          d18 = parX + d6;
          d19 = (parZ + 1);
          d20 = parZ;
        }
        else {
          
          d15 = d5;
          d17 = d4;
          d16 = (parX + 1) - d6;
          d18 = (parX + 1) - d6;
          d19 = parZ;
          d20 = (parZ + 1);
        } 
        
        flag2 = true;
        float f12 = icon1.getInterpolatedU(0.0D);
        float f9 = icon1.getInterpolatedU(8.0D);
        float f8 = icon1.getInterpolatedV((1.0D - d15) * 16.0D * 0.5D);
        float f7 = icon1.getInterpolatedV((1.0D - d17) * 16.0D * 0.5D);
        float f13 = icon1.getInterpolatedV(8.0D);
        tessellator.setBrightness(block.getMixedBrightnessForBlock(renderer.blockAccess, k1, parY, l1));
        float f14 = 1.0F;
        
        if (j1 < 2) {
          
          f14 *= f5;
        }
        else {
          
          f14 *= f6;
        } 
        
        tessellator.setColorOpaque_F(f4 * f14 * f, f4 * f14 * f1, f4 * f14 * f2);
        tessellator.addVertexWithUV(d16, parY + d15, d19, f12, f8);
        tessellator.addVertexWithUV(d18, parY + d17, d20, f9, f7);
        tessellator.addVertexWithUV(d18, (parY + 0), d20, f9, f13);
        tessellator.addVertexWithUV(d16, (parY + 0), d19, f12, f13);
      } 
    } 
    
    renderer.renderMinY = d0;
    renderer.renderMaxY = d1;
    return flag2;
  }

  public boolean shouldRender3DInInventory(int modelId) {
    return true;
  }
}
