package jp.mc.ancientred.starminer.basics.block.render;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import jp.mc.ancientred.starminer.basics.common.VecUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCauldron;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;

public class BlockManBazookaRenderHelper
  implements ISimpleBlockRenderingHandler
{
  public static final int RENDER_TYPE = 4341806;
  
  public int getRenderId() {
    return 4341806;
  }

  public void renderInventoryBlock(Block par1Block, int metadata, int modelID, RenderBlocks renderer) {}
  
  private static Vec3[] vec = new Vec3[60];
  public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
    int i;
    double mainHight = 1.2D;
    
    double tNTHight = 0.4D;
    double sleeveHight = 0.1D;
    double bodyHight = mainHight - sleeveHight;
    
    double marginLw = 0.1D;
    double marginHi = 1.0D - marginLw;
    double inbound = 0.1D;
    int meta = world.getBlockMetadata(x, y, z);
    
    double lw = 0.2D;
    double hi = 1.0D - lw;
    float roatateX = 0.0F;
    float roatateZ = 0.0F;

    
    IIcon iconCauldron = BlockCauldron.getCauldronIcon("inner");
    IIcon iconTnt = Blocks.tnt.getIcon(2, 0);
    IIcon iconIron = Blocks.nether_brick.getIcon(0, 0);
    float cDRNmaxU = iconCauldron.getMaxU();
    float cDRNmaxV = iconCauldron.getMaxV();
    float cDRNminV = iconCauldron.getMinV();
    float cDRNminU = iconCauldron.getMinU();
    
    float tNTmaxU = iconTnt.getMaxU();
    float tNTmaxV = iconTnt.getInterpolatedV(2.0D);
    float tNTminU = iconTnt.getMinU();
    float tNTminV = iconTnt.getInterpolatedV(4.0D);
    
    float iRONmaxU = iconIron.getMaxU();
    float iRONmaxV = iconIron.getInterpolatedV(2.0D);
    float iRONminU = iconIron.getMinU();
    float iRONminV = iconIron.getInterpolatedV(4.0D);
    
    Tessellator tes = Tessellator.instance;
    int brightness = block.getMixedBrightnessForBlock(world, x, y, z);
    tes.setBrightness(brightness);
    tes.setColorOpaque_F(1.0F, 1.0F, 1.0F);
    
    double xMin = marginLw + inbound;
    double xMax = marginHi - inbound;
    
    int vecCount = 0;
    vec[vecCount++] = VecUtils.createVec3(xMax, tNTHight, marginHi);
    vec[vecCount++] = VecUtils.createVec3(xMin, bodyHight, marginHi);
    vec[vecCount++] = VecUtils.createVec3(xMax, 0.0D, marginHi);
    vec[vecCount++] = VecUtils.createVec3(xMin, tNTHight, marginHi);
    vec[vecCount++] = VecUtils.createVec3(xMax, bodyHight, marginHi);
    vec[vecCount++] = VecUtils.createVec3(xMin, mainHight, marginHi);
    
    vec[vecCount++] = VecUtils.createVec3(xMax, tNTHight, marginLw);
    vec[vecCount++] = VecUtils.createVec3(xMin, bodyHight, marginLw);
    vec[vecCount++] = VecUtils.createVec3(xMax, 0.0D, marginLw);
    vec[vecCount++] = VecUtils.createVec3(xMin, tNTHight, marginLw);
    vec[vecCount++] = VecUtils.createVec3(xMax, bodyHight, marginLw);
    vec[vecCount++] = VecUtils.createVec3(xMin, mainHight, marginLw);
    
    double zMin = marginLw + inbound;
    double zMax = marginHi - inbound;
    
    vec[vecCount++] = VecUtils.createVec3(marginLw, tNTHight, zMax);
    vec[vecCount++] = VecUtils.createVec3(marginLw, bodyHight, zMin);
    vec[vecCount++] = VecUtils.createVec3(marginLw, 0.0D, zMax);
    vec[vecCount++] = VecUtils.createVec3(marginLw, tNTHight, zMin);
    vec[vecCount++] = VecUtils.createVec3(marginLw, bodyHight, zMax);
    vec[vecCount++] = VecUtils.createVec3(marginLw, mainHight, zMin);
    
    vec[vecCount++] = VecUtils.createVec3(marginHi, tNTHight, zMax);
    vec[vecCount++] = VecUtils.createVec3(marginHi, bodyHight, zMin);
    vec[vecCount++] = VecUtils.createVec3(marginHi, 0.0D, zMax);
    vec[vecCount++] = VecUtils.createVec3(marginHi, tNTHight, zMin);
    vec[vecCount++] = VecUtils.createVec3(marginHi, bodyHight, zMax);
    vec[vecCount++] = VecUtils.createVec3(marginHi, mainHight, zMin);
    xMin = marginLw;
    xMax = marginLw + inbound;
    zMin = marginHi - inbound;
    zMax = marginHi;
    
    vec[vecCount++] = VecUtils.createVec3(xMax, tNTHight, zMax);
    vec[vecCount++] = VecUtils.createVec3(xMin, bodyHight, zMin);
    vec[vecCount++] = VecUtils.createVec3(xMax, 0.0D, zMax);
    vec[vecCount++] = VecUtils.createVec3(xMin, tNTHight, zMin);
    vec[vecCount++] = VecUtils.createVec3(xMax, bodyHight, zMax);
    vec[vecCount++] = VecUtils.createVec3(xMin, mainHight, zMin);
    xMin = marginHi - inbound;
    xMax = marginHi;
    zMin = marginHi - inbound;
    zMax = marginHi;
    
    vec[vecCount++] = VecUtils.createVec3(xMax, tNTHight, zMin);
    vec[vecCount++] = VecUtils.createVec3(xMin, bodyHight, zMax);
    vec[vecCount++] = VecUtils.createVec3(xMax, 0.0D, zMin);
    vec[vecCount++] = VecUtils.createVec3(xMin, tNTHight, zMax);
    vec[vecCount++] = VecUtils.createVec3(xMax, bodyHight, zMin);
    vec[vecCount++] = VecUtils.createVec3(xMin, mainHight, zMax);
    
    xMin = marginLw;
    xMax = marginLw + inbound;
    zMin = marginLw;
    zMax = marginLw + inbound;
    
    vec[vecCount++] = VecUtils.createVec3(xMax, tNTHight, zMin);
    vec[vecCount++] = VecUtils.createVec3(xMin, bodyHight, zMax);
    vec[vecCount++] = VecUtils.createVec3(xMax, 0.0D, zMin);
    vec[vecCount++] = VecUtils.createVec3(xMin, tNTHight, zMax);
    vec[vecCount++] = VecUtils.createVec3(xMax, bodyHight, zMin);
    vec[vecCount++] = VecUtils.createVec3(xMin, mainHight, zMax);
    
    xMin = marginHi - inbound;
    xMax = marginHi;
    zMin = marginLw;
    zMax = marginLw + inbound;
    
    vec[vecCount++] = VecUtils.createVec3(xMax, tNTHight, zMax);
    vec[vecCount++] = VecUtils.createVec3(xMin, bodyHight, zMin);
    vec[vecCount++] = VecUtils.createVec3(xMax, 0.0D, zMax);
    vec[vecCount++] = VecUtils.createVec3(xMin, tNTHight, zMin);
    vec[vecCount++] = VecUtils.createVec3(xMax, bodyHight, zMax);
    vec[vecCount++] = VecUtils.createVec3(xMin, mainHight, zMin);

    double tntSize = 0.35D;

    switch (meta) {
      case 0:
        for (i = 0; i < vecCount; i += 2) {
          float maxU, maxV, minU, minV; int A = i, B = i + 1;
          (vec[A]).xCoord += x; (vec[A]).yCoord += y; (vec[A]).zCoord += z;
          (vec[B]).xCoord += x; (vec[B]).yCoord += y; (vec[B]).zCoord += z;
          
          if (i % 6 == 0) {
            maxU = cDRNmaxU;
            maxV = cDRNmaxV;
            minU = cDRNminU;
            minV = cDRNminV;
          } else if (i % 6 == 2) {
            maxU = tNTmaxU;
            maxV = tNTmaxV;
            minU = tNTminU;
            minV = tNTminV;
          } else {
            maxU = iRONmaxU;
            maxV = iRONmaxV;
            minU = iRONminU;
            minV = iRONminV;
          } 
          tes.addVertexWithUV((vec[A]).xCoord, (vec[A]).yCoord, (vec[A]).zCoord, maxU, maxV);
          tes.addVertexWithUV((vec[A]).xCoord, (vec[B]).yCoord, (vec[A]).zCoord, maxU, minV);
          tes.addVertexWithUV((vec[B]).xCoord, (vec[B]).yCoord, (vec[B]).zCoord, minU, minV);
          tes.addVertexWithUV((vec[B]).xCoord, (vec[A]).yCoord, (vec[B]).zCoord, minU, maxV);
          
          tes.addVertexWithUV((vec[B]).xCoord, (vec[A]).yCoord, (vec[B]).zCoord, maxU, maxV);
          tes.addVertexWithUV((vec[B]).xCoord, (vec[B]).yCoord, (vec[B]).zCoord, maxU, minV);
          tes.addVertexWithUV((vec[A]).xCoord, (vec[B]).yCoord, (vec[A]).zCoord, minU, minV);
          tes.addVertexWithUV((vec[A]).xCoord, (vec[A]).yCoord, (vec[A]).zCoord, minU, maxV);
        } 
        renderer.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, lw, 1.0D);
        renderer.setOverrideBlockTexture(Blocks.stonebrick.getIcon(0, 0));
        renderer.renderStandardBlock(block, x, y, z);
        
        renderer.setOverrideBlockTexture(Blocks.tnt.getIcon(2, 0));
        renderer.setRenderBounds(0.5D - tntSize, 0.05D, 0.5D - tntSize, 0.5D + tntSize, 0.05D + tntSize, 0.5D + tntSize);
        renderer.renderStandardBlock(block, x, y, z);
        break;
      
      case 1:
        for (i = 0; i < vecCount; i += 2) {
          float maxU, maxV, minU, minV; int A = i, B = i + 1;
          (vec[A]).yCoord = 1.0D - (vec[A]).yCoord;
          (vec[B]).yCoord = 1.0D - (vec[B]).yCoord;
          
          (vec[A]).xCoord += x; (vec[A]).yCoord += y; (vec[A]).zCoord += z;
          (vec[B]).xCoord += x; (vec[B]).yCoord += y; (vec[B]).zCoord += z;
          
          if (i % 6 == 0) {
            maxU = cDRNmaxU;
            maxV = cDRNmaxV;
            minU = cDRNminU;
            minV = cDRNminV;
          } else if (i % 6 == 2) {
            maxU = tNTmaxU;
            maxV = tNTmaxV;
            minU = tNTminU;
            minV = tNTminV;
          } else {
            maxU = iRONmaxU;
            maxV = iRONmaxV;
            minU = iRONminU;
            minV = iRONminV;
          } 
          tes.addVertexWithUV((vec[A]).xCoord, (vec[A]).yCoord, (vec[A]).zCoord, maxU, maxV);
          tes.addVertexWithUV((vec[A]).xCoord, (vec[B]).yCoord, (vec[A]).zCoord, maxU, minV);
          tes.addVertexWithUV((vec[B]).xCoord, (vec[B]).yCoord, (vec[B]).zCoord, minU, minV);
          tes.addVertexWithUV((vec[B]).xCoord, (vec[A]).yCoord, (vec[B]).zCoord, minU, maxV);
          
          tes.addVertexWithUV((vec[B]).xCoord, (vec[A]).yCoord, (vec[B]).zCoord, maxU, maxV);
          tes.addVertexWithUV((vec[B]).xCoord, (vec[B]).yCoord, (vec[B]).zCoord, maxU, minV);
          tes.addVertexWithUV((vec[A]).xCoord, (vec[B]).yCoord, (vec[A]).zCoord, minU, minV);
          tes.addVertexWithUV((vec[A]).xCoord, (vec[A]).yCoord, (vec[A]).zCoord, minU, maxV);
        } 
        renderer.setRenderBounds(0.0D, hi, 0.0D, 1.0D, 1.0D, 1.0D);
        renderer.setOverrideBlockTexture(Blocks.stonebrick.getIcon(0, 0));
        renderer.renderStandardBlock(block, x, y, z);
        
        renderer.setOverrideBlockTexture(Blocks.tnt.getIcon(2, 0));
        renderer.setRenderBounds(0.5D - tntSize, 0.95D - tntSize, 0.5D - tntSize, 0.5D + tntSize, 0.95D, 0.5D + tntSize);
        renderer.renderStandardBlock(block, x, y, z);
        break;
      
      case 2:
        for (i = 0; i < vecCount; i += 2) {
          float maxU, maxV, minU, minV; int A = i, B = i + 1;
          double swap = (vec[A]).xCoord;
          (vec[A]).xCoord = (vec[A]).yCoord;
          (vec[A]).yCoord = swap;
          
          swap = (vec[B]).xCoord;
          (vec[B]).xCoord = (vec[B]).yCoord;
          (vec[B]).yCoord = swap;
          
          (vec[A]).xCoord += x; (vec[A]).yCoord += y; (vec[A]).zCoord += z;
          (vec[B]).xCoord += x; (vec[B]).yCoord += y; (vec[B]).zCoord += z;
          
          if (i % 6 == 0) {
            maxU = cDRNmaxU;
            maxV = cDRNmaxV;
            minU = cDRNminU;
            minV = cDRNminV;
          } else if (i % 6 == 2) {
            maxU = tNTmaxU;
            maxV = tNTmaxV;
            minU = tNTminU;
            minV = tNTminV;
          } else {
            maxU = iRONmaxU;
            maxV = iRONmaxV;
            minU = iRONminU;
            minV = iRONminV;
          } 
          tes.addVertexWithUV((vec[A]).xCoord, (vec[A]).yCoord, (vec[A]).zCoord, maxU, maxV);
          tes.addVertexWithUV((vec[B]).xCoord, (vec[A]).yCoord, (vec[A]).zCoord, maxU, minV);
          tes.addVertexWithUV((vec[B]).xCoord, (vec[B]).yCoord, (vec[B]).zCoord, minU, minV);
          tes.addVertexWithUV((vec[A]).xCoord, (vec[B]).yCoord, (vec[B]).zCoord, minU, maxV);
          
          tes.addVertexWithUV((vec[A]).xCoord, (vec[B]).yCoord, (vec[B]).zCoord, maxU, maxV);
          tes.addVertexWithUV((vec[B]).xCoord, (vec[B]).yCoord, (vec[B]).zCoord, maxU, minV);
          tes.addVertexWithUV((vec[B]).xCoord, (vec[A]).yCoord, (vec[A]).zCoord, minU, minV);
          tes.addVertexWithUV((vec[A]).xCoord, (vec[A]).yCoord, (vec[A]).zCoord, minU, maxV);
        } 
        renderer.setRenderBounds(0.0D, 0.0D, 0.0D, lw, 1.0D, 1.0D);
        renderer.setOverrideBlockTexture(Blocks.stonebrick.getIcon(0, 0));
        renderer.renderStandardBlock(block, x, y, z);
        
        renderer.setOverrideBlockTexture(Blocks.tnt.getIcon(2, 0));
        renderer.setRenderBounds(0.05D, 0.5D - tntSize, 0.5D - tntSize, 0.05D + tntSize, 0.5D + tntSize, 0.5D + tntSize);
        renderer.renderStandardBlock(block, x, y, z);
        break;
      case 3:
        for (i = 0; i < vecCount; i += 2) {
          float maxU, maxV, minU, minV; int A = i, B = i + 1;

          
          double swap = (vec[A]).xCoord;
          (vec[A]).xCoord = 1.0D - (vec[A]).yCoord;
          (vec[A]).yCoord = swap;
          
          swap = (vec[B]).xCoord;
          (vec[B]).xCoord = 1.0D - (vec[B]).yCoord;
          (vec[B]).yCoord = swap;
          
          (vec[A]).xCoord += x; (vec[A]).yCoord += y; (vec[A]).zCoord += z;
          (vec[B]).xCoord += x; (vec[B]).yCoord += y; (vec[B]).zCoord += z;
          
          if (i % 6 == 0) {
            maxU = cDRNmaxU;
            maxV = cDRNmaxV;
            minU = cDRNminU;
            minV = cDRNminV;
          } else if (i % 6 == 2) {
            maxU = tNTmaxU;
            maxV = tNTmaxV;
            minU = tNTminU;
            minV = tNTminV;
          } else {
            maxU = iRONmaxU;
            maxV = iRONmaxV;
            minU = iRONminU;
            minV = iRONminV;
          } 
          tes.addVertexWithUV((vec[A]).xCoord, (vec[A]).yCoord, (vec[A]).zCoord, maxU, maxV);
          tes.addVertexWithUV((vec[B]).xCoord, (vec[A]).yCoord, (vec[A]).zCoord, maxU, minV);
          tes.addVertexWithUV((vec[B]).xCoord, (vec[B]).yCoord, (vec[B]).zCoord, minU, minV);
          tes.addVertexWithUV((vec[A]).xCoord, (vec[B]).yCoord, (vec[B]).zCoord, minU, maxV);
          
          tes.addVertexWithUV((vec[A]).xCoord, (vec[B]).yCoord, (vec[B]).zCoord, maxU, maxV);
          tes.addVertexWithUV((vec[B]).xCoord, (vec[B]).yCoord, (vec[B]).zCoord, maxU, minV);
          tes.addVertexWithUV((vec[B]).xCoord, (vec[A]).yCoord, (vec[A]).zCoord, minU, minV);
          tes.addVertexWithUV((vec[A]).xCoord, (vec[A]).yCoord, (vec[A]).zCoord, minU, maxV);
        } 
        renderer.setRenderBounds(hi, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
        renderer.setOverrideBlockTexture(Blocks.stonebrick.getIcon(0, 0));
        renderer.renderStandardBlock(block, x, y, z);
        
        renderer.setOverrideBlockTexture(Blocks.tnt.getIcon(2, 0));
        renderer.setRenderBounds(0.95D - tntSize, 0.5D - tntSize, 0.5D - tntSize, 0.95D, 0.5D + tntSize, 0.5D + tntSize);
        renderer.renderStandardBlock(block, x, y, z);
        break;
      case 4:
        for (i = 0; i < vecCount; i += 2) {
          float maxU, maxV, minU, minV; int A = i, B = i + 1;

          
          double swap = (vec[A]).zCoord;
          (vec[A]).zCoord = (vec[A]).yCoord;
          (vec[A]).yCoord = swap;
          
          swap = (vec[B]).zCoord;
          (vec[B]).zCoord = (vec[B]).yCoord;
          (vec[B]).yCoord = swap;
          
          (vec[A]).xCoord += x; (vec[A]).yCoord += y; (vec[A]).zCoord += z;
          (vec[B]).xCoord += x; (vec[B]).yCoord += y; (vec[B]).zCoord += z;
          
          if (i % 6 == 0) {
            maxU = cDRNmaxU;
            maxV = cDRNmaxV;
            minU = cDRNminU;
            minV = cDRNminV;
          } else if (i % 6 == 2) {
            maxU = tNTmaxU;
            maxV = tNTmaxV;
            minU = tNTminU;
            minV = tNTminV;
          } else {
            maxU = iRONmaxU;
            maxV = iRONmaxV;
            minU = iRONminU;
            minV = iRONminV;
          } 
          tes.addVertexWithUV((vec[A]).xCoord, (vec[A]).yCoord, (vec[A]).zCoord, maxU, maxV);
          tes.addVertexWithUV((vec[A]).xCoord, (vec[A]).yCoord, (vec[B]).zCoord, maxU, minV);
          tes.addVertexWithUV((vec[B]).xCoord, (vec[B]).yCoord, (vec[B]).zCoord, minU, minV);
          tes.addVertexWithUV((vec[B]).xCoord, (vec[B]).yCoord, (vec[A]).zCoord, minU, maxV);
          
          tes.addVertexWithUV((vec[B]).xCoord, (vec[B]).yCoord, (vec[A]).zCoord, maxU, maxV);
          tes.addVertexWithUV((vec[B]).xCoord, (vec[B]).yCoord, (vec[B]).zCoord, maxU, minV);
          tes.addVertexWithUV((vec[A]).xCoord, (vec[A]).yCoord, (vec[B]).zCoord, minU, minV);
          tes.addVertexWithUV((vec[A]).xCoord, (vec[A]).yCoord, (vec[A]).zCoord, minU, maxV);
        } 
        renderer.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, lw);
        renderer.setOverrideBlockTexture(Blocks.stonebrick.getIcon(0, 0));
        renderer.renderStandardBlock(block, x, y, z);
        
        renderer.setOverrideBlockTexture(Blocks.tnt.getIcon(2, 0));
        renderer.setRenderBounds(0.5D - tntSize, 0.5D - tntSize, 0.05D, 0.5D + tntSize, 0.5D + tntSize, 0.05D + tntSize);
        renderer.renderStandardBlock(block, x, y, z);
        break;
      case 5:
        for (i = 0; i < vecCount; i += 2) {
          float maxU, maxV, minU, minV; int A = i, B = i + 1;

          
          double swap = (vec[A]).zCoord;
          (vec[A]).zCoord = 1.0D - (vec[A]).yCoord;
          (vec[A]).yCoord = swap;
          
          swap = (vec[B]).zCoord;
          (vec[B]).zCoord = 1.0D - (vec[B]).yCoord;
          (vec[B]).yCoord = swap;
          
          (vec[A]).xCoord += x; (vec[A]).yCoord += y; (vec[A]).zCoord += z;
          (vec[B]).xCoord += x; (vec[B]).yCoord += y; (vec[B]).zCoord += z;
          
          if (i % 6 == 0) {
            maxU = cDRNmaxU;
            maxV = cDRNmaxV;
            minU = cDRNminU;
            minV = cDRNminV;
          } else if (i % 6 == 2) {
            maxU = tNTmaxU;
            maxV = tNTmaxV;
            minU = tNTminU;
            minV = tNTminV;
          } else {
            maxU = iRONmaxU;
            maxV = iRONmaxV;
            minU = iRONminU;
            minV = iRONminV;
          } 
          tes.addVertexWithUV((vec[A]).xCoord, (vec[A]).yCoord, (vec[A]).zCoord, maxU, maxV);
          tes.addVertexWithUV((vec[A]).xCoord, (vec[A]).yCoord, (vec[B]).zCoord, maxU, minV);
          tes.addVertexWithUV((vec[B]).xCoord, (vec[B]).yCoord, (vec[B]).zCoord, minU, minV);
          tes.addVertexWithUV((vec[B]).xCoord, (vec[B]).yCoord, (vec[A]).zCoord, minU, maxV);
          
          tes.addVertexWithUV((vec[B]).xCoord, (vec[B]).yCoord, (vec[A]).zCoord, maxU, maxV);
          tes.addVertexWithUV((vec[B]).xCoord, (vec[B]).yCoord, (vec[B]).zCoord, maxU, minV);
          tes.addVertexWithUV((vec[A]).xCoord, (vec[A]).yCoord, (vec[B]).zCoord, minU, minV);
          tes.addVertexWithUV((vec[A]).xCoord, (vec[A]).yCoord, (vec[A]).zCoord, minU, maxV);
        } 
        renderer.setRenderBounds(0.0D, 0.0D, hi, 1.0D, 1.0D, 1.0D);
        renderer.setOverrideBlockTexture(Blocks.stonebrick.getIcon(0, 0));
        renderer.renderStandardBlock(block, x, y, z);
        
        renderer.setOverrideBlockTexture(Blocks.tnt.getIcon(2, 0));
        renderer.setRenderBounds(0.5D - tntSize, 0.5D - tntSize, 0.95D - tntSize, 0.5D + tntSize, 0.5D + tntSize, 0.95D);
        renderer.renderStandardBlock(block, x, y, z);
        break;
    } 

    renderer.clearOverrideBlockTexture();
    return true;
  }

  public boolean shouldRender3DInInventory(int modelId) {
    return false;
  }
}
