package jp.mc.ancientred.starminer.basics.ai.mobai;

import jp.mc.ancientred.starminer.basics.ai.EntityAIBegGEx;
import jp.mc.ancientred.starminer.basics.ai.EntityAIEatSpaceEtherGEx;
import jp.mc.ancientred.starminer.basics.ai.EntityAILeapAtTargetGEx;
import jp.mc.ancientred.starminer.basics.ai.EntityAILookAtTradePlayerGEx;
import jp.mc.ancientred.starminer.basics.ai.EntityAILookIdleGEx;
import jp.mc.ancientred.starminer.basics.ai.EntityAIMateGEx;
import jp.mc.ancientred.starminer.basics.ai.EntityAIVillagerMateGEx;
import jp.mc.ancientred.starminer.basics.ai.EntityAIWatchClosest2GEx;
import jp.mc.ancientred.starminer.basics.ai.EntityAIWatchClosestGEx;
import jp.mc.ancientred.starminer.core.ai.AbstractGravityAIHelper;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIFollowOwner;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAIOcelotAttack;
import net.minecraft.entity.ai.EntityAIOwnerHurtByTarget;
import net.minecraft.entity.ai.EntityAIOwnerHurtTarget;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAIPlay;
import net.minecraft.entity.ai.EntityAIRunAroundLikeCrazy;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITargetNonTamed;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAITradePlayer;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;

public class MobAnimalsAIHelpers {
  public static class VillagerGravityAIHelper extends AbstractGravityAIHelper {
    public void initAI(EntityLiving entityLiving) {
      super.initAI(entityLiving);
      
      this.gNavigator.setBreakDoors(true);
      this.gNavigator.setAvoidsWater(true);
      this.gTasks.addTask(0, (EntityAIBase)new EntityAISwimming(entityLiving));
      this.gTasks.addTask(1, (EntityAIBase)new EntityAIAvoidEntity((EntityCreature)entityLiving, EntityZombie.class, 8.0F, 0.6D, 0.6D));
      this.gTasks.addTask(1, (EntityAIBase)new EntityAITradePlayer((EntityVillager)entityLiving));
      this.gTasks.addTask(1, (EntityAIBase)new EntityAILookAtTradePlayerGEx((EntityVillager)entityLiving));

      this.gTasks.addTask(6, (EntityAIBase)new EntityAIVillagerMateGEx((EntityVillager)entityLiving));
      
      this.gTasks.addTask(8, (EntityAIBase)new EntityAIPlay((EntityVillager)entityLiving, 0.32D));
      this.gTasks.addTask(9, (EntityAIBase)new EntityAIWatchClosest2GEx(entityLiving, EntityPlayer.class, 3.0F, 1.0F));
      this.gTasks.addTask(9, (EntityAIBase)new EntityAIWatchClosest2GEx(entityLiving, EntityVillager.class, 5.0F, 0.02F));
      this.gTasks.addTask(9, (EntityAIBase)new EntityAIWander((EntityCreature)entityLiving, 0.6D));
      this.gTasks.addTask(10, (EntityAIBase)new EntityAIWatchClosestGEx(entityLiving, EntityLiving.class, 8.0F));
    }
  }
  
  public static class ChickenGravityAIHelper extends AbstractGravityAIHelper {
    public void initAI(EntityLiving entityLiving) {
      super.initAI(entityLiving);
      
      this.gTasks.addTask(0, (EntityAIBase)new EntityAISwimming(entityLiving));
      this.gTasks.addTask(1, (EntityAIBase)new EntityAIPanic((EntityCreature)entityLiving, 1.4D));
      this.gTasks.addTask(2, (EntityAIBase)new EntityAIMateGEx((EntityAnimal)entityLiving, 1.0D));
      this.gTasks.addTask(3, (EntityAIBase)new EntityAITempt((EntityCreature)entityLiving, 1.0D, Items.wheat_seeds, false));
      this.gTasks.addTask(4, (EntityAIBase)new EntityAIFollowParent((EntityAnimal)entityLiving, 1.1D));
      this.gTasks.addTask(5, (EntityAIBase)new EntityAIWander((EntityCreature)entityLiving, 1.0D));
      this.gTasks.addTask(6, (EntityAIBase)new EntityAIWatchClosestGEx(entityLiving, EntityPlayer.class, 6.0F));
      this.gTasks.addTask(7, (EntityAIBase)new EntityAILookIdleGEx(entityLiving));
    }
  }
  
  public static class CowGravityAIHelper
    extends AbstractGravityAIHelper {
    public void initAI(EntityLiving entityLiving) {
      super.initAI(entityLiving);
      
      this.gNavigator.setAvoidsWater(true);
      this.gTasks.addTask(0, (EntityAIBase)new EntityAISwimming(entityLiving));
      this.gTasks.addTask(1, (EntityAIBase)new EntityAIPanic((EntityCreature)entityLiving, 2.0D));
      this.gTasks.addTask(2, (EntityAIBase)new EntityAIMateGEx((EntityAnimal)entityLiving, 1.0D));
      this.gTasks.addTask(3, (EntityAIBase)new EntityAITempt((EntityCreature)entityLiving, 1.25D, Items.wheat, false));
      this.gTasks.addTask(4, (EntityAIBase)new EntityAIFollowParent((EntityAnimal)entityLiving, 1.25D));
      this.gTasks.addTask(5, (EntityAIBase)new EntityAIWander((EntityCreature)entityLiving, 1.0D));
      this.gTasks.addTask(6, (EntityAIBase)new EntityAIWatchClosestGEx(entityLiving, EntityPlayer.class, 6.0F));
      this.gTasks.addTask(7, (EntityAIBase)new EntityAILookIdleGEx(entityLiving));
    }
  }
  
  public static class PigGravityAIHelper
    extends AbstractGravityAIHelper {
    public void initAI(EntityLiving entityLiving) {
      super.initAI(entityLiving);
      
      this.gNavigator.setAvoidsWater(true);
      this.gTasks.addTask(0, (EntityAIBase)new EntityAISwimming(entityLiving));
      this.gTasks.addTask(1, (EntityAIBase)new EntityAIPanic((EntityCreature)entityLiving, 1.25D));
      this.gTasks.addTask(2, (EntityAIBase)((EntityPig)entityLiving).getAIControlledByPlayer());
      this.gTasks.addTask(3, (EntityAIBase)new EntityAIMateGEx((EntityAnimal)entityLiving, 1.0D));
      this.gTasks.addTask(4, (EntityAIBase)new EntityAITempt((EntityCreature)entityLiving, 1.2D, Items.carrot_on_a_stick, false));
      this.gTasks.addTask(4, (EntityAIBase)new EntityAITempt((EntityCreature)entityLiving, 1.2D, Items.carrot, false));
      this.gTasks.addTask(5, (EntityAIBase)new EntityAIFollowParent((EntityAnimal)entityLiving, 1.1D));
      this.gTasks.addTask(6, (EntityAIBase)new EntityAIWander((EntityCreature)entityLiving, 1.0D));
      this.gTasks.addTask(7, (EntityAIBase)new EntityAIWatchClosestGEx(entityLiving, EntityPlayer.class, 6.0F));
      this.gTasks.addTask(8, (EntityAIBase)new EntityAILookIdleGEx(entityLiving));
    }
  }
  
  public static class SheepGravityAIHelper
    extends AbstractGravityAIHelper {
    public void initAI(EntityLiving entityLiving) {
      super.initAI(entityLiving);
      
      this.gNavigator.setAvoidsWater(true);
      this.gTasks.addTask(0, (EntityAIBase)new EntityAISwimming(entityLiving));
      this.gTasks.addTask(1, (EntityAIBase)new EntityAIPanic((EntityCreature)entityLiving, 1.25D));
      this.gTasks.addTask(2, (EntityAIBase)new EntityAIMateGEx((EntityAnimal)entityLiving, 1.0D));
      this.gTasks.addTask(3, (EntityAIBase)new EntityAITempt((EntityCreature)entityLiving, 1.1D, Items.wheat, false));
      this.gTasks.addTask(4, (EntityAIBase)new EntityAIFollowParent((EntityAnimal)entityLiving, 1.1D));
      
      this.gTasks.addTask(5, (EntityAIBase)new EntityAIEatSpaceEtherGEx((EntitySheep)entityLiving));
      this.gTasks.addTask(6, (EntityAIBase)new EntityAIWander((EntityCreature)entityLiving, 1.0D));
      this.gTasks.addTask(7, (EntityAIBase)new EntityAIWatchClosestGEx(entityLiving, EntityPlayer.class, 6.0F));
      this.gTasks.addTask(8, (EntityAIBase)new EntityAILookIdleGEx(entityLiving));
    }
  }
  
  public static class HorseGravityAIHelper
    extends AbstractGravityAIHelper {
    public void initAI(EntityLiving entityLiving) {
      super.initAI(entityLiving);
      
      this.gNavigator.setAvoidsWater(true);
      this.gTasks.addTask(0, (EntityAIBase)new EntityAISwimming(entityLiving));
      this.gTasks.addTask(1, (EntityAIBase)new EntityAIPanic((EntityCreature)entityLiving, 1.2D));
      this.gTasks.addTask(1, (EntityAIBase)new EntityAIRunAroundLikeCrazy((EntityHorse)entityLiving, 1.2D));
      this.gTasks.addTask(2, (EntityAIBase)new EntityAIMateGEx((EntityAnimal)entityLiving, 1.0D));
      this.gTasks.addTask(4, (EntityAIBase)new EntityAIFollowParent((EntityAnimal)entityLiving, 1.0D));
      this.gTasks.addTask(6, (EntityAIBase)new EntityAIWander((EntityCreature)entityLiving, 0.7D));
      this.gTasks.addTask(7, (EntityAIBase)new EntityAIWatchClosestGEx(entityLiving, EntityPlayer.class, 6.0F));
      this.gTasks.addTask(8, (EntityAIBase)new EntityAILookIdleGEx(entityLiving));
    }
  }
  
  public static class OcerotGravityAIHelper
    extends AbstractGravityAIHelper {
    public void initAI(EntityLiving entityLiving) {
      super.initAI(entityLiving);
      
      this.gNavigator.setAvoidsWater(true);
      this.gTasks.addTask(1, (EntityAIBase)new EntityAISwimming(entityLiving));
      this.gTasks.addTask(2, (EntityAIBase)((EntityTameable)entityLiving).getAISit());
      this.gTasks.addTask(3, (EntityAIBase)new EntityAITempt((EntityCreature)entityLiving, 0.6D, Items.fish, true));
      this.gTasks.addTask(4, (EntityAIBase)new EntityAIAvoidEntity((EntityCreature)entityLiving, EntityPlayer.class, 16.0F, 0.8D, 1.33D));
      this.gTasks.addTask(5, (EntityAIBase)new EntityAIFollowOwner((EntityTameable)entityLiving, 1.0D, 10.0F, 5.0F));
      
      this.gTasks.addTask(7, (EntityAIBase)new EntityAILeapAtTargetGEx(entityLiving, 0.3F));
      this.gTasks.addTask(8, (EntityAIBase)new EntityAIOcelotAttack(entityLiving));
      this.gTasks.addTask(9, (EntityAIBase)new EntityAIMateGEx((EntityAnimal)entityLiving, 0.8D));
      this.gTasks.addTask(10, (EntityAIBase)new EntityAIWander((EntityCreature)entityLiving, 0.8D));
      this.gTasks.addTask(11, (EntityAIBase)new EntityAIWatchClosestGEx(entityLiving, EntityPlayer.class, 10.0F));
      this.gTargetTasks.addTask(1, (EntityAIBase)new EntityAITargetNonTamed((EntityTameable)entityLiving, EntityChicken.class, 750, false));
    }
  }
  
  public static class WolfGravityAIHelper
    extends AbstractGravityAIHelper {
    public void initAI(EntityLiving entityLiving) {
      super.initAI(entityLiving);
      
      this.gNavigator.setAvoidsWater(true);
      this.gTasks.addTask(1, (EntityAIBase)new EntityAISwimming(entityLiving));
      this.gTasks.addTask(2, (EntityAIBase)((EntityTameable)entityLiving).getAISit());
      this.gTasks.addTask(3, (EntityAIBase)new EntityAILeapAtTargetGEx(entityLiving, 0.4F));
      this.gTasks.addTask(4, (EntityAIBase)new EntityAIAttackOnCollide((EntityCreature)entityLiving, 1.0D, true));
      this.gTasks.addTask(5, (EntityAIBase)new EntityAIFollowOwner((EntityTameable)entityLiving, 1.0D, 10.0F, 2.0F));
      this.gTasks.addTask(6, (EntityAIBase)new EntityAIMateGEx((EntityAnimal)entityLiving, 1.0D));
      this.gTasks.addTask(7, (EntityAIBase)new EntityAIWander((EntityCreature)entityLiving, 1.0D));
      this.gTasks.addTask(8, (EntityAIBase)new EntityAIBegGEx((EntityWolf)entityLiving, 8.0F));
      this.gTasks.addTask(9, (EntityAIBase)new EntityAIWatchClosestGEx(entityLiving, EntityPlayer.class, 8.0F));
      this.gTasks.addTask(9, (EntityAIBase)new EntityAILookIdleGEx(entityLiving));
      this.gTargetTasks.addTask(1, (EntityAIBase)new EntityAIOwnerHurtByTarget((EntityTameable)entityLiving));
      this.gTargetTasks.addTask(2, (EntityAIBase)new EntityAIOwnerHurtTarget((EntityTameable)entityLiving));
      this.gTargetTasks.addTask(3, (EntityAIBase)new EntityAIHurtByTarget((EntityCreature)entityLiving, true));
      this.gTargetTasks.addTask(4, (EntityAIBase)new EntityAITargetNonTamed((EntityTameable)entityLiving, EntitySheep.class, 200, false));
    }
  }
  
  public static void register() {
    AbstractGravityAIHelper.gAiConstructMap.put(EntityVillager.class, VillagerGravityAIHelper.class);
    AbstractGravityAIHelper.gAiConstructMap.put(EntityChicken.class, ChickenGravityAIHelper.class);
    AbstractGravityAIHelper.gAiConstructMap.put(EntityCow.class, CowGravityAIHelper.class);
    AbstractGravityAIHelper.gAiConstructMap.put(EntityMooshroom.class, CowGravityAIHelper.class);
    AbstractGravityAIHelper.gAiConstructMap.put(EntityPig.class, PigGravityAIHelper.class);
    AbstractGravityAIHelper.gAiConstructMap.put(EntitySheep.class, SheepGravityAIHelper.class);
    AbstractGravityAIHelper.gAiConstructMap.put(EntityHorse.class, HorseGravityAIHelper.class);
    AbstractGravityAIHelper.gAiConstructMap.put(EntityOcelot.class, OcerotGravityAIHelper.class);
    AbstractGravityAIHelper.gAiConstructMap.put(EntityWolf.class, WolfGravityAIHelper.class);
  }
}
