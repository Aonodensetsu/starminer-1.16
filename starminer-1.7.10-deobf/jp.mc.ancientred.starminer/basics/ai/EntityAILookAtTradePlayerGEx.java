package jp.mc.ancientred.starminer.basics.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;

public class EntityAILookAtTradePlayerGEx extends EntityAIWatchClosestGEx {
  public EntityAILookAtTradePlayerGEx(EntityVillager villager) {
    super((EntityLiving)villager, EntityPlayer.class, 8.0F);
    this.theMerchant = villager;
  }
  private final EntityVillager theMerchant;
  public boolean shouldExecute() {
    if (this.theMerchant.isTrading()) {
      this.closestEntity = (Entity)this.theMerchant.getCustomer();
      return true;
    } 
    return false;
  }
}
