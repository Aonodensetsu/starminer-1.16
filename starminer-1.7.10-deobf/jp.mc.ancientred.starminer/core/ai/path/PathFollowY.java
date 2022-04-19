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

public class PathFollowY {
  public static Vec3 getEntityPosition(GPathNavigate navi) {
    return Vec3.createVectorHelper(navi.theEntity.posX, getPathableYPos(navi) + 0.5D, navi.theEntity.posZ);
  }

  private static int getPathableYPos(GPathNavigate navi) {
    int verticalFix = 1;
    double footY = navi.theEntity.boundingBox.minY;
    if (navi.gDir == GravityDirection.downTOup_YP) {
      verticalFix = -1;
      footY = navi.theEntity.boundingBox.maxY;
    } 
    
    if (navi.theEntity.isInWater() && navi.canSwim) {
      
      int posYInt = (int)footY;
      Block block = navi.worldObj.getBlock(MathHelper.floor_double(navi.theEntity.posX), posYInt, MathHelper.floor_double(navi.theEntity.posZ));
      int searchLimitCnt = 0;

      do {
        if (block != Blocks.flowing_water && block != Blocks.water)
        {
          return posYInt;
        }
        
        posYInt += verticalFix;
        block = navi.worldObj.getBlock(MathHelper.floor_double(navi.theEntity.posX), posYInt, MathHelper.floor_double(navi.theEntity.posZ));
        ++searchLimitCnt;
      }
      while (searchLimitCnt <= 16);
      
      return (int)footY;
    } 

    return (int)(footY + verticalFix * 0.5D);
  }

  public static void pathFollow(GPathNavigate navi) {
    Vec3 vecEntityNow = getEntityPosition(navi);
    int pathLen = navi.currentPath.getCurrentPathLength();

    int floorY = MathHelper.floor_double(vecEntityNow.yCoord);
    for (int pathIndex = navi.currentPath.getCurrentPathIndex(); pathIndex < navi.currentPath.getCurrentPathLength(); pathIndex++) {
      
      if ((navi.currentPath.getPathPointFromIndex(pathIndex)).yCoord != floorY) {
        
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
    int yWidth = (int)navi.theEntity.height + 1;
    int zWidth = xWidth;
    
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
    int entityZInt = MathHelper.floor_double(vecEntityNow.zCoord);
    double distX = vetTarget.xCoord - vecEntityNow.xCoord;
    double distZ = vetTarget.zCoord - vecEntityNow.zCoord;
    double distXZ = distX * distX + distZ * distZ;
    
    if (distXZ < 1.0E-8D)
    {
      return false;
    }

    double divideXZ = 1.0D / Math.sqrt(distXZ);
    distX *= divideXZ;
    distZ *= divideXZ;
    xWidth += 2;
    zWidth += 2;

    if (!isSafeToStandAt(navi, entityXInt, (int)vecEntityNow.yCoord, entityZInt, xWidth, yWidth, zWidth, vecEntityNow, distX, distZ))
    {
      return false;
    }

    xWidth -= 2;
    zWidth -= 2;
    double divideX = 1.0D / Math.abs(distX);
    double divideZ = 1.0D / Math.abs(distZ);
    double moveAmountX = (entityXInt * 1) - vecEntityNow.xCoord;
    double moveAmountZ = (entityZInt * 1) - vecEntityNow.zCoord;
    
    if (distX >= 0.0D)
    {
      moveAmountX++;
    }
    
    if (distZ >= 0.0D)
    {
      moveAmountZ++;
    }
    
    moveAmountX /= distX;
    moveAmountZ /= distZ;
    int directionX = (distX < 0.0D) ? -1 : 1;
    int directionZ = (distZ < 0.0D) ? -1 : 1;
    int targetXInt = MathHelper.floor_double(vetTarget.xCoord);
    int targetZInt = MathHelper.floor_double(vetTarget.zCoord);
    int distanceXInt = targetXInt - entityXInt;
    int distanceZInt = targetZInt - entityZInt;

    do {
      if (distanceXInt * directionX <= 0 && distanceZInt * directionZ <= 0)
      {
        return true;
      }
      
      if (moveAmountX < moveAmountZ)
      {
        moveAmountX += divideX;
        entityXInt += directionX;
        distanceXInt = targetXInt - entityXInt;
      }
      else
      {
        moveAmountZ += divideZ;
        entityZInt += directionZ;
        distanceZInt = targetZInt - entityZInt;
      }
    
    } while (isSafeToStandAt(navi, entityXInt, (int)vecEntityNow.yCoord, entityZInt, xWidth, yWidth, zWidth, vecEntityNow, distX, distZ));
    
    return false;
  }

  private static boolean isSafeToStandAt(GPathNavigate navi, int xCoord, int yCoord, int zCoord, int xWidth, int yWidth, int zWidth, Vec3 vecEntityNow, double distX, double distZ) {
    int minX = xCoord - xWidth / 2;
    int minZ = zCoord - zWidth / 2;
    
    if (!isPositionClear(navi.worldObj, minX, yCoord, minZ, xWidth, yWidth, zWidth, vecEntityNow, distX, distZ))
    {
      
      return false;
    }

    int verticalFix = (navi.gDir == GravityDirection.upTOdown_YN) ? -1 : 1;

    for (int xx = minX; xx < minX + xWidth; xx++) {
      
      for (int zz = minZ; zz < minZ + zWidth; zz++) {
        
        double dX = xx + 0.5D - vecEntityNow.xCoord;
        double dZ = zz + 0.5D - vecEntityNow.zCoord;
        
        if (dX * distX + dZ * distZ >= 0.0D) {
          
          Block block = navi.worldObj.getBlock(xx, yCoord + verticalFix, zz);
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

  private static boolean isPositionClear(World world, int minX, int minY, int minZ, int xWidth, int yWidth, int zWidth, Vec3 vecEntityNow, double distX, double distZ) {
    for (int xx = minX; xx < minX + xWidth; xx++) {
      
      for (int yy = minY; yy < minY + yWidth; yy++) {
        
        for (int zz = minZ; zz < minZ + zWidth; zz++) {
          
          double d2 = xx + 0.5D - vecEntityNow.xCoord;
          double d3 = zz + 0.5D - vecEntityNow.zCoord;
          
          if (d2 * distX + d3 * distZ >= 0.0D) {
            
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
