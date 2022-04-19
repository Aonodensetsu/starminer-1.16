package jp.mc.ancientred.starminer.core.common;

import jp.mc.ancientred.starminer.api.GravityDirection;
import jp.mc.ancientred.starminer.core.SMCoreModContainer;
import jp.mc.ancientred.starminer.core.entity.EntityLivingGravitized;
import jp.mc.ancientred.starminer.core.entity.ExtendedPropertyGravity;
import jp.mc.ancientred.starminer.core.packet.SMCorePacketHandlerClient;
import jp.mc.ancientred.starminer.core.packet.SMCorePacketHandlerServer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class CoreProxyClient extends CoreProxy {
  public void registerNetworkHandler() {
    SMCoreModContainer.coreChannel.register(new SMCorePacketHandlerClient());
    SMCoreModContainer.coreChannel.register(new SMCorePacketHandlerServer());
  }
  
  public boolean isOtherPlayer(EntityPlayer player) {
    return player instanceof net.minecraft.client.entity.EntityOtherPlayerMP;
  }
  
  public void setFlyMovementByGravity(EntityPlayer entityPlayer) {
    if (entityPlayer instanceof EntityPlayerSP) {
      EntityPlayerSP entityPlayerSP = (EntityPlayerSP)entityPlayer;
      if (entityPlayerSP.capabilities.isFlying)
        switch (ExtendedPropertyGravity.getGravityDirection((Entity)entityPlayerSP)) {
          case southTOnorth_ZN:
            if (entityPlayerSP.movementInput.sneak) {
              entityPlayerSP.motionZ -= 0.15D; entityPlayerSP.motionY += 0.15D;
            } 
            if (entityPlayerSP.movementInput.jump) {
              entityPlayerSP.motionZ += 0.15D; entityPlayerSP.motionY -= 0.15D;
            } 
            break;
          case northTOsouth_ZP:
            if (entityPlayerSP.movementInput.sneak) {
              entityPlayerSP.motionZ += 0.15D; entityPlayerSP.motionY += 0.15D;
            } 
            if (entityPlayerSP.movementInput.jump) {
              entityPlayerSP.motionZ -= 0.15D; entityPlayerSP.motionY -= 0.15D;
            } 
            break;
          case westTOeast_XP:
            if (entityPlayerSP.movementInput.sneak) {
              entityPlayerSP.motionX += 0.15D; entityPlayerSP.motionY += 0.15D;
            } 
            if (entityPlayerSP.movementInput.jump) {
              entityPlayerSP.motionX -= 0.15D; entityPlayerSP.motionY -= 0.15D;
            } 
            break;
          case eastTOwest_XN:
            if (entityPlayerSP.movementInput.sneak) {
              entityPlayerSP.motionX -= 0.15D; entityPlayerSP.motionY += 0.15D;
            } 
            if (entityPlayerSP.movementInput.jump) {
              entityPlayerSP.motionX += 0.15D; entityPlayerSP.motionY -= 0.15D;
            } 
            break;
          case downTOup_YP:
            if (entityPlayerSP.movementInput.sneak) {
              entityPlayerSP.motionY += 0.15D; entityPlayerSP.motionY += 0.15D;
            } 
            if (entityPlayerSP.movementInput.jump) {
              entityPlayerSP.motionY -= 0.15D; entityPlayerSP.motionY -= 0.15D;
            } 
            break;
        }  
    } 
  }
  
  public void warnOrKickIllegalGravity(EntityLivingGravitized entityLivingGravitized, ExtendedPropertyGravity gravity) {}
}
