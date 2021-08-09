package dev.bluecom.starminer.api;

import java.util.ArrayList;
import net.minecraft.entity.player.PlayerEntity;

public interface IRotateSleepingViewHandler {
	ArrayList<IRotateSleepingViewHandler> handlerList = new ArrayList<>();
	
	boolean rotateSleepingFPView();
	
	boolean rotateTPPlayerSleeping(PlayerEntity paramEntityPlayer);
}
