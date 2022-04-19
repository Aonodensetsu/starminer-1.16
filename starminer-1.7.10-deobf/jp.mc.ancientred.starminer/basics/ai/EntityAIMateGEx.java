package jp.mc.ancientred.starminer.basics.ai;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraft.world.World;

public class EntityAIMateGEx
  extends AbstractEntityAIMateGEx
{
  private EntityAnimal theAnimal;
  World theWorld;
  private EntityAnimal targetMate;
  int spawnBabyDelay;
  double moveSpeed;
  
  public EntityAIMateGEx(EntityAnimal p_i1619_1_, double p_i1619_2_) {
    this.theAnimal = p_i1619_1_;
    this.theWorld = p_i1619_1_.worldObj;
    this.moveSpeed = p_i1619_2_;
    setMutexBits(3);
  }

  public boolean shouldExecute() {
    if (!this.theAnimal.isInLove())
    {
      return false;
    }

    this.targetMate = getNearbyMate();
    return (this.targetMate != null);
  }

  public boolean continueExecuting() {
    return (this.targetMate.isEntityAlive() && this.targetMate.isInLove() && this.spawnBabyDelay < 60);
  }

  public void resetTask() {
    this.targetMate = null;
    this.spawnBabyDelay = 0;
  }

  public void updateTask() {
    this.theAnimal.getLookHelper().setLookPositionWithEntity((Entity)this.targetMate, 10.0F, this.theAnimal.getVerticalFaceSpeed());
    this.theAnimal.getNavigator().tryMoveToEntityLiving((Entity)this.targetMate, this.moveSpeed);
    this.spawnBabyDelay++;
    
    if (this.spawnBabyDelay >= 60 && this.theAnimal.getDistanceSqToEntity((Entity)this.targetMate) < 9.0D)
    {
      spawnBaby();
    }
  }

  private EntityAnimal getNearbyMate() {
    float f = 8.0F;
    List list = this.theWorld.getEntitiesWithinAABB(this.theAnimal.getClass(), this.theAnimal.boundingBox.expand(f, f, f));
    double d0 = Double.MAX_VALUE;
    EntityAnimal entityanimal = null;
    Iterator<EntityAnimal> iterator = list.iterator();
    
    while (iterator.hasNext()) {
      
      EntityAnimal entityanimal1 = iterator.next();
      
      if (this.theAnimal.canMateWith(entityanimal1) && this.theAnimal.getDistanceSqToEntity((Entity)entityanimal1) < d0) {
        
        entityanimal = entityanimal1;
        d0 = this.theAnimal.getDistanceSqToEntity((Entity)entityanimal1);
      } 
    } 
    
    return entityanimal;
  }

  private void spawnBaby() {
    EntityAgeable entityageableBaby = this.theAnimal.createChild((EntityAgeable)this.targetMate);
    
    if (entityageableBaby != null) {
      
      EntityPlayer entityplayer = this.theAnimal.func_146083_cb();
      
      if (entityplayer == null && this.targetMate.func_146083_cb() != null)
      {
        entityplayer = this.targetMate.func_146083_cb();
      }
      
      if (entityplayer != null) {
        
        entityplayer.triggerAchievement(StatList.animalsBredStat);
        
        if (this.theAnimal instanceof net.minecraft.entity.passive.EntityCow)
        {
          entityplayer.triggerAchievement((StatBase)AchievementList.breedCow);
        }
      } 
      
      this.theAnimal.setGrowingAge(6000);
      this.targetMate.setGrowingAge(6000);
      this.theAnimal.resetInLove();
      this.targetMate.resetInLove();
      entityageableBaby.setGrowingAge(-24000);
      entityageableBaby.setLocationAndAngles(this.theAnimal.posX, this.theAnimal.posY, this.theAnimal.posZ, 0.0F, 0.0F);
      passGravityStateToChild((EntityLiving)this.theAnimal, (EntityLiving)entityageableBaby);
      this.theWorld.spawnEntityInWorld((Entity)entityageableBaby);
      Random random = this.theAnimal.getRNG();
      
      for (int i = 0; i < 7; i++) {
        
        double d0 = random.nextGaussian() * 0.02D;
        double d1 = random.nextGaussian() * 0.02D;
        double d2 = random.nextGaussian() * 0.02D;
        this.theWorld.spawnParticle("heart", this.theAnimal.posX + (random.nextFloat() * this.theAnimal.width * 2.0F) - this.theAnimal.width, this.theAnimal.posY + 0.5D + (random.nextFloat() * this.theAnimal.height), this.theAnimal.posZ + (random.nextFloat() * this.theAnimal.width * 2.0F) - this.theAnimal.width, d0, d1, d2);
      } 
      
      if (this.theWorld.getGameRules().getGameRuleBooleanValue("doMobLoot"))
      {
        this.theWorld.spawnEntityInWorld((Entity)new EntityXPOrb(this.theWorld, this.theAnimal.posX, this.theAnimal.posY, this.theAnimal.posZ, random.nextInt(7) + 1));
      }
    } 
  }
}
