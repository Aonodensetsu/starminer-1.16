package jp.mc.ancientred.starminer.basics.common;

import jp.mc.ancientred.starminer.basics.SMModContainer;
import jp.mc.ancientred.starminer.basics.dimention.MapFromSky;
import jp.mc.ancientred.starminer.basics.gui.ContainerStarCore;
import jp.mc.ancientred.starminer.basics.packet.SMPacketHandlerServer;
import jp.mc.ancientred.starminer.basics.tileentity.TileEntityGravityGenerator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.launchwrapper.LogWrapper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.event.world.WorldEvent;
import org.apache.logging.log4j.Level;

public class CommonProxyServer
  extends CommonProxy
{
  public World getClientWorld() {
    return null;
  }
  
  public void registerNetworkHandler() {
    SMModContainer.channel.register(new SMPacketHandlerServer());
  }
  
  public void registerRenderHelper() {}
  
  public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
    if (ID == SMModContainer.guiStarCoreId) {
      TileEntity te = world.getTileEntity(x, y, z);
      if (te != null && te instanceof TileEntityGravityGenerator) {
        return new ContainerStarCore(player, (TileEntityGravityGenerator)te);
      }
    } 
    return null;
  }
  
  public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
    return null;
  }

  public void handleWorldLoadEvent(WorldEvent.Load event) {
    if (event.world.provider.dimensionId == 0 && event.world.provider instanceof net.minecraft.world.WorldProviderSurface) {
      LogWrapper.log(Level.INFO, "[Starminer]Creating dimention 0(surface) ground texture at server....", new Object[0]);
      MapFromSky.createMapDataFromSky(event.world);
    } 
  }
  
  public void doWakeupAll(WorldServer worldServer) {}
  
  public void canselLightGapUpdate(IChunkProvider chunkProvider_Client) {}
  
  public void showGrappleGunGuideMessage() {}
  
  public void setRemainingHighlightTicksOFF() {}
}
