package jp.mc.ancientred.starminer.basics.dimention;

import net.minecraft.entity.Entity;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class TeleporterForSpace
  extends Teleporter
{
  public TeleporterForSpace(WorldServer par1WorldServer) {
    super(par1WorldServer);
  }

  public void placeInPortal(Entity par1Entity, double par2, double par4, double par6, float par8) {
    par1Entity.setLocationAndAngles(par1Entity.posX, par1Entity.posY, par1Entity.posZ, par1Entity.rotationYaw, par1Entity.rotationPitch);
  }
  
  public void removeStalePortalLocations(long par1) {}
}
