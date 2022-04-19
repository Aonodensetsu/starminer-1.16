package jp.mc.ancientred.starminer.core.packet;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import jp.mc.ancientred.starminer.api.GravityDirection;
import jp.mc.ancientred.starminer.core.SMCoreModContainer;
import jp.mc.ancientred.starminer.core.entity.EntityLivingGravitized;
import jp.mc.ancientred.starminer.core.entity.ExtendedPropertyGravity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;

public class SMCorePacketHandlerServer
{
  @SubscribeEvent
  public void onServerPacket(FMLNetworkEvent.ServerCustomPacketEvent event) throws IOException {
    FMLProxyPacket packet = event.packet;
    String channelName = event.packet.channel();
    
    NetHandlerPlayServer theNetHandlerPlayServer = (NetHandlerPlayServer)event.handler;
    EntityPlayerMP thePlayer = theNetHandlerPlayServer.playerEntity;
    
    if (channelName.equals("StarminerCore")) {

      int packetType = packet.payload().readInt();

      if (packetType == 1) {
        receiveGravityStateChangeOnServer(packet, thePlayer);
      }
    } 
  }

  private void receiveGravityStateChangeOnServer(FMLProxyPacket packet, EntityPlayerMP player) {
    try {
      ByteBuf data = packet.payload();
      EntityPlayerMP entityPlayerMP = player;
      
      int entityId = data.readInt();
      int gravityDirectionInt = data.readInt();
      
      if (entityPlayerMP.getEntityId() == entityId) {
        
        ExtendedPropertyGravity gravity = ExtendedPropertyGravity.getExtendedPropertyGravity((Entity)entityPlayerMP);

        GravityDirection old = gravity.gravityDirection;

        gravity.gravityDirection = GravityDirection.values()[gravityDirectionInt];
        
        if (old != gravity.gravityDirection)
        {
          
          entityPlayerMP.setPosition(((EntityPlayer)entityPlayerMP).posX, ((EntityPlayer)entityPlayerMP).posY, ((EntityPlayer)entityPlayerMP).posZ);
          Object cast = entityPlayerMP;
          if (cast instanceof EntityLivingGravitized) {
            ((EntityLivingGravitized)cast).fixOutFromOpaqueBlock(gravity.gravityDirection);
          }

          NetworkRegistry.TargetPoint targetPoint = new NetworkRegistry.TargetPoint(((EntityPlayer)entityPlayerMP).worldObj.provider.dimensionId, ((EntityPlayer)entityPlayerMP).posX, ((EntityPlayer)entityPlayerMP).posY, ((EntityPlayer)entityPlayerMP).posZ, 50.0D);
          SMCoreModContainer.coreChannel.sendToAllAround(packet, targetPoint);
        }
      
      } 
    } catch (Exception e) {
      e.printStackTrace();
    } 
  }
}
