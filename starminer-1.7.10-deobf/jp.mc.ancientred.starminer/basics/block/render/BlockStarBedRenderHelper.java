package jp.mc.ancientred.starminer.basics.block.render;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import jp.mc.ancientred.starminer.basics.block.bed.BlockStarBedBody;
import jp.mc.ancientred.starminer.basics.block.bed.BlockStarBedHead;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

public class BlockStarBedRenderHelper
  implements ISimpleBlockRenderingHandler
{
  public static final int RENDER_TYPE = 4341808;
  
  public int getRenderId() {
    return 4341808;
  }

  public void renderInventoryBlock(Block par1Block, int metadata, int modelID, RenderBlocks renderer) {}

  public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
    boolean isBedHead;
    int gravDir, connDir;
    IIcon topIcon, endIcon, sideIcon;
    double end;
    IIcon starIcon = null;
    int sideTopCnt = 1;
    if (block instanceof BlockStarBedBody) {
      isBedHead = false;
      gravDir = ((BlockStarBedBody)block).getGravityDirection(world, x, y, z);
      connDir = ((BlockStarBedBody)block).getConnectionDirection(world, x, y, z);
      if (gravDir == -1) return true; 
      topIcon = ((BlockStarBedBody)block).getBedTopIcon();
      endIcon = ((BlockStarBedBody)block).getBedEndIcon();
      sideIcon = ((BlockStarBedBody)block).getBedSideIcon();
      starIcon = ((BlockStarBedBody)block).getBedStarIcon();
      sideTopCnt = 2;
    } else if (block instanceof BlockStarBedHead) {
      isBedHead = true;
      gravDir = ((BlockStarBedHead)block).getGravityDirection(world, x, y, z);
      connDir = ((BlockStarBedHead)block).getConnectionDirection(world, x, y, z);
      if (connDir == -1) return true; 
      topIcon = ((BlockStarBedHead)block).getBedTopIcon();
      endIcon = ((BlockStarBedHead)block).getBedEndIcon();
      sideIcon = ((BlockStarBedHead)block).getBedSideIcon();
    } else {
      return true;
    } 
    Tessellator tessellator = Tessellator.instance;
    double bedHeight = 0.5625D;
    double bedHeightRev = 1.0D - bedHeight;
    
    float color = 0.5F;
    float colorTopR = 1.0F;
    float colorTopG = 0.8F;
    float colorTopB = 0.6F;

    int brightness = block.getMixedBrightnessForBlock(world, x, y, z);
    tessellator.setBrightness(brightness);
    tessellator.setColorOpaque_F(color, color, color);
    IIcon icon = Blocks.planks.getBlockTextureFromSide(0);
    if (renderer.hasOverrideBlockTexture()) icon = renderer.overrideBlockTexture; 
    double minU = icon.getMinU();
    double maxU = icon.getMaxU();
    double minV = icon.getMinV();
    double maxV = icon.getMaxV();
    
    switch (gravDir) {
      case 3:
        x_2 = x + 1.0D - 0.1875D;
        y_1 = y + 0.0D;
        y_2 = y + 1.0D;
        z_1 = z + 0.0D;
        z_2 = z + 1.0D;
        tessellator.addVertexWithUV(x_2, y_2, z_2, minU, maxV);
        tessellator.addVertexWithUV(x_2, y_1, z_2, minU, minV);
        tessellator.addVertexWithUV(x_2, y_1, z_1, maxU, minV);
        tessellator.addVertexWithUV(x_2, y_2, z_1, maxU, maxV);
        break;
      case 2:
        x_1 = x + 0.0D + 0.1875D;
        y_1 = y + 0.0D;
        y_2 = y + 1.0D;
        z_1 = z + 0.0D;
        z_2 = z + 1.0D;
        tessellator.addVertexWithUV(x_1, y_2, z_1, minU, maxV);
        tessellator.addVertexWithUV(x_1, y_1, z_1, minU, minV);
        tessellator.addVertexWithUV(x_1, y_1, z_2, maxU, minV);
        tessellator.addVertexWithUV(x_1, y_2, z_2, maxU, maxV);
        break;
      case 5:
        x_1 = x + 0.0D;
        x_2 = x + 1.0D;
        y_1 = y + 0.0D;
        y_2 = y + 1.0D;
        z_2 = z + 1.0D - 0.1875D;
        tessellator.addVertexWithUV(x_2, y_1, z_2, minU, maxV);
        tessellator.addVertexWithUV(x_2, y_2, z_2, minU, minV);
        tessellator.addVertexWithUV(x_1, y_2, z_2, maxU, minV);
        tessellator.addVertexWithUV(x_1, y_1, z_2, maxU, maxV);
        break;
      case 4:
        x_1 = x + 0.0D;
        x_2 = x + 1.0D;
        y_1 = y + 0.0D;
        y_2 = y + 1.0D;
        z_1 = z + 0.0D + 0.1875D;
        tessellator.addVertexWithUV(x_1, y_1, z_1, minU, maxV);
        tessellator.addVertexWithUV(x_1, y_2, z_1, minU, minV);
        tessellator.addVertexWithUV(x_2, y_2, z_1, maxU, minV);
        tessellator.addVertexWithUV(x_2, y_1, z_1, maxU, maxV);
        break;
      case 1:
        x_1 = x + 0.0D;
        x_2 = x + 1.0D;
        y_2 = y + 1.0D - 0.1875D;
        z_1 = z + 0.0D;
        z_2 = z + 1.0D;
        tessellator.addVertexWithUV(x_2, y_2, z_2, minU, maxV);
        tessellator.addVertexWithUV(x_2, y_2, z_1, minU, minV);
        tessellator.addVertexWithUV(x_1, y_2, z_1, maxU, minV);
        tessellator.addVertexWithUV(x_1, y_2, z_2, maxU, maxV);
        break;
      case 0:
        x_1 = x + 0.0D;
        x_2 = x + 1.0D;
        y_1 = y + 0.0D + 0.1875D;
        z_1 = z + 0.0D;
        z_2 = z + 1.0D;
        tessellator.addVertexWithUV(x_1, y_1, z_2, minU, maxV);
        tessellator.addVertexWithUV(x_1, y_1, z_1, minU, minV);
        tessellator.addVertexWithUV(x_2, y_1, z_1, maxU, minV);
        tessellator.addVertexWithUV(x_2, y_1, z_2, maxU, maxV);
        break;
    } 

    tessellator.setColorOpaque_F(colorTopR, colorTopR, colorTopR);
    icon = topIcon;
    if (renderer.hasOverrideBlockTexture()) icon = renderer.overrideBlockTexture; 
    for (int i = 0; i < sideTopCnt; i++) {
      if (i == 1 && starIcon != null) {
        icon = starIcon;
        bedHeight += 0.02D;
        bedHeightRev = 1.0D - bedHeight;
      } 
      
      minU = icon.getMinU();
      maxU = icon.getMaxU();
      minV = icon.getMinV();
      maxV = icon.getMaxV();
      
      double d1 = -1.0D;
      double d2 = minU;
      double d3 = maxU;
      double d4 = minV;
      double d5 = minV;
      double d6 = minU;
      double d7 = maxU;
      double d8 = maxV;
      double d9 = maxV;

      switch (gravDir) {
        case 3:
          switch (connDir) {
            
            case 1:
              d1 = 3.0D;
              break;

            case 5:
              d1 = 0.0D;
              break;
            
            case 4:
              d1 = 2.0D;
              break;
          } 
          
          if (d1 == 0.0D) { d3 = minU; d4 = maxV; d6 = maxU; d9 = minV; }
          else if (d1 == 2.0D) { d2 = maxU; d5 = maxV; d7 = minU; d8 = minV; }
          else if (d1 == 3.0D) { d2 = maxU; d5 = maxV; d7 = minU; d8 = minV; d3 = minU; d4 = maxV; d6 = maxU; d9 = minV; }
          
          x_2 = x + bedHeightRev;
          y_1 = y + 0.0D;
          y_2 = y + 1.0D;
          z_1 = z + 0.0D;
          z_2 = z + 1.0D;
          tessellator.addVertexWithUV(x_2, y_1, z_1, d6, d8);
          tessellator.addVertexWithUV(x_2, y_1, z_2, d2, d4);
          tessellator.addVertexWithUV(x_2, y_2, z_2, d3, d5);
          tessellator.addVertexWithUV(x_2, y_2, z_1, d7, d9);
          break;
        case 2:
          switch (connDir) {

            case 0:
              d1 = 3.0D;
              break;
            
            case 5:
              d1 = 0.0D;
              break;
            
            case 4:
              d1 = 2.0D;
              break;
          } 
          
          if (d1 == 0.0D) { d3 = minU; d4 = maxV; d6 = maxU; d9 = minV; }
          else if (d1 == 2.0D) { d2 = maxU; d5 = maxV; d7 = minU; d8 = minV; }
          else if (d1 == 3.0D) { d2 = maxU; d5 = maxV; d7 = minU; d8 = minV; d3 = minU; d4 = maxV; d6 = maxU; d9 = minV; }
          
          x_2 = x + bedHeight;
          y_1 = y + 0.0D;
          y_2 = y + 1.0D;
          z_1 = z + 0.0D;
          z_2 = z + 1.0D;
          tessellator.addVertexWithUV(x_2, y_2, z_1, d6, d8);
          tessellator.addVertexWithUV(x_2, y_2, z_2, d2, d4);
          tessellator.addVertexWithUV(x_2, y_1, z_2, d3, d5);
          tessellator.addVertexWithUV(x_2, y_1, z_1, d7, d9);
          break;
        case 5:
          switch (connDir) {
            
            case 3:
              d1 = 2.0D;
              break;
            
            case 2:
              d1 = 0.0D;
              break;
            
            case 1:
              d1 = 3.0D;
              break;
          } 

          if (d1 == 0.0D) { d3 = minU; d4 = maxV; d6 = maxU; d9 = minV; }
          else if (d1 == 2.0D) { d2 = maxU; d5 = maxV; d7 = minU; d8 = minV; }
          else if (d1 == 3.0D) { d2 = maxU; d5 = maxV; d7 = minU; d8 = minV; d3 = minU; d4 = maxV; d6 = maxU; d9 = minV; }
          
          x_1 = x + 0.0D;
          x_2 = x + 1.0D;
          y_1 = y + 0.0D;
          y_2 = y + 1.0D;
          z_2 = z + bedHeightRev;
          tessellator.addVertexWithUV(x_2, y_1, z_2, d6, d8);
          tessellator.addVertexWithUV(x_1, y_1, z_2, d2, d4);
          tessellator.addVertexWithUV(x_1, y_2, z_2, d3, d5);
          tessellator.addVertexWithUV(x_2, y_2, z_2, d7, d9);
          break;
        case 4:
          switch (connDir) {
            
            case 3:
              d1 = 2.0D;
              break;
            
            case 2:
              d1 = 0.0D;
              break;

            case 0:
              d1 = 3.0D;
              break;
          } 
          
          if (d1 == 0.0D) { d3 = minU; d4 = maxV; d6 = maxU; d9 = minV; }
          else if (d1 == 2.0D) { d2 = maxU; d5 = maxV; d7 = minU; d8 = minV; }
          else if (d1 == 3.0D) { d2 = maxU; d5 = maxV; d7 = minU; d8 = minV; d3 = minU; d4 = maxV; d6 = maxU; d9 = minV; }
          
          x_1 = x + 0.0D;
          x_2 = x + 1.0D;
          y_1 = y + 0.0D;
          y_2 = y + 1.0D;
          z_2 = z + bedHeight;
          tessellator.addVertexWithUV(x_2, y_2, z_2, d6, d8);
          tessellator.addVertexWithUV(x_1, y_2, z_2, d2, d4);
          tessellator.addVertexWithUV(x_1, y_1, z_2, d3, d5);
          tessellator.addVertexWithUV(x_2, y_1, z_2, d7, d9);
          break;
        case 1:
          switch (connDir) {
            
            case 3:
              d1 = 2.0D;
              break;
            
            case 2:
              d1 = 0.0D;
              break;

            case 4:
              d1 = 3.0D;
              break;
          } 
          
          if (d1 == 0.0D) { d3 = minU; d4 = maxV; d6 = maxU; d9 = minV; }
          else if (d1 == 2.0D) { d2 = maxU; d5 = maxV; d7 = minU; d8 = minV; }
          else if (d1 == 3.0D) { d2 = maxU; d5 = maxV; d7 = minU; d8 = minV; d3 = minU; d4 = maxV; d6 = maxU; d9 = minV; }
          
          x_1 = x + 0.0D;
          x_2 = x + 1.0D;
          y_2 = y + bedHeightRev;
          z_1 = z + 0.0D;
          z_2 = z + 1.0D;
          tessellator.addVertexWithUV(x_2, y_2, z_2, d6, d8);
          tessellator.addVertexWithUV(x_1, y_2, z_2, d2, d4);
          tessellator.addVertexWithUV(x_1, y_2, z_1, d3, d5);
          tessellator.addVertexWithUV(x_2, y_2, z_1, d7, d9);
          break;
        
        case 0:
          switch (connDir) {
            
            case 3:
              d1 = 2.0D;
              break;
            
            case 2:
              d1 = 0.0D;
              break;
            
            case 5:
              d1 = 3.0D;
              break;
          } 

          if (d1 == 0.0D) { d3 = minU; d4 = maxV; d6 = maxU; d9 = minV; }
          else if (d1 == 2.0D) { d2 = maxU; d5 = maxV; d7 = minU; d8 = minV; }
          else if (d1 == 3.0D) { d2 = maxU; d5 = maxV; d7 = minU; d8 = minV; d3 = minU; d4 = maxV; d6 = maxU; d9 = minV; }
          
          x_1 = x + 0.0D;
          x_2 = x + 1.0D;
          y_2 = y + bedHeight;
          z_1 = z + 0.0D;
          z_2 = z + 1.0D;
          tessellator.addVertexWithUV(x_2, y_2, z_1, d6, d8);
          tessellator.addVertexWithUV(x_1, y_2, z_1, d2, d4);
          tessellator.addVertexWithUV(x_1, y_2, z_2, d3, d5);
          tessellator.addVertexWithUV(x_2, y_2, z_2, d7, d9);
          break;
      } 

    } 
    bedHeight = 0.5625D;
    bedHeightRev = 1.0D - bedHeight;

    icon = endIcon;
    minU = icon.getMinU();
    maxU = icon.getMaxU();
    minV = icon.getMinV();
    maxV = icon.getMaxV();
    double roatU1 = minU;
    double roatU2 = maxU;
    double roatV1 = minV;
    double roatV2 = minV;
    double roatU3 = minU;
    double roatU4 = maxU;
    double roatV3 = maxV;
    double roatV4 = maxV;
    tessellator.setColorOpaque_F(colorTopG, colorTopG, colorTopG);

    double roat = -1.0D;
    
    double x_1 = x + 0.0D;
    double x_2 = x + 1.0D;
    double y_1 = y + 0.0D;
    double y_2 = y + 1.0D;
    double z_1 = z + 0.0D;
    double z_2 = z + 1.0D;
    
    switch (connDir) {
      
      case 3:
        end = x_2;
        switch (gravDir) {
          
          case 5:
            tessellator.addVertexWithUV(x_1, y_1, z_2, sideIcon.getMaxU(), sideIcon.getMaxV());
            tessellator.addVertexWithUV(x_1, y_1, z_1, sideIcon.getMaxU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_2, y_1, z_1, sideIcon.getMinU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_2, y_1, z_2, sideIcon.getMinU(), sideIcon.getMaxV());
            
            tessellator.addVertexWithUV(x_2, y_2, z_2, sideIcon.getMinU(), sideIcon.getMaxV());
            tessellator.addVertexWithUV(x_2, y_2, z_1, sideIcon.getMinU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_1, y_2, z_1, sideIcon.getMaxU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_1, y_2, z_2, sideIcon.getMaxU(), sideIcon.getMaxV());
            
            if (isBedHead) {
              double swp = y_1;
              y_1 = y_2;
              y_2 = swp;
              
              end = x_1;
            } 
            
            roat = 2.0D;
            break;
          
          case 4:
            tessellator.addVertexWithUV(x_2, y_1, z_1, sideIcon.getMinU(), sideIcon.getMaxV());
            tessellator.addVertexWithUV(x_2, y_1, z_2, sideIcon.getMinU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_1, y_1, z_2, sideIcon.getMaxU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_1, y_1, z_1, sideIcon.getMaxU(), sideIcon.getMaxV());
            
            tessellator.addVertexWithUV(x_1, y_2, z_1, sideIcon.getMaxU(), sideIcon.getMaxV());
            tessellator.addVertexWithUV(x_1, y_2, z_2, sideIcon.getMaxU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_2, y_2, z_2, sideIcon.getMinU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_2, y_2, z_1, sideIcon.getMinU(), sideIcon.getMaxV());
            
            if (isBedHead) {
              double swp = y_1;
              y_1 = y_2;
              y_2 = swp;
              
              end = x_1;
            } 
            
            roat = 0.0D;
            break;
          
          case 1:
            tessellator.addVertexWithUV(x_2, y_2, z_1, sideIcon.getMinU(), sideIcon.getMaxV());
            tessellator.addVertexWithUV(x_2, y_1, z_1, sideIcon.getMinU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_1, y_1, z_1, sideIcon.getMaxU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_1, y_2, z_1, sideIcon.getMaxU(), sideIcon.getMaxV());
            
            tessellator.addVertexWithUV(x_1, y_2, z_2, sideIcon.getMaxU(), sideIcon.getMaxV());
            tessellator.addVertexWithUV(x_1, y_1, z_2, sideIcon.getMaxU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_2, y_1, z_2, sideIcon.getMinU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_2, y_2, z_2, sideIcon.getMinU(), sideIcon.getMaxV());
            
            if (isBedHead) {
              double swp = z_1;
              z_1 = z_2;
              z_2 = swp;
              
              end = x_1;
            } 
            
            roat = 3.0D;
            break;
          
          case 0:
            tessellator.addVertexWithUV(x_1, y_1, z_1, sideIcon.getMaxU(), sideIcon.getMaxV());
            tessellator.addVertexWithUV(x_1, y_2, z_1, sideIcon.getMaxU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_2, y_2, z_1, sideIcon.getMinU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_2, y_1, z_1, sideIcon.getMinU(), sideIcon.getMaxV());
            
            tessellator.addVertexWithUV(x_2, y_1, z_2, sideIcon.getMinU(), sideIcon.getMaxV());
            tessellator.addVertexWithUV(x_2, y_2, z_2, sideIcon.getMinU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_1, y_2, z_2, sideIcon.getMaxU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_1, y_1, z_2, sideIcon.getMaxU(), sideIcon.getMaxV());
            
            if (isBedHead) {
              double swp = z_1;
              z_1 = z_2;
              z_2 = swp;
              
              end = x_1;
            } 
            break;
        } 

        if (roat == 0.0D) { roatU2 = minU; roatV1 = maxV; roatU3 = maxU; roatV4 = minV; }
        else if (roat == 2.0D) { roatU1 = maxU; roatV2 = maxV; roatU4 = minU; roatV3 = minV; }
        else if (roat == 3.0D) { roatU1 = maxU; roatV2 = maxV; roatU4 = minU; roatV3 = minV; roatU2 = minU; roatV1 = maxV; roatU3 = maxU; roatV4 = minV; }
        
        tessellator.addVertexWithUV(end, y_1, z_1, roatU3, roatV3);
        tessellator.addVertexWithUV(end, y_2, z_1, roatU1, roatV1);
        tessellator.addVertexWithUV(end, y_2, z_2, roatU2, roatV2);
        tessellator.addVertexWithUV(end, y_1, z_2, roatU4, roatV4);
        break;

      case 2:
        end = x_1;
        switch (gravDir) {
          
          case 5:
            tessellator.addVertexWithUV(x_2, y_2, z_2, sideIcon.getMaxU(), sideIcon.getMaxV());
            tessellator.addVertexWithUV(x_2, y_2, z_1, sideIcon.getMaxU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_1, y_2, z_1, sideIcon.getMinU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_1, y_2, z_2, sideIcon.getMinU(), sideIcon.getMaxV());
            
            tessellator.addVertexWithUV(x_1, y_1, z_2, sideIcon.getMinU(), sideIcon.getMaxV());
            tessellator.addVertexWithUV(x_1, y_1, z_1, sideIcon.getMinU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_2, y_1, z_1, sideIcon.getMaxU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_2, y_1, z_2, sideIcon.getMaxU(), sideIcon.getMaxV());
            
            if (isBedHead) {
              double swp = y_1;
              y_1 = y_2;
              y_2 = swp;
              
              end = x_2;
            } 
            
            roat = 0.0D;
            break;
          
          case 4:
            tessellator.addVertexWithUV(x_1, y_2, z_1, sideIcon.getMinU(), sideIcon.getMaxV());
            tessellator.addVertexWithUV(x_1, y_2, z_2, sideIcon.getMinU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_2, y_2, z_2, sideIcon.getMaxU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_2, y_2, z_1, sideIcon.getMaxU(), sideIcon.getMaxV());
            
            tessellator.addVertexWithUV(x_2, y_1, z_1, sideIcon.getMaxU(), sideIcon.getMaxV());
            tessellator.addVertexWithUV(x_2, y_1, z_2, sideIcon.getMaxU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_1, y_1, z_2, sideIcon.getMinU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_1, y_1, z_1, sideIcon.getMinU(), sideIcon.getMaxV());
            
            if (isBedHead) {
              double swp = y_1;
              y_1 = y_2;
              y_2 = swp;
              
              end = x_2;
            } 
            
            roat = 2.0D;
            break;
          case 1:
            tessellator.addVertexWithUV(x_1, y_2, z_2, sideIcon.getMinU(), sideIcon.getMaxV());
            tessellator.addVertexWithUV(x_1, y_1, z_2, sideIcon.getMinU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_2, y_1, z_2, sideIcon.getMaxU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_2, y_2, z_2, sideIcon.getMaxU(), sideIcon.getMaxV());
            
            tessellator.addVertexWithUV(x_2, y_2, z_1, sideIcon.getMaxU(), sideIcon.getMaxV());
            tessellator.addVertexWithUV(x_2, y_1, z_1, sideIcon.getMaxU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_1, y_1, z_1, sideIcon.getMinU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_1, y_2, z_1, sideIcon.getMinU(), sideIcon.getMaxV());

            
            if (isBedHead) {
              double swp = z_1;
              z_1 = z_2;
              z_2 = swp;
              
              end = x_2;
            } 
            roat = 3.0D;
            break;
          
          case 0:
            tessellator.addVertexWithUV(x_2, y_1, z_2, sideIcon.getMaxU(), sideIcon.getMaxV());
            tessellator.addVertexWithUV(x_2, y_2, z_2, sideIcon.getMaxU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_1, y_2, z_2, sideIcon.getMinU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_1, y_1, z_2, sideIcon.getMinU(), sideIcon.getMaxV());
            
            tessellator.addVertexWithUV(x_1, y_1, z_1, sideIcon.getMinU(), sideIcon.getMaxV());
            tessellator.addVertexWithUV(x_1, y_2, z_1, sideIcon.getMinU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_2, y_2, z_1, sideIcon.getMaxU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_2, y_1, z_1, sideIcon.getMaxU(), sideIcon.getMaxV());
            
            if (isBedHead) {
              double swp = z_1;
              z_1 = z_2;
              z_2 = swp;
              
              end = x_2;
            } 
            break;
        } 

        if (roat == 0.0D) { roatU2 = minU; roatV1 = maxV; roatU3 = maxU; roatV4 = minV; }
        else if (roat == 2.0D) { roatU1 = maxU; roatV2 = maxV; roatU4 = minU; roatV3 = minV; }
        else if (roat == 3.0D) { roatU1 = maxU; roatV2 = maxV; roatU4 = minU; roatV3 = minV; roatU2 = minU; roatV1 = maxV; roatU3 = maxU; roatV4 = minV; }
        
        tessellator.addVertexWithUV(end, y_1, z_2, roatU3, roatV3);
        tessellator.addVertexWithUV(end, y_2, z_2, roatU1, roatV1);
        tessellator.addVertexWithUV(end, y_2, z_1, roatU2, roatV2);
        tessellator.addVertexWithUV(end, y_1, z_1, roatU4, roatV4);
        break;
      
      case 5:
        end = z_2;
        switch (gravDir) {
          
          case 3:
            tessellator.addVertexWithUV(x_2, y_1, z_2, sideIcon.getMinU(), sideIcon.getMaxV());
            tessellator.addVertexWithUV(x_1, y_1, z_2, sideIcon.getMinU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_1, y_1, z_1, sideIcon.getMaxU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_2, y_1, z_1, sideIcon.getMaxU(), sideIcon.getMaxV());
            
            tessellator.addVertexWithUV(x_2, y_2, z_1, sideIcon.getMaxU(), sideIcon.getMaxV());
            tessellator.addVertexWithUV(x_1, y_2, z_1, sideIcon.getMaxU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_1, y_2, z_2, sideIcon.getMinU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_2, y_2, z_2, sideIcon.getMinU(), sideIcon.getMaxV());
            
            if (isBedHead) {
              double swp = y_1;
              y_1 = y_2;
              y_2 = swp;
              
              end = z_1;
            } 
            
            roat = 0.0D;
            break;
          
          case 2:
            tessellator.addVertexWithUV(x_1, y_1, z_1, sideIcon.getMaxU(), sideIcon.getMaxV());
            tessellator.addVertexWithUV(x_2, y_1, z_1, sideIcon.getMaxU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_2, y_1, z_2, sideIcon.getMinU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_1, y_1, z_2, sideIcon.getMinU(), sideIcon.getMaxV());
            
            tessellator.addVertexWithUV(x_1, y_2, z_2, sideIcon.getMinU(), sideIcon.getMaxV());
            tessellator.addVertexWithUV(x_2, y_2, z_2, sideIcon.getMinU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_2, y_2, z_1, sideIcon.getMaxU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_1, y_2, z_1, sideIcon.getMaxU(), sideIcon.getMaxV());
            
            if (isBedHead) {
              double swp = y_1;
              y_1 = y_2;
              y_2 = swp;
              
              end = z_1;
            } 
            
            roat = 2.0D;
            break;
          
          case 1:
            tessellator.addVertexWithUV(x_1, y_2, z_1, sideIcon.getMaxU(), sideIcon.getMaxV());
            tessellator.addVertexWithUV(x_1, y_1, z_1, sideIcon.getMaxU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_1, y_1, z_2, sideIcon.getMinU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_1, y_2, z_2, sideIcon.getMinU(), sideIcon.getMaxV());
            
            tessellator.addVertexWithUV(x_2, y_2, z_2, sideIcon.getMinU(), sideIcon.getMaxV());
            tessellator.addVertexWithUV(x_2, y_1, z_2, sideIcon.getMinU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_2, y_1, z_1, sideIcon.getMaxU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_2, y_2, z_1, sideIcon.getMaxU(), sideIcon.getMaxV());

            
            if (isBedHead) {
              double swp = x_1;
              x_1 = x_2;
              x_2 = swp;
              
              end = z_1;
            } 
            roat = 3.0D;
            break;
          
          case 0:
            tessellator.addVertexWithUV(x_1, y_1, z_2, sideIcon.getMinU(), sideIcon.getMaxV());
            tessellator.addVertexWithUV(x_1, y_2, z_2, sideIcon.getMinU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_1, y_2, z_1, sideIcon.getMaxU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_1, y_1, z_1, sideIcon.getMaxU(), sideIcon.getMaxV());
            
            tessellator.addVertexWithUV(x_2, y_1, z_1, sideIcon.getMaxU(), sideIcon.getMaxV());
            tessellator.addVertexWithUV(x_2, y_2, z_1, sideIcon.getMaxU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_2, y_2, z_2, sideIcon.getMinU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_2, y_1, z_2, sideIcon.getMinU(), sideIcon.getMaxV());
            
            if (isBedHead) {
              double swp = x_1;
              x_1 = x_2;
              x_2 = swp;
              
              end = z_1;
            } 
            break;
        } 

        if (roat == 0.0D) { roatU2 = minU; roatV1 = maxV; roatU3 = maxU; roatV4 = minV; }
        else if (roat == 2.0D) { roatU1 = maxU; roatV2 = maxV; roatU4 = minU; roatV3 = minV; }
        else if (roat == 3.0D) { roatU1 = maxU; roatV2 = maxV; roatU4 = minU; roatV3 = minV; roatU2 = minU; roatV1 = maxV; roatU3 = maxU; roatV4 = minV; }
        
        tessellator.addVertexWithUV(x_2, y_1, end, roatU3, roatV3);
        tessellator.addVertexWithUV(x_2, y_2, end, roatU1, roatV1);
        tessellator.addVertexWithUV(x_1, y_2, end, roatU2, roatV2);
        tessellator.addVertexWithUV(x_1, y_1, end, roatU4, roatV4);
        break;

      case 4:
        end = z_1;
        switch (gravDir) {
          
          case 3:
            tessellator.addVertexWithUV(x_2, y_2, z_1, sideIcon.getMinU(), sideIcon.getMaxV());
            tessellator.addVertexWithUV(x_1, y_2, z_1, sideIcon.getMinU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_1, y_2, z_2, sideIcon.getMaxU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_2, y_2, z_2, sideIcon.getMaxU(), sideIcon.getMaxV());
            
            tessellator.addVertexWithUV(x_2, y_1, z_2, sideIcon.getMaxU(), sideIcon.getMaxV());
            tessellator.addVertexWithUV(x_1, y_1, z_2, sideIcon.getMaxU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_1, y_1, z_1, sideIcon.getMinU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_2, y_1, z_1, sideIcon.getMinU(), sideIcon.getMaxV());
            
            if (isBedHead) {
              double swp = y_1;
              y_1 = y_2;
              y_2 = swp;
              
              end = z_2;
            } 
            
            roat = 2.0D;
            break;
          
          case 2:
            tessellator.addVertexWithUV(x_1, y_2, z_2, sideIcon.getMaxU(), sideIcon.getMaxV());
            tessellator.addVertexWithUV(x_2, y_2, z_2, sideIcon.getMaxU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_2, y_2, z_1, sideIcon.getMinU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_1, y_2, z_1, sideIcon.getMinU(), sideIcon.getMaxV());
            
            tessellator.addVertexWithUV(x_1, y_1, z_1, sideIcon.getMinU(), sideIcon.getMaxV());
            tessellator.addVertexWithUV(x_2, y_1, z_1, sideIcon.getMinU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_2, y_1, z_2, sideIcon.getMaxU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_1, y_1, z_2, sideIcon.getMaxU(), sideIcon.getMaxV());
            
            if (isBedHead) {
              double swp = y_1;
              y_1 = y_2;
              y_2 = swp;
              
              end = z_2;
            } 
            
            roat = 0.0D;
            break;
          
          case 1:
            tessellator.addVertexWithUV(x_2, y_2, z_2, sideIcon.getMaxU(), sideIcon.getMaxV());
            tessellator.addVertexWithUV(x_2, y_1, z_2, sideIcon.getMaxU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_2, y_1, z_1, sideIcon.getMinU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_2, y_2, z_1, sideIcon.getMinU(), sideIcon.getMaxV());
            
            tessellator.addVertexWithUV(x_1, y_2, z_1, sideIcon.getMinU(), sideIcon.getMaxV());
            tessellator.addVertexWithUV(x_1, y_1, z_1, sideIcon.getMinU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_1, y_1, z_2, sideIcon.getMaxU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_1, y_2, z_2, sideIcon.getMaxU(), sideIcon.getMaxV());

            
            if (isBedHead) {
              double swp = x_1;
              x_1 = x_2;
              x_2 = swp;
              
              end = z_2;
            } 
            roat = 3.0D;
            break;
          
          case 0:
            tessellator.addVertexWithUV(x_2, y_1, z_1, sideIcon.getMinU(), sideIcon.getMaxV());
            tessellator.addVertexWithUV(x_2, y_2, z_1, sideIcon.getMinU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_2, y_2, z_2, sideIcon.getMaxU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_2, y_1, z_2, sideIcon.getMaxU(), sideIcon.getMaxV());
            
            tessellator.addVertexWithUV(x_1, y_1, z_2, sideIcon.getMaxU(), sideIcon.getMaxV());
            tessellator.addVertexWithUV(x_1, y_2, z_2, sideIcon.getMaxU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_1, y_2, z_1, sideIcon.getMinU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_1, y_1, z_1, sideIcon.getMinU(), sideIcon.getMaxV());
            
            if (isBedHead) {
              double swp = x_1;
              x_1 = x_2;
              x_2 = swp;
              
              end = z_2;
            } 
            break;
        } 

        if (roat == 0.0D) { roatU2 = minU; roatV1 = maxV; roatU3 = maxU; roatV4 = minV; }
        else if (roat == 2.0D) { roatU1 = maxU; roatV2 = maxV; roatU4 = minU; roatV3 = minV; }
        else if (roat == 3.0D) { roatU1 = maxU; roatV2 = maxV; roatU4 = minU; roatV3 = minV; roatU2 = minU; roatV1 = maxV; roatU3 = maxU; roatV4 = minV; }
        
        tessellator.addVertexWithUV(x_1, y_1, end, roatU3, roatV3);
        tessellator.addVertexWithUV(x_1, y_2, end, roatU1, roatV1);
        tessellator.addVertexWithUV(x_2, y_2, end, roatU2, roatV2);
        tessellator.addVertexWithUV(x_2, y_1, end, roatU4, roatV4);
        break;

      case 1:
        end = y_2;
        switch (gravDir) {
          
          case 3:
            tessellator.addVertexWithUV(x_2, y_2, z_2, sideIcon.getMinU(), sideIcon.getMaxV());
            tessellator.addVertexWithUV(x_1, y_2, z_2, sideIcon.getMinU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_1, y_1, z_2, sideIcon.getMaxU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_2, y_1, z_2, sideIcon.getMaxU(), sideIcon.getMaxV());
            
            tessellator.addVertexWithUV(x_2, y_1, z_1, sideIcon.getMaxU(), sideIcon.getMaxV());
            tessellator.addVertexWithUV(x_1, y_1, z_1, sideIcon.getMaxU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_1, y_2, z_1, sideIcon.getMinU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_2, y_2, z_1, sideIcon.getMinU(), sideIcon.getMaxV());
            
            if (isBedHead) {
              double swp = z_1;
              z_1 = z_2;
              z_2 = swp;
              
              end = y_1;
            } 
            
            roat = 2.0D;
            break;
          
          case 2:
            tessellator.addVertexWithUV(x_1, y_1, z_2, sideIcon.getMaxU(), sideIcon.getMaxV());
            tessellator.addVertexWithUV(x_2, y_1, z_2, sideIcon.getMaxU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_2, y_2, z_2, sideIcon.getMinU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_1, y_2, z_2, sideIcon.getMinU(), sideIcon.getMaxV());
            
            tessellator.addVertexWithUV(x_1, y_2, z_1, sideIcon.getMinU(), sideIcon.getMaxV());
            tessellator.addVertexWithUV(x_2, y_2, z_1, sideIcon.getMinU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_2, y_1, z_1, sideIcon.getMaxU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_1, y_1, z_1, sideIcon.getMaxU(), sideIcon.getMaxV());
            
            if (isBedHead) {
              double swp = z_1;
              z_1 = z_2;
              z_2 = swp;
              
              end = y_1;
            } 
            
            roat = 0.0D;
            break;
          
          case 5:
            tessellator.addVertexWithUV(x_2, y_1, z_2, sideIcon.getMaxU(), sideIcon.getMaxV());
            tessellator.addVertexWithUV(x_2, y_1, z_1, sideIcon.getMaxU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_2, y_2, z_1, sideIcon.getMinU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_2, y_2, z_2, sideIcon.getMinU(), sideIcon.getMaxV());
            
            tessellator.addVertexWithUV(x_1, y_2, z_2, sideIcon.getMinU(), sideIcon.getMaxV());
            tessellator.addVertexWithUV(x_1, y_2, z_1, sideIcon.getMinU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_1, y_1, z_1, sideIcon.getMaxU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_1, y_1, z_2, sideIcon.getMaxU(), sideIcon.getMaxV());

            
            if (isBedHead) {
              double swp = x_1;
              x_1 = x_2;
              x_2 = swp;
              
              end = y_1;
            } 
            roat = 3.0D;
            break;
          
          case 4:
            tessellator.addVertexWithUV(x_2, y_2, z_1, sideIcon.getMinU(), sideIcon.getMaxV());
            tessellator.addVertexWithUV(x_2, y_2, z_2, sideIcon.getMinU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_2, y_1, z_2, sideIcon.getMaxU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_2, y_1, z_1, sideIcon.getMaxU(), sideIcon.getMaxV());
            
            tessellator.addVertexWithUV(x_1, y_1, z_1, sideIcon.getMaxU(), sideIcon.getMaxV());
            tessellator.addVertexWithUV(x_1, y_1, z_2, sideIcon.getMaxU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_1, y_2, z_2, sideIcon.getMinU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_1, y_2, z_1, sideIcon.getMinU(), sideIcon.getMaxV());
            
            if (isBedHead) {
              double swp = x_1;
              x_1 = x_2;
              x_2 = swp;
              
              end = y_1;
            } 
            break;
        } 

        if (roat == 0.0D) { roatU2 = minU; roatV1 = maxV; roatU3 = maxU; roatV4 = minV; }
        else if (roat == 2.0D) { roatU1 = maxU; roatV2 = maxV; roatU4 = minU; roatV3 = minV; }
        else if (roat == 3.0D) { roatU1 = maxU; roatV2 = maxV; roatU4 = minU; roatV3 = minV; roatU2 = minU; roatV1 = maxV; roatU3 = maxU; roatV4 = minV; }
        
        tessellator.addVertexWithUV(x_1, end, z_1, roatU3, roatV3);
        tessellator.addVertexWithUV(x_1, end, z_2, roatU1, roatV1);
        tessellator.addVertexWithUV(x_2, end, z_2, roatU2, roatV2);
        tessellator.addVertexWithUV(x_2, end, z_1, roatU4, roatV4);
        break;

      case 0:
        end = y_1;
        switch (gravDir) {
          
          case 3:
            tessellator.addVertexWithUV(x_2, y_1, z_1, sideIcon.getMinU(), sideIcon.getMaxV());
            tessellator.addVertexWithUV(x_1, y_1, z_1, sideIcon.getMinU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_1, y_2, z_1, sideIcon.getMaxU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_2, y_2, z_1, sideIcon.getMaxU(), sideIcon.getMaxV());
            
            tessellator.addVertexWithUV(x_2, y_2, z_2, sideIcon.getMaxU(), sideIcon.getMaxV());
            tessellator.addVertexWithUV(x_1, y_2, z_2, sideIcon.getMaxU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_1, y_1, z_2, sideIcon.getMinU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_2, y_1, z_2, sideIcon.getMinU(), sideIcon.getMaxV());
            
            if (isBedHead) {
              double swp = z_1;
              z_1 = z_2;
              z_2 = swp;
              
              end = y_2;
            } 
            
            roat = 0.0D;
            break;
          
          case 2:
            tessellator.addVertexWithUV(x_1, y_2, z_1, sideIcon.getMaxU(), sideIcon.getMaxV());
            tessellator.addVertexWithUV(x_2, y_2, z_1, sideIcon.getMaxU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_2, y_1, z_1, sideIcon.getMinU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_1, y_1, z_1, sideIcon.getMinU(), sideIcon.getMaxV());
            
            tessellator.addVertexWithUV(x_1, y_1, z_2, sideIcon.getMinU(), sideIcon.getMaxV());
            tessellator.addVertexWithUV(x_2, y_1, z_2, sideIcon.getMinU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_2, y_2, z_2, sideIcon.getMaxU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_1, y_2, z_2, sideIcon.getMaxU(), sideIcon.getMaxV());
            
            if (isBedHead) {
              double swp = z_1;
              z_1 = z_2;
              z_2 = swp;
              
              end = y_2;
            } 
            
            roat = 2.0D;
            break;
          
          case 5:
            tessellator.addVertexWithUV(x_1, y_2, z_2, sideIcon.getMaxU(), sideIcon.getMaxV());
            tessellator.addVertexWithUV(x_1, y_2, z_1, sideIcon.getMaxU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_1, y_1, z_1, sideIcon.getMinU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_1, y_1, z_2, sideIcon.getMinU(), sideIcon.getMaxV());
            
            tessellator.addVertexWithUV(x_2, y_1, z_2, sideIcon.getMinU(), sideIcon.getMaxV());
            tessellator.addVertexWithUV(x_2, y_1, z_1, sideIcon.getMinU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_2, y_2, z_1, sideIcon.getMaxU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_2, y_2, z_2, sideIcon.getMaxU(), sideIcon.getMaxV());

            
            if (isBedHead) {
              double swp = x_1;
              x_1 = x_2;
              x_2 = swp;
              
              end = y_2;
            } 
            roat = 3.0D;
            break;
          
          case 4:
            tessellator.addVertexWithUV(x_1, y_1, z_1, sideIcon.getMinU(), sideIcon.getMaxV());
            tessellator.addVertexWithUV(x_1, y_1, z_2, sideIcon.getMinU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_1, y_2, z_2, sideIcon.getMaxU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_1, y_2, z_1, sideIcon.getMaxU(), sideIcon.getMaxV());
            
            tessellator.addVertexWithUV(x_2, y_2, z_1, sideIcon.getMaxU(), sideIcon.getMaxV());
            tessellator.addVertexWithUV(x_2, y_2, z_2, sideIcon.getMaxU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_2, y_1, z_2, sideIcon.getMinU(), sideIcon.getMinV());
            tessellator.addVertexWithUV(x_2, y_1, z_1, sideIcon.getMinU(), sideIcon.getMaxV());
            
            if (isBedHead) {
              double swp = x_1;
              x_1 = x_2;
              x_2 = swp;
              
              end = y_2;
            } 
            break;
        } 

        if (roat == 0.0D) { roatU2 = minU; roatV1 = maxV; roatU3 = maxU; roatV4 = minV; }
        else if (roat == 2.0D) { roatU1 = maxU; roatV2 = maxV; roatU4 = minU; roatV3 = minV; }
        else if (roat == 3.0D) { roatU1 = maxU; roatV2 = maxV; roatU4 = minU; roatV3 = minV; roatU2 = minU; roatV1 = maxV; roatU3 = maxU; roatV4 = minV; }
        
        tessellator.addVertexWithUV(x_2, end, z_1, roatU3, roatV3);
        tessellator.addVertexWithUV(x_2, end, z_2, roatU1, roatV1);
        tessellator.addVertexWithUV(x_1, end, z_2, roatU2, roatV2);
        tessellator.addVertexWithUV(x_1, end, z_1, roatU4, roatV4);
        break;
    } 

    renderer.flipTexture = false;
    return true;
  }
  
  public boolean shouldRender3DInInventory(int modelId) {
    return false;
  }
}
