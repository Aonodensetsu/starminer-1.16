package jp.mc.ancientred.starminer.basics.packet;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import jp.mc.ancientred.starminer.basics.Config;
import jp.mc.ancientred.starminer.basics.SMModContainer;
import jp.mc.ancientred.starminer.basics.dimention.MapFromSky;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;

public class SMPacketSender
{
  public static void sendRespawnPacketToPlayer(EntityPlayerMP entityPlayer, int dimentionId, EnumDifficulty difficulty, WorldType worldType, WorldSettings.GameType gameType) {
    ByteBuf buf;
    PacketBuffer buffef = new PacketBuffer(buf = Unpooled.buffer());
    
    try {
      buffef.writeInt(18);
      
      S07PacketRespawn packetRespawn = new S07PacketRespawn(dimentionId, difficulty, worldType, gameType);
      packetRespawn.writePacketData(buffef);

      FMLProxyPacket thePacket = new FMLProxyPacket(buf, "Starminer");

      SMModContainer.channel.sendTo(thePacket, entityPlayer);
    }
    catch (Exception e) {
      e.printStackTrace();
    } 
  }

  public static void sendReRidePacket(Entity riding, Entity riddenBy) {
    ByteBufOutputStream bbos = new ByteBufOutputStream(Unpooled.buffer());

    
    try { bbos.writeInt(20);
      bbos.writeInt(riding.getEntityId());
      bbos.writeInt(riddenBy.getEntityId());
      
      FMLProxyPacket thePacket = new FMLProxyPacket(bbos.buffer(), "Starminer");
      
      NetworkRegistry.TargetPoint point = new NetworkRegistry.TargetPoint(riddenBy.dimension, riddenBy.posX, riddenBy.posY, riddenBy.posZ, 60.0D);
      SMModContainer.channel.sendToAllAround(thePacket, point); }
    
    catch (Exception e)
    { e.printStackTrace(); }
    finally { 
      try { bbos.close(); } catch (Exception ex) {} }
  
  }
  
  public static void sendSkyMapPacketToPlayer(EntityPlayerMP entityPlayer) {
    ByteBufOutputStream bbos = new ByteBufOutputStream(Unpooled.buffer());

    try { bbos.writeInt(12);
      bbos.writeBoolean(Config.enableFakeRotatorOnlyVannilaBlock);
      bbos.writeShort(MapFromSky.mapDataFromSky.colors.length);
      bbos.write(MapFromSky.mapDataFromSky.colors);
      
      FMLProxyPacket thePacket = new FMLProxyPacket(bbos.buffer(), "Starminer");
      
      SMModContainer.channel.sendTo(thePacket, entityPlayer); }
    
    catch (Exception e)
    { e.printStackTrace(); }
    finally { 
      try { bbos.close(); } catch (Exception ex) {} }
  
  }

  public static Packet createGUIActPacket(int data) {
    ByteBufOutputStream bbos = new ByteBufOutputStream(Unpooled.buffer());

    try { bbos.writeInt(10);
      bbos.writeInt(data);
      
      FMLProxyPacket thePacket = new FMLProxyPacket(bbos.buffer(), "Starminer");
      
      return (Packet)thePacket; }
    catch (Exception e)
    { e.printStackTrace(); }
    finally { 
      try { bbos.close(); } catch (Exception ex) {} }
    
    return null;
  }

  public static Packet createTEGcoreDescriptionPacket(int xCoord, int yCoord, int zCoord, double gravityRange, double starRad, int type, boolean useBufferArea) {
    ByteBufOutputStream bbos = new ByteBufOutputStream(Unpooled.buffer());
    
    try { bbos.writeInt(14);
      bbos.writeInt(xCoord);
      bbos.writeInt(yCoord);
      bbos.writeInt(zCoord);
      bbos.writeDouble(gravityRange);
      bbos.writeDouble(starRad);
      bbos.writeInt(type);
      bbos.writeBoolean(useBufferArea);

      FMLProxyPacket thePacket = new FMLProxyPacket(bbos.buffer(), "Starminer");
      
      return (Packet)thePacket; }
    catch (Exception e)
    { e.printStackTrace(); }
    finally { 
      try { bbos.close(); } catch (Exception ex) {} }
    
    return null;
  }

  public static Packet createTENaviDescriptionPacket(int xCoord, int yCoord, int zCoord, float lookX, float lookY, float lookZ, int activeTickCount) {
    ByteBufOutputStream bbos = new ByteBufOutputStream(Unpooled.buffer());

    try { bbos.writeInt(16);
      bbos.writeInt(xCoord);
      bbos.writeInt(yCoord);
      bbos.writeInt(zCoord);
      bbos.writeFloat(lookX);
      bbos.writeFloat(lookY);
      bbos.writeFloat(lookZ);
      bbos.writeInt(activeTickCount);
      
      FMLProxyPacket thePacket = new FMLProxyPacket(bbos.buffer(), "Starminer");
      
      return (Packet)thePacket; }
    catch (Exception e)
    { e.printStackTrace(); }
    finally { 
      try { bbos.close(); } catch (Exception ex) {} }
    
    return null;
  }
}
