package jp.mc.ancientred.starminer.basics.ai;

import jp.mc.ancientred.starminer.api.Gravity;
import jp.mc.ancientred.starminer.basics.tileentity.TileEntityGravityGenerator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class EntityAIVillagerMateGEx
  extends AbstractEntityAIMateGEx {
  private EntityVillager theVillager;
  private EntityVillager mateTarget;
  private World worldObj;
  private int matingTimeout;
  private Gravity gravity;
  
  public EntityAIVillagerMateGEx(EntityVillager villager) {
    this.theVillager = villager;
    this.gravity = Gravity.getGravityProp((Entity)this.theVillager);
    this.worldObj = villager.worldObj;
    setMutexBits(3);
  }

  public boolean shouldExecute() {
    if (this.theVillager.getGrowingAge() != 0)
    {
      return false;
    }
    if (this.theVillager.getRNG().nextInt(60) != 0)
    {
      return false;
    }

    if (this.gravity == null) {
      return false;
    }
    if (!canAttractedVillagerMate())
    {
      return false;
    }

    Entity entity = this.worldObj.findNearestEntityWithinAABB(EntityVillager.class, this.theVillager.boundingBox.expand(8.0D, 3.0D, 8.0D), (Entity)this.theVillager);
    
    if (entity == null)
    {
      return false;
    }

    this.mateTarget = (EntityVillager)entity;
    return (this.mateTarget.getGrowingAge() == 0);
  }

  public void startExecuting() {
    this.matingTimeout = 600;
    this.theVillager.setMating(true);
  }

  public void resetTask() {
    this.mateTarget = null;
    this.theVillager.setMating(false);
  }

  public boolean continueExecuting() {
    return (this.matingTimeout >= 0 && canAttractedVillagerMate() && this.theVillager.getGrowingAge() == 0);
  }

  public void updateTask() {
    this.matingTimeout--;
    this.theVillager.getLookHelper().setLookPositionWithEntity((Entity)this.mateTarget, 10.0F, 30.0F);
    
    if (this.theVillager.getDistanceSqToEntity((Entity)this.mateTarget) > 2.25D) {
      
      this.theVillager.getNavigator().tryMoveToEntityLiving((Entity)this.mateTarget, 0.25D);
    }
    else if (this.matingTimeout == 0 && this.mateTarget.isMating()) {
      
      giveBirth();
    } 
    
    if (this.theVillager.getRNG().nextInt(35) == 0)
    {
      
      this.worldObj.setEntityState((Entity)this.theVillager, (byte)12);
    }
  }

  private boolean canAttractedVillagerMate() {
    if (!this.gravity.isAttracted)
    {
      return false;
    }

    TileEntity te = this.worldObj.getTileEntity(this.gravity.attractedPosX, this.gravity.attractedPosY, this.gravity.attractedPosZ);
    if (te == null || !(te instanceof TileEntityGravityGenerator)) {
      return false;
    }
    TileEntityGravityGenerator teGCore = (TileEntityGravityGenerator)te;
    return teGCore.isEntityMateableNow((EntityLivingBase)this.theVillager);
  }

  private void giveBirth() {
    TileEntity te = this.worldObj.getTileEntity(this.gravity.attractedPosX, this.gravity.attractedPosY, this.gravity.attractedPosZ);
    if (te == null || !(te instanceof TileEntityGravityGenerator)) {
      return;
    }
    TileEntityGravityGenerator teGCore = (TileEntityGravityGenerator)te;
    teGCore.notifyMate((EntityLivingBase)this.theVillager);

    EntityVillager entityvillagerBaby = this.theVillager.createChild((EntityAgeable)this.mateTarget);
    this.mateTarget.setGrowingAge(1000);
    this.theVillager.setGrowingAge(1000);
    entityvillagerBaby.setGrowingAge(-24000);
    entityvillagerBaby.setLocationAndAngles(this.theVillager.posX, this.theVillager.posY, this.theVillager.posZ, 0.0F, 0.0F);
    passGravityStateToChild((EntityLiving)this.theVillager, (EntityLiving)entityvillagerBaby);
    this.worldObj.spawnEntityInWorld((Entity)entityvillagerBaby);
    this.worldObj.setEntityState((Entity)entityvillagerBaby, (byte)12);
  }
}
