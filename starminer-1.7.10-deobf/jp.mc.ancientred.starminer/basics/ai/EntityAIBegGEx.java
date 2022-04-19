package jp.mc.ancientred.starminer.basics.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EntityAIBegGEx
  extends EntityAIBase {
  private EntityWolf theWolf;
  private EntityPlayer thePlayer;
  private World worldObject;
  private float minPlayerDistance;
  private int begTick;
  
  public EntityAIBegGEx(EntityWolf p_i1617_1_, float p_i1617_2_) {
    this.theWolf = p_i1617_1_;
    this.worldObject = p_i1617_1_.worldObj;
    this.minPlayerDistance = p_i1617_2_;
    setMutexBits(2);
  }

  public boolean shouldExecute() {
    this.thePlayer = this.worldObject.getClosestPlayerToEntity((Entity)this.theWolf, this.minPlayerDistance);
    return (this.thePlayer == null) ? false : hasPlayerGotBoneInHand(this.thePlayer);
  }

  public boolean continueExecuting() {
    return !this.thePlayer.isEntityAlive() ? false : ((this.theWolf.getDistanceSqToEntity((Entity)this.thePlayer) > (this.minPlayerDistance * this.minPlayerDistance)) ? false : ((this.begTick > 0 && hasPlayerGotBoneInHand(this.thePlayer))));
  }

  public void startExecuting() {
    this.theWolf.func_70918_i(true);
    this.begTick = 40 + this.theWolf.getRNG().nextInt(40);
  }

  public void resetTask() {
    this.theWolf.func_70918_i(false);
    this.thePlayer = null;
  }

  public void updateTask() {
    this.theWolf.getLookHelper().setLookPositionWithEntity((Entity)this.thePlayer, 10.0F, this.theWolf.getVerticalFaceSpeed());
    this.begTick--;
  }

  private boolean hasPlayerGotBoneInHand(EntityPlayer p_75382_1_) {
    ItemStack itemstack = p_75382_1_.inventory.getCurrentItem();
    return (itemstack == null) ? false : ((!this.theWolf.isTamed() && itemstack.getItem() == Items.bone) ? true : this.theWolf.isBreedingItem(itemstack));
  }
}
