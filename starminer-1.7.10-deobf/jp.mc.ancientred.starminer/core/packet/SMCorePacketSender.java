package jp.mc.ancientred.starminer.core.packet;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import jp.mc.ancientred.starminer.api.GravityDirection;
import jp.mc.ancientred.starminer.core.SMCoreModContainer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;

public class SMCorePacketSender
{
  public static void sendAttractedChangePacketToPlayer(EntityPlayerMP entityPlayer, boolean attractedState, boolean immidiate, int attractedX, int attractedY, int attractedZ) {
    ByteBufOutputStream bbos = new ByteBufOutputStream(Unpooled.buffer());

    try { bbos.writeInt(0);
      bbos.writeInt(entityPlayer.getEntityId());
      bbos.writeBoolean(immidiate);
      bbos.writeBoolean(attractedState);
      bbos.writeInt(attractedX);
      bbos.writeInt(attractedY);
      bbos.writeInt(attractedZ);

      FMLProxyPacket thePacket = new FMLProxyPacket(bbos.buffer(), "StarminerCore");

      SMCoreModContainer.coreChannel.sendTo(thePacket, entityPlayer); }
    
    catch (Exception e)
    { e.printStackTrace(); }
    finally { 
      try { bbos.close(); } catch (Exception ex) {} }
  
  }

  public static void sendGravityStatePacketToServer(Entity entity, GravityDirection newGravityDirection) {
    ByteBufOutputStream bbos = new ByteBufOutputStream(Unpooled.buffer());

    try { bbos.writeInt(1);
      bbos.writeInt(entity.getEntityId());
      bbos.writeInt(newGravityDirection.ordinal());

      FMLProxyPacket thePacket = new FMLProxyPacket(bbos.buffer(), "StarminerCore");

      SMCoreModContainer.coreChannel.sendToServer(thePacket); }
    
    catch (Exception e)
    { e.printStackTrace(); }
    finally { 
      try { bbos.close(); } catch (Exception ex) {} }
  
  }

  public static void sendMobAttractedChangePacketToAllAround(Entity entity, boolean attractedState, boolean immidiate, int attractedX, int attractedY, int attractedZ) {
    ByteBufOutputStream bbos = new ByteBufOutputStream(Unpooled.buffer());

    try { bbos.writeInt(2);
      bbos.writeInt(entity.getEntityId());
      bbos.writeBoolean(immidiate);
      bbos.writeBoolean(attractedState);
      bbos.writeInt(attractedX);
      bbos.writeInt(attractedY);
      bbos.writeInt(attractedZ);

      FMLProxyPacket thePacket = new FMLProxyPacket(bbos.buffer(), "StarminerCore");

      NetworkRegistry.TargetPoint targetPoint = new NetworkRegistry.TargetPoint(entity.worldObj.provider.dimensionId, entity.posX, entity.posY, entity.posZ, 50.0D);
      SMCoreModContainer.coreChannel.sendToAllAround(thePacket, targetPoint); }
    
    catch (Exception e)
    { e.printStackTrace(); }
    finally { 
      try { bbos.close(); } catch (Exception ex) {} }
  
  }
}
