package jp.mc.ancientred.starminer.api;

import java.util.ArrayList;
import net.minecraft.entity.player.EntityPlayer;

public interface IRotateSleepingViewHandler
{
  public static final ArrayList<IRotateSleepingViewHandler> handlerList = new ArrayList<IRotateSleepingViewHandler>();
  
  boolean rotateSleepingFPView();
  
  boolean rotateTPPlayerSleeping(EntityPlayer paramEntityPlayer);
}
