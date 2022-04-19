package jp.mc.ancientred.starminer.basics.ai;

import jp.mc.ancientred.starminer.api.Gravity;
import jp.mc.ancientred.starminer.core.entity.EntityLivingGravitized;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;

public abstract class AbstractEntityAIMateGEx extends EntityAIBase {
  protected void passGravityStateToChild(EntityLiving parent, EntityLiving baby) {
    Gravity gravityParent = Gravity.getGravityProp((Entity)parent);
    Gravity gravityChild = Gravity.getGravityProp((Entity)baby);
    Object cast = baby;
    if (cast instanceof EntityLivingGravitized && gravityParent != null && gravityChild != null)
      ((EntityLivingGravitized)cast).preSetSpawnGravity(gravityParent.gravityDirection, gravityParent.attractedPosX, gravityParent.attractedPosY, gravityParent.attractedPosZ); 
  }
}
