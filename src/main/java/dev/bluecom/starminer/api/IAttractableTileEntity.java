package dev.bluecom.starminer.api;

import dev.bluecom.starminer.api.util.GravityDirection;
import net.minecraft.entity.Entity;

public interface IAttractableTileEntity {
	GravityDirection getCurrentGravity(Entity paramEntity);
	boolean inGravityRange(Entity paramEntity);
}
