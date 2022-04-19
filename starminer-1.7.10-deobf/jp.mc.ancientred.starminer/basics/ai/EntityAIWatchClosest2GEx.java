package jp.mc.ancientred.starminer.basics.ai;

import net.minecraft.entity.EntityLiving;

public class EntityAIWatchClosest2GEx extends EntityAIWatchClosestGEx {
  public EntityAIWatchClosest2GEx(EntityLiving entityLiving, Class clazz, float maxDistanceForPlayer, float randomValueThresHold) {
    super(entityLiving, clazz, maxDistanceForPlayer, randomValueThresHold);
    setMutexBits(3);
  }
}
