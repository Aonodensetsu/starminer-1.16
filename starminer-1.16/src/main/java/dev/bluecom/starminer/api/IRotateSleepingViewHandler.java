package dev.bluecom.starminer.api;

import java.util.ArrayList;
import net.minecraft.entity.player.PlayerEntity;

public interface IRotateSleepingViewHandler {
	public static final ArrayList<IRotateSleepingViewHandler> handlerList = new ArrayList<IRotateSleepingViewHandler>();
	
	boolean rotateSleepingFPView();
	
	boolean rotateTPPlayerSleeping(PlayerEntity paramEntityPlayer);
}
