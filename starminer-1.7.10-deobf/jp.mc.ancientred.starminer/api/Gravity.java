package jp.mc.ancientred.starminer.api;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.IExtendedEntityProperties;

public abstract class Gravity
  implements IExtendedEntityProperties
{
  public static final String EXTENDED_PROP_GRAVITY_KEY = "starminer.Gravity";
  public GravityDirection gravityDirection = GravityDirection.upTOdown_YN;

  
  public GravityDirection gravityDirectionNext = GravityDirection.upTOdown_YN;

  
  public boolean isAttracted = false;
  
  public int attractedPosX = 0;
  
  public int attractedPosY = 0;
  
  public int attractedPosZ = 0;

  public int attractUpdateTickCount = 0;

  public int acceptExceptionalGravityTick = 0;

  public static Gravity getGravityProp(Entity entity) {
    return (Gravity)entity.getExtendedProperties("starminer.Gravity");
  }

  public static final GravityDirection getGravityDirection(Entity entity) {
    Gravity gp = getGravityProp(entity);
    if (gp == null) return GravityDirection.upTOdown_YN; 
    return gp.gravityDirection;
  }

  public static boolean isEntityZeroGravity(Entity entity) {
    if (entity != null) {
      Gravity gp = getGravityProp(entity);
      return (gp != null && gp.isZeroGravity());
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
  
  public abstract Vec3 getGravityFixedLook(float paramFloat1, float paramFloat2);
  
  public abstract Vec3 getGravityFixedPlayerEyePoz(EntityPlayer paramEntityPlayer, float paramFloat);
  
  public abstract void setGravityFixedPlayerShootVec(EntityPlayer paramEntityPlayer, Entity paramEntity, float paramFloat);
  
  public abstract void setResistInOpaqueBlockDamegeTick(int paramInt);
}
