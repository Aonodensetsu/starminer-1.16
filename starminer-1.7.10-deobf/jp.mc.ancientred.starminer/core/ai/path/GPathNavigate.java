package jp.mc.ancientred.starminer.core.ai.path;

import jp.mc.ancientred.starminer.api.GravityDirection;
import jp.mc.ancientred.starminer.core.entity.ExtendedPropertyGravity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class GPathNavigate
  extends PathNavigate
{
  protected EntityLiving theEntity;
  protected World worldObj;
  public PathEntity currentPath;
  private double speed;
  private IAttributeInstance pathSearchRange;
  private boolean noSunPathfind;
  protected int totalTicks;
  protected int ticksAtLastPos;
  protected Vec3 lastPosCheck = Vec3.createVectorHelper(0.0D, 0.0D, 0.0D);
  
  private boolean canPassOpenWoodenDoors = true;
  private boolean canPassClosedWoodenDoors;
  private boolean avoidsWater;
  protected boolean canSwim;
  protected GravityDirection gDir = GravityDirection.upTOdown_YN;

  public GPathNavigate(EntityLiving entityLiving, World world) {
    super(entityLiving, world);
    this.theEntity = entityLiving;
    this.worldObj = world;
    this.pathSearchRange = entityLiving.getEntityAttribute(SharedMonsterAttributes.followRange);
  }
  
  private void updateGDir() {
    ExtendedPropertyGravity gravity = ExtendedPropertyGravity.getExtendedPropertyGravity((Entity)this.theEntity);
    if (gravity != null) {
      this.gDir = gravity.gravityDirection;
    } else {
      this.gDir = GravityDirection.upTOdown_YN;
    } 
  }

  public void setAvoidsWater(boolean avoidsWater) {
    this.avoidsWater = avoidsWater;
  }

  public boolean getAvoidsWater() {
    return this.avoidsWater;
  }

  public void setBreakDoors(boolean canPassClosedWoodenDoors) {
    this.canPassClosedWoodenDoors = canPassClosedWoodenDoors;
  }

  public void setEnterDoors(boolean canPassOpenWoodenDoors) {
    this.canPassOpenWoodenDoors = canPassOpenWoodenDoors;
  }

  public boolean getCanBreakDoors() {
    return this.canPassClosedWoodenDoors;
  }

  public void setAvoidSun(boolean noSunPathfind) {
    this.noSunPathfind = noSunPathfind;
  }

  public void setSpeed(double speed) {
    this.speed = speed;
  }

  public void setCanSwim(boolean canSwim) {
    this.canSwim = canSwim;
  }

  public float getPathSearchRange() {
    return (float)this.pathSearchRange.getAttributeValue();
  }

  public GPathEntity getPathToXYZ(double targetX, double targetY, double targetZ) {
    updateGDir();
    return !canNavigate() ? null : getEntityPathToXYZPrivate(this.worldObj, (Entity)this.theEntity, MathHelper.floor_double(targetX), (int)targetY, MathHelper.floor_double(targetZ), getPathSearchRange(), this.canPassOpenWoodenDoors, this.canPassClosedWoodenDoors, this.avoidsWater, this.canSwim);
  }

  public boolean tryMoveToXYZ(double targetX, double targetY, double targetZ, double speed) {
    updateGDir();
    GPathEntity pathentity = getPathToXYZ(MathHelper.floor_double(targetX), (int)targetY, MathHelper.floor_double(targetZ));

    return setPath(pathentity, speed);
  }

  public GPathEntity getPathToEntityLiving(Entity targetEntity) {
    updateGDir();
    return !canNavigate() ? null : getPathEntityToEntityPrivate(this.worldObj, (Entity)this.theEntity, targetEntity, getPathSearchRange(), this.canPassOpenWoodenDoors, this.canPassClosedWoodenDoors, this.avoidsWater, this.canSwim);
  }

  public boolean tryMoveToEntityLiving(Entity targetEntity, double speed) {
    updateGDir();
    GPathEntity pathentity = getPathToEntityLiving(targetEntity);
    return (pathentity != null) ? setPath(pathentity, speed) : false;
  }

  private GPathEntity getPathEntityToEntityPrivate(World world, Entity entity, Entity targetEntity, float pathSearchRange, boolean canOpenWDoor, boolean canPassClosedDoor, boolean avoidsWater, boolean canSwim) {
    world.theProfiler.startSection("pathfind");
    int entityX = MathHelper.floor_double(entity.posX);
    int entityY = MathHelper.floor_double(entity.posY + 1.0D);
    int entityZ = MathHelper.floor_double(entity.posZ);
    int expand = (int)(pathSearchRange + 16.0F);
    int minX = entityX - expand;
    int minY = entityY - expand;
    int minZ = entityZ - expand;
    int maxX = entityX + expand;
    int maxY = entityY + expand;
    int maxZ = entityZ + expand;
    ChunkCache chunkcache = new ChunkCache(world, minX, minY, minZ, maxX, maxY, maxZ, 0);
    GPathEntity pathentity = (new GPathFinder((IBlockAccess)chunkcache, canOpenWDoor, canPassClosedDoor, avoidsWater, canSwim)).createEntityPathTo(entity, targetEntity, pathSearchRange);
    world.theProfiler.endSection();
    return pathentity;
  }

  private GPathEntity getEntityPathToXYZPrivate(World world, Entity entity, int targetXInt, int targetYInt, int targetZInt, float pathSearchRange, boolean canOpenWDoor, boolean canPassClosedDoor, boolean avoidsWater, boolean canSwim) {
    world.theProfiler.startSection("pathfind");
    int entityX = MathHelper.floor_double(entity.posX);
    int entityY = MathHelper.floor_double(entity.posY);
    int entityZ = MathHelper.floor_double(entity.posZ);
    int expand = (int)(pathSearchRange + 8.0F);
    int minX = entityX - expand;
    int minY = entityY - expand;
    int minZ = entityZ - expand;
    int maxX = entityX + expand;
    int maxY = entityY + expand;
    int maxZ = entityZ + expand;
    ChunkCache chunkcache = new ChunkCache(world, minX, minY, minZ, maxX, maxY, maxZ, 0);
    GPathEntity pathentity = (new GPathFinder((IBlockAccess)chunkcache, canOpenWDoor, canPassClosedDoor, avoidsWater, canSwim)).createEntityPathTo(entity, targetXInt, targetYInt, targetZInt, pathSearchRange);
    world.theProfiler.endSection();
    return pathentity;
  }

  public boolean setPath(PathEntity pathEntity, double speed) {
    updateGDir();
    if (pathEntity == null) {
      
      this.currentPath = null;
      return false;
    } 

    if (!pathEntity.isSamePath(this.currentPath))
    {
      this.currentPath = pathEntity;
    }
    
    if (this.noSunPathfind)
    {
      removeSunnyPath();
    }
    
    if (this.currentPath.getCurrentPathLength() == 0)
    {
      return false;
    }

    this.speed = speed;
    Vec3 vec3 = null;
    switch (this.gDir)
    { case northTOsouth_ZP:
      case southTOnorth_ZN:
        vec3 = PathFollowZ.getEntityPosition(this);

        this.ticksAtLastPos = this.totalTicks;
        this.lastPosCheck.xCoord = vec3.xCoord;
        this.lastPosCheck.yCoord = vec3.yCoord;
        this.lastPosCheck.zCoord = vec3.zCoord;
        return true;case westTOeast_XP: case eastTOwest_XN: vec3 = PathFollowX.getEntityPosition(this); this.ticksAtLastPos = this.totalTicks; this.lastPosCheck.xCoord = vec3.xCoord; this.lastPosCheck.yCoord = vec3.yCoord; this.lastPosCheck.zCoord = vec3.zCoord; return true; }  vec3 = PathFollowY.getEntityPosition(this); this.ticksAtLastPos = this.totalTicks; this.lastPosCheck.xCoord = vec3.xCoord; this.lastPosCheck.yCoord = vec3.yCoord; this.lastPosCheck.zCoord = vec3.zCoord; return true;
  }

  public PathEntity getPath() {
    return this.currentPath;
  }

  public void onUpdateNavigation() {
    updateGDir();
    this.totalTicks++;
    
    if (!noPath()) {
      
      if (canNavigate())
      {
        switch (this.gDir) {
          case northTOsouth_ZP:
          case southTOnorth_ZN:
            PathFollowZ.pathFollow(this);
            break;
          case westTOeast_XP:
          case eastTOwest_XN:
            PathFollowX.pathFollow(this);
            break;
          
          default:
            PathFollowY.pathFollow(this);
            break;
        } 
      
      }
      if (!noPath()) {
        
        Vec3 vec3 = this.currentPath.getPosition((Entity)this.theEntity);
        if (vec3 != null)
        {
          this.theEntity.getMoveHelper().setMoveTo(vec3.xCoord, vec3.yCoord, vec3.zCoord, this.speed);
        }
      } 
    } 
  }

  public boolean noPath() {
    return (this.currentPath == null || this.currentPath.isFinished());
  }

  public void clearPathEntity() {
    this.currentPath = null;
  }

  private boolean canNavigate() {
    return (this.theEntity.onGround || (this.canSwim && isInLiquid()) || (this.theEntity.isRiding() && this.theEntity instanceof net.minecraft.entity.monster.EntityZombie && this.theEntity.ridingEntity instanceof net.minecraft.entity.passive.EntityChicken));
  }

  private boolean isInLiquid() {
    return (this.theEntity.isInWater() || this.theEntity.handleLavaMovement());
  }

  private void removeSunnyPath() {
    if (this.worldObj.provider instanceof jp.mc.ancientred.starminer.api.IZeroGravityWorldProvider)
      return; 
    if (!this.worldObj.canBlockSeeTheSky(MathHelper.floor_double(this.theEntity.posX), (int)(this.theEntity.boundingBox.minY + 0.5D), MathHelper.floor_double(this.theEntity.posZ)))
    {
      for (int i = 0; i < this.currentPath.getCurrentPathLength(); i++) {
        
        PathPoint pathpoint = this.currentPath.getPathPointFromIndex(i);
        
        if (this.worldObj.canBlockSeeTheSky(pathpoint.xCoord, pathpoint.yCoord, pathpoint.zCoord)) {
          
          this.currentPath.setCurrentPathLength(i - 1);
          return;
        } 
      } 
    }
  }
}
