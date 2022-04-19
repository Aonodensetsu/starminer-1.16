package jp.mc.ancientred.starminer.core.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;

public class EntityAIWatchClosestGEx extends EntityAIBase {
  private EntityLiving theWatcher;
  protected Entity closestEntity;
  private float maxDistanceForPlayer;
  private int lookTime;
  private float randomValueThresHold;
  private Class watchedClass;
  
  public EntityAIWatchClosestGEx(EntityLiving p_i1631_1_, Class clazz, float maxDistanceForPlayer) {
    this.theWatcher = p_i1631_1_;
    this.watchedClass = clazz;
    this.maxDistanceForPlayer = maxDistanceForPlayer;
    this.randomValueThresHold = 0.02F;
    setMutexBits(2);
  }

  public EntityAIWatchClosestGEx(EntityLiving p_i1632_1_, Class clazz, float maxDistanceForPlayer, float randomValueThresHold) {
    this.theWatcher = p_i1632_1_;
    this.watchedClass = clazz;
    this.maxDistanceForPlayer = maxDistanceForPlayer;
    this.randomValueThresHold = randomValueThresHold;
    setMutexBits(2);
  }

  public boolean shouldExecute() {
    if (this.theWatcher.getRNG().nextFloat() >= this.randomValueThresHold) {
      return false;
    }
    if (this.theWatcher.getAttackTarget() != null) {
      this.closestEntity = (Entity)this.theWatcher.getAttackTarget();
    }
    
    if (this.watchedClass == EntityPlayer.class) {
      this.closestEntity = (Entity)this.theWatcher.worldObj.getClosestPlayerToEntity((Entity)this.theWatcher, this.maxDistanceForPlayer);
    } else {
      this.closestEntity = this.theWatcher.worldObj.findNearestEntityWithinAABB(this.watchedClass, this.theWatcher.boundingBox.expand(this.maxDistanceForPlayer, 3.0D, this.maxDistanceForPlayer), (Entity)this.theWatcher);
    } 
    
    return (this.closestEntity != null);
  }

  public boolean continueExecuting() {
    return !this.closestEntity.isEntityAlive() ? false : ((this.theWatcher.getDistanceSqToEntity(this.closestEntity) > (this.maxDistanceForPlayer * this.maxDistanceForPlayer)) ? false : ((this.lookTime > 0)));
  }

  public void startExecuting() {
    this.lookTime = 40 + this.theWatcher.getRNG().nextInt(40);
  }

  public void resetTask() {
    this.closestEntity = null;
  }

  public void updateTask() {
    this.theWatcher.getLookHelper().setLookPositionWithEntity(this.closestEntity, 10.0F, this.theWatcher.getVerticalFaceSpeed());
    this.lookTime--;
  }
}
