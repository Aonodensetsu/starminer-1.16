package jp.mc.ancientred.starminer.api;

import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.Teleporter;

public interface IMountTeleportHandler {
  boolean handleSpaceTeleport(IEntityTransfer paramIEntityTransfer, Teleporter paramTeleporter, int paramInt, double paramDouble1, double paramDouble2, double paramDouble3, List paramList);
  
  public static interface IEntityTransfer {
    Entity travelEntityToDimension(Entity param1Entity, int param1Int, Teleporter param1Teleporter);
    
    void travelPlayerToDimension(EntityPlayer param1EntityPlayer, int param1Int, Teleporter param1Teleporter);
  }
}
