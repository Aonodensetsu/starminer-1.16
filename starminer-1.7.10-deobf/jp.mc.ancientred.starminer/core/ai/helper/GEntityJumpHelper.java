package jp.mc.ancientred.starminer.core.ai.helper;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityJumpHelper;

public class GEntityJumpHelper
  extends EntityJumpHelper {
  private EntityLiving entity;
  private boolean isJumping;
  
  public GEntityJumpHelper(EntityLiving entityLiving) {
    super(entityLiving);
    this.entity = entityLiving;
  }

  public void setJumping() {
    this.isJumping = true;
  }

  public void doJump() {
    this.entity.setJumping(this.isJumping);
    this.isJumping = false;
  }
}
