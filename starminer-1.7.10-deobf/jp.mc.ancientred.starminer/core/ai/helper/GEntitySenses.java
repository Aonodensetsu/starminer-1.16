package jp.mc.ancientred.starminer.core.ai.helper;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntitySenses;

public class GEntitySenses
  extends EntitySenses {
  EntityLiving entityObj;
  List seenEntities = new ArrayList();
  List unseenEntities = new ArrayList();

  public GEntitySenses(EntityLiving entityLiving) {
    super(entityLiving);
    this.entityObj = entityLiving;
  }

  public void clearSensingCache() {
    this.seenEntities.clear();
    this.unseenEntities.clear();
  }

  public boolean canSee(Entity p_75522_1_) {
    if (this.seenEntities.contains(p_75522_1_))
    {
      return true;
    }
    if (this.unseenEntities.contains(p_75522_1_))
    {
      return false;
    }

    this.entityObj.worldObj.theProfiler.startSection("canSee");
    boolean flag = this.entityObj.canEntityBeSeen(p_75522_1_);
    this.entityObj.worldObj.theProfiler.endSection();
    
    if (flag) {
      
      this.seenEntities.add(p_75522_1_);
    }
    else {
      
      this.unseenEntities.add(p_75522_1_);
    } 
    
    return flag;
  }
}
