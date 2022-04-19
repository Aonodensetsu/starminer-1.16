package jp.mc.ancientred.starminer.basics.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.world.World;

public class EntityAIEatSpaceEtherGEx
  extends EntityAIBase
{
  private EntitySheep theEntity;
  private World therWorld;
  int eatingTick;
  
  public EntityAIEatSpaceEtherGEx(EntitySheep sheep) {
    this.theEntity = sheep;
    this.therWorld = sheep.worldObj;
  }
  
  public boolean shouldExecute() {
    return (this.theEntity.getSheared() && !this.theEntity.isChild() && this.theEntity.getRNG().nextInt(2000) == 0);
  }
  
  public void startExecuting() {
    this.eatingTick = 40;
    this.therWorld.setEntityState((Entity)this.theEntity, (byte)10);
    this.theEntity.getNavigator().clearPathEntity();
  }
  
  public void resetTask() {
    this.eatingTick = 0;
  }
  
  public boolean continueExecuting() {
    return (this.eatingTick > 0);
  }
  
  public void updateTask() {
    this.eatingTick = Math.max(0, this.eatingTick - 1);
    
    if (this.eatingTick > 0 && this.eatingTick <= 4)
      this.theEntity.eatGrassBonus(); 
  }
}
