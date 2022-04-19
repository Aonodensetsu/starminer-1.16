package jp.mc.ancientred.starminer.basics.common;

import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.event.world.WorldEvent;

public class CommonProxy implements IGuiHandler {
  public void registerNetworkHandler() {}
  
  public void registerRenderHelper() {}
  
  public World getClientWorld() {
    return null;
  }
  
  public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
    return null;
  } public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
    return null;
  }
  
  public void handleWorldLoadEvent(WorldEvent.Load event) {}
  
  public void doWakeupAll(WorldServer worldServer) {}
  
  public void canselLightGapUpdate(IChunkProvider chunkProviderClient) {}
  
  public void showGrappleGunGuideMessage() {}
  
  public void setRemainingHighlightTicksOFF() {}
}
