package jp.mc.ancientred.starminer.basics.ai.mobai;

import jp.mc.ancientred.starminer.basics.ai.EntityAILookIdleGEx;
import jp.mc.ancientred.starminer.basics.ai.EntityAIWatchClosestGEx;
import jp.mc.ancientred.starminer.core.ai.AbstractGravityAIHelper;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAICreeperSwell;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;

public class MobMonstersAIHelpers {
  public static class CreeperGravityAIHelper extends AbstractGravityAIHelper {
    public void initAI(EntityLiving entityLiving) {
      super.initAI(entityLiving);
      
      this.gTasks.addTask(1, (EntityAIBase)new EntityAISwimming(entityLiving));
      this.gTasks.addTask(2, (EntityAIBase)new EntityAICreeperSwell((EntityCreeper)entityLiving));
      this.gTasks.addTask(3, (EntityAIBase)new EntityAIAvoidEntity((EntityCreature)entityLiving, EntityOcelot.class, 6.0F, 1.0D, 1.2D));
      this.gTasks.addTask(4, (EntityAIBase)new EntityAIAttackOnCollide((EntityCreature)entityLiving, 1.0D, false));
      this.gTasks.addTask(5, (EntityAIBase)new EntityAIWander((EntityCreature)entityLiving, 0.8D));
      this.gTasks.addTask(6, (EntityAIBase)new EntityAIWatchClosestGEx(entityLiving, EntityPlayer.class, 8.0F));
      this.gTasks.addTask(6, (EntityAIBase)new EntityAILookIdleGEx(entityLiving));
      this.gTargetTasks.addTask(1, (EntityAIBase)new EntityAINearestAttackableTarget((EntityCreature)entityLiving, EntityPlayer.class, 0, true));
      this.gTargetTasks.addTask(2, (EntityAIBase)new EntityAIHurtByTarget((EntityCreature)entityLiving, false));
    }
  }

  public static void register() {
    AbstractGravityAIHelper.gAiConstructMap.put(EntityCreeper.class, CreeperGravityAIHelper.class);
  }
}
