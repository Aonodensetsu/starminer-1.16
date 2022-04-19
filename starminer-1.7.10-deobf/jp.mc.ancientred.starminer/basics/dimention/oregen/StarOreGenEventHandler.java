package jp.mc.ancientred.starminer.basics.dimention.oregen;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.terraingen.OreGenEvent;

public class StarOreGenEventHandler
{
  private StarOreGen starOreGenerator = new StarOreGen();
  
  @SubscribeEvent
  public void handleOrgGenPostEvent(OreGenEvent.Post event) {
    if (event instanceof OreGenEvent.Post && 
      event.world.provider instanceof net.minecraft.world.WorldProviderSurface)
      this.starOreGenerator.generateOreInSurface(event.world, event.rand, event.worldX, event.worldZ); 
  }
}
