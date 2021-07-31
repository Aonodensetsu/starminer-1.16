package dev.bluecom.starminer.api;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.common.util.LazyOptional;

public abstract class Gravity {
  public static final String EXTENDED_PROP_GRAVITY_KEY = "starminer.Gravity";
  public GravityDirection gravityDirection = GravityDirection.upTOdown_YN;
  public GravityDirection gravityDirectionNext = GravityDirection.upTOdown_YN;
  public boolean isAttracted = false;
  public int attractedPosX = 0;
  public int attractedPosY = 0;
  public int attractedPosZ = 0;
  public int attractUpdateTickCount = 0;
  public int acceptExceptionalGravityTick = 0;

  public static GravityCapability getGravityProp(Entity entity) {
    LazyOptional<IGravityCapability> gravity = entity.getCapability(GravityProvider.GRAVITY);
    return (GravityCapability) gravity.orElse(new GravityCapability());
  }

  public static final GravityDirection getGravityDirection(Entity entity) {
    GravityCapability gp = getGravityProp(entity);
    if (gp == null) return GravityDirection.upTOdown_YN; 
    return gp.getGravityDir();
  }

  public static boolean isEntityZeroGravity(Entity entity) {
    if (entity != null) {
      GravityCapability gp = getGravityProp(entity);
      return (gp != null && gp.getGravityZero());
    } 
    return false;
  }
  
  public abstract boolean isZeroGravity();
  public abstract boolean isAttracted();
  public abstract void setAttractedBy(IAttractableTileEntity paramIAttractableTileEntity);
  public abstract boolean isAttractedBy(IAttractableTileEntity paramIAttractableTileEntity);
  public abstract void loseAttractedBy();
  public abstract void setTemporaryGravityDirection(GravityDirection paramGravityDirection, int paramInt);
  public abstract void setTemporaryZeroGravity(int paramInt);
  public abstract Vector3d getGravityFixedLook(float paramFloat1, float paramFloat2);
  public abstract Vector3d getGravityFixedPlayerEyePoz(PlayerEntity paramEntityPlayer, float paramFloat);
  public abstract void setGravityFixedPlayerShootVec(PlayerEntity paramEntityPlayer, Entity paramEntity, float paramFloat);
  public abstract void setResistInOpaqueBlockDamegeTick(int paramInt);
}
