package jp.mc.ancientred.starminer.basics.packet;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import jp.mc.ancientred.starminer.basics.Config;
import jp.mc.ancientred.starminer.basics.SMModContainer;
import jp.mc.ancientred.starminer.basics.dimention.MapFromSky;
import jp.mc.ancientred.starminer.basics.tileentity.TileEntityGravityGenerator;
import jp.mc.ancientred.starminer.basics.tileentity.TileEntityNavigator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class SMPacketHandlerClient
{
  @SubscribeEvent
  public void onClientPacket(FMLNetworkEvent.ClientCustomPacketEvent event) throws IOException {
    FMLProxyPacket packet = event.packet;
    String channelName = event.packet.channel();
    
    NetHandlerPlayClient theNetHandlerPlayClient = (NetHandlerPlayClient)event.handler;
    
    if (channelName.equals("Starminer")) {

      int packetType = packet.payload().readInt();

      if (packetType == 14 || packetType == 16) {
        updateTileEntityOnClient(packetType, packet);
      }

      if (packetType == 18) {
        receiveDimentionRespawnPacket(packet);
      }

      if (packetType == 20) {
        receiveReRidePacket(packet);
      }
      
      if (packetType == 12) {
        receiveMapFromSky(packet);
      }
    } 
  }
  
  private void receiveDimentionRespawnPacket(FMLProxyPacket packet) {
    try {
      EntityClientPlayerMP entityClientPlayerMP = (Minecraft.getMinecraft()).thePlayer;
      ByteBuf data = packet.payload();
      
      S07PacketRespawn packetRespawn = new S07PacketRespawn();
      PacketBuffer dummyBuff = new PacketBuffer(data);
      packetRespawn.readPacketData(dummyBuff);
      
      ((EntityPlayer)entityClientPlayerMP).lastTickPosY = ((EntityPlayer)entityClientPlayerMP).posY = (packetRespawn.func_149082_c() != 0) ? -10.0D : 288.0D;
      
      Minecraft.getMinecraft().getNetHandler().handleRespawn(packetRespawn);

      entityClientPlayerMP = (Minecraft.getMinecraft()).thePlayer;
      ((EntityPlayer)entityClientPlayerMP).lastTickPosY = ((EntityPlayer)entityClientPlayerMP).posY = (((EntityPlayer)entityClientPlayerMP).worldObj.provider instanceof jp.mc.ancientred.starminer.api.IZeroGravityWorldProvider) ? -10.0D : 288.0D;
    }
    catch (Exception ex) {
      ex.printStackTrace();
    } 
  }

  private void receiveReRidePacket(FMLProxyPacket packet) {
    World world = SMModContainer.proxy.getClientWorld();
    try {
      ByteBuf data = packet.payload();
      int ridingEntity = data.readInt();
      int riddenByEntity = data.readInt();
      
      Entity entityRiding = world.getEntityByID(ridingEntity);
      Entity entityRiddenBy = world.getEntityByID(riddenByEntity);
      if (entityRiding != null && entityRiddenBy != null && !entityRiding.isDead && !entityRiddenBy.isDead && entityRiding.ridingEntity == null)
      {
        
        entityRiding.mountEntity(entityRiddenBy);
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    } 
  }

  private void receiveMapFromSky(FMLProxyPacket packet) {
    try {
      ByteBuf data = packet.payload();
      Config.enableFakeRotatorOnlyVannilaBlock = data.readBoolean();
      int dataLength = data.readShort();
      
      if (MapFromSky.skyMapclientData == null) {
        
        MapFromSky.skyMapclientData = new byte[dataLength];
      } else {
        
        MapFromSky.doRecompileSkyMapList = true;
      } 
      data.readBytes(MapFromSky.skyMapclientData);
      MapFromSky.hasSkyMapImageData = true;
    }
    catch (Exception ex) {
      ex.printStackTrace();
    } 
  }

  private void updateTileEntityOnClient(int packetType, FMLProxyPacket packet) {
    ByteBuf data = packet.payload();

    try {
      TileEntity tileEntity;
      
      int x = data.readInt();
      int y = data.readInt();
      int z = data.readInt();
      
      World world = SMModContainer.proxy.getClientWorld();
      
      switch (packetType) {
        case 14:
          tileEntity = world.getTileEntity(x, y, z);
          
          if (tileEntity != null && tileEntity instanceof TileEntityGravityGenerator) {
            ((TileEntityGravityGenerator)tileEntity).gravityRange = data.readDouble();
            ((TileEntityGravityGenerator)tileEntity).starRad = data.readDouble();
            ((TileEntityGravityGenerator)tileEntity).type = data.readInt();
            if (data.isReadable(1)) {
              ((TileEntityGravityGenerator)tileEntity).useBufferArea = data.readBoolean();
            }
          } 
          break;
        case 16:
          tileEntity = world.getTileEntity(x, y, z);
          
          if (tileEntity != null && tileEntity instanceof TileEntityNavigator) {
            ((TileEntityNavigator)tileEntity).lookX = data.readFloat();
            ((TileEntityNavigator)tileEntity).lookY = data.readFloat();
            ((TileEntityNavigator)tileEntity).lookZ = data.readFloat();
            ((TileEntityNavigator)tileEntity).activeTickCount = data.readInt();
          } 
          break;
      } 
    } catch (Exception e) {
      e.printStackTrace();
    } 
  }
}
