package jp.mc.ancientred.starminer.basics.packet;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import jp.mc.ancientred.starminer.api.Gravity;
import jp.mc.ancientred.starminer.api.GravityDirection;
import jp.mc.ancientred.starminer.basics.SMModContainer;
import jp.mc.ancientred.starminer.basics.gui.ContainerStarCore;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;

public class SMPacketHandlerServer
{
  @SubscribeEvent
  public void onServerPacket(FMLNetworkEvent.ServerCustomPacketEvent event) throws IOException {
    FMLProxyPacket packet = event.packet;
    String channelName = event.packet.channel();
    
    NetHandlerPlayServer theNetHandlerPlayServer = (NetHandlerPlayServer)event.handler;
    EntityPlayerMP thePlayer = theNetHandlerPlayServer.playerEntity;
    
    if (channelName.equals("Starminer")) {

      int packetType = packet.payload().readInt();

      if (packetType == 10)
      {
        if (thePlayer != null && thePlayer.openContainer instanceof ContainerStarCore) {
          
          ContainerStarCore container = (ContainerStarCore)thePlayer.openContainer;
          container.receiveButtonAction(packet.payload().readInt());
        } 
      }
    } 
  }

  private void receiveGravityStateChangeOnServer(FMLProxyPacket packet, EntityPlayerMP player) {
    try {
      ByteBuf data = packet.payload();
      int entityId = data.readInt();
      int gravityDirectionInt = data.readInt();
      
      if (player.getEntityId() == entityId) {
        
        Gravity gravity = Gravity.getGravityProp((Entity)player);

        GravityDirection old = gravity.gravityDirection;

        gravity.gravityDirection = GravityDirection.values()[gravityDirectionInt];
        
        if (old != gravity.gravityDirection)
        {
          
          player.setPosition(player.posX, player.posY, player.posZ);

          NetworkRegistry.TargetPoint targetPoint = new NetworkRegistry.TargetPoint(player.worldObj.provider.dimensionId, player.posX, player.posY, player.posZ, 50.0D);
          SMModContainer.channel.sendToAllAround(packet, targetPoint);
        }
      
      } 
    } catch (Exception e) {
      e.printStackTrace();
    } 
  }
}
