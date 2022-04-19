package jp.mc.ancientred.starminer.core.common;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import jp.mc.ancientred.starminer.api.GravityDirection;
import jp.mc.ancientred.starminer.core.entity.ExtendedPropertyGravity;
import jp.mc.ancientred.starminer.core.packet.SMCorePacketSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.IExtendedEntityProperties;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;

public class SMCommonEventHandler
{
  @SubscribeEvent
  public void registerExtendedProperty(EntityEvent.EntityConstructing event) {
    event.entity.registerExtendedProperties("starminer.Gravity", (IExtendedEntityProperties)new ExtendedPropertyGravity());
  }
  @SubscribeEvent
  public void handlePlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
    try {
      if (event.player instanceof EntityPlayerMP)
      {
        ExtendedPropertyGravity gProp = ExtendedPropertyGravity.getExtendedPropertyGravity((Entity)event.player);
        if (gProp != null && gProp.isAttracted)
        {
          SMCorePacketSender.sendAttractedChangePacketToPlayer((EntityPlayerMP)event.player, true, true, gProp.attractedPosX, gProp.attractedPosY, gProp.attractedPosZ);
        
        }
      
      }

    }
    catch (Exception ex) {
      ex.printStackTrace();
    } 
  }
  @SubscribeEvent
  public void handlePlayerTossedItem(ItemTossEvent event) {
    ExtendedPropertyGravity gravity = ExtendedPropertyGravity.getExtendedPropertyGravity((Entity)event.player);
    if (gravity != null && gravity.gravityDirection != GravityDirection.upTOdown_YN) {
      EntityItem entityitem = event.entityItem;
      
      gravity.setGravityFixedPlayerShootVec(event.player, (Entity)entityitem, 1.0F);
      entityitem.motionX *= 0.2D;
      entityitem.motionY *= 0.2D;
      entityitem.motionZ *= 0.2D;
    } 
  }
}
