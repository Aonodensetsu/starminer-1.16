package jp.mc.ancientred.starminer.core.entity;

import java.lang.ref.WeakReference;
import jp.mc.ancientred.starminer.api.Gravity;
import jp.mc.ancientred.starminer.api.GravityDirection;
import jp.mc.ancientred.starminer.api.IAttractableTileEntity;
import jp.mc.ancientred.starminer.core.TransformUtils;
import jp.mc.ancientred.starminer.core.common.VecUtils;
import jp.mc.ancientred.starminer.core.packet.SMCorePacketSender;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class ExtendedPropertyGravity
  extends Gravity
{
  private static final String NBT_TAG_KEY = "stmn.gravity";
  private static final float TURN_SPEED_START = 0.05F;
  private WeakReference<Entity> entityWeakRef;
  public int temporatyZeroGTick;
  public int keepGravityDirTick;
  public int normalGravityEffectRedistTick = 0;
  
  public float turnRate = 100.0F;
  public float prevTurnRate = 100.0F;
  public float turnSpeed = 0.0F;
  
  public float onChangeRoatDirX = 0.0F;
  public float onChangeRoatDirY = 0.0F;
  public float onChangeRoatDirZ = 0.0F;
  public float onChangeTurnYaw = 0.0F;

  public int illegalGStateTickCount = 0;
  
  public int unsynchronizedWarnCount = 0;

  public boolean changeGravityImmidiate;

  public void init(Entity entity, World world) {
    this.entityWeakRef = new WeakReference<Entity>(entity);
  }

  public void saveNBTData(NBTTagCompound compound) {
    NBTTagCompound myNBT = new NBTTagCompound();
    myNBT.setFloat("turnRate", this.turnRate);
    myNBT.setFloat("turnSpeed", this.turnSpeed);
    
    myNBT.setFloat("onChangeRoatDirX", this.onChangeRoatDirX);
    myNBT.setFloat("onChangeRoatDirY", this.onChangeRoatDirY);
    myNBT.setFloat("onChangeRoatDirZ", this.onChangeRoatDirZ);
    myNBT.setFloat("onChangeTurnYaw", this.onChangeTurnYaw);
    
    myNBT.setBoolean("isAttracted", this.isAttracted);
    myNBT.setInteger("attractedPosX", this.attractedPosX);
    myNBT.setInteger("attractedPosY", this.attractedPosY);
    myNBT.setInteger("attractedPosZ", this.attractedPosZ);
    
    myNBT.setInteger("gdir", this.gravityDirection.ordinal());
    
    compound.setTag("stmn.gravity", (NBTBase)myNBT);
  }

  public void loadNBTData(NBTTagCompound compound) {
    if (compound.hasKey("stmn.gravity")) {
      NBTTagCompound myNBT = compound.getCompoundTag("stmn.gravity");
      this.turnRate = myNBT.getFloat("turnRate");
      this.turnSpeed = myNBT.getFloat("turnSpeed");
      
      this.onChangeRoatDirX = myNBT.getFloat("onChangeRoatDirX");
      this.onChangeRoatDirY = myNBT.getFloat("onChangeRoatDirY");
      this.onChangeRoatDirZ = myNBT.getFloat("onChangeRoatDirZ");
      this.onChangeTurnYaw = myNBT.getFloat("onChangeTurnYaw");
      
      this.isAttracted = myNBT.getBoolean("isAttracted");
      this.attractedPosX = myNBT.getInteger("attractedPosX");
      this.attractedPosY = myNBT.getInteger("attractedPosY");
      this.attractedPosZ = myNBT.getInteger("attractedPosZ");
      
      if (myNBT.hasKey("gdir")) {
        try {
          int gdir = myNBT.getInteger("gdir");
          if (gdir >= 0 && gdir < (GravityDirection.values()).length) {
            this.gravityDirectionNext = this.gravityDirection = GravityDirection.values()[gdir];
            Entity entity = this.entityWeakRef.get();
            if (entity instanceof EntityLivingGravitized) {
              entity.setPosition(entity.posX, entity.posY, entity.posZ);
              ((EntityLivingGravitized)entity).hideInEntityGDirToPotionDw(this.gravityDirection);
            } 
          } 
        } catch (Exception ex) {}
      }
    } 
  }

  public boolean isZeroGravity() {
    Entity entity = this.entityWeakRef.get();
    if (entity != null) return isZeroGravity(entity.worldObj.provider instanceof jp.mc.ancientred.starminer.api.IZeroGravityWorldProvider); 
    return false;
  }
  
  public boolean isAttracted() {
    return this.isAttracted;
  }
  
  public void setAttractedBy(IAttractableTileEntity tileEntity) {
    if (tileEntity instanceof TileEntity) {
      TileEntity te = (TileEntity)tileEntity;
      
      Entity entity = this.entityWeakRef.get();
      if (entity != null) {
        
        this.isAttracted = true;
        this.attractedPosX = te.xCoord;
        this.attractedPosY = te.yCoord;
        this.attractedPosZ = te.zCoord;
        if (entity instanceof EntityPlayerMP) {
          
          SMCorePacketSender.sendAttractedChangePacketToPlayer((EntityPlayerMP)entity, true, false, te.xCoord, te.yCoord, te.zCoord);

        }
        else {
 
          SMCorePacketSender.sendMobAttractedChangePacketToAllAround(entity, true, false, te.xCoord, te.yCoord, te.zCoord);
        } 
      } 
    } 
  }

  public boolean isAttractedBy(IAttractableTileEntity tileEntity) {
    if (this.isAttracted && tileEntity instanceof TileEntity) {
      TileEntity te = (TileEntity)tileEntity;
      if (te.xCoord == this.attractedPosX && te.yCoord == this.attractedPosY && te.zCoord == this.attractedPosZ)
      {
        
        return true;
      }
    } 
    return false;
  }
  
  public void loseAttractedBy() {
    Entity entity = this.entityWeakRef.get();
    if (entity != null) {
      
      this.isAttracted = false;
      if (entity instanceof EntityPlayerMP) {
        
        SMCorePacketSender.sendAttractedChangePacketToPlayer((EntityPlayerMP)entity, false, false, 0, 0, 0);

      }
      else {

        SMCorePacketSender.sendMobAttractedChangePacketToAllAround(entity, false, false, 0, 0, 0);
      } 
    } 
  }

  public Vec3 getGravityFixedLook(float pitch, float yaw) {
    float zFix, xFix;
    switch (this.gravityDirection) {
      case southTOnorth_ZN:
        zVal = MathHelper.sin(-pitch * 0.017453292F);
        zFix = MathHelper.cos(-pitch * 0.017453292F);
        yVal = MathHelper.cos(-yaw * 0.017453292F - 3.1415927F);
        xVal = -MathHelper.sin(-yaw * 0.017453292F - 3.1415927F);
        return VecUtils.createVec3((xVal * zFix), (yVal * zFix), zVal);
      case northTOsouth_ZP:
        zVal = -MathHelper.sin(-pitch * 0.017453292F);
        zFix = MathHelper.cos(-pitch * 0.017453292F);
        yVal = -MathHelper.cos(-yaw * 0.017453292F - 3.1415927F);
        xVal = -MathHelper.sin(-yaw * 0.017453292F - 3.1415927F);
        return VecUtils.createVec3((xVal * zFix), (yVal * zFix), zVal);
      case westTOeast_XP:
        xVal = -MathHelper.sin(-pitch * 0.017453292F);
        xFix = MathHelper.cos(-pitch * 0.017453292F);
        zVal = -MathHelper.cos(-yaw * 0.017453292F - 3.1415927F);
        yVal = -MathHelper.sin(-yaw * 0.017453292F - 3.1415927F);
        return VecUtils.createVec3(xVal, (yVal * xFix), (zVal * xFix));
      case eastTOwest_XN:
        xVal = MathHelper.sin(-pitch * 0.017453292F);
        xFix = MathHelper.cos(-pitch * 0.017453292F);
        zVal = -MathHelper.cos(-yaw * 0.017453292F - 3.1415927F);
        yVal = MathHelper.sin(-yaw * 0.017453292F - 3.1415927F);
        return VecUtils.createVec3(xVal, (yVal * xFix), (zVal * xFix));
      case downTOup_YP:
        yVal = -MathHelper.sin(-pitch * 0.017453292F);
        yFix = MathHelper.cos(-pitch * 0.017453292F);
        zVal = MathHelper.cos(-yaw * 0.017453292F - 3.1415927F);
        xVal = -MathHelper.sin(-yaw * 0.017453292F - 3.1415927F);
        return VecUtils.createVec3((xVal * yFix), yVal, (zVal * yFix));
    } 
    float yVal = MathHelper.sin(-pitch * 0.017453292F);
    float yFix = -MathHelper.cos(-pitch * 0.017453292F);
    float zVal = MathHelper.cos(-yaw * 0.017453292F - 3.1415927F);
    float xVal = MathHelper.sin(-yaw * 0.017453292F - 3.1415927F);
    return VecUtils.createVec3((xVal * yFix), yVal, (zVal * yFix));
  }

  public Vec3 getGravityFixedPlayerEyePoz(EntityPlayer parEntityPlayer, float partialTick) {
    World world = parEntityPlayer.worldObj;
    double d0 = parEntityPlayer.prevPosX + (parEntityPlayer.posX - parEntityPlayer.prevPosX) * partialTick;
    double d1 = parEntityPlayer.prevPosY + (parEntityPlayer.posY - parEntityPlayer.prevPosY) * partialTick + (world.isRemote ? (parEntityPlayer.getEyeHeight() - parEntityPlayer.getDefaultEyeHeight()) : parEntityPlayer.getEyeHeight());
    double d2 = parEntityPlayer.prevPosZ + (parEntityPlayer.posZ - parEntityPlayer.prevPosZ) * partialTick;
    
    Vec3 fixedPozVec3 = VecUtils.createVec3(d0, d1, d2);
    
    if (parEntityPlayer instanceof EntityPlayerMP) {
      fixedPozVec3 = TransformUtils.fixEyePositionByGravityServer(parEntityPlayer, fixedPozVec3);
    } else {
      fixedPozVec3 = TransformUtils.fixEyePositionByGravityClient((Entity)parEntityPlayer, fixedPozVec3);
    } 
    return fixedPozVec3;
  }

  public void setGravityFixedPlayerShootVec(EntityPlayer parShooterEntity, Entity projectileEntity, float partialTick) {
    Vec3 fixedPozVec3 = getGravityFixedPlayerEyePoz(parShooterEntity, partialTick);
    
    World world = parShooterEntity.worldObj;
    float f1 = parShooterEntity.prevRotationPitch + (parShooterEntity.rotationPitch - parShooterEntity.prevRotationPitch) * partialTick;
    float f2 = parShooterEntity.prevRotationYaw + (parShooterEntity.rotationYaw - parShooterEntity.prevRotationYaw) * partialTick;
    
    float f3 = MathHelper.cos(-f2 * 0.017453292F - 3.1415927F);
    float f4 = MathHelper.sin(-f2 * 0.017453292F - 3.1415927F);
    float f5 = -MathHelper.cos(-f1 * 0.017453292F);
    float f6 = MathHelper.sin(-f1 * 0.017453292F);
    float f7 = f4 * f5;
    float f8 = f3 * f5;
    
    Vec3 vecLook = parShooterEntity.getLook(partialTick);
    projectileEntity.posX = fixedPozVec3.xCoord + vecLook.xCoord;
    projectileEntity.posY = fixedPozVec3.yCoord + vecLook.yCoord;
    projectileEntity.posZ = fixedPozVec3.zCoord + vecLook.zCoord;
    projectileEntity.setPosition(projectileEntity.posX, projectileEntity.posY, projectileEntity.posZ);
    projectileEntity.yOffset = 0.0F;
    
    projectileEntity.motionX = vecLook.xCoord;
    projectileEntity.motionZ = vecLook.zCoord;
    projectileEntity.motionY = vecLook.yCoord;
  }

  public void setResistInOpaqueBlockDamegeTick(int tick) {
    Entity entity = this.entityWeakRef.get();
    if (entity instanceof EntityLivingGravitized) {
      ((EntityLivingGravitized)entity).redistInOpaqueBlockDamageTick = 20;
    }
  }

  public void updateGravityDirectionState(Entity entity) {
    GravityDirection gravityDirNew = this.gravityDirection;

    if (this.normalGravityEffectRedistTick > 0) {
      return;
    }
    if (this.turnRate < 1.0F)
      return; 
    if (!this.isAttracted) {

      gravityDirNew = GravityDirection.upTOdown_YN;
    }
    else {
      
      TileEntity te = entity.worldObj.getTileEntity(this.attractedPosX, this.attractedPosY, this.attractedPosZ);
      if (te == null || !(te instanceof IAttractableTileEntity))
        return;  IAttractableTileEntity teGravity = (IAttractableTileEntity)te;

      gravityDirNew = teGravity.getCurrentGravity(entity);
    } 

    if (this.gravityDirection != gravityDirNew) {
      
      if (this.changeGravityImmidiate) {
        
        this.gravityDirection = gravityDirNew;
        this.changeGravityImmidiate = false;
      } else {
        
        changeGravityDirection(gravityDirNew);
      } 

      this.keepGravityDirTick = 15;
    } 
  }
  public void setTemporaryZeroGravity(int tick) {
    this.temporatyZeroGTick = tick;
  }

  public void setTemporaryGravityDirection(GravityDirection nextGravityDirection, int tick) {
    GravityDirection old = this.gravityDirection;
    
    if (this.keepGravityDirTick <= 0 && this.turnRate >= 1.0F) {
      changeGravityDirection(nextGravityDirection);
    }
    
    this.normalGravityEffectRedistTick = tick;
    
    if (old != this.gravityDirectionNext)
    {
      
      this.keepGravityDirTick = 30;
    }
  }

  public void changeGravityDirection(GravityDirection nextGravityDirection) {
    if (this.gravityDirection == nextGravityDirection)
      return;  this.prevTurnRate = this.turnRate = 0.0F;
    this.turnSpeed = 0.05F;
    this.onChangeRoatDirX = 0.0F;
    this.onChangeRoatDirY = 0.0F;
    this.onChangeRoatDirZ = 0.0F;
    this.onChangeTurnYaw = 0.0F;
    
    switch (this.gravityDirection) {
      case northTOsouth_ZP:
        switch (nextGravityDirection) {

          case southTOnorth_ZN:
            this.onChangeRoatDirX = -2.0F;
            break;
          case westTOeast_XP:
            this.onChangeRoatDirY = -1.0F;
            this.onChangeTurnYaw = -90.0F;
            break;
          case eastTOwest_XN:
            this.onChangeRoatDirY = 1.0F;
            this.onChangeTurnYaw = 90.0F;
            break;
          case downTOup_YP:
            this.onChangeRoatDirX = 1.0F;
            break;
          case upTOdown_YN:
            this.onChangeRoatDirX = -1.0F;
            break;
        } 
        break;
      case southTOnorth_ZN:
        switch (nextGravityDirection) {
          case northTOsouth_ZP:
            this.onChangeRoatDirX = -2.0F;
            break;

          case westTOeast_XP:
            this.onChangeRoatDirY = 1.0F;
            this.onChangeTurnYaw = 90.0F;
            break;
          case eastTOwest_XN:
            this.onChangeRoatDirY = -1.0F;
            this.onChangeTurnYaw = -90.0F;
            break;
          case downTOup_YP:
            this.onChangeRoatDirX = -1.0F;
            break;
          case upTOdown_YN:
            this.onChangeRoatDirX = 1.0F;
            break;
        } 
        break;
      case westTOeast_XP:
        switch (nextGravityDirection) {
          case northTOsouth_ZP:
            this.onChangeRoatDirY = 1.0F;
            this.onChangeTurnYaw = 90.0F;
            break;
          case southTOnorth_ZN:
            this.onChangeRoatDirY = -1.0F;
            this.onChangeTurnYaw = -90.0F;
            break;

          case eastTOwest_XN:
            this.onChangeRoatDirZ = -2.0F;
            break;
          case downTOup_YP:
            this.onChangeRoatDirZ = -1.0F;
            this.onChangeTurnYaw = -180.0F;
            break;
          case upTOdown_YN:
            this.onChangeRoatDirZ = 1.0F;
            break;
        } 
        break;
      case eastTOwest_XN:
        switch (nextGravityDirection) {
          case northTOsouth_ZP:
            this.onChangeRoatDirY = -1.0F;
            this.onChangeTurnYaw = -90.0F;
            break;
          case southTOnorth_ZN:
            this.onChangeRoatDirY = 1.0F;
            this.onChangeTurnYaw = 90.0F;
            break;
          case westTOeast_XP:
            this.onChangeRoatDirZ = -2.0F;
            break;

          case downTOup_YP:
            this.onChangeRoatDirZ = 1.0F;
            this.onChangeTurnYaw = -180.0F;
            break;
          case upTOdown_YN:
            this.onChangeRoatDirZ = -1.0F;
            break;
        } 
        break;
      case downTOup_YP:
        switch (nextGravityDirection) {
          case northTOsouth_ZP:
            this.onChangeRoatDirX = -1.0F;
            break;
          case southTOnorth_ZN:
            this.onChangeRoatDirX = 1.0F;
            break;
          case westTOeast_XP:
            this.onChangeRoatDirZ = 1.0F;
            this.onChangeTurnYaw = 180.0F;
            break;
          case eastTOwest_XN:
            this.onChangeRoatDirZ = -1.0F;
            this.onChangeTurnYaw = 180.0F;
            break;

          case upTOdown_YN:
            this.onChangeRoatDirX = -2.0F;
            break;
        } 
        break;
      case upTOdown_YN:
        switch (nextGravityDirection) {
          case northTOsouth_ZP:
            this.onChangeRoatDirX = 1.0F;
            break;
          case southTOnorth_ZN:
            this.onChangeRoatDirX = -1.0F;
            break;
          case westTOeast_XP:
            this.onChangeRoatDirZ = -1.0F;
            break;
          case eastTOwest_XN:
            this.onChangeRoatDirZ = 1.0F;
            break;
          case downTOup_YP:
            this.onChangeRoatDirX = -2.0F;
            break;
        } 

        break;
    } 
    
    this.gravityDirectionNext = nextGravityDirection;
  }

  public void updateAtractedStateAndGravityZero(Entity entity) {
    if (this.isAttracted && 
      !isStillAttracted(entity))
    {
      loseAttractedBy();
    }
  }

  private boolean isStillAttracted(Entity entity) {
    Block block = entity.worldObj.getBlock(this.attractedPosX, this.attractedPosY, this.attractedPosZ);
    if (block == null) return false;

    TileEntity tileEntity = entity.worldObj.getTileEntity(this.attractedPosX, this.attractedPosY, this.attractedPosZ);
    if (!(tileEntity instanceof IAttractableTileEntity)) return false;

    return ((IAttractableTileEntity)tileEntity).isStillInAttractedState(entity);
  }

  public void validateGravity(EntityPlayer entityPlayer, boolean isGravityZero) {
    if (!this.isAttracted && !isGravityZero && this.gravityDirection != GravityDirection.upTOdown_YN && !entityPlayer.isPlayerSleeping())
    {

      if (this.acceptExceptionalGravityTick > 0)
      {
        this.illegalGStateTickCount += 2;
      }
    }

    this.illegalGStateTickCount--;
    if (this.illegalGStateTickCount < 0) {
      this.illegalGStateTickCount = 0;
    }
  }

  public static ExtendedPropertyGravity getExtendedPropertyGravity(Entity entity) {
    return (ExtendedPropertyGravity)entity.getExtendedProperties("starminer.Gravity");
  }

  public boolean isZeroGravity(boolean isOutSpace) {
    return (this.temporatyZeroGTick > 0 || (!this.isAttracted && isOutSpace && this.normalGravityEffectRedistTick <= 0));
  }

  public static boolean isAttracted(Entity entity) {
    if (entity != null) {
      ExtendedPropertyGravity gp = getExtendedPropertyGravity(entity);
      return (gp != null && gp.isAttracted);
    } 
    return false;
  }

  public static boolean isEntityAbnormalGravity(Entity entity) {
    if (entity != null) {
      ExtendedPropertyGravity gp = getExtendedPropertyGravity(entity);
      return (gp != null && gp.gravityDirection != GravityDirection.upTOdown_YN);
    } 
    return false;
  }

  public static boolean isEntityNormalGravity(Entity entity) {
    if (entity != null) {
      ExtendedPropertyGravity gp = getExtendedPropertyGravity(entity);
      return (gp == null || gp.gravityDirection == GravityDirection.upTOdown_YN);
    } 
    return true;
  }
}
