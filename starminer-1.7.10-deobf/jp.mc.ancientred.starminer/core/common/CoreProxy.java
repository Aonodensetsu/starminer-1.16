package jp.mc.ancientred.starminer.core.common;

import jp.mc.ancientred.starminer.core.entity.EntityLivingGravitized;
import jp.mc.ancientred.starminer.core.entity.ExtendedPropertyGravity;
import net.minecraft.entity.player.EntityPlayer;

public class CoreProxy
{
  public boolean isOtherPlayer(EntityPlayer player) {
    return true;
  }
  
  public void registerNetworkHandler() {}
  
  public void setFlyMovementByGravity(EntityPlayer entityPlayer) {}
  
  public void warnOrKickIllegalGravity(EntityLivingGravitized entityLivingGravitized, ExtendedPropertyGravity gravity) {}
}
