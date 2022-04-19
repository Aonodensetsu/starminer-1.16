package jp.mc.ancientred.starminer.core.ai.path;

import jp.mc.ancientred.starminer.api.GravityDirection;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class PathFollowZ {
  public static Vec3 getEntityPosition(GPathNavigate navi) {
    return Vec3.createVectorHelper(navi.theEntity.posX, navi.theEntity.posY, getPathableZPos(navi) + 0.5D);
  }

  private static int getPathableZPos(GPathNavigate navi) {
    int verticalFix = 1;
    double footZ = navi.theEntity.boundingBox.minZ;
    if (navi.gDir == GravityDirection.northTOsouth_ZP) {
      verticalFix = -1;
      footZ = navi.theEntity.boundingBox.maxZ;
    } 
    
    if (navi.theEntity.isInWater() && navi.canSwim) {
      
      int posXInt = MathHelper.floor_double(footZ);
      Block block = navi.worldObj.getBlock(posXInt, (int)navi.theEntity.posY, MathHelper.floor_double(navi.theEntity.posZ));
      int searchLimitCnt = 0;

      do {
        if (block != Blocks.flowing_water && block != Blocks.water)
        {
          return posXInt;
        }
        
        posXInt += verticalFix;
        block = navi.worldObj.getBlock(posXInt, (int)navi.theEntity.posY, MathHelper.floor_double(navi.theEntity.posZ));
        ++searchLimitCnt;
      }
      while (searchLimitCnt <= 16);
      
      return MathHelper.floor_double(footZ);
    } 

    return MathHelper.floor_double(footZ + verticalFix * 0.5D);
  }

  public static void pathFollow(GPathNavigate navi) {
    Vec3 vecEntityNow = getEntityPosition(navi);
    int pathLen = navi.currentPath.getCurrentPathLength();

    int floorZ = MathHelper.floor_double(vecEntityNow.zCoord);
    for (int pathIndex = navi.currentPath.getCurrentPathIndex(); pathIndex < navi.currentPath.getCurrentPathLength(); pathIndex++) {
      
      if ((navi.currentPath.getPathPointFromIndex(pathIndex)).zCoord != floorZ) {
        
        pathLen = pathIndex;
        
        break;
      } 
    } 
    float wRange = navi.theEntity.width * navi.theEntity.width;
    
    for (int i = navi.currentPath.getCurrentPathIndex(); i < pathLen; i++) {

      if (vecEntityNow.squareDistanceTo(navi.currentPath.getVectorFromIndex((Entity)navi.theEntity, i)) < wRange)
      {
        navi.currentPath.setCurrentPathIndex(i + 1);
      }
    } 
    
    int xWidth = MathHelper.ceiling_float_int(navi.theEntity.width);
    int yWidth = xWidth;
    int zWidth = (int)navi.theEntity.height + 1;
    
    for (int j = pathLen - 1; j >= navi.currentPath.getCurrentPathIndex(); j--) {

      if (isDirectPathBetweenPoints(navi, vecEntityNow, navi.currentPath.getVectorFromIndex((Entity)navi.theEntity, j), xWidth, yWidth, zWidth)) {
        
        navi.currentPath.setCurrentPathIndex(j);
        
        break;
      } 
    } 
    
    if (navi.totalTicks - navi.ticksAtLastPos > 100) {
      
      if (vecEntityNow.squareDistanceTo(navi.lastPosCheck) < 2.25D)
      {
        navi.clearPathEntity();
      }
      
      navi.ticksAtLastPos = navi.totalTicks;
      navi.lastPosCheck.xCoord = vecEntityNow.xCoord;
      navi.lastPosCheck.yCoord = vecEntityNow.yCoord;
      navi.lastPosCheck.zCoord = vecEntityNow.zCoord;
    } 
  }

  private static boolean isDirectPathBetweenPoints(GPathNavigate navi, Vec3 vecEntityNow, Vec3 vetTarget, int xWidth, int yWidth, int zWidth) {
    int entityXInt = MathHelper.floor_double(vecEntityNow.xCoord);
    int entityYInt = (int)vecEntityNow.yCoord;
    double distX = vetTarget.xCoord - vecEntityNow.xCoord;
    double distY = vetTarget.yCoord - vecEntityNow.yCoord;
    double distXY = distX * distX + distY * distY;
    
    if (distXY < 1.0E-8D)
    {
      return false;
    }

    double divideXY = 1.0D / Math.sqrt(distXY);
    distX *= divideXY;
    distY *= divideXY;
    xWidth += 2;
    yWidth += 2;

    if (!isSafeToStandAt(navi, entityXInt, entityYInt, MathHelper.floor_double(vecEntityNow.zCoord), xWidth, yWidth, zWidth, vecEntityNow, distX, distY))
    {
      return false;
    }

    xWidth -= 2;
    yWidth -= 2;
    double divideX = 1.0D / Math.abs(distX);
    double divideY = 1.0D / Math.abs(distY);
    double moveAmountX = (entityXInt * 1) - vecEntityNow.xCoord;
    double moveAmountY = (entityYInt * 1) - vecEntityNow.yCoord;
    
    if (distX >= 0.0D)
    {
      moveAmountX++;
    }
    
    if (distY >= 0.0D)
    {
      moveAmountY++;
    }
    
    moveAmountX /= distX;
    moveAmountY /= distY;
    int directionX = (distX < 0.0D) ? -1 : 1;
    int directionY = (distY < 0.0D) ? -1 : 1;
    int targetXInt = MathHelper.floor_double(vetTarget.xCoord);
    int targetYInt = MathHelper.floor_double(vetTarget.yCoord);
    int distanceXInt = targetXInt - entityXInt;
    int distanceYInt = targetYInt - entityYInt;

    do {
      if (distanceXInt * directionX <= 0 && distanceYInt * directionY <= 0)
      {
        return true;
      }
      
      if (moveAmountX < moveAmountY)
      {
        moveAmountX += divideX;
        entityXInt += directionX;
        distanceXInt = targetXInt - entityXInt;
      }
      else
      {
        moveAmountY += divideY;
        entityYInt += directionY;
        distanceYInt = targetYInt - entityYInt;
      }
    
    } while (isSafeToStandAt(navi, entityXInt, entityYInt, MathHelper.floor_double(vecEntityNow.zCoord), xWidth, yWidth, zWidth, vecEntityNow, distX, distY));
    
    return false;
  }

  private static boolean isSafeToStandAt(GPathNavigate navi, int xCoord, int yCoord, int zCoord, int xWidth, int yWidth, int zWidth, Vec3 vecEntityNow, double distX, double distY) {
    int minX = xCoord - xWidth / 2;
    int minY = yCoord - yWidth / 2;
    
    if (!isPositionClear(navi.worldObj, minX, minY, zCoord, xWidth, yWidth, zWidth, vecEntityNow, distX, distY))
    {
      
      return false;
    }

    int verticalFix = (navi.gDir == GravityDirection.southTOnorth_ZN) ? -1 : 1;

    for (int xx = minX; xx < minX + xWidth; xx++) {
      
      for (int yy = minY; yy < minY + yWidth; yy++) {
        
        double dX = xx + 0.5D - vecEntityNow.xCoord;
        double dY = yy + 0.5D - vecEntityNow.yCoord;
        
        if (dX * distX + dY * distY >= 0.0D) {
          
          Block block = navi.worldObj.getBlock(xx, yy, zCoord + verticalFix);
          Material material = block.getMaterial();

          if (material == Material.air)
          {
            return false;
          }
          
          if (material == Material.water && !navi.theEntity.isInWater())
          {
            return false;
          }
          
          if (material == Material.lava)
          {
            return false;
          }
        } 
      } 
    } 
    
    return true;
  }

  private static boolean isPositionClear(World world, int minX, int minY, int minZ, int xWidth, int yWidth, int zWidth, Vec3 vecEntityNow, double distX, double distY) {
    for (int xx = minX; xx < minX + xWidth; xx++) {
      
      for (int yy = minY; yy < minY + yWidth; yy++) {
        
        for (int zz = minZ; zz < minZ + zWidth; zz++) {
          
          double d2 = xx + 0.5D - vecEntityNow.xCoord;
          double d3 = yy + 0.5D - vecEntityNow.yCoord;
          
          if (d2 * distX + d3 * distY >= 0.0D) {
            
            Block block = world.getBlock(xx, yy, zz);
            
            if (!block.isPassable((IBlockAccess)world, xx, yy, zz))
            {
              return false;
            }
          } 
        } 
      } 
    } 
    
    return true;
  }
}
