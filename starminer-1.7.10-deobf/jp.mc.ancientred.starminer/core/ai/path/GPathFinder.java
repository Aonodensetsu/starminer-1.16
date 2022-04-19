package jp.mc.ancientred.starminer.core.ai.path;

import jp.mc.ancientred.starminer.api.GravityDirection;
import jp.mc.ancientred.starminer.core.entity.ExtendedPropertyGravity;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IntHashMap;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;

public class GPathFinder {
  private IBlockAccess worldMap;
  private GPath path = new GPath();
  private IntHashMap pointMap = new IntHashMap();
  private GPathPoint[] pathOptions = new GPathPoint[32];
  
  private boolean isWoddenDoorAllowed;
  private boolean isMovementBlockAllowed;
  private boolean isPathingInWater;
  private boolean canEntityDrown;
  private static final int OFFWATER_SEARCH_LIM = 30;
  private static final int SAFETRYCNT_MAX = 10;
  private GravityDirection gDir = GravityDirection.upTOdown_YN;
  private static final int RENDERTYPE_RAIL = 9;
  
  public GPathFinder(IBlockAccess p_i2137_1_, boolean p_i2137_2_, boolean p_i2137_3_, boolean p_i2137_4_, boolean p_i2137_5_) {
    this.worldMap = p_i2137_1_;
    this.isWoddenDoorAllowed = p_i2137_2_;
    this.isMovementBlockAllowed = p_i2137_3_;
    this.isPathingInWater = p_i2137_4_;
    this.canEntityDrown = p_i2137_5_;
  }

  public GPathEntity createEntityPathTo(Entity entity, Entity dstEntity, float distanceRange) {
    double dWidthHalf = (dstEntity.width / 2.0F);
    AxisAlignedBB bb = dstEntity.boundingBox;
    switch (ExtendedPropertyGravity.getGravityDirection(entity)) {
      case northTOsouth_ZP:
        return createEntityPathTo(entity, (bb.maxX + bb.minX) / 2.0D, (bb.maxY + bb.minY) / 2.0D, bb.maxZ - dWidthHalf, distanceRange);

      case southTOnorth_ZN:
        return createEntityPathTo(entity, (bb.maxX + bb.minX) / 2.0D, (bb.maxY + bb.minY) / 2.0D, bb.minZ + dWidthHalf, distanceRange);

      case westTOeast_XP:
        return createEntityPathTo(entity, bb.maxX - dWidthHalf, (bb.maxY + bb.minY) / 2.0D, (bb.maxZ + bb.minZ) / 2.0D, distanceRange);

      case eastTOwest_XN:
        return createEntityPathTo(entity, bb.minX + dWidthHalf, (bb.maxY + bb.minY) / 2.0D, (bb.maxZ + bb.minZ) / 2.0D, distanceRange);

      case downTOup_YP:
        return createEntityPathTo(entity, (bb.maxX + bb.minX) / 2.0D, bb.maxY - dWidthHalf, (bb.maxZ + bb.minZ) / 2.0D, distanceRange);
    } 

    return createEntityPathTo(entity, (bb.maxX + bb.minX) / 2.0D, bb.minY + dWidthHalf, (bb.maxZ + bb.minZ) / 2.0D, distanceRange);
  }

  public GPathEntity createEntityPathTo(Entity entity, int targetXInt, int targetYInt, int targetZInt, float distanceRange) {
    return createEntityPathTo(entity, (targetXInt + 0.5F), (targetYInt + 0.5F), (targetZInt + 0.5F), distanceRange);
  }

  private GPathEntity createEntityPathTo(Entity entity, double targetX, double targetY, double targetZ, float distanceRange) {
    GPathPoint GPathPointStart, gPathPoint2;
    ExtendedPropertyGravity gravity = ExtendedPropertyGravity.getExtendedPropertyGravity(entity);
    if (gravity != null) {
      this.gDir = gravity.gravityDirection;
    } else {
      this.gDir = GravityDirection.upTOdown_YN;
    } 
    
    this.path.clearPath();
    this.pointMap.clearMap();
    boolean flag = this.isPathingInWater;
    int xx = MathHelper.floor_double(entity.boundingBox.minX + (entity.boundingBox.maxX - entity.boundingBox.minX) / 2.0D);
    int yy = MathHelper.floor_double(entity.boundingBox.minY + (entity.boundingBox.maxY - entity.boundingBox.minY) / 2.0D);
    int zz = MathHelper.floor_double(entity.boundingBox.minZ + (entity.boundingBox.maxZ - entity.boundingBox.minZ) / 2.0D);
    int searchCnt = 0;
    switch (this.gDir)
    { case northTOsouth_ZP:
        zz = MathHelper.floor_double(entity.boundingBox.maxZ - 0.5D);
        if (this.canEntityDrown && entity.isInWater()) {
          
          zz = (int)entity.boundingBox.maxZ;
          
          Block block = this.worldMap.getBlock(MathHelper.floor_double(entity.posX), MathHelper.floor_double(entity.posY), zz);
          for (; block == Blocks.flowing_water || block == Blocks.water; 
            block = this.worldMap.getBlock(MathHelper.floor_double(entity.posX), MathHelper.floor_double(entity.posY), zz)) {
            
            zz--;
            if (++searchCnt >= 30)
              break; 
          } 
          flag = this.isPathingInWater;
          this.isPathingInWater = false;
        }
        else {
          
          zz = MathHelper.floor_double(entity.boundingBox.maxZ - 0.5D);
        } 

        GPathPointStart = openPoint(xx, yy, zz);
        
        GPathPointEnd = openPoint(MathHelper.floor_double(targetX), MathHelper.floor_double(targetY), MathHelper.floor_double(targetZ));
        GPathPointCollideCheck = getCollideCheck(entity);
        GPathEntity = addToPath(entity, GPathPointStart, GPathPointEnd, GPathPointCollideCheck, distanceRange);
        this.isPathingInWater = flag;
        return GPathEntity;case southTOnorth_ZN: zz = MathHelper.floor_double(entity.boundingBox.minZ + 0.5D); if (this.canEntityDrown && entity.isInWater()) { zz = (int)entity.boundingBox.minZ; Block block = this.worldMap.getBlock(MathHelper.floor_double(entity.posX), MathHelper.floor_double(entity.posY), zz); for (; block == Blocks.flowing_water || block == Blocks.water; block = this.worldMap.getBlock(MathHelper.floor_double(entity.posX), MathHelper.floor_double(entity.posY), zz)) { zz++; if (++searchCnt >= 30) break;  }  flag = this.isPathingInWater; this.isPathingInWater = false; } else { zz = MathHelper.floor_double(entity.boundingBox.minZ + 0.5D); }  gPathPoint2 = openPoint(xx, yy, zz); GPathPointEnd = openPoint(MathHelper.floor_double(targetX), MathHelper.floor_double(targetY), MathHelper.floor_double(targetZ)); GPathPointCollideCheck = getCollideCheck(entity); GPathEntity = addToPath(entity, gPathPoint2, GPathPointEnd, GPathPointCollideCheck, distanceRange); this.isPathingInWater = flag; return GPathEntity;case westTOeast_XP: xx = MathHelper.floor_double(entity.boundingBox.maxX - 0.5D); if (this.canEntityDrown && entity.isInWater()) { xx = (int)entity.boundingBox.maxX; Block block = this.worldMap.getBlock(xx, MathHelper.floor_double(entity.posY), MathHelper.floor_double(entity.posZ)); for (; block == Blocks.flowing_water || block == Blocks.water; block = this.worldMap.getBlock(xx, MathHelper.floor_double(entity.posY), MathHelper.floor_double(entity.posZ))) { xx--; if (++searchCnt >= 30) break;  }  flag = this.isPathingInWater; this.isPathingInWater = false; } else { xx = MathHelper.floor_double(entity.boundingBox.maxX - 0.5D); }  gPathPoint1 = openPoint(xx, yy, zz); GPathPointEnd = openPoint(MathHelper.floor_double(targetX), MathHelper.floor_double(targetY), MathHelper.floor_double(targetZ)); GPathPointCollideCheck = getCollideCheck(entity); GPathEntity = addToPath(entity, gPathPoint1, GPathPointEnd, GPathPointCollideCheck, distanceRange); this.isPathingInWater = flag; return GPathEntity;case eastTOwest_XN: xx = MathHelper.floor_double(entity.boundingBox.minX + 0.5D); if (this.canEntityDrown && entity.isInWater()) { xx = (int)entity.boundingBox.minX; Block block = this.worldMap.getBlock(xx, MathHelper.floor_double(entity.posY), MathHelper.floor_double(entity.posZ)); for (; block == Blocks.flowing_water || block == Blocks.water; block = this.worldMap.getBlock(xx, MathHelper.floor_double(entity.posY), MathHelper.floor_double(entity.posZ))) { xx++; if (++searchCnt >= 30) break;  }  flag = this.isPathingInWater; this.isPathingInWater = false; } else { xx = MathHelper.floor_double(entity.boundingBox.minX + 0.5D); }  gPathPoint1 = openPoint(xx, yy, zz); GPathPointEnd = openPoint(MathHelper.floor_double(targetX), MathHelper.floor_double(targetY), MathHelper.floor_double(targetZ)); GPathPointCollideCheck = getCollideCheck(entity); GPathEntity = addToPath(entity, gPathPoint1, GPathPointEnd, GPathPointCollideCheck, distanceRange); this.isPathingInWater = flag; return GPathEntity;case downTOup_YP: yy = MathHelper.floor_double(entity.boundingBox.maxY - 0.5D); if (this.canEntityDrown && entity.isInWater()) { yy = (int)entity.boundingBox.maxY; Block block = this.worldMap.getBlock(MathHelper.floor_double(entity.posX), yy, MathHelper.floor_double(entity.posZ)); for (; block == Blocks.flowing_water || block == Blocks.water; block = this.worldMap.getBlock(MathHelper.floor_double(entity.posX), yy, MathHelper.floor_double(entity.posZ))) { yy--; if (++searchCnt >= 30) break;  }  flag = this.isPathingInWater; this.isPathingInWater = false; } else { yy = MathHelper.floor_double(entity.boundingBox.maxY - 0.5D); }  gPathPoint1 = openPoint(xx, yy, zz); GPathPointEnd = openPoint(MathHelper.floor_double(targetX), MathHelper.floor_double(targetY), MathHelper.floor_double(targetZ)); GPathPointCollideCheck = getCollideCheck(entity); GPathEntity = addToPath(entity, gPathPoint1, GPathPointEnd, GPathPointCollideCheck, distanceRange); this.isPathingInWater = flag; return GPathEntity; }  yy = MathHelper.floor_double(entity.boundingBox.minY + 0.5D); if (this.canEntityDrown && entity.isInWater()) { yy = (int)entity.boundingBox.minY; Block block = this.worldMap.getBlock(MathHelper.floor_double(entity.posX), yy, MathHelper.floor_double(entity.posZ)); for (; block == Blocks.flowing_water || block == Blocks.water; block = this.worldMap.getBlock(MathHelper.floor_double(entity.posX), yy, MathHelper.floor_double(entity.posZ))) { yy++; if (++searchCnt >= 30) break;  }  flag = this.isPathingInWater; this.isPathingInWater = false; } else { yy = MathHelper.floor_double(entity.boundingBox.minY + 0.5D); }  GPathPoint gPathPoint1 = openPoint(xx, yy, zz); GPathPoint GPathPointEnd = openPoint(MathHelper.floor_double(targetX), MathHelper.floor_double(targetY), MathHelper.floor_double(targetZ)); GPathPoint GPathPointCollideCheck = getCollideCheck(entity); GPathEntity GPathEntity = addToPath(entity, gPathPoint1, GPathPointEnd, GPathPointCollideCheck, distanceRange); this.isPathingInWater = flag; return GPathEntity;
  }
  
  private GPathPoint getCollideCheck(Entity entity) {
    GPathPoint ret = null;
    
    switch (this.gDir)
    { case northTOsouth_ZP:
        ret = new GPathPoint(MathHelper.floor_float(entity.width), MathHelper.floor_float(entity.width), -MathHelper.floor_float(entity.height));

        return ret;case southTOnorth_ZN: ret = new GPathPoint(MathHelper.floor_float(entity.width), MathHelper.floor_float(entity.width), MathHelper.floor_float(entity.height)); return ret;case westTOeast_XP: ret = new GPathPoint(-MathHelper.floor_float(entity.height), MathHelper.floor_float(entity.width), MathHelper.floor_float(entity.width)); return ret;case eastTOwest_XN: ret = new GPathPoint(MathHelper.floor_float(entity.height), MathHelper.floor_float(entity.width), MathHelper.floor_float(entity.width)); return ret;case downTOup_YP: ret = new GPathPoint(MathHelper.floor_float(entity.width), -MathHelper.floor_float(entity.height), MathHelper.floor_float(entity.width)); return ret; }  ret = new GPathPoint(MathHelper.floor_float(entity.width), MathHelper.floor_float(entity.height), MathHelper.floor_float(entity.width)); return ret;
  }

  private GPathEntity addToPath(Entity entity, GPathPoint startPoint, GPathPoint endPoint, GPathPoint collideCheck, float maxDistance) {
    startPoint.totalPathDistance = 0.0F;
    startPoint.distanceToNext = startPoint.distanceToSquared(endPoint);
    startPoint.distanceToTarget = startPoint.distanceToNext;
    this.path.clearPath();
    this.path.addPoint(startPoint);
    GPathPoint GPathPoint3 = startPoint;
    
    while (!this.path.isPathEmpty()) {
      
      GPathPoint GPathPoint4 = this.path.dequeue();
      
      if (GPathPoint4.equals(endPoint))
      {
        return createEntityPath(startPoint, endPoint);
      }
      
      if (GPathPoint4.distanceToSquared(endPoint) < GPathPoint3.distanceToSquared(endPoint))
      {
        GPathPoint3 = GPathPoint4;
      }
      
      GPathPoint4.visited = true;
      int i = findPathOptions(entity, GPathPoint4, collideCheck, endPoint, maxDistance);
      
      for (int j = 0; j < i; j++) {
        
        GPathPoint GPathPoint5 = this.pathOptions[j];
        float f1 = GPathPoint4.totalPathDistance + GPathPoint4.distanceToSquared(GPathPoint5);
        
        if (!GPathPoint5.isAssigned() || f1 < GPathPoint5.totalPathDistance) {
          
          GPathPoint5.previous = GPathPoint4;
          GPathPoint5.totalPathDistance = f1;
          GPathPoint5.distanceToNext = GPathPoint5.distanceToSquared(endPoint);
          
          if (GPathPoint5.isAssigned()) {
            
            this.path.changeDistance(GPathPoint5, GPathPoint5.totalPathDistance + GPathPoint5.distanceToNext);
          }
          else {
            
            GPathPoint5.distanceToTarget = GPathPoint5.totalPathDistance + GPathPoint5.distanceToNext;
            this.path.addPoint(GPathPoint5);
          } 
        } 
      } 
    } 
    
    if (GPathPoint3 == startPoint)
    {
      return null;
    }

    return createEntityPath(startPoint, GPathPoint3);
  }

  private int findPathOptions(Entity entity, GPathPoint currentPoint, GPathPoint collideCheck, GPathPoint endPoint, float maxDistance) {
    int i = 0;
    byte vCheckLen = 0;
    
    GPathPoint GPathPoint3 = null;
    GPathPoint GPathPoint4 = null;
    GPathPoint GPathPoint5 = null;
    GPathPoint GPathPoint6 = null;
    switch (this.gDir) {
      case northTOsouth_ZP:
        if (getVerticalOffset(entity, currentPoint.xCoord, currentPoint.yCoord, currentPoint.zCoord - 1, collideCheck) == 1)
        {
          vCheckLen = 1;
        }
        
        GPathPoint3 = getSafePointZP(entity, currentPoint.xCoord + 1, currentPoint.yCoord, currentPoint.zCoord, collideCheck, vCheckLen);
        GPathPoint4 = getSafePointZP(entity, currentPoint.xCoord, currentPoint.yCoord + 1, currentPoint.zCoord, collideCheck, vCheckLen);
        GPathPoint5 = getSafePointZP(entity, currentPoint.xCoord, currentPoint.yCoord - 1, currentPoint.zCoord, collideCheck, vCheckLen);
        GPathPoint6 = getSafePointZP(entity, currentPoint.xCoord - 1, currentPoint.yCoord, currentPoint.zCoord, collideCheck, vCheckLen);
        break;
      case southTOnorth_ZN:
        if (getVerticalOffset(entity, currentPoint.xCoord, currentPoint.yCoord, currentPoint.zCoord + 1, collideCheck) == 1)
        {
          vCheckLen = 1;
        }
        
        GPathPoint3 = getSafePointZN(entity, currentPoint.xCoord + 1, currentPoint.yCoord, currentPoint.zCoord, collideCheck, vCheckLen);
        GPathPoint4 = getSafePointZN(entity, currentPoint.xCoord, currentPoint.yCoord + 1, currentPoint.zCoord, collideCheck, vCheckLen);
        GPathPoint5 = getSafePointZN(entity, currentPoint.xCoord, currentPoint.yCoord - 1, currentPoint.zCoord, collideCheck, vCheckLen);
        GPathPoint6 = getSafePointZN(entity, currentPoint.xCoord - 1, currentPoint.yCoord, currentPoint.zCoord, collideCheck, vCheckLen);
        break;
      case westTOeast_XP:
        if (getVerticalOffset(entity, currentPoint.xCoord - 1, currentPoint.yCoord, currentPoint.zCoord, collideCheck) == 1)
        {
          vCheckLen = 1;
        }
        
        GPathPoint3 = getSafePointXP(entity, currentPoint.xCoord, currentPoint.yCoord + 1, currentPoint.zCoord, collideCheck, vCheckLen);
        GPathPoint4 = getSafePointXP(entity, currentPoint.xCoord, currentPoint.yCoord, currentPoint.zCoord + 1, collideCheck, vCheckLen);
        GPathPoint5 = getSafePointXP(entity, currentPoint.xCoord, currentPoint.yCoord, currentPoint.zCoord - 1, collideCheck, vCheckLen);
        GPathPoint6 = getSafePointXP(entity, currentPoint.xCoord, currentPoint.yCoord - 1, currentPoint.zCoord, collideCheck, vCheckLen);
        break;
      case eastTOwest_XN:
        if (getVerticalOffset(entity, currentPoint.xCoord + 1, currentPoint.yCoord, currentPoint.zCoord, collideCheck) == 1)
        {
          vCheckLen = 1;
        }
        
        GPathPoint3 = getSafePointXN(entity, currentPoint.xCoord, currentPoint.yCoord + 1, currentPoint.zCoord, collideCheck, vCheckLen);
        GPathPoint4 = getSafePointXN(entity, currentPoint.xCoord, currentPoint.yCoord, currentPoint.zCoord + 1, collideCheck, vCheckLen);
        GPathPoint5 = getSafePointXN(entity, currentPoint.xCoord, currentPoint.yCoord, currentPoint.zCoord - 1, collideCheck, vCheckLen);
        GPathPoint6 = getSafePointXN(entity, currentPoint.xCoord, currentPoint.yCoord - 1, currentPoint.zCoord, collideCheck, vCheckLen);
        break;
      case downTOup_YP:
        if (getVerticalOffset(entity, currentPoint.xCoord, currentPoint.yCoord - 1, currentPoint.zCoord, collideCheck) == 1)
        {
          vCheckLen = 1;
        }
        
        GPathPoint3 = getSafePointYP(entity, currentPoint.xCoord, currentPoint.yCoord, currentPoint.zCoord + 1, collideCheck, vCheckLen);
        GPathPoint4 = getSafePointYP(entity, currentPoint.xCoord - 1, currentPoint.yCoord, currentPoint.zCoord, collideCheck, vCheckLen);
        GPathPoint5 = getSafePointYP(entity, currentPoint.xCoord + 1, currentPoint.yCoord, currentPoint.zCoord, collideCheck, vCheckLen);
        GPathPoint6 = getSafePointYP(entity, currentPoint.xCoord, currentPoint.yCoord, currentPoint.zCoord - 1, collideCheck, vCheckLen);
        break;
      default:
        if (getVerticalOffset(entity, currentPoint.xCoord, currentPoint.yCoord + 1, currentPoint.zCoord, collideCheck) == 1)
        {
          vCheckLen = 1;
        }
        
        GPathPoint3 = getSafePointYN(entity, currentPoint.xCoord, currentPoint.yCoord, currentPoint.zCoord + 1, collideCheck, vCheckLen);
        GPathPoint4 = getSafePointYN(entity, currentPoint.xCoord - 1, currentPoint.yCoord, currentPoint.zCoord, collideCheck, vCheckLen);
        GPathPoint5 = getSafePointYN(entity, currentPoint.xCoord + 1, currentPoint.yCoord, currentPoint.zCoord, collideCheck, vCheckLen);
        GPathPoint6 = getSafePointYN(entity, currentPoint.xCoord, currentPoint.yCoord, currentPoint.zCoord - 1, collideCheck, vCheckLen);
        break;
    } 

    if (GPathPoint3 != null && !GPathPoint3.visited && GPathPoint3.distanceTo(endPoint) < maxDistance)
    {
      this.pathOptions[i++] = GPathPoint3;
    }
    
    if (GPathPoint4 != null && !GPathPoint4.visited && GPathPoint4.distanceTo(endPoint) < maxDistance)
    {
      this.pathOptions[i++] = GPathPoint4;
    }
    
    if (GPathPoint5 != null && !GPathPoint5.visited && GPathPoint5.distanceTo(endPoint) < maxDistance)
    {
      this.pathOptions[i++] = GPathPoint5;
    }
    
    if (GPathPoint6 != null && !GPathPoint6.visited && GPathPoint6.distanceTo(endPoint) < maxDistance)
    {
      this.pathOptions[i++] = GPathPoint6;
    }
    
    return i;
  }

  private final GPathPoint openPoint(int p_75854_1_, int p_75854_2_, int p_75854_3_) {
    int l = GPathPoint.makeHash(p_75854_1_, p_75854_2_, p_75854_3_);
    GPathPoint gPathPoint = (GPathPoint)this.pointMap.lookup(l);
    
    if (gPathPoint == null) {
      
      gPathPoint = new GPathPoint(p_75854_1_, p_75854_2_, p_75854_3_);
      this.pointMap.addKey(l, gPathPoint);
    } 
    
    return gPathPoint;
  }

  public int getVerticalOffset(Entity entity, int xCoord, int yCoord, int zCoord, GPathPoint collideCheck) {
    return canEntityStandAt(entity, xCoord, yCoord, zCoord, collideCheck, this.isPathingInWater, this.isMovementBlockAllowed, this.isWoddenDoorAllowed);
  }

  public static int canEntityStandAt(Entity entity, int xCoord, int yCoord, int zCoord, GPathPoint collideCheck, boolean isPathingInWater, boolean isMovementBlockAllowed, boolean isWoddenDoorAllowed) {
    boolean onTrap = false;
    
    int xMin = Math.min(xCoord, xCoord + collideCheck.xCoord);
    int xMax = Math.max(xCoord, xCoord + collideCheck.xCoord);
    int yMin = Math.min(yCoord, yCoord + collideCheck.yCoord);
    int yMax = Math.max(yCoord, yCoord + collideCheck.yCoord);
    int zMin = Math.min(zCoord, zCoord + collideCheck.zCoord);
    int zMax = Math.max(zCoord, zCoord + collideCheck.zCoord);
    for (int x = xMin; x <= xMax; x++) {
      
      for (int y = yMin; y <= yMax; y++) {
        
        for (int z = zMin; z <= zMax; z++) {
          
          Block block = entity.worldObj.getBlock(x, y, z);
          
          if (block.getMaterial() != Material.air) {
            
            if (block == Blocks.trapdoor) {
              
              onTrap = true;
            }
            else if (block != Blocks.flowing_water && block != Blocks.water) {
              
              if (!isWoddenDoorAllowed && block == Blocks.wooden_door)
              {
                return 0;
              }
            }
            else {
              
              if (isPathingInWater)
              {
                return -1;
              }
              
              onTrap = true;
            } 
            
            int blockRenderType = block.getRenderType();
            
            if (entity.worldObj.getBlock(x, y, z).getRenderType() == 9) {
              
              int j2 = MathHelper.floor_double(entity.posX);
              int l1 = MathHelper.floor_double(entity.posY);
              int i2 = MathHelper.floor_double(entity.posZ);
              
              if (entity.worldObj.getBlock(j2, l1, i2).getRenderType() != 9 && entity.worldObj.getBlock(j2, l1 - 1, i2).getRenderType() != 9)
              {
                
                return -3;
              }
            }
            else if (!block.isPassable((IBlockAccess)entity.worldObj, x, y, z) && (!isMovementBlockAllowed || block != Blocks.wooden_door)) {

              if (blockRenderType == 11 || block == Blocks.fence_gate || blockRenderType == 32)
              {
                return -3;
              }
              
              if (block == Blocks.trapdoor)
              {
                return -4;
              }
              
              Material material = block.getMaterial();
              
              if (material != Material.lava)
              {
                
                return 0;
              }
              
              if (!entity.handleLavaMovement())
              {
                
                return -2;
              }
            } 
          } 
        } 
      } 
    } 
    
    return onTrap ? 2 : 1;
  }

  private GPathEntity createEntityPath(GPathPoint p_75853_1_, GPathPoint p_75853_2_) {
    int i = 1;
    
    GPathPoint GPathPoint2;
    for (GPathPoint2 = p_75853_2_; GPathPoint2.previous != null; GPathPoint2 = GPathPoint2.previous)
    {
      i++;
    }
    
    GPathPoint[] aGPathPoint = new GPathPoint[i];
    GPathPoint2 = p_75853_2_;
    i--;
    
    for (aGPathPoint[i] = p_75853_2_; GPathPoint2.previous != null; aGPathPoint[i] = GPathPoint2) {
      
      GPathPoint2 = GPathPoint2.previous;
      i--;
    } 
    
    return new GPathEntity(aGPathPoint);
  }

  private GPathPoint getSafePointZP(Entity entity, int xCoord, int yCoord, int zCoord, GPathPoint collideCheck, int vCheckLen) {
    GPathPoint GPathPoint1 = null;
    int vCheckRes = getVerticalOffset(entity, xCoord, yCoord, zCoord, collideCheck);
    
    if (vCheckRes == 2)
    {
      
      return openPoint(xCoord, yCoord, zCoord);
    }

    if (vCheckRes == 1)
    {
      GPathPoint1 = openPoint(xCoord, yCoord, zCoord);
    }
    
    if (GPathPoint1 == null && vCheckLen > 0 && vCheckRes != -3 && vCheckRes != -4 && getVerticalOffset(entity, xCoord, yCoord, zCoord - vCheckLen, collideCheck) == 1) {

      GPathPoint1 = openPoint(xCoord, yCoord, zCoord - vCheckLen);
      zCoord -= vCheckLen;
    } 
    
    if (GPathPoint1 != null) {

      int safePointTryCnt = 0;
      int vChreckResSub = 0;

      while (true) {
        vChreckResSub = getVerticalOffset(entity, xCoord, yCoord, zCoord + 1, collideCheck);
        
        if (this.isPathingInWater && vChreckResSub == -1)
        {
          return null;
        }
        
        if (vChreckResSub != 1) {
          break;
        }

        if (safePointTryCnt++ >= entity.getMaxFallHeight() || safePointTryCnt > 10)
        {
          
          return null;
        }
        
        zCoord++;
        
        GPathPoint1 = openPoint(xCoord, yCoord, zCoord);
      } 
      
      if (vChreckResSub == -2)
      {
        
        return null;
      }
    } 
    
    return GPathPoint1;
  }

  private GPathPoint getSafePointZN(Entity entity, int xCoord, int yCoord, int zCoord, GPathPoint collideCheck, int vCheckLen) {
    GPathPoint GPathPoint1 = null;
    int vCheckRes = getVerticalOffset(entity, xCoord, yCoord, zCoord, collideCheck);
    
    if (vCheckRes == 2)
    {
      
      return openPoint(xCoord, yCoord, zCoord);
    }

    if (vCheckRes == 1)
    {
      GPathPoint1 = openPoint(xCoord, yCoord, zCoord);
    }
    
    if (GPathPoint1 == null && vCheckLen > 0 && vCheckRes != -3 && vCheckRes != -4 && getVerticalOffset(entity, xCoord, yCoord, zCoord + vCheckLen, collideCheck) == 1) {

      GPathPoint1 = openPoint(xCoord, yCoord, zCoord + vCheckLen);
      zCoord += vCheckLen;
    } 
    
    if (GPathPoint1 != null) {

      int safePointTryCnt = 0;
      int vChreckResSub = 0;

      while (true) {
        vChreckResSub = getVerticalOffset(entity, xCoord, yCoord, zCoord - 1, collideCheck);
        
        if (this.isPathingInWater && vChreckResSub == -1)
        {
          return null;
        }
        
        if (vChreckResSub != 1) {
          break;
        }

        if (safePointTryCnt++ >= entity.getMaxFallHeight() || safePointTryCnt > 10)
        {
          
          return null;
        }
        
        zCoord--;
        
        GPathPoint1 = openPoint(xCoord, yCoord, zCoord);
      } 
      
      if (vChreckResSub == -2)
      {
        
        return null;
      }
    } 
    
    return GPathPoint1;
  }

  private GPathPoint getSafePointXP(Entity entity, int xCoord, int yCoord, int zCoord, GPathPoint collideCheck, int vCheckLen) {
    GPathPoint GPathPoint1 = null;
    int vCheckRes = getVerticalOffset(entity, xCoord, yCoord, zCoord, collideCheck);
    
    if (vCheckRes == 2)
    {
      
      return openPoint(xCoord, yCoord, zCoord);
    }

    if (vCheckRes == 1)
    {
      GPathPoint1 = openPoint(xCoord, yCoord, zCoord);
    }
    
    if (GPathPoint1 == null && vCheckLen > 0 && vCheckRes != -3 && vCheckRes != -4 && getVerticalOffset(entity, xCoord - vCheckLen, yCoord, zCoord, collideCheck) == 1) {

      GPathPoint1 = openPoint(xCoord - vCheckLen, yCoord, zCoord);
      xCoord -= vCheckLen;
    } 
    
    if (GPathPoint1 != null) {

      int safePointTryCnt = 0;
      int vChreckResSub = 0;

      while (true) {
        vChreckResSub = getVerticalOffset(entity, xCoord + 1, yCoord, zCoord, collideCheck);
        
        if (this.isPathingInWater && vChreckResSub == -1)
        {
          return null;
        }
        
        if (vChreckResSub != 1) {
          break;
        }

        if (safePointTryCnt++ >= entity.getMaxFallHeight() || safePointTryCnt > 10)
        {
          
          return null;
        }
        
        xCoord++;
        
        GPathPoint1 = openPoint(xCoord, yCoord, zCoord);
      } 
      
      if (vChreckResSub == -2)
      {
        
        return null;
      }
    } 
    
    return GPathPoint1;
  }

  private GPathPoint getSafePointXN(Entity entity, int xCoord, int yCoord, int zCoord, GPathPoint collideCheck, int vCheckLen) {
    GPathPoint GPathPoint1 = null;
    int vCheckRes = getVerticalOffset(entity, xCoord, yCoord, zCoord, collideCheck);
    
    if (vCheckRes == 2)
    {
      
      return openPoint(xCoord, yCoord, zCoord);
    }

    if (vCheckRes == 1)
    {
      GPathPoint1 = openPoint(xCoord, yCoord, zCoord);
    }
    
    if (GPathPoint1 == null && vCheckLen > 0 && vCheckRes != -3 && vCheckRes != -4 && getVerticalOffset(entity, xCoord + vCheckLen, yCoord, zCoord, collideCheck) == 1) {

      GPathPoint1 = openPoint(xCoord + vCheckLen, yCoord, zCoord);
      xCoord += vCheckLen;
    } 
    
    if (GPathPoint1 != null) {

      int safePointTryCnt = 0;
      int vChreckResSub = 0;

      while (true) {
        vChreckResSub = getVerticalOffset(entity, xCoord - 1, yCoord, zCoord, collideCheck);
        
        if (this.isPathingInWater && vChreckResSub == -1)
        {
          return null;
        }
        
        if (vChreckResSub != 1) {
          break;
        }

        if (safePointTryCnt++ >= entity.getMaxFallHeight() || safePointTryCnt > 10)
        {
          
          return null;
        }
        
        xCoord--;
        
        GPathPoint1 = openPoint(xCoord, yCoord, zCoord);
      } 
      
      if (vChreckResSub == -2)
      {
        
        return null;
      }
    } 
    
    return GPathPoint1;
  }

  private GPathPoint getSafePointYP(Entity entity, int xCoord, int yCoord, int zCoord, GPathPoint collideCheck, int vCheckLen) {
    GPathPoint GPathPoint1 = null;
    int vCheckRes = getVerticalOffset(entity, xCoord, yCoord, zCoord, collideCheck);
    
    if (vCheckRes == 2)
    {
      
      return openPoint(xCoord, yCoord, zCoord);
    }

    if (vCheckRes == 1)
    {
      GPathPoint1 = openPoint(xCoord, yCoord, zCoord);
    }
    
    if (GPathPoint1 == null && vCheckLen > 0 && vCheckRes != -3 && vCheckRes != -4 && getVerticalOffset(entity, xCoord, yCoord - vCheckLen, zCoord, collideCheck) == 1) {

      GPathPoint1 = openPoint(xCoord, yCoord - vCheckLen, zCoord);
      yCoord -= vCheckLen;
    } 
    
    if (GPathPoint1 != null) {

      int safePointTryCnt = 0;
      int vChreckResSub = 0;
      
      while (yCoord < 255) {
        
        vChreckResSub = getVerticalOffset(entity, xCoord, yCoord + 1, zCoord, collideCheck);
        
        if (this.isPathingInWater && vChreckResSub == -1)
        {
          return null;
        }
        
        if (vChreckResSub != 1) {
          break;
        }

        if (safePointTryCnt++ >= entity.getMaxFallHeight())
        {
          
          return null;
        }
        
        yCoord++;
        
        if (yCoord < 255)
        {
          GPathPoint1 = openPoint(xCoord, yCoord, zCoord);
        }
      } 
      
      if (vChreckResSub == -2)
      {
        
        return null;
      }
    } 
    
    return GPathPoint1;
  }

  private GPathPoint getSafePointYN(Entity entity, int xCoord, int yCoord, int zCoord, GPathPoint collideCheck, int vCheckLen) {
    GPathPoint GPathPoint1 = null;
    int vCheckRes = getVerticalOffset(entity, xCoord, yCoord, zCoord, collideCheck);
    
    if (vCheckRes == 2)
    {
      
      return openPoint(xCoord, yCoord, zCoord);
    }

    if (vCheckRes == 1)
    {
      GPathPoint1 = openPoint(xCoord, yCoord, zCoord);
    }
    
    if (GPathPoint1 == null && vCheckLen > 0 && vCheckRes != -3 && vCheckRes != -4 && getVerticalOffset(entity, xCoord, yCoord + vCheckLen, zCoord, collideCheck) == 1) {

      
      GPathPoint1 = openPoint(xCoord, yCoord + vCheckLen, zCoord);
      yCoord += vCheckLen;
    } 
    
    if (GPathPoint1 != null) {

      int safePointTryCnt = 0;
      int vChreckResSub = 0;
      
      while (yCoord > 0) {
        
        vChreckResSub = getVerticalOffset(entity, xCoord, yCoord - 1, zCoord, collideCheck);
        
        if (this.isPathingInWater && vChreckResSub == -1)
        {
          return null;
        }
        
        if (vChreckResSub != 1) {
          break;
        }

        
        if (safePointTryCnt++ >= entity.getMaxFallHeight())
        {
          
          return null;
        }
        
        yCoord--;
        
        if (yCoord > 0)
        {
          GPathPoint1 = openPoint(xCoord, yCoord, zCoord);
        }
      } 
      
      if (vChreckResSub == -2)
      {
        
        return null;
      }
    } 
    
    return GPathPoint1;
  }
}
