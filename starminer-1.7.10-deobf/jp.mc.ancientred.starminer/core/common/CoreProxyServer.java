package jp.mc.ancientred.starminer.core.common;

import cpw.mods.fml.server.FMLServerHandler;
import jp.mc.ancientred.starminer.core.CoreConfig;
import jp.mc.ancientred.starminer.core.SMCoreModContainer;
import jp.mc.ancientred.starminer.core.entity.EntityLivingGravitized;
import jp.mc.ancientred.starminer.core.entity.ExtendedPropertyGravity;
import jp.mc.ancientred.starminer.core.packet.SMCorePacketHandlerServer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class CoreProxyServer
  extends CoreProxy {
  public void registerNetworkHandler() {
    SMCoreModContainer.coreChannel.register(new SMCorePacketHandlerServer());
  }
  
  public boolean isOtherPlayer(EntityPlayer player) {
    return true;
  }

  public void setFlyMovementByGravity(EntityPlayer entityPlayer) {}

  public void warnOrKickIllegalGravity(EntityLivingGravitized entityLivingGravitized, ExtendedPropertyGravity gravity) {
    if (FMLServerHandler.instance() != null) {
      if (CoreConfig.showUnsynchronizedWarning)
      {
        
        FMLServerHandler.instance().getServer().logWarning(((EntityPlayerMP)entityLivingGravitized).getCommandSenderName() + " had gravity unsynchronization long time!");
      }
      
      if (CoreConfig.unsynchronizedWarnToKick != 0 && gravity.unsynchronizedWarnCount >= CoreConfig.unsynchronizedWarnToKick) {
        
        FMLServerHandler.instance().getServer().logWarning(((EntityPlayerMP)entityLivingGravitized).getCommandSenderName() + " was kicked for gravity unsynchronization for far long time!");
        
        ((EntityPlayerMP)entityLivingGravitized).playerNetServerHandler.kickPlayerFromServer("You had too long time gravity unsynchronized with server!!!");
        return;
      } 
    } 
  }
}
