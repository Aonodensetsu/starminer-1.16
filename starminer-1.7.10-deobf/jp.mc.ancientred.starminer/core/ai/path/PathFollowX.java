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

public class PathFollowX {
  public static Vec3 getEntityPosition(GPathNavigate navi) {
    return Vec3.createVectorHelper(getPathableXPos(navi) + 0.5D, navi.theEntity.posY, navi.theEntity.posZ);
  }

  private static int getPathableXPos(GPathNavigate navi) {
    int verticalFix = 1;
    double footX = navi.theEntity.boundingBox.minX;
    if (navi.gDir == GravityDirection.westTOeast_XP) {
      verticalFix = -1;
      footX = navi.theEntity.boundingBox.maxX;
    } 
    
    if (navi.theEntity.isInWater() && navi.canSwim) {
      
      int posZInt = (int)footX;
      Block block = navi.worldObj.getBlock(MathHelper.floor_double(navi.theEntity.posX), (int)navi.theEntity.posY, posZInt);
      int searchLimitCnt = 0;
      
      do {
        if (block != Blocks.flowing_water && block != Blocks.water)
        {
          return posZInt;
        }
        
        posZInt += verticalFix;
        block = navi.worldObj.getBlock(MathHelper.floor_double(navi.theEntity.posX), (int)navi.theEntity.posY, posZInt);
        ++searchLimitCnt;
      }
      while (searchLimitCnt <= 16);
      
      return MathHelper.floor_double(footX);
    } 

    return MathHelper.floor_double(footX + verticalFix * 0.5D);
  }

  public static void pathFollow(GPathNavigate navi) {
    Vec3 vecEntityNow = getEntityPosition(navi);
    int pathSearchLen = navi.currentPath.getCurrentPathLength();

    int floorX = MathHelper.floor_double(vecEntityNow.xCoord);
    for (int pathIndex = navi.currentPath.getCurrentPathIndex(); pathIndex < navi.currentPath.getCurrentPathLength(); pathIndex++) {
      
      if ((navi.currentPath.getPathPointFromIndex(pathIndex)).xCoord != floorX) {
        
        pathSearchLen = pathIndex;
        
        break;
      } 
    } 
    float wRange = navi.theEntity.width * navi.theEntity.width;
    
    for (int i = navi.currentPath.getCurrentPathIndex(); i < pathSearchLen; i++) {

      
      if (vecEntityNow.squareDistanceTo(navi.currentPath.getVectorFromIndex((Entity)navi.theEntity, i)) < wRange)
      {
        navi.currentPath.setCurrentPathIndex(i + 1);
      }
    } 
    
    int xWidth = (int)navi.theEntity.height + 1;
    int yWidth = MathHelper.ceiling_float_int(navi.theEntity.width);
    int zWidth = yWidth;
    
    for (int j = pathSearchLen - 1; j >= navi.currentPath.getCurrentPathIndex(); j--) {

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
    int entityYInt = (int)vecEntityNow.yCoord;
    int entityZInt = MathHelper.floor_double(vecEntityNow.zCoord);
    double distY = vetTarget.yCoord - vecEntityNow.yCoord;
    double distZ = vetTarget.zCoord - vecEntityNow.zCoord;
    double distYZ = distY * distY + distZ * distZ;
    
    if (distYZ < 1.0E-8D)
    {
      return false;
    }

    double divideYZ = 1.0D / Math.sqrt(distYZ);
    distY *= divideYZ;
    distZ *= divideYZ;
    yWidth += 2;
    zWidth += 2;

    if (!isSafeToStandAt(navi, MathHelper.floor_double(vecEntityNow.xCoord), entityYInt, entityZInt, xWidth, yWidth, zWidth, vecEntityNow, distY, distZ))
    {
      return false;
    }

    yWidth -= 2;
    zWidth -= 2;
    double divideY = 1.0D / Math.abs(distY);
    double divideZ = 1.0D / Math.abs(distZ);
    double moveAmountY = (entityYInt * 1) - vecEntityNow.yCoord;
    double moveAmountZ = (entityZInt * 1) - vecEntityNow.zCoord;
    
    if (distY >= 0.0D)
    {
      moveAmountY++;
    }
    
    if (distZ >= 0.0D)
    {
      moveAmountZ++;
    }
    
    moveAmountY /= distY;
    moveAmountZ /= distZ;
    int directionY = (distY < 0.0D) ? -1 : 1;
    int directionZ = (distZ < 0.0D) ? -1 : 1;
    int targetYInt = MathHelper.floor_double(vetTarget.yCoord);
    int targetZInt = MathHelper.floor_double(vetTarget.zCoord);
    int distanceYInt = targetYInt - entityYInt;
    int distanceZInt = targetZInt - entityZInt;

    do {
      if (distanceYInt * directionY <= 0 && distanceZInt * directionZ <= 0)
      {
        return true;
      }
      
      if (moveAmountY < moveAmountZ)
      {
        moveAmountY += divideY;
        entityYInt += directionY;
        distanceYInt = targetYInt - entityYInt;
      }
      else
      {
        moveAmountZ += divideZ;
        entityZInt += directionZ;
        distanceZInt = targetZInt - entityZInt;
      }
    
    } while (isSafeToStandAt(navi, MathHelper.floor_double(vecEntityNow.xCoord), entityYInt, entityZInt, xWidth, yWidth, zWidth, vecEntityNow, distY, distZ));
    
    return false;
  }

  private static boolean isSafeToStandAt(GPathNavigate navi, int xCoord, int yCoord, int zCoord, int xWidth, int yWidth, int zWidth, Vec3 vecEntityNow, double distY, double distZ) {
    int minY = yCoord - yWidth / 2;
    int minZ = zCoord - zWidth / 2;
    
    if (!isPositionClear(navi.worldObj, xCoord, minY, minZ, xWidth, yWidth, zWidth, vecEntityNow, distY, distZ))
    {
      
      return false;
    }

    int verticalFix = (navi.gDir == GravityDirection.eastTOwest_XN) ? -1 : 1;

    for (int yy = minY; yy < minY + yWidth; yy++) {
      
      for (int zz = minZ; zz < minZ + zWidth; zz++) {
        
        double dY = yy + 0.5D - vecEntityNow.yCoord;
        double dZ = zz + 0.5D - vecEntityNow.zCoord;
        
        if (dY * distY + dZ * distZ >= 0.0D) {
          
          Block block = navi.worldObj.getBlock(xCoord + verticalFix, yy, zz);
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

  private static boolean isPositionClear(World world, int minX, int minY, int minZ, int xWidth, int yWidth, int zWidth, Vec3 vecEntityNow, double distY, double distZ) {
    for (int xx = minX; xx < minX + xWidth; xx++) {
      
      for (int yy = minY; yy < minY + yWidth; yy++) {
        
        for (int zz = minZ; zz < minZ + zWidth; zz++) {
          
          double d2 = yy + 0.5D - vecEntityNow.yCoord;
          double d3 = zz + 0.5D - vecEntityNow.zCoord;
          
          if (d2 * distY + d3 * distZ >= 0.0D) {
            
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
