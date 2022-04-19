package jp.mc.ancientred.starminer.core.packet;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import jp.mc.ancientred.starminer.api.GravityDirection;
import jp.mc.ancientred.starminer.core.entity.ExtendedPropertyGravity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class SMCorePacketHandlerClient
{
  @SubscribeEvent
  public void onClientPacket(FMLNetworkEvent.ClientCustomPacketEvent event) throws IOException {
    FMLProxyPacket packet = event.packet;
    String channelName = event.packet.channel();
    
    NetHandlerPlayClient theNetHandlerPlayClient = (NetHandlerPlayClient)event.handler;
    
    if (channelName.equals("StarminerCore")) {

      int packetType = packet.payload().readInt();

      if (packetType == 0) {
        receiveAttractPacketOnClient(packet);
      }

      if (packetType == 2) {
        receiveMobEntityAttractPacketOnClient(packet);
      }

      if (packetType == 1) {
        receiveGravityStateChangeOnClient(packet);
      }
    } 
  }

  private void receiveAttractPacketOnClient(FMLProxyPacket packet) {
    try {
      ByteBuf data = packet.payload();
      EntityClientPlayerMP entityClientPlayerMP = (Minecraft.getMinecraft()).thePlayer;
      
      int entityId = data.readInt();
      boolean immidiate = data.readBoolean();
      boolean attractedState = data.readBoolean();
      int attractedX = data.readInt();
      int attractedY = data.readInt();
      int attractedZ = data.readInt();
      
      if (entityClientPlayerMP.getEntityId() == entityId) {
        
        Entity entity = ((EntityPlayer)entityClientPlayerMP).worldObj.getEntityByID(entityId);
        if (entity != null && entity == entityClientPlayerMP) {
          ExtendedPropertyGravity gravity = ExtendedPropertyGravity.getExtendedPropertyGravity((Entity)entityClientPlayerMP);

          if (attractedState) {
            gravity.isAttracted = true;
            gravity.attractedPosX = attractedX;
            gravity.attractedPosY = attractedY;
            gravity.attractedPosZ = attractedZ;
            
            gravity.changeGravityImmidiate = immidiate;
          } else {
            gravity.isAttracted = false;
            gravity.attractedPosX = 0;
            gravity.attractedPosY = 0;
            gravity.attractedPosZ = 0;
          } 
        } 
      } 
    } catch (Exception ex) {
      ex.printStackTrace();
    } 
  }

  private void receiveMobEntityAttractPacketOnClient(FMLProxyPacket packet) {
    try {
      ByteBuf data = packet.payload();
      EntityClientPlayerMP entityClientPlayerMP = (Minecraft.getMinecraft()).thePlayer;
      
      int entityId = data.readInt();
      boolean immidiate = data.readBoolean();
      boolean attractedState = data.readBoolean();
      int attractedX = data.readInt();
      int attractedY = data.readInt();
      int attractedZ = data.readInt();
      
      Entity entity = ((EntityPlayer)entityClientPlayerMP).worldObj.getEntityByID(entityId);
      if (entity != null) {
        ExtendedPropertyGravity gravity = ExtendedPropertyGravity.getExtendedPropertyGravity(entity);

        if (attractedState) {
          gravity.isAttracted = true;
          gravity.attractedPosX = attractedX;
          gravity.attractedPosY = attractedY;
          gravity.attractedPosZ = attractedZ;
        } else {
          gravity.isAttracted = false;
          gravity.attractedPosX = 0;
          gravity.attractedPosY = 0;
          gravity.attractedPosZ = 0;
        } 
      } 
    } catch (Exception ex) {
      ex.printStackTrace();
    } 
  }

  private void receiveGravityStateChangeOnClient(FMLProxyPacket packet) {
    try {
      ByteBuf data = packet.payload();
      EntityClientPlayerMP entityClientPlayerMP = (Minecraft.getMinecraft()).thePlayer;
      
      int entityId = data.readInt();
      int gravityDirectionInt = data.readInt();
      
      if (entityClientPlayerMP.getEntityId() != entityId)
      {
        World world = ((EntityPlayer)entityClientPlayerMP).worldObj;
        if (world == null)
          return;  Entity entity = world.getEntityByID(entityId);
        if (entity == null)
          return;  ExtendedPropertyGravity gravity = ExtendedPropertyGravity.getExtendedPropertyGravity(entity);
        if (gravity == null) {
          return;
        }
        GravityDirection old = gravity.gravityDirection;

        gravity.gravityDirection = GravityDirection.values()[gravityDirectionInt];
        
        if (old != gravity.gravityDirection)
        {
          
          entity.setPosition(entity.posX, entity.posY, entity.posZ);
        }
      }
    
    } catch (Exception e) {
      e.printStackTrace();
    } 
  }
}
