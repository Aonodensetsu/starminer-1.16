package jp.mc.ancientred.starminer.core.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.List;
import jp.mc.ancientred.starminer.api.GravityDirection;
import jp.mc.ancientred.starminer.core.CoreConfig;
import jp.mc.ancientred.starminer.core.SMCoreModContainer;
import jp.mc.ancientred.starminer.core.TransformUtils;
import jp.mc.ancientred.starminer.core.ai.AbstractGravityAIHelper;
import jp.mc.ancientred.starminer.core.common.VecUtils;
import jp.mc.ancientred.starminer.core.obfuscar.SMCoreReflectionHelper;
import jp.mc.ancientred.starminer.core.packet.SMCorePacketSender;
import net.minecraft.block.Block;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.potion.Potion;
import net.minecraft.server.management.ItemInWorldManager;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ReportedException;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

public abstract class EntityLivingGravitized
  extends EntityLivingBase
{
  private static int DW_POTIONEFFECT = 8;
  
  private List threadLocalBBList;

  public EntityLivingGravitized(World par1World) {
    super(par1World);

    this.invalidGravityTickCount = 0;
    
    this.gravityUpdateTick = 0;
    
    this.redistInOpaqueBlockDamageTick = 0;
    
    this.commonInstanceFlag = 0;
    
    this.hasInitServerPlayer = false;
    
    this.hasInitClientGravity = false;

    this.noGAIHelper = false;

    this.savedFootPos = 0.0D; this.nextStepDistancePrivate = 1; this.threadLocalBBList = tlList.get();
  } private static ThreadLocal<List> tlList = new ThreadLocal<List>() {
      protected List initialValue() { return new ArrayList(); }
    }; 
	
	private int nextStepDistancePrivate; 
	private int invalidGravityTickCount; 
	private int gravityUpdateTick;
	public int redistInOpaqueBlockDamageTick;
	public int commonInstanceFlag; 
	private boolean hasInitServerPlayer; 
	private boolean hasInitClientGravity; 
	protected boolean collidedGravityHorizontally; 
	private AbstractGravityAIHelper gAIHelper; 
	private boolean noGAIHelper; 
	private boolean isInMethodSetSize; 
	private double savedFootPos; 
	
	private void init() { if (this instanceof EntityPlayerMP) { ItemInWorldManager itemInWorldManager = ((EntityPlayerMP)this).theItemInWorldManager; if (itemInWorldManager != null) itemInWorldManager.setBlockReachDistance(itemInWorldManager.getBlockReachDistance() + 2.0D);  }  this.hasInitServerPlayer = true; } 
	
	public void onEntityUpdate() { ExtendedPropertyGravity gravity = ExtendedPropertyGravity.getExtendedPropertyGravity((Entity)this); boolean isOutSpace = this.worldObj.provider instanceof jp.mc.ancientred.starminer.api.IZeroGravityWorldProvider; boolean isGravityZero = gravity.isZeroGravity(isOutSpace); if (this instanceof EntityPlayer) { if (this.worldObj.isRemote) { if (!SMCoreModContainer.proxy.isOtherPlayer((EntityPlayer)this)) { gravity.keepGravityDirTick--; if (gravity.keepGravityDirTick < 0) gravity.keepGravityDirTick = 0;  gravity.temporatyZeroGTick--; if (gravity.temporatyZeroGTick < 0) gravity.temporatyZeroGTick = 0;  boolean sendGravityStatePacket = false; if (this.ridingEntity != null) { gravity.changeGravityDirection(GravityDirection.upTOdown_YN); gravity.turnRate = 1.0F; changeGravityToNext(gravity); sendGravityStatePacket = true; } else { gravity.prevTurnRate = gravity.turnRate; if (gravity.turnRate < 1.0F) { gravity.turnRate += gravity.turnSpeed; gravity.turnSpeed *= 1.7F; if (gravity.turnRate >= 1.0F) { changeGravityToNext(gravity); sendGravityStatePacket = true; fixOutFromOpaqueBlock(gravity.gravityDirection); }  }  if (!isGravityZero) gravity.updateGravityDirectionState((Entity)this);  }  if (sendGravityStatePacket || ++this.gravityUpdateTick % CoreConfig.gravityUpdateFreq == 0) { SMCorePacketSender.sendGravityStatePacketToServer((Entity)this, gravity.gravityDirection); this.gravityUpdateTick = 0; }  gravity.normalGravityEffectRedistTick--; if (gravity.normalGravityEffectRedistTick < 0) gravity.normalGravityEffectRedistTick = 0;  }  } else { if (!this.hasInitServerPlayer) init();  gravity.attractUpdateTickCount--; if (gravity.attractUpdateTickCount < 0) gravity.attractUpdateTickCount = 0;  if (this.ridingEntity != null) { gravity.loseAttractedBy(); gravity.gravityDirection = GravityDirection.upTOdown_YN; } else { gravity.updateAtractedStateAndGravityZero((Entity)this); }  gravity.validateGravity((EntityPlayer)this, isGravityZero); if (gravity.illegalGStateTickCount > CoreConfig.illegalGStateTickToCheck) { if (!isPlayerSleeping()) { gravity.unsynchronizedWarnCount++; try { SMCoreModContainer.proxy.warnOrKickIllegalGravity(this, gravity); } catch (Exception ex) { ex.printStackTrace(); }  }  gravity.illegalGStateTickCount = 0; }  this.redistInOpaqueBlockDamageTick--; if (this.redistInOpaqueBlockDamageTick < 0) this.redistInOpaqueBlockDamageTick = 0;  gravity.acceptExceptionalGravityTick--; if (gravity.acceptExceptionalGravityTick < 0) gravity.acceptExceptionalGravityTick = 0;  }  } else { gravity.turnRate = 1.0F; gravity.attractUpdateTickCount--; if (gravity.attractUpdateTickCount < 0) gravity.attractUpdateTickCount = 0;  gravity.keepGravityDirTick--; if (gravity.keepGravityDirTick < 0) gravity.keepGravityDirTick = 0;  gravity.temporatyZeroGTick--; if (gravity.temporatyZeroGTick < 0) gravity.temporatyZeroGTick = 0;  if (!this.worldObj.isRemote) { if ((gravity.isAttracted || gravity.gravityDirection != GravityDirection.upTOdown_YN) && this.riddenByEntity != null) this.riddenByEntity.mountEntity(null);  gravity.updateAtractedStateAndGravityZero((Entity)this); }  if (!isGravityZero) { gravity.changeGravityImmidiate = false; gravity.updateGravityDirectionState((Entity)this); }  if (gravity.gravityDirectionNext != gravity.gravityDirection) { changeGravityToNext(gravity); gravity.normalGravityEffectRedistTick = 20; fixOutFromOpaqueBlock(gravity.gravityDirection); }  gravity.normalGravityEffectRedistTick--; if (gravity.normalGravityEffectRedistTick < 0) gravity.normalGravityEffectRedistTick = 0;  this.redistInOpaqueBlockDamageTick--; if (this.redistInOpaqueBlockDamageTick < 0) this.redistInOpaqueBlockDamageTick = 0;  gravity.acceptExceptionalGravityTick--; if (gravity.acceptExceptionalGravityTick < 0) gravity.acceptExceptionalGravityTick = 0;  }  super.onEntityUpdate(); hideInEntityGDirToPotionDw(gravity.gravityDirection); if (this instanceof EntityPlayer && !this.worldObj.isRemote && !this.isDead) if (this.posY < -128.0D) kill();   gravity.changeGravityImmidiate = false; } private void changeGravityToNext(ExtendedPropertyGravity gravity) { gravity.gravityDirection = gravity.gravityDirectionNext; this.rotationYaw += gravity.onChangeTurnYaw; this.prevRotationYaw += gravity.onChangeTurnYaw; this.renderYawOffset += gravity.onChangeTurnYaw; this.prevRenderYawOffset += gravity.onChangeTurnYaw; this.fallDistance = 0.0F; setPosition(this.posX, this.posY, this.posZ); } 
	
	public void preSetSpawnGravity(GravityDirection gdir, int attractedX, int attractedY, int attractedZ) { ExtendedPropertyGravity gravityThis = ExtendedPropertyGravity.getExtendedPropertyGravity((Entity)this); gravityThis.gravityDirectionNext = gravityThis.gravityDirection = gdir; gravityThis.isAttracted = true; gravityThis.attractedPosX = attractedX; gravityThis.attractedPosY = attractedY; gravityThis.attractedPosZ = attractedZ; setPosition(this.posX, this.posY, this.posZ); hideInEntityGDirToPotionDw(gravityThis.gravityDirection); } 
	
	public void hideInEntityGDirToPotionDw(GravityDirection gDir) { byte original = this.dataWatcher.getWatchableObjectByte(DW_POTIONEFFECT); if (original > 0) { this.dataWatcher.updateObject(DW_POTIONEFFECT, Byte.valueOf((byte)(0x1 | 1 + gDir.ordinal() << 4))); } else { this.dataWatcher.updateObject(DW_POTIONEFFECT, Byte.valueOf((byte)(0x80 | 1 + gDir.ordinal() << 4))); }  } protected void setSize(float width, float height) { this.isInMethodSetSize = true;
    double dWidthHalf = (this.width / 2.0F);
    switch (ExtendedPropertyGravity.getGravityDirection((Entity)this)) {
      case northTOsouth_ZP:
        this.savedFootPos = this.boundingBox.maxZ;
        break;
      case southTOnorth_ZN:
        this.savedFootPos = this.boundingBox.minZ;
        break;
      case westTOeast_XP:
        this.savedFootPos = this.boundingBox.maxX;
        break;
      case eastTOwest_XN:
        this.savedFootPos = this.boundingBox.minX;
        break;
      case downTOup_YP:
        this.savedFootPos = this.boundingBox.maxY;
        break;
      default:
        this.savedFootPos = this.boundingBox.minY;
        break;
    } 

    super.setSize(width, height);
    if (this.isInMethodSetSize)
    {
      fixAfterSetSize();
    }
    this.isInMethodSetSize = false; }
  public void func_145781_i(int dwNum) { if (dwNum == DW_POTIONEFFECT && this.worldObj.isRemote && !this.hasInitClientGravity) { try { byte potionDWValue = getDataWatcher().getWatchableObjectByte(DW_POTIONEFFECT); int hiddenGValue = ((potionDWValue & 0x70) >>> 4) - 1; if (hiddenGValue >= 0 && hiddenGValue < (GravityDirection.values()).length) { ExtendedPropertyGravity gravity = ExtendedPropertyGravity.getExtendedPropertyGravity((Entity)this); gravity.gravityDirectionNext = gravity.gravityDirection = GravityDirection.values()[hiddenGValue]; if (!(this.worldObj.provider instanceof jp.mc.ancientred.starminer.api.IZeroGravityWorldProvider)) gravity.normalGravityEffectRedistTick = 60;  setPosition(this.posX, this.posY, this.posZ); }  } catch (Exception ex) { ex.printStackTrace(); }  this.hasInitClientGravity = true; }  }
  
  public void swingItem() { super.swingItem(); if (!isSneaking() && ExtendedPropertyGravity.isEntityZeroGravity((Entity)this) && this instanceof EntityPlayer && getHeldItem() == null) { Vec3 look = getLookVec(); double speed = 0.02D; double min = -0.25D; double max = 0.25D; if ((look.xCoord > 0.0D && min <= this.motionX) || (look.xCoord < 0.0D && max >= this.motionX)) this.motionX -= look.xCoord * speed;  if ((look.yCoord > 0.0D && min <= this.motionY) || (look.yCoord < 0.0D && max >= this.motionY)) this.motionY -= look.yCoord * speed;  if ((look.zCoord > 0.0D && min <= this.motionZ) || (look.zCoord < 0.0D && max >= this.motionZ)) this.motionZ -= look.zCoord * speed;  }  }
  
  protected void updateFallState(double par1, boolean par3) { if (ExtendedPropertyGravity.isAttracted((Entity)this)) return;  if (ExtendedPropertyGravity.isEntityZeroGravity((Entity)this)) return;  super.updateFallState(par1, par3); }
  
  public boolean isEntityInsideOpaqueBlock() { if (this.redistInOpaqueBlockDamageTick > 0) return false;  ExtendedPropertyGravity gravity = ExtendedPropertyGravity.getExtendedPropertyGravity((Entity)this); if (gravity.gravityDirection != GravityDirection.upTOdown_YN) { double entityPosX; double entityPosY; double entityPosZ; switch (ExtendedPropertyGravity.getGravityDirection((Entity)this)) { case southTOnorth_ZN: entityPosX = this.boundingBox.minX + (this.boundingBox.maxX - this.boundingBox.minX) / 2.0D; entityPosY = this.boundingBox.minY + (this.boundingBox.maxY - this.boundingBox.minY) / 2.0D; entityPosZ = this.boundingBox.minZ + getEyeHeight(); break;case northTOsouth_ZP: entityPosX = this.boundingBox.minX + (this.boundingBox.maxX - this.boundingBox.minX) / 2.0D; entityPosY = this.boundingBox.minY + (this.boundingBox.maxY - this.boundingBox.minY) / 2.0D; entityPosZ = this.boundingBox.maxZ - getEyeHeight(); break;case westTOeast_XP: entityPosX = this.boundingBox.maxX - getEyeHeight(); entityPosY = this.boundingBox.minY + (this.boundingBox.maxY - this.boundingBox.minY) / 2.0D; entityPosZ = this.boundingBox.minZ + (this.boundingBox.maxZ - this.boundingBox.minZ) / 2.0D; break;case eastTOwest_XN: entityPosX = this.boundingBox.minX + getEyeHeight(); entityPosY = this.boundingBox.minY + (this.boundingBox.maxY - this.boundingBox.minY) / 2.0D; entityPosZ = this.boundingBox.minZ + (this.boundingBox.maxZ - this.boundingBox.minZ) / 2.0D; break;case downTOup_YP: entityPosX = this.boundingBox.minX + (this.boundingBox.maxX - this.boundingBox.minX) / 2.0D; entityPosY = this.boundingBox.maxY - getEyeHeight(); entityPosZ = this.boundingBox.minZ + (this.boundingBox.maxZ - this.boundingBox.minZ) / 2.0D; break;default: entityPosX = this.boundingBox.minX + (this.boundingBox.maxX - this.boundingBox.minX) / 2.0D; entityPosY = this.boundingBox.minY + getEyeHeight(); entityPosZ = this.boundingBox.minZ + (this.boundingBox.maxZ - this.boundingBox.minZ) / 2.0D; break; }  if (this.worldObj.getBlock(MathHelper.floor_double(entityPosX), MathHelper.floor_double(entityPosY), MathHelper.floor_double(entityPosZ)).isNormalCube()) return true;  } else { for (int i = 0; i < 8; i++) { float addX = (((i >> 0) % 2) - 0.5F) * this.width * 0.8F; float addY = (((i >> 1) % 2) - 0.5F) * 0.1F; float addZ = (((i >> 2) % 2) - 0.5F) * this.width * 0.8F; int x = MathHelper.floor_double(this.posX + addX); int y = MathHelper.floor_double(this.posY + getEyeHeight() + addY); int z = MathHelper.floor_double(this.posZ + addZ); if (this.worldObj.getBlock(x, y, z).isNormalCube()) return true;  }  }  return false; } 
  
  @SideOnly(Side.CLIENT) public Vec3 getPosition(float par1) { if (par1 == 1.0F) return TransformUtils.fixEyePositionByGravityClient((Entity)this, VecUtils.createVec3(this.posX, this.posY, this.posZ));  double d0 = this.prevPosX + (this.posX - this.prevPosX) * par1; double d1 = this.prevPosY + (this.posY - this.prevPosY) * par1; double d2 = this.prevPosZ + (this.posZ - this.prevPosZ) * par1; return TransformUtils.fixEyePositionByGravityClient((Entity)this, VecUtils.createVec3(d0, d1, d2)); } 
  
  public Vec3 getLook(float par1) { float pitch, yaw; if (par1 == 1.0F) { pitch = this.rotationPitch; yaw = this.rotationYaw; } else { pitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * par1; yaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * par1; }  ExtendedPropertyGravity gravity = ExtendedPropertyGravity.getExtendedPropertyGravity((Entity)this); if (gravity == null) return super.getLook(par1);  return gravity.getGravityFixedLook(pitch, yaw); } 
  
  protected void jump() { switch (ExtendedPropertyGravity.getGravityDirection((Entity)this)) { case southTOnorth_ZN: this.motionZ = 0.41999998688697815D; if (isPotionActive(Potion.jump)) this.motionZ += ((getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F);  if (isSprinting()) { float f = this.rotationYaw * 0.017453292F; this.motionX -= (MathHelper.sin(f) * 0.2F); this.motionY -= (MathHelper.cos(f) * 0.2F); }  break;case northTOsouth_ZP: this.motionZ = -0.41999998688697815D; if (isPotionActive(Potion.jump)) this.motionZ -= ((getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F);  if (isSprinting()) { float f = this.rotationYaw * 0.017453292F; this.motionX -= (MathHelper.sin(f) * 0.2F); this.motionY += (MathHelper.cos(f) * 0.2F); }  break;case westTOeast_XP: this.motionX = -0.41999998688697815D; if (isPotionActive(Potion.jump)) this.motionX -= ((getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F);  if (isSprinting()) { float f = this.rotationYaw * 0.017453292F; this.motionY -= (MathHelper.sin(f) * 0.2F); this.motionZ += (MathHelper.cos(f) * 0.2F); }  break;case eastTOwest_XN: this.motionX = 0.41999998688697815D; if (isPotionActive(Potion.jump)) this.motionX += ((getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F);  if (isSprinting()) { float f = this.rotationYaw * 0.017453292F; this.motionY += (MathHelper.sin(f) * 0.2F); this.motionZ += (MathHelper.cos(f) * 0.2F); }  break;case downTOup_YP: this.motionY = -0.41999998688697815D; if (isPotionActive(Potion.jump)) this.motionY -= ((getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F);  if (isSprinting()) { float f = this.rotationYaw * 0.017453292F; this.motionX -= (MathHelper.sin(f) * 0.2F); this.motionZ -= (MathHelper.cos(f) * 0.2F); }  if (this instanceof net.minecraft.entity.passive.EntityChicken) this.motionY *= 1.6666666666666667D;  break;default: this.motionY = 0.41999998688697815D; if (isPotionActive(Potion.jump)) this.motionY += ((getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F);  if (isSprinting()) { float f = this.rotationYaw * 0.017453292F; this.motionX -= (MathHelper.sin(f) * 0.2F); this.motionZ += (MathHelper.cos(f) * 0.2F); }  break; }  this.isAirBorne = true; ForgeHooks.onLivingJump(this); } 
  
  public void moveEntityWithHeading(float moveStrafing, float moveForward) { ExtendedPropertyGravity gravity = ExtendedPropertyGravity.getExtendedPropertyGravity((Entity)this); boolean isGravityZero = gravity.isZeroGravity(this.worldObj.provider instanceof jp.mc.ancientred.starminer.api.IZeroGravityWorldProvider); boolean isAbnormalGravity = (gravity.gravityDirection != GravityDirection.upTOdown_YN); boolean isNormalGravity = !isAbnormalGravity; if (isNormalGravity && !isGravityZero) if (!(this instanceof EntityPlayer) || !((EntityPlayer)this).capabilities.isFlying || this.ridingEntity != null) { super.moveEntityWithHeading(moveStrafing, moveForward); return; }   if (this.isJumping) if (isInWater() || handleLavaMovement()) switch (gravity.gravityDirection) { case southTOnorth_ZN: this.motionY -= 0.03999999910593033D; this.motionZ += 0.03999999910593033D; break;case northTOsouth_ZP: this.motionY -= 0.03999999910593033D; this.motionZ -= 0.03999999910593033D; break;case westTOeast_XP: this.motionY -= 0.03999999910593033D; this.motionX -= 0.03999999910593033D; break;case eastTOwest_XN: this.motionY -= 0.03999999910593033D; this.motionX += 0.03999999910593033D; break;case downTOup_YP: this.motionY -= 0.03999999910593033D; this.motionY -= 0.03999999910593033D; break; }    double savedX = this.motionX; double savedY = this.motionY; double savedZ = this.motionZ; if (isGravityZero && (!(this instanceof EntityPlayer) || !((EntityPlayer)this).capabilities.isFlying)) { moveStrafing = 0.0F; moveForward = 0.0F; }  if (isInWater() && (!(this instanceof EntityPlayer) || !((EntityPlayer)this).capabilities.isFlying)) { double d = this.posY; moveFlying(moveStrafing, moveForward, isAIEnabled() ? 0.04F : 0.02F); moveEntity(this.motionX, this.motionY, this.motionZ); this.motionX *= 0.800000011920929D; this.motionY *= 0.800000011920929D; this.motionZ *= 0.800000011920929D; switch (gravity.gravityDirection) { case southTOnorth_ZN: d = this.posZ; this.motionZ -= 0.02D; if (this.isCollidedHorizontally && isOffsetPositionInLiquid(this.motionX, this.motionY, this.motionZ + 0.6000000238418579D - this.posZ + d)) this.motionZ = 0.30000001192092896D;  break;case northTOsouth_ZP: d = this.posZ; this.motionZ += 0.02D; if (this.isCollidedHorizontally && isOffsetPositionInLiquid(this.motionX, this.motionY, this.motionZ - 0.6000000238418579D - this.posZ + d)) this.motionZ = -0.30000001192092896D;  break;case westTOeast_XP: d = this.posX; this.motionX += 0.02D; if (this.isCollidedHorizontally && isOffsetPositionInLiquid(this.motionX - 0.6000000238418579D - this.posX + d, this.motionY, this.motionZ)) this.motionX = -0.30000001192092896D;  break;case eastTOwest_XN: d = this.posX; this.motionX -= 0.02D; if (this.isCollidedHorizontally && isOffsetPositionInLiquid(this.motionX + 0.6000000238418579D - this.posX + d, this.motionY, this.motionZ)) this.motionX = 0.30000001192092896D;  break;case downTOup_YP: this.motionY += 0.02D; if (this.isCollidedHorizontally && isOffsetPositionInLiquid(this.motionX, this.motionY - 0.6000000238418579D - this.posY + d, this.motionZ)) this.motionY = -0.30000001192092896D;  break;default: this.motionY -= 0.02D; if (this.isCollidedHorizontally && isOffsetPositionInLiquid(this.motionX, this.motionY + 0.6000000238418579D - this.posY + d, this.motionZ)) this.motionY = 0.30000001192092896D;  break; }  } else if (handleLavaMovement() && (!(this instanceof EntityPlayer) || !((EntityPlayer)this).capabilities.isFlying)) { double d = this.posY; moveFlying(moveStrafing, moveForward, 0.02F); moveEntity(this.motionX, this.motionY, this.motionZ); this.motionX *= 0.5D; this.motionY *= 0.5D; this.motionZ *= 0.5D; switch (gravity.gravityDirection) { case southTOnorth_ZN: d = this.posZ; this.motionZ -= 0.02D; if (this.isCollidedHorizontally && isOffsetPositionInLiquid(this.motionX, this.motionY, this.motionZ + 0.6000000238418579D - this.posZ + d)) this.motionZ = 0.30000001192092896D;  break;case northTOsouth_ZP: d = this.posZ; this.motionZ += 0.02D; if (this.isCollidedHorizontally && isOffsetPositionInLiquid(this.motionX, this.motionY, this.motionZ - 0.6000000238418579D - this.posZ + d)) this.motionZ = -0.30000001192092896D;  break;case westTOeast_XP: d = this.posX; this.motionX += 0.02D; if (this.isCollidedHorizontally && isOffsetPositionInLiquid(this.motionX - 0.6000000238418579D - this.posX + d, this.motionY, this.motionZ)) this.motionX = -0.30000001192092896D;  break;case eastTOwest_XN: d = this.posX; this.motionX -= 0.02D; if (this.isCollidedHorizontally && isOffsetPositionInLiquid(this.motionX + 0.6000000238418579D - this.posX + d, this.motionY, this.motionZ)) this.motionX = 0.30000001192092896D;  break;case downTOup_YP: this.motionY += 0.02D; if (this.isCollidedHorizontally && isOffsetPositionInLiquid(this.motionX, this.motionY - 0.6000000238418579D - this.posY + d, this.motionZ)) this.motionY = -0.30000001192092896D;  break;default: this.motionY -= 0.02D; if (this.isCollidedHorizontally && isOffsetPositionInLiquid(this.motionX, this.motionY + 0.6000000238418579D - this.posY + d, this.motionZ)) this.motionY = 0.30000001192092896D;  break; }  } else { int blockOnX, blockOnY, blockOnZ; float f4; double roatCenterX = this.posX; double roatCenterY = this.posY - this.yOffset + (this.width / 2.0F); double roatCenterZ = this.posZ; switch (gravity.gravityDirection) { case southTOnorth_ZN: blockOnX = MathHelper.floor_double(roatCenterX); blockOnY = MathHelper.floor_double(roatCenterY); blockOnZ = MathHelper.floor_double(roatCenterZ - 1.0D); break;case northTOsouth_ZP: blockOnX = MathHelper.floor_double(roatCenterX); blockOnY = MathHelper.floor_double(roatCenterY); blockOnZ = MathHelper.floor_double(roatCenterZ + 1.0D); break;case westTOeast_XP: blockOnX = MathHelper.floor_double(roatCenterX + 1.0D); blockOnY = MathHelper.floor_double(roatCenterY); blockOnZ = MathHelper.floor_double(roatCenterZ); break;case eastTOwest_XN: blockOnX = MathHelper.floor_double(roatCenterX - 1.0D); blockOnY = MathHelper.floor_double(roatCenterY); blockOnZ = MathHelper.floor_double(roatCenterZ); break;case downTOup_YP: blockOnX = MathHelper.floor_double(roatCenterX); blockOnY = MathHelper.floor_double(roatCenterY + 1.5D); blockOnZ = MathHelper.floor_double(roatCenterZ); break;default: blockOnX = MathHelper.floor_double(this.posX); blockOnY = MathHelper.floor_double(this.boundingBox.minY) - 1; blockOnZ = MathHelper.floor_double(this.posZ); break; }  Block blockOn = this.worldObj.getBlock(blockOnX, blockOnY, blockOnZ); float horizontalSpeed = 0.91F; if (this.onGround) { horizontalSpeed = 0.54600006F; Block i = blockOn; if (i != null) horizontalSpeed = i.slipperiness * 0.91F;  }  float f3 = 0.16277136F / horizontalSpeed * horizontalSpeed * horizontalSpeed; if (this.onGround) { f4 = getAIMoveSpeed() * f3; } else { f4 = this.jumpMovementFactor; }  moveFlying(moveStrafing, moveForward, f4); horizontalSpeed = 0.91F; if (this.onGround) { horizontalSpeed = 0.54600006F; Block j = blockOn; if (j != null) horizontalSpeed = j.slipperiness * 0.91F;  }  if (!isGravityZero && isOnLadder()) { double dLimit = 0.15000000596046448D; this.fallDistance = 0.0F; if (gravity.gravityDirection != GravityDirection.eastTOwest_XN) { if (this.motionX < -dLimit) this.motionX = -dLimit;  } else if (gravity.gravityDirection != GravityDirection.westTOeast_XP && this.motionX > dLimit) { this.motionX = dLimit; }  if (gravity.gravityDirection != GravityDirection.southTOnorth_ZN) { if (this.motionZ < -dLimit) this.motionZ = -dLimit;  } else if (gravity.gravityDirection != GravityDirection.northTOsouth_ZP && this.motionZ > dLimit) { this.motionZ = dLimit; }  if (gravity.gravityDirection != GravityDirection.upTOdown_YN) { if (this.motionY > dLimit) this.motionY = dLimit;  } else if (gravity.gravityDirection != GravityDirection.downTOup_YP && this.motionY < -dLimit) { this.motionY = -dLimit; }  boolean flag = (isSneaking() && this instanceof EntityPlayer); switch (gravity.gravityDirection) { case southTOnorth_ZN: if (flag && this.motionZ < 0.0D) this.motionZ = 0.0D;  break;case northTOsouth_ZP: if (flag && this.motionZ > 0.0D) this.motionZ = 0.0D;  break;case westTOeast_XP: if (flag && this.motionX > 0.0D) this.motionX = 0.0D;  break;case eastTOwest_XN: if (flag && this.motionX < 0.0D) this.motionX = 0.0D;  break;case downTOup_YP: if (flag && this.motionY > 0.0D) this.motionY = 0.0D;  break;default: if (flag && this.motionY < 0.0D) this.motionY = 0.0D;  break; }  }  moveEntity(this.motionX, this.motionY, this.motionZ); if (!isGravityZero) { if (isOnLadder()) switch (ExtendedPropertyGravity.getGravityDirection((Entity)this)) { case southTOnorth_ZN: if (this.collidedGravityHorizontally) this.motionZ = 0.2D;  break;case northTOsouth_ZP: if (this.collidedGravityHorizontally) this.motionZ = -0.2D;  break;case westTOeast_XP: if (this.collidedGravityHorizontally) this.motionX = -0.2D;  break;case eastTOwest_XN: if (this.collidedGravityHorizontally) this.motionX = 0.2D;  break;case downTOup_YP: if (this.isCollidedHorizontally) this.motionY = -0.2D;  break;default: if (this.isCollidedHorizontally) this.motionY = 0.2D;  break; }   if (this.worldObj.isRemote && (!this.worldObj.blockExists((int)this.posX, 0, (int)this.posZ) || !(this.worldObj.getChunkFromBlockCoords((int)this.posX, (int)this.posZ)).isChunkLoaded)) { if (this.posY > 0.0D) { switch (ExtendedPropertyGravity.getGravityDirection((Entity)this)) { case southTOnorth_ZN: this.motionZ -= 0.1D; break;case northTOsouth_ZP: this.motionZ += 0.1D; break;case westTOeast_XP: this.motionX += 0.1D; break;case eastTOwest_XN: this.motionX -= 0.1D; break;case downTOup_YP: this.motionY += 0.1D; break;default: this.motionY -= 0.1D; break; }  } else { this.motionY = 0.0D; }  } else { switch (ExtendedPropertyGravity.getGravityDirection((Entity)this)) { case southTOnorth_ZN: this.motionZ -= 0.08D; break;case northTOsouth_ZP: this.motionZ += 0.08D; break;case westTOeast_XP: this.motionX += 0.08D; break;case eastTOwest_XN: this.motionX -= 0.08D; break;case downTOup_YP: this.motionY += 0.08D; break;default: this.motionY -= 0.08D; break; }  }  }  if (!isGravityZero || (this instanceof EntityPlayer && ((EntityPlayer)this).capabilities.isFlying)) switch (ExtendedPropertyGravity.getGravityDirection((Entity)this)) { case southTOnorth_ZN: case northTOsouth_ZP: this.motionY *= horizontalSpeed; this.motionX *= horizontalSpeed; this.motionZ *= isGravityZero ? 0.8D : 0.9800000190734863D; break;case westTOeast_XP: case eastTOwest_XN: this.motionY *= horizontalSpeed; this.motionX *= isGravityZero ? 0.8D : 0.9800000190734863D; this.motionZ *= horizontalSpeed; break;default: this.motionY *= isGravityZero ? 0.8D : 0.9800000190734863D; this.motionX *= horizontalSpeed; this.motionZ *= horizontalSpeed; break; }   }  this.prevLimbSwingAmount = this.limbSwingAmount; double d0 = this.posX - this.prevPosX; double d1 = this.posZ - this.prevPosZ; double d2 = this.posY - this.prevPosY; float f6 = MathHelper.sqrt_double(d0 * d0 + d1 * d1 + d2 * d2) * 4.0F; if (f6 > 1.0F) f6 = 1.0F;  this.limbSwingAmount += (f6 - this.limbSwingAmount) * 0.4F; this.limbSwing += this.limbSwingAmount; if (!isGravityZero && this instanceof EntityPlayer && ((EntityPlayer)this).capabilities.isFlying && this.ridingEntity == null) { switch (ExtendedPropertyGravity.getGravityDirection((Entity)this)) { case southTOnorth_ZN: case northTOsouth_ZP: this.motionZ = savedZ * 0.6D; return;case westTOeast_XP: case eastTOwest_XN: this.motionX = savedX * 0.6D; return; }  this.motionY = savedY * 0.6D; }  } 
  
  public void moveEntity(double argXMove, double argYMove, double argZMove) { List bbList = this.threadLocalBBList; ExtendedPropertyGravity gravity = ExtendedPropertyGravity.getExtendedPropertyGravity((Entity)this); GravityDirection gDir = gravity.gravityDirection; boolean isAbnormalGravity = (gDir != GravityDirection.upTOdown_YN); boolean isNormalGravity = !isAbnormalGravity; double dWidthHalf = (this.width / 2.0F); if (this.isInMethodSetSize) { fixAfterSetSize(); argXMove = argYMove = argZMove = 0.0D; this.isInMethodSetSize = false; }  if (this.noClip) { this.boundingBox.offset(argXMove, argYMove, argZMove); switch (gDir) { case northTOsouth_ZP: this.posX = (this.boundingBox.minX + this.boundingBox.maxX) / 2.0D; this.posY = this.boundingBox.minY + this.yOffset; this.posZ = this.boundingBox.maxZ - dWidthHalf; return;case southTOnorth_ZN: this.posX = (this.boundingBox.minX + this.boundingBox.maxX) / 2.0D; this.posY = this.boundingBox.minY + this.yOffset; this.posZ = this.boundingBox.minZ + dWidthHalf; return;case westTOeast_XP: this.posX = this.boundingBox.maxX - dWidthHalf; this.posY = this.boundingBox.minY + this.yOffset; this.posZ = (this.boundingBox.minZ + this.boundingBox.maxZ) / 2.0D; return;case eastTOwest_XN: this.posX = this.boundingBox.minX + dWidthHalf; this.posY = this.boundingBox.minY + this.yOffset; this.posZ = (this.boundingBox.minZ + this.boundingBox.maxZ) / 2.0D; return;case downTOup_YP: this.posX = (this.boundingBox.minX + this.boundingBox.maxX) / 2.0D; this.posY = this.boundingBox.minY + this.yOffset; this.posZ = (this.boundingBox.minZ + this.boundingBox.maxZ) / 2.0D; return; }  this.posX = (this.boundingBox.minX + this.boundingBox.maxX) / 2.0D; this.posY = this.boundingBox.minY + this.yOffset - this.yOffset2; this.posZ = (this.boundingBox.minZ + this.boundingBox.maxZ) / 2.0D; } else { this.worldObj.theProfiler.startSection("move"); this.yOffset2 *= 0.4F; double savedPosX = this.posX; double savedPosY = this.posY; double savedPosZ = this.posZ; if (this.isInWeb) { this.isInWeb = false; argXMove *= 0.25D; argYMove *= 0.05000000074505806D; argZMove *= 0.25D; this.motionX = 0.0D; this.motionY = 0.0D; this.motionZ = 0.0D; }  double savedXMove = argXMove; double savedYMove = argYMove; double savedZMove = argZMove; AxisAlignedBB axisalignedbb = this.boundingBox.copy(); boolean isSneakingPlayer = (this.onGround && isSneaking() && this instanceof EntityPlayer); if (isSneakingPlayer) { double aBit = 0.05D; double sub = -1.0D; switch (gDir) { case northTOsouth_ZP: sub = 1.0D;case southTOnorth_ZN: for (; argXMove != 0.0D && getCollidingBoundingBoxesPrivate(bbList, this.worldObj, (Entity)this, this.boundingBox.getOffsetBoundingBox(argXMove, 0.0D, sub), gDir).isEmpty(); savedXMove = argXMove) { if (-aBit <= argXMove && argXMove < aBit) { argXMove = 0.0D; } else if (argXMove > 0.0D) { argXMove -= aBit; } else { argXMove += aBit; }  }  for (; argYMove != 0.0D && getCollidingBoundingBoxesPrivate(bbList, this.worldObj, (Entity)this, this.boundingBox.getOffsetBoundingBox(0.0D, argYMove, sub), gDir).isEmpty(); savedYMove = argYMove) { if (-aBit <= argYMove && argYMove < aBit) { argYMove = 0.0D; } else if (argYMove > 0.0D) { argYMove -= aBit; } else { argYMove += aBit; }  }  while (argXMove != 0.0D && argYMove != 0.0D && getCollidingBoundingBoxesPrivate(bbList, this.worldObj, (Entity)this, this.boundingBox.getOffsetBoundingBox(argXMove, argYMove, sub), gDir).isEmpty()) { if (argXMove < aBit && argXMove >= -aBit) { argXMove = 0.0D; } else if (argXMove > 0.0D) { argXMove -= aBit; } else { argXMove += aBit; }  if (argYMove < aBit && argYMove >= -aBit) { argYMove = 0.0D; } else if (argYMove > 0.0D) { argYMove -= aBit; } else { argYMove += aBit; }  savedXMove = argXMove; savedYMove = argYMove; }  break;case westTOeast_XP: sub = 1.0D;case eastTOwest_XN: for (; argYMove != 0.0D && getCollidingBoundingBoxesPrivate(bbList, this.worldObj, (Entity)this, this.boundingBox.getOffsetBoundingBox(sub, argYMove, 0.0D), gDir).isEmpty(); savedYMove = argYMove) { if (-aBit <= argYMove && argYMove < aBit) { argYMove = 0.0D; } else if (argYMove > 0.0D) { argYMove -= aBit; } else { argYMove += aBit; }  }  for (; argZMove != 0.0D && getCollidingBoundingBoxesPrivate(bbList, this.worldObj, (Entity)this, this.boundingBox.getOffsetBoundingBox(sub, 0.0D, argZMove), gDir).isEmpty(); savedZMove = argZMove) { if (-aBit <= argZMove && argZMove < aBit) { argZMove = 0.0D; } else if (argZMove > 0.0D) { argZMove -= aBit; } else { argZMove += aBit; }  }  while (argZMove != 0.0D && argYMove != 0.0D && getCollidingBoundingBoxesPrivate(bbList, this.worldObj, (Entity)this, this.boundingBox.getOffsetBoundingBox(sub, argYMove, argZMove), gDir).isEmpty()) { if (argZMove < aBit && argZMove >= -aBit) { argZMove = 0.0D; } else if (argZMove > 0.0D) { argZMove -= aBit; } else { argZMove += aBit; }  if (argYMove < aBit && argYMove >= -aBit) { argYMove = 0.0D; } else if (argYMove > 0.0D) { argYMove -= aBit; } else { argYMove += aBit; }  savedZMove = argZMove; savedYMove = argYMove; }  break;case downTOup_YP: sub = 1.0D;default: for (; argXMove != 0.0D && getCollidingBoundingBoxesPrivate(bbList, this.worldObj, (Entity)this, this.boundingBox.getOffsetBoundingBox(argXMove, sub, 0.0D), gDir).isEmpty(); savedXMove = argXMove) { if (-aBit <= argXMove && argXMove < aBit) { argXMove = 0.0D; } else if (argXMove > 0.0D) { argXMove -= aBit; } else { argXMove += aBit; }  }  for (; argZMove != 0.0D && getCollidingBoundingBoxesPrivate(bbList, this.worldObj, (Entity)this, this.boundingBox.getOffsetBoundingBox(0.0D, sub, argZMove), gDir).isEmpty(); savedZMove = argZMove) { if (-aBit <= argZMove && argZMove < aBit) { argZMove = 0.0D; } else if (argZMove > 0.0D) { argZMove -= aBit; } else { argZMove += aBit; }  }  while (argXMove != 0.0D && argZMove != 0.0D && getCollidingBoundingBoxesPrivate(bbList, this.worldObj, (Entity)this, this.boundingBox.getOffsetBoundingBox(argXMove, sub, argZMove), gDir).isEmpty()) { if (argXMove < aBit && argXMove >= -aBit) { argXMove = 0.0D; } else if (argXMove > 0.0D) { argXMove -= aBit; } else { argXMove += aBit; }  if (argZMove < aBit && argZMove >= -aBit) { argZMove = 0.0D; } else if (argZMove > 0.0D) { argZMove -= aBit; } else { argZMove += aBit; }  savedXMove = argXMove; savedZMove = argZMove; }  break; }  }  List<AxisAlignedBB> list = getCollidingBoundingBoxesPrivate(bbList, this.worldObj, (Entity)this, this.boundingBox.addCoord(argXMove, argYMove, argZMove), gDir); for (int i = 0; i < list.size(); i++) argYMove = ((AxisAlignedBB)list.get(i)).calculateYOffset(this.boundingBox, argYMove);  this.boundingBox.offset(0.0D, argYMove, 0.0D); if (!this.field_70135_K && savedYMove != argYMove) { argZMove = 0.0D; argYMove = 0.0D; argXMove = 0.0D; }  boolean flag1 = (this.onGround || (savedYMove != argYMove && savedYMove < 0.0D)); int j; for (j = 0; j < list.size(); j++) argXMove = ((AxisAlignedBB)list.get(j)).calculateXOffset(this.boundingBox, argXMove);  this.boundingBox.offset(argXMove, 0.0D, 0.0D); if (!this.field_70135_K && savedXMove != argXMove) { argZMove = 0.0D; argYMove = 0.0D; argXMove = 0.0D; }  for (j = 0; j < list.size(); j++) argZMove = ((AxisAlignedBB)list.get(j)).calculateZOffset(this.boundingBox, argZMove);  this.boundingBox.offset(0.0D, 0.0D, argZMove); if (!this.field_70135_K && savedZMove != argZMove) { argZMove = 0.0D; argYMove = 0.0D; argXMove = 0.0D; }  this.collidedGravityHorizontally = false; switch (gDir) { case southTOnorth_ZN: case northTOsouth_ZP: this.collidedGravityHorizontally = (savedXMove != argXMove || savedYMove != argYMove); break;case westTOeast_XP: case eastTOwest_XN: this.collidedGravityHorizontally = (savedZMove != argZMove || savedYMove != argYMove); break;default: this.collidedGravityHorizontally = (savedXMove != argXMove || savedZMove != argZMove); break; }  if (this.stepHeight > 0.0F && flag1 && (isSneakingPlayer || this.yOffset2 < 0.05F) && this.collidedGravityHorizontally) { int k; double d3 = argXMove; double d1 = argYMove; double d2 = argZMove; AxisAlignedBB axisalignedbb1 = this.boundingBox.copy(); this.boundingBox.setBB(axisalignedbb); switch (gDir) { case northTOsouth_ZP: argXMove = savedXMove; argYMove = savedYMove; argZMove = -(this.stepHeight); list = getCollidingBoundingBoxesPrivate(bbList, this.worldObj, (Entity)this, this.boundingBox.addCoord(savedXMove, savedYMove, argZMove), gDir); for (k = 0; k < list.size(); k++) argZMove = ((AxisAlignedBB)list.get(k)).calculateZOffset(this.boundingBox, argZMove);  this.boundingBox.offset(0.0D, 0.0D, argZMove); if (!this.field_70135_K && savedZMove != argZMove) { argZMove = 0.0D; argYMove = 0.0D; argXMove = 0.0D; }  for (k = 0; k < list.size(); k++) argYMove = ((AxisAlignedBB)list.get(k)).calculateYOffset(this.boundingBox, argYMove);  this.boundingBox.offset(0.0D, argYMove, 0.0D); if (!this.field_70135_K && savedYMove != argYMove) { argZMove = 0.0D; argYMove = 0.0D; argXMove = 0.0D; }  for (k = 0; k < list.size(); k++) argXMove = ((AxisAlignedBB)list.get(k)).calculateXOffset(this.boundingBox, argXMove);  this.boundingBox.offset(argXMove, 0.0D, 0.0D); if (!this.field_70135_K && savedXMove != argXMove) { argZMove = 0.0D; argYMove = 0.0D; argXMove = 0.0D; }  if (!this.field_70135_K && savedZMove != argZMove) { argZMove = 0.0D; argYMove = 0.0D; argXMove = 0.0D; } else { argZMove = this.stepHeight; for (k = 0; k < list.size(); k++) argZMove = ((AxisAlignedBB)list.get(k)).calculateZOffset(this.boundingBox, argZMove);  this.boundingBox.offset(0.0D, 0.0D, argZMove); }  if (d3 * d3 + d1 * d1 >= argXMove * argXMove + argYMove * argYMove) { argXMove = d3; argYMove = d1; argZMove = d2; this.boundingBox.setBB(axisalignedbb1); }  break;case southTOnorth_ZN: argXMove = savedXMove; argYMove = savedYMove; argZMove = this.stepHeight; list = getCollidingBoundingBoxesPrivate(bbList, this.worldObj, (Entity)this, this.boundingBox.addCoord(savedXMove, savedYMove, argZMove), gDir); for (k = 0; k < list.size(); k++) argZMove = ((AxisAlignedBB)list.get(k)).calculateZOffset(this.boundingBox, argZMove);  this.boundingBox.offset(0.0D, 0.0D, argZMove); if (!this.field_70135_K && savedZMove != argZMove) { argZMove = 0.0D; argYMove = 0.0D; argXMove = 0.0D; }  for (k = 0; k < list.size(); k++) argYMove = ((AxisAlignedBB)list.get(k)).calculateYOffset(this.boundingBox, argYMove);  this.boundingBox.offset(0.0D, argYMove, 0.0D); if (!this.field_70135_K && savedYMove != argYMove) { argZMove = 0.0D; argYMove = 0.0D; argXMove = 0.0D; }  for (k = 0; k < list.size(); k++) argXMove = ((AxisAlignedBB)list.get(k)).calculateXOffset(this.boundingBox, argXMove);  this.boundingBox.offset(argXMove, 0.0D, 0.0D); if (!this.field_70135_K && savedXMove != argXMove) { argZMove = 0.0D; argYMove = 0.0D; argXMove = 0.0D; }  if (!this.field_70135_K && savedZMove != argZMove) { argZMove = 0.0D; argYMove = 0.0D; argXMove = 0.0D; } else { argZMove = -this.stepHeight; for (k = 0; k < list.size(); k++) argZMove = ((AxisAlignedBB)list.get(k)).calculateZOffset(this.boundingBox, argZMove);  this.boundingBox.offset(0.0D, 0.0D, argZMove); }  if (d3 * d3 + d1 * d1 >= argXMove * argXMove + argYMove * argYMove) { argXMove = d3; argYMove = d1; argZMove = d2; this.boundingBox.setBB(axisalignedbb1); }  break;case westTOeast_XP: argXMove = -(this.stepHeight); argYMove = savedYMove; argZMove = savedZMove; list = getCollidingBoundingBoxesPrivate(bbList, this.worldObj, (Entity)this, this.boundingBox.addCoord(argXMove, savedYMove, savedZMove), gDir); for (k = 0; k < list.size(); k++) argXMove = ((AxisAlignedBB)list.get(k)).calculateXOffset(this.boundingBox, argXMove);  this.boundingBox.offset(argXMove, 0.0D, 0.0D); if (!this.field_70135_K && savedXMove != argXMove) { argZMove = 0.0D; argYMove = 0.0D; argXMove = 0.0D; }  for (k = 0; k < list.size(); k++) argYMove = ((AxisAlignedBB)list.get(k)).calculateYOffset(this.boundingBox, argYMove);  this.boundingBox.offset(0.0D, argYMove, 0.0D); if (!this.field_70135_K && savedYMove != argYMove) { argZMove = 0.0D; argYMove = 0.0D; argXMove = 0.0D; }  for (k = 0; k < list.size(); k++) argZMove = ((AxisAlignedBB)list.get(k)).calculateZOffset(this.boundingBox, argZMove);  this.boundingBox.offset(0.0D, 0.0D, argZMove); if (!this.field_70135_K && savedZMove != argZMove) { argZMove = 0.0D; argYMove = 0.0D; argXMove = 0.0D; }  if (!this.field_70135_K && savedXMove != savedXMove) { argZMove = 0.0D; argYMove = 0.0D; argXMove = 0.0D; } else { argXMove = this.stepHeight; for (k = 0; k < list.size(); k++) argXMove = ((AxisAlignedBB)list.get(k)).calculateXOffset(this.boundingBox, argXMove);  this.boundingBox.offset(argXMove, 0.0D, 0.0D); }  if (d1 * d1 + d2 * d2 >= argYMove * argYMove + argZMove * argZMove) { argXMove = d3; argYMove = d1; argZMove = d2; this.boundingBox.setBB(axisalignedbb1); }  break;case eastTOwest_XN: argXMove = this.stepHeight; argYMove = savedYMove; argZMove = savedZMove; list = getCollidingBoundingBoxesPrivate(bbList, this.worldObj, (Entity)this, this.boundingBox.addCoord(argXMove, savedYMove, savedZMove), gDir); for (k = 0; k < list.size(); k++) argXMove = ((AxisAlignedBB)list.get(k)).calculateXOffset(this.boundingBox, argXMove);  this.boundingBox.offset(argXMove, 0.0D, 0.0D); if (!this.field_70135_K && savedXMove != argXMove) { argZMove = 0.0D; argYMove = 0.0D; argXMove = 0.0D; }  for (k = 0; k < list.size(); k++) argYMove = ((AxisAlignedBB)list.get(k)).calculateYOffset(this.boundingBox, argYMove);  this.boundingBox.offset(0.0D, argYMove, 0.0D); if (!this.field_70135_K && savedYMove != argYMove) { argZMove = 0.0D; argYMove = 0.0D; argXMove = 0.0D; }  for (k = 0; k < list.size(); k++) argZMove = ((AxisAlignedBB)list.get(k)).calculateZOffset(this.boundingBox, argZMove);  this.boundingBox.offset(0.0D, 0.0D, argZMove); if (!this.field_70135_K && savedZMove != argZMove) { argZMove = 0.0D; argYMove = 0.0D; argXMove = 0.0D; }  if (!this.field_70135_K && savedXMove != savedXMove) { argZMove = 0.0D; argYMove = 0.0D; argXMove = 0.0D; } else { argXMove = -this.stepHeight; for (k = 0; k < list.size(); k++) argXMove = ((AxisAlignedBB)list.get(k)).calculateXOffset(this.boundingBox, argXMove);  this.boundingBox.offset(argXMove, 0.0D, 0.0D); }  if (d1 * d1 + d2 * d2 >= argYMove * argYMove + argZMove * argZMove) { argXMove = d3; argYMove = d1; argZMove = d2; this.boundingBox.setBB(axisalignedbb1); }  break;case downTOup_YP: argXMove = savedXMove; argYMove = -(this.stepHeight); argZMove = savedZMove; list = getCollidingBoundingBoxesPrivate(bbList, this.worldObj, (Entity)this, this.boundingBox.addCoord(savedXMove, argYMove, savedZMove), gDir); for (k = 0; k < list.size(); k++) argYMove = ((AxisAlignedBB)list.get(k)).calculateYOffset(this.boundingBox, argYMove);  this.boundingBox.offset(0.0D, argYMove, 0.0D); if (!this.field_70135_K && savedYMove != argYMove) { argZMove = 0.0D; argYMove = 0.0D; argXMove = 0.0D; }  for (k = 0; k < list.size(); k++) argXMove = ((AxisAlignedBB)list.get(k)).calculateXOffset(this.boundingBox, argXMove);  this.boundingBox.offset(argXMove, 0.0D, 0.0D); if (!this.field_70135_K && savedXMove != argXMove) { argZMove = 0.0D; argYMove = 0.0D; argXMove = 0.0D; }  for (k = 0; k < list.size(); k++) argZMove = ((AxisAlignedBB)list.get(k)).calculateZOffset(this.boundingBox, argZMove);  this.boundingBox.offset(0.0D, 0.0D, argZMove); if (!this.field_70135_K && savedZMove != argZMove) { argZMove = 0.0D; argYMove = 0.0D; argXMove = 0.0D; }  if (!this.field_70135_K && savedYMove != argYMove) { argZMove = 0.0D; argYMove = 0.0D; argXMove = 0.0D; } else { argYMove = this.stepHeight; for (k = 0; k < list.size(); k++) argYMove = ((AxisAlignedBB)list.get(k)).calculateYOffset(this.boundingBox, argYMove);  this.boundingBox.offset(0.0D, argYMove, 0.0D); }  if (d3 * d3 + d2 * d2 >= argXMove * argXMove + argZMove * argZMove) { argXMove = d3; argYMove = d1; argZMove = d2; this.boundingBox.setBB(axisalignedbb1); }  break;default: argXMove = savedXMove; argYMove = this.stepHeight; argZMove = savedZMove; list = getCollidingBoundingBoxesPrivate(bbList, this.worldObj, (Entity)this, this.boundingBox.addCoord(savedXMove, argYMove, savedZMove), gDir); for (k = 0; k < list.size(); k++) argYMove = ((AxisAlignedBB)list.get(k)).calculateYOffset(this.boundingBox, argYMove);  this.boundingBox.offset(0.0D, argYMove, 0.0D); if (!this.field_70135_K && savedYMove != argYMove) { argZMove = 0.0D; argYMove = 0.0D; argXMove = 0.0D; }  for (k = 0; k < list.size(); k++) argXMove = ((AxisAlignedBB)list.get(k)).calculateXOffset(this.boundingBox, argXMove);  this.boundingBox.offset(argXMove, 0.0D, 0.0D); if (!this.field_70135_K && savedXMove != argXMove) { argZMove = 0.0D; argYMove = 0.0D; argXMove = 0.0D; }  for (k = 0; k < list.size(); k++) argZMove = ((AxisAlignedBB)list.get(k)).calculateZOffset(this.boundingBox, argZMove);  this.boundingBox.offset(0.0D, 0.0D, argZMove); if (!this.field_70135_K && savedZMove != argZMove) { argZMove = 0.0D; argYMove = 0.0D; argXMove = 0.0D; }  if (!this.field_70135_K && savedYMove != argYMove) { argZMove = 0.0D; argYMove = 0.0D; argXMove = 0.0D; } else { argYMove = -this.stepHeight; for (k = 0; k < list.size(); k++) argYMove = ((AxisAlignedBB)list.get(k)).calculateYOffset(this.boundingBox, argYMove);  this.boundingBox.offset(0.0D, argYMove, 0.0D); }  if (d3 * d3 + d2 * d2 >= argXMove * argXMove + argZMove * argZMove) { argXMove = d3; argYMove = d1; argZMove = d2; this.boundingBox.setBB(axisalignedbb1); }  break; }  }  this.worldObj.theProfiler.endSection(); this.worldObj.theProfiler.startSection("rest"); switch (gDir) { case northTOsouth_ZP: this.posX = (this.boundingBox.minX + this.boundingBox.maxX) / 2.0D; this.posY = this.boundingBox.minY + this.yOffset; this.posZ = this.boundingBox.maxZ - dWidthHalf; break;case southTOnorth_ZN: this.posX = (this.boundingBox.minX + this.boundingBox.maxX) / 2.0D; this.posY = this.boundingBox.minY + this.yOffset; this.posZ = this.boundingBox.minZ + dWidthHalf; break;case westTOeast_XP: this.posX = this.boundingBox.maxX - dWidthHalf; this.posY = this.boundingBox.minY + this.yOffset; this.posZ = (this.boundingBox.minZ + this.boundingBox.maxZ) / 2.0D; break;case eastTOwest_XN: this.posX = this.boundingBox.minX + dWidthHalf; this.posY = this.boundingBox.minY + this.yOffset; this.posZ = (this.boundingBox.minZ + this.boundingBox.maxZ) / 2.0D; break;case downTOup_YP: this.posX = (this.boundingBox.minX + this.boundingBox.maxX) / 2.0D; this.posY = this.boundingBox.minY + this.yOffset; this.posZ = (this.boundingBox.minZ + this.boundingBox.maxZ) / 2.0D; break;default: this.posX = (this.boundingBox.minX + this.boundingBox.maxX) / 2.0D; this.posY = this.boundingBox.minY + this.yOffset - this.yOffset2; this.posZ = (this.boundingBox.minZ + this.boundingBox.maxZ) / 2.0D; break; }  this.isCollidedHorizontally = (savedXMove != argXMove || savedZMove != argZMove); this.isCollidedVertically = (savedYMove != argYMove); this.isCollided = (this.isCollidedHorizontally || this.isCollidedVertically); switch (gDir) { case southTOnorth_ZN: this.onGround = (savedZMove != argZMove && savedZMove < 0.0D); updateFallState(argZMove, this.onGround); break;case northTOsouth_ZP: this.onGround = (savedZMove != argZMove && savedZMove > 0.0D); updateFallState(-argZMove, this.onGround); break;case westTOeast_XP: this.onGround = (savedXMove != argXMove && savedXMove > 0.0D); updateFallState(-argXMove, this.onGround); break;case eastTOwest_XN: this.onGround = (savedXMove != argXMove && savedXMove < 0.0D); updateFallState(argXMove, this.onGround); break;case downTOup_YP: this.onGround = (savedYMove != argYMove && savedYMove > 0.0D); updateFallState(-argYMove, this.onGround); break;default: this.onGround = (savedYMove != argYMove && savedYMove < 0.0D); updateFallState(argYMove, this.onGround); break; }  if (savedXMove != argXMove) this.motionX = 0.0D;  if (savedYMove != argYMove) this.motionY = 0.0D;  if (savedZMove != argZMove) this.motionZ = 0.0D;  double mvdXFrmPsNow = this.posX - savedPosX; double mvdYFrmPsNow = this.posY - savedPosY; double mvdZFrmPsNow = this.posZ - savedPosZ; if (canTriggerWalking() && !isSneakingPlayer && this.ridingEntity == null) { int blockOnX, blockOnY, blockOnZ; Block blockOn; if (isAbnormalGravity) { double roatCenterX = this.posX; double roatCenterY = this.posY - this.yOffset + dWidthHalf; double roatCenterZ = this.posZ; switch (gDir) { case southTOnorth_ZN: blockOnX = MathHelper.floor_double(roatCenterX); blockOnY = MathHelper.floor_double(roatCenterY); blockOnZ = MathHelper.floor_double(roatCenterZ - 1.0D); break;case northTOsouth_ZP: blockOnX = MathHelper.floor_double(roatCenterX); blockOnY = MathHelper.floor_double(roatCenterY); blockOnZ = MathHelper.floor_double(roatCenterZ + 1.0D); break;case westTOeast_XP: blockOnX = MathHelper.floor_double(roatCenterX + 1.0D); blockOnY = MathHelper.floor_double(roatCenterY); blockOnZ = MathHelper.floor_double(roatCenterZ); break;case eastTOwest_XN: blockOnX = MathHelper.floor_double(roatCenterX - 1.0D); blockOnY = MathHelper.floor_double(roatCenterY); blockOnZ = MathHelper.floor_double(roatCenterZ); break;case downTOup_YP: blockOnX = MathHelper.floor_double(roatCenterX); blockOnY = MathHelper.floor_double(roatCenterY + 1.5D); blockOnZ = MathHelper.floor_double(roatCenterZ); break;default: return; }  blockOn = this.worldObj.getBlock(blockOnX, blockOnY, blockOnZ); } else { blockOnX = MathHelper.floor_double(this.posX); blockOnY = MathHelper.floor_double(this.posY - 0.20000000298023224D - this.yOffset); blockOnZ = MathHelper.floor_double(this.posZ); blockOn = this.worldObj.getBlock(blockOnX, blockOnY, blockOnZ); if (blockOn == Blocks.air) { int k1 = this.worldObj.getBlock(blockOnX, blockOnY - 1, blockOnZ).getRenderType(); if (k1 == 11 || k1 == 32 || k1 == 21) blockOn = this.worldObj.getBlock(blockOnX, blockOnY - 1, blockOnZ);  }  }  if (blockOn != Blocks.ladder && (isNormalGravity || gDir == GravityDirection.downTOup_YP)) mvdYFrmPsNow = 0.0D;  this.distanceWalkedModified = (float)(this.distanceWalkedModified + MathHelper.sqrt_double(mvdXFrmPsNow * mvdXFrmPsNow + mvdYFrmPsNow * mvdYFrmPsNow + mvdZFrmPsNow * mvdZFrmPsNow) * 0.6D); this.distanceWalkedOnStepModified = (float)(this.distanceWalkedOnStepModified + MathHelper.sqrt_double(mvdXFrmPsNow * mvdXFrmPsNow + mvdYFrmPsNow * mvdYFrmPsNow + mvdZFrmPsNow * mvdZFrmPsNow) * 0.6D); if (this.distanceWalkedOnStepModified > this.nextStepDistancePrivate && blockOn != null && blockOn != Blocks.air) { this.nextStepDistancePrivate = (int)this.distanceWalkedOnStepModified + 1; if (isInWater()) { float f = MathHelper.sqrt_double(this.motionX * this.motionX * 0.20000000298023224D + this.motionY * this.motionY + this.motionZ * this.motionZ * 0.20000000298023224D) * 0.35F; if (f > 1.0F) f = 1.0F;  playSound("liquid.swim", f, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F); }  playStepSound(blockOnX, blockOnY, blockOnZ, blockOn); blockOn.onEntityWalking(this.worldObj, blockOnX, blockOnY, blockOnZ, (Entity)this); }  }  try { doBlockCollisions(); } catch (Throwable throwable) { CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Checking entity tile collision"); CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity being checked for collision"); addEntityCrashInfo(crashreportcategory); throw new ReportedException(crashreport); }  boolean flag2 = isWet(); if (SMCoreReflectionHelper.field_fire == null) SMCoreReflectionHelper.initFiledAccessFire();  try { int fireVal = SMCoreReflectionHelper.field_fire.getInt(this); if (this.worldObj.func_147470_e(this.boundingBox.contract(0.001D, 0.001D, 0.001D))) { dealFireDamage(1); if (!flag2) { SMCoreReflectionHelper.field_fire.setInt(this, ++fireVal); if (fireVal == 0) setFire(8);  }  } else if (fireVal <= 0) { SMCoreReflectionHelper.field_fire.setInt(this, fireVal = -this.fireResistance); }  if (flag2 && fireVal > 0) { playSound("random.fizz", 0.7F, 1.6F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F); SMCoreReflectionHelper.field_fire.setInt(this, fireVal = -this.fireResistance); }  } catch (IllegalAccessException elgaEx) { elgaEx.printStackTrace(); }  this.worldObj.theProfiler.endSection(); }  } 
  
  public boolean isOnLadder() { int x, y, z; double widthHalf = (this.width / 2.0F); double padding = 0.01D; AxisAlignedBB bb = this.boundingBox; switch (ExtendedPropertyGravity.getGravityDirection((Entity)this)) { case southTOnorth_ZN: x = MathHelper.floor_double(bb.minX + widthHalf); y = MathHelper.floor_double(bb.minY + widthHalf); z = MathHelper.floor_double(bb.minZ + padding); break;case northTOsouth_ZP: x = MathHelper.floor_double(bb.minX + widthHalf); y = MathHelper.floor_double(bb.minY + widthHalf); z = MathHelper.floor_double(bb.maxZ - padding); break;case westTOeast_XP: x = MathHelper.floor_double(bb.maxX - padding); y = MathHelper.floor_double(bb.minY + widthHalf); z = MathHelper.floor_double(bb.minZ + widthHalf); break;case eastTOwest_XN: x = MathHelper.floor_double(bb.minX + padding); y = MathHelper.floor_double(bb.minY + widthHalf); z = MathHelper.floor_double(bb.minZ + widthHalf); break;case downTOup_YP: x = MathHelper.floor_double(this.posX); y = MathHelper.floor_double(bb.maxY - padding); z = MathHelper.floor_double(this.posZ); break;default: x = MathHelper.floor_double(this.posX); y = MathHelper.floor_double(this.boundingBox.minY); z = MathHelper.floor_double(this.posZ); block = this.worldObj.getBlock(x, y, z); return ForgeHooks.isLivingOnLadder(block, this.worldObj, x, y, z, this); }  Block block = this.worldObj.getBlock(x, y, z); return (block != null && block.isLadder((IBlockAccess)this.worldObj, x, y, z, this)); } 
  
  public void moveFlying(float par1, float par2, float par3) { float f3 = par1 * par1 + par2 * par2; if (f3 >= 1.0E-4F) { f3 = MathHelper.sqrt_float(f3); if (f3 < 1.0F) f3 = 1.0F;  f3 = par3 / f3; par1 *= f3; par2 *= f3; switch (ExtendedPropertyGravity.getGravityDirection((Entity)this)) { case southTOnorth_ZN: par1 = -par1; yawFront = MathHelper.sin(this.rotationYaw * 3.1415927F / 180.0F); yawSholder = -MathHelper.cos(this.rotationYaw * 3.1415927F / 180.0F); this.motionX += (par1 * yawSholder - par2 * yawFront); this.motionY += (par2 * yawSholder + par1 * yawFront); return;case northTOsouth_ZP: yawFront = MathHelper.sin(this.rotationYaw * 3.1415927F / 180.0F); yawSholder = MathHelper.cos(this.rotationYaw * 3.1415927F / 180.0F); this.motionX += (par1 * yawSholder - par2 * yawFront); this.motionY += (par2 * yawSholder + par1 * yawFront); return;case westTOeast_XP: yawFront = MathHelper.sin(this.rotationYaw * 3.1415927F / 180.0F); yawSholder = MathHelper.cos(this.rotationYaw * 3.1415927F / 180.0F); this.motionY += (par1 * yawSholder - par2 * yawFront); this.motionZ += (par2 * yawSholder + par1 * yawFront); return;case eastTOwest_XN: par1 = -par1; yawFront = -MathHelper.sin(this.rotationYaw * 3.1415927F / 180.0F); yawSholder = MathHelper.cos(this.rotationYaw * 3.1415927F / 180.0F); this.motionY += (par1 * yawSholder - par2 * yawFront); this.motionZ += (par2 * yawSholder + par1 * yawFront); return;case downTOup_YP: par1 = -par1; yawFront = MathHelper.sin(this.rotationYaw * 3.1415927F / 180.0F); yawSholder = -MathHelper.cos(this.rotationYaw * 3.1415927F / 180.0F); this.motionX += (par1 * yawSholder - par2 * yawFront); this.motionZ += (par2 * yawSholder + par1 * yawFront); return; }  float yawFront = MathHelper.sin(this.rotationYaw * 3.1415927F / 180.0F); float yawSholder = MathHelper.cos(this.rotationYaw * 3.1415927F / 180.0F); this.motionX += (par1 * yawSholder - par2 * yawFront); this.motionZ += (par2 * yawSholder + par1 * yawFront); }  } 
  
  public void setPosition(double argX, double argY, double argZ) { this.posX = argX; this.posY = argY; this.posZ = argZ; double dWidthHalf = (this.width / 2.0F); double dHeight = this.height; setEntityBoundingBoxG(argX, argY, argZ, dWidthHalf, dHeight); } private void setEntityBoundingBoxG(double argX, double argY, double argZ, double dWidthHalf, double dHeight) { this.boundingBox.setBounds(argX - dWidthHalf, argY - this.yOffset + this.yOffset2, argZ - dWidthHalf, argX + dWidthHalf, argY - this.yOffset + this.yOffset2 + dHeight, argZ + dWidthHalf); try { if (this.worldObj.isRemote) { fixPositonAndBBClient(argX, argY, argZ); } else { fixPositonAndBBServer(argX, argY, argZ); }  } catch (Exception ex) {} } 
  
  public void fixPositonAndBBClient(double argX, double argY, double argZ) { double height = this.height; double widthHalf = (this.width / 2.0F); GravityDirection gDir = ExtendedPropertyGravity.getGravityDirection((Entity)this); switch (gDir) { case upTOdown_YN: return;case downTOup_YP: if (SMCoreModContainer.proxy.isOtherPlayer((EntityPlayer)this)) return;  break; }  gDir.rotateAABBAt(this.boundingBox, this.boundingBox.minX + widthHalf, this.boundingBox.minY + widthHalf, this.boundingBox.minZ + widthHalf); } 
  
  public void fixPositonAndBBServer(double argX, double argY, double par5) { double height = this.height; double widthHalf = (this.width / 2.0F); GravityDirection gDir = ExtendedPropertyGravity.getGravityDirection((Entity)this); switch (gDir) { case upTOdown_YN: return;case downTOup_YP: if (this instanceof EntityPlayerMP) { this.boundingBox.minY = argY; this.boundingBox.maxY = argY + height; }  break; }  gDir.rotateAABBAt(this.boundingBox, this.boundingBox.minX + widthHalf, this.boundingBox.minY + widthHalf, this.boundingBox.minZ + widthHalf); } protected float func_110146_f(float par1, float par2) { float f2 = MathHelper.wrapAngleTo180_float(par1 - this.renderYawOffset); this.renderYawOffset += f2 * 0.3F; float f3 = MathHelper.wrapAngleTo180_float(this.rotationYaw - this.renderYawOffset); boolean flag = (f3 < -90.0F || f3 >= 90.0F); if (f3 < -75.0F) f3 = -75.0F;  if (f3 >= 75.0F) f3 = 75.0F;  if (ExtendedPropertyGravity.isEntityAbnormalGravity((Entity)this)) { this.renderYawOffset = this.rotationYaw; } else { this.renderYawOffset = this.rotationYaw - f3; if (f3 * f3 > 2500.0F) this.renderYawOffset += f3 * 0.2F;  }  if (flag) par2 *= -1.0F;  return par2; } private final List getCollidingBoundingBoxesPrivate(List<AxisAlignedBB> bbList, World world, Entity p_72945_1_, AxisAlignedBB p_72945_2_, GravityDirection gDir) { bbList.clear(); int xMin = MathHelper.floor_double(p_72945_2_.minX); int xMax = MathHelper.floor_double(p_72945_2_.maxX + 1.0D); int yMin = MathHelper.floor_double(p_72945_2_.minY); int yMax = MathHelper.floor_double(p_72945_2_.maxY + 1.0D); int zMin = MathHelper.floor_double(p_72945_2_.minZ); int zMax = MathHelper.floor_double(p_72945_2_.maxZ + 1.0D); xMin += (gDir.collideCheckExpandX < 0) ? gDir.collideCheckExpandX : 0; yMin += (gDir.collideCheckExpandY < 0) ? gDir.collideCheckExpandY : 0; zMin += (gDir.collideCheckExpandZ < 0) ? gDir.collideCheckExpandZ : 0; xMax += (gDir.collideCheckExpandX > 0) ? gDir.collideCheckExpandX : 0; yMax += (gDir.collideCheckExpandY > 0) ? gDir.collideCheckExpandY : 0; zMax += (gDir.collideCheckExpandZ > 0) ? gDir.collideCheckExpandZ : 0; for (int k1 = xMin; k1 < xMax; k1++) { for (int l1 = zMin; l1 < zMax; l1++) { if (world.blockExists(k1, 64, l1)) for (int i2 = yMin; i2 < yMax; i2++) { Block block; if (k1 >= -30000000 && k1 < 30000000 && l1 >= -30000000 && l1 < 30000000) { block = world.getBlock(k1, i2, l1); } else { block = Blocks.stone; }  block.addCollisionBoxesToList(world, k1, i2, l1, p_72945_2_, bbList, p_72945_1_); }   }  }  double d0 = 0.25D; List<Entity> list = world.getEntitiesWithinAABBExcludingEntity(p_72945_1_, p_72945_2_.expand(d0, d0, d0)); for (int j2 = 0; j2 < list.size(); j2++) { AxisAlignedBB axisalignedbb1 = ((Entity)list.get(j2)).getBoundingBox(); if (axisalignedbb1 != null && axisalignedbb1.intersectsWith(p_72945_2_)) bbList.add(axisalignedbb1);  axisalignedbb1 = p_72945_1_.getCollisionBox(list.get(j2)); if (axisalignedbb1 != null && axisalignedbb1.intersectsWith(p_72945_2_)) bbList.add(axisalignedbb1);  }  return bbList; } 
  
  public boolean updateAITasksForGravity() { ExtendedPropertyGravity gravity = ExtendedPropertyGravity.getExtendedPropertyGravity((Entity)this); EntityLiving tthis = (EntityLiving)this; if (gravity.isAttracted && ensureGAIHelper(tthis)) { AbstractGravityAIHelper.injectGAIHelper(this.gAIHelper, tthis); this.entityAge++; SMCoreReflectionHelper.invokeDespawnEntity(tthis); this.gAIHelper.gSenses.clearSensingCache(); this.gAIHelper.gTargetTasks.onUpdateTasks(); this.gAIHelper.gTasks.onUpdateTasks(); this.gAIHelper.gNavigator.onUpdateNavigation(); SMCoreReflectionHelper.invokeUpdateAITick(tthis); this.gAIHelper.gMoveHelper.onUpdateMoveHelper(); this.gAIHelper.gLookHelper.onUpdateLook(); this.gAIHelper.gJumpHelper.doJump(); return true; }  if (this.gAIHelper != null) AbstractGravityAIHelper.resetOrgAIHelper(this.gAIHelper, tthis);  return false; } private boolean ensureGAIHelper(EntityLiving entityLiving) { if (this.noGAIHelper) return false;  if (this.gAIHelper == null) { Class<AbstractGravityAIHelper.DefaultGravityAIHelper> clazz1; Class<? extends AbstractGravityAIHelper> clazz = (Class<? extends AbstractGravityAIHelper>)AbstractGravityAIHelper.gAiConstructMap.get(entityLiving.getClass()); if (clazz == null) clazz1 = AbstractGravityAIHelper.DefaultGravityAIHelper.class;  try { this.gAIHelper = (AbstractGravityAIHelper)clazz1.newInstance(); this.gAIHelper.initAI(entityLiving); return true; } catch (Exception e) { e.printStackTrace(); this.noGAIHelper = true; return false; }  }  return true; } private void fixAfterSetSize() { double dHeight = this.height;
    double dWidthHalf = (this.width / 2.0F);
    setEntityBoundingBoxG(this.posX, this.posY, this.posZ, dWidthHalf, dHeight);
    switch (ExtendedPropertyGravity.getGravityDirection((Entity)this)) {
      case northTOsouth_ZP:
        this.boundingBox.maxZ = this.savedFootPos;
        this.boundingBox.minZ = this.boundingBox.maxZ - dHeight;
        return;
      case southTOnorth_ZN:
        this.boundingBox.minZ = this.savedFootPos;
        this.boundingBox.maxZ = this.boundingBox.minZ + dHeight;
        return;
      case westTOeast_XP:
        this.boundingBox.maxX = this.savedFootPos;
        this.boundingBox.minX = this.boundingBox.maxX - dHeight;
        return;
      case eastTOwest_XN:
        this.boundingBox.minX = this.savedFootPos;
        this.boundingBox.maxX = this.boundingBox.minX + dHeight;
        return;
      case downTOup_YP:
        this.boundingBox.maxY = this.savedFootPos;
        this.boundingBox.minY = this.boundingBox.maxY - dHeight;
        return;
    } 
    this.boundingBox.minY = this.savedFootPos;
    this.boundingBox.maxY = this.boundingBox.minY + dHeight; }

  public void setSizeProxy(float par1, float par2) {
    setSize(par1, par2);
  }

  public boolean canEntityBeSeen(Entity target) {
    return (this.worldObj.rayTraceBlocks(getEyePositionVec((Entity)this), getEyePositionVec(target)) == null);
  }
  private Vec3 getEyePositionVec(Entity entity) {
    double dEyeHeight = entity.getEyeHeight();
    AxisAlignedBB bb = entity.boundingBox;
    switch (ExtendedPropertyGravity.getGravityDirection(entity)) {
      case northTOsouth_ZP:
        return Vec3.createVectorHelper((bb.maxX + bb.minX) / 2.0D, (bb.maxY + bb.minY) / 2.0D, bb.maxZ - dEyeHeight);

      case southTOnorth_ZN:
        return Vec3.createVectorHelper((bb.maxX + bb.minX) / 2.0D, (bb.maxY + bb.minY) / 2.0D, bb.minZ + dEyeHeight);

      case westTOeast_XP:
        return Vec3.createVectorHelper(bb.maxX - dEyeHeight, (bb.maxY + bb.minY) / 2.0D, (bb.maxZ + bb.minZ) / 2.0D);

      case eastTOwest_XN:
        return Vec3.createVectorHelper(bb.minX + dEyeHeight, (bb.maxY + bb.minY) / 2.0D, (bb.maxZ + bb.minZ) / 2.0D);

      case downTOup_YP:
        return Vec3.createVectorHelper((bb.maxX + bb.minX) / 2.0D, bb.maxY - dEyeHeight, (bb.maxZ + bb.minZ) / 2.0D);
    } 

    return Vec3.createVectorHelper((bb.maxX + bb.minX) / 2.0D, bb.minY + dEyeHeight, (bb.maxZ + bb.minZ) / 2.0D);
  }

  public void setPositionAndRotation2(double posX, double posY, double posZ, float yaw, float pitch, int prInc) {
    if (ExtendedPropertyGravity.getGravityDirection((Entity)this) == GravityDirection.downTOup_YP)
    {
      
      posY = this.serverPosY / 32.0D;
    }
    super.setPositionAndRotation2(posX, posY, posZ, yaw, pitch, prInc);
  }

  public void fixOutFromOpaqueBlock(GravityDirection gDir) {
    List<AxisAlignedBB> list = getCollidingBoundingBoxesPrivate(this.threadLocalBBList, this.worldObj, (Entity)this, this.boundingBox, gDir);

    boolean needFix = false;
    double dHeight = this.height;
    
    switch (gDir) {
      case northTOsouth_ZP:
        dummyMove = -dHeight;
        bbShifted = this.boundingBox.getOffsetBoundingBox(0.0D, 0.0D, dHeight);
        for (i = 0; i < list.size(); i++) {
          dummyMove = ((AxisAlignedBB)list.get(i)).calculateZOffset(bbShifted, dummyMove);
        }
        if (dummyMove != dHeight) {
          this.posZ += dHeight - -dummyMove;
          setPosition(this.posX, this.posY, this.posZ);
        } 
        return;
      case southTOnorth_ZN:
        dummyMove = dHeight;
        bbShifted = this.boundingBox.getOffsetBoundingBox(0.0D, 0.0D, -dHeight);
        for (i = 0; i < list.size(); i++) {
          dummyMove = ((AxisAlignedBB)list.get(i)).calculateZOffset(bbShifted, dummyMove);
        }
        if (dummyMove != dHeight) {
          this.posZ -= dHeight - dummyMove;
          setPosition(this.posX, this.posY, this.posZ);
        } 
        return;
      case westTOeast_XP:
        dummyMove = -dHeight;
        bbShifted = this.boundingBox.getOffsetBoundingBox(dHeight, 0.0D, 0.0D);
        for (i = 0; i < list.size(); i++) {
          dummyMove = ((AxisAlignedBB)list.get(i)).calculateXOffset(bbShifted, dummyMove);
        }
        if (dummyMove != dHeight) {
          this.posX += dHeight - -dummyMove;
          setPosition(this.posX, this.posY, this.posZ);
        } 
        return;
      case eastTOwest_XN:
        dummyMove = dHeight;
        bbShifted = this.boundingBox.getOffsetBoundingBox(-dHeight, 0.0D, 0.0D);
        for (i = 0; i < list.size(); i++) {
          dummyMove = ((AxisAlignedBB)list.get(i)).calculateXOffset(bbShifted, dummyMove);
        }
        if (dummyMove != dHeight) {
          this.posX -= dHeight - dummyMove;
          setPosition(this.posX, this.posY, this.posZ);
        } 
        return;
      case downTOup_YP:
        dummyMove = -dHeight;
        bbShifted = this.boundingBox.getOffsetBoundingBox(0.0D, dHeight, 0.0D);
        for (i = 0; i < list.size(); i++) {
          dummyMove = ((AxisAlignedBB)list.get(i)).calculateYOffset(bbShifted, dummyMove);
        }
        if (dummyMove != dHeight) {
          this.posY += dHeight - -dummyMove;
          setPosition(this.posX, this.posY, this.posZ);
        } 
        return;
    } 
    double dummyMove = dHeight;
    AxisAlignedBB bbShifted = this.boundingBox.getOffsetBoundingBox(0.0D, -dHeight, 0.0D);
    for (int i = 0; i < list.size(); i++) {
      dummyMove = ((AxisAlignedBB)list.get(i)).calculateYOffset(bbShifted, dummyMove);
    }
    if (dummyMove != dHeight) {
      this.posY -= dHeight - dummyMove;
      setPosition(this.posX, this.posY, this.posZ);
    } 
  }

  public void applyEntityCollision(Entity targetEntity) {
    if (targetEntity.riddenByEntity != this && targetEntity.ridingEntity != this) {
      
      ExtendedPropertyGravity gravity = ExtendedPropertyGravity.getExtendedPropertyGravity(targetEntity);
      if (gravity == null) {
        super.applyEntityCollision(targetEntity);
      } else {
        double diffY;
        switch (gravity.gravityDirection) {
          case southTOnorth_ZN:
          case northTOsouth_ZP:
            diffX = targetEntity.posX - this.posX;
            diffY = targetEntity.posY - this.posY;
            diffMix = MathHelper.abs_max(diffX, diffY);
            
            if (diffMix >= 0.009999999776482582D) {
              
              diffMix = MathHelper.sqrt_double(diffMix);
              diffX /= diffMix;
              diffY /= diffMix;
              double d3 = 1.0D / diffMix;
              
              if (d3 > 1.0D)
              {
                d3 = 1.0D;
              }
              
              diffX *= d3;
              diffY *= d3;
              diffX *= 0.05000000074505806D;
              diffY *= 0.05000000074505806D;
              diffX *= (1.0F - this.entityCollisionReduction);
              diffY *= (1.0F - this.entityCollisionReduction);
              addVelocity(-diffX, -diffY, 0.0D);
              targetEntity.addVelocity(diffX, diffY, 0.0D);
            } 
            return;
          case westTOeast_XP:
          case eastTOwest_XN:
            diffY = targetEntity.posY - this.posY;
            diffZ = targetEntity.posZ - this.posZ;
            diffMix = MathHelper.abs_max(diffY, diffZ);
            
            if (diffMix >= 0.009999999776482582D) {
              
              diffMix = MathHelper.sqrt_double(diffMix);
              diffY /= diffMix;
              diffZ /= diffMix;
              double d3 = 1.0D / diffMix;
              
              if (d3 > 1.0D)
              {
                d3 = 1.0D;
              }
              
              diffY *= d3;
              diffZ *= d3;
              diffY *= 0.05000000074505806D;
              diffZ *= 0.05000000074505806D;
              diffY *= (1.0F - this.entityCollisionReduction);
              diffZ *= (1.0F - this.entityCollisionReduction);
              addVelocity(0.0D, -diffY, -diffZ);
              targetEntity.addVelocity(0.0D, diffY, diffZ);
            } 
            return;
        } 
        
        double diffX = targetEntity.posX - this.posX;
        double diffZ = targetEntity.posZ - this.posZ;
        double diffMix = MathHelper.abs_max(diffX, diffZ);
        
        if (diffMix >= 0.009999999776482582D) {
          
          diffMix = MathHelper.sqrt_double(diffMix);
          diffX /= diffMix;
          diffZ /= diffMix;
          double d3 = 1.0D / diffMix;
          
          if (d3 > 1.0D)
          {
            d3 = 1.0D;
          }
          
          diffX *= d3;
          diffZ *= d3;
          diffX *= 0.05000000074505806D;
          diffZ *= 0.05000000074505806D;
          diffX *= (1.0F - this.entityCollisionReduction);
          diffZ *= (1.0F - this.entityCollisionReduction);
          addVelocity(-diffX, 0.0D, -diffZ);
          targetEntity.addVelocity(diffX, 0.0D, diffZ);
        } 
      } 
    } 
  }
}
