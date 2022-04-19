package jp.mc.ancientred.starminer.core.ai;

import java.util.HashMap;
import jp.mc.ancientred.starminer.core.ai.helper.GEntityJumpHelper;
import jp.mc.ancientred.starminer.core.ai.helper.GEntityLookHelper;
import jp.mc.ancientred.starminer.core.ai.helper.GEntityMoveHelper;
import jp.mc.ancientred.starminer.core.ai.helper.GEntitySenses;
import jp.mc.ancientred.starminer.core.ai.path.GPathNavigate;
import jp.mc.ancientred.starminer.core.obfuscar.SMCoreReflectionHelper;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityJumpHelper;
import net.minecraft.entity.ai.EntityLookHelper;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.ai.EntitySenses;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.PathNavigate;

public abstract class AbstractGravityAIHelper
{
  public static final HashMap<Class<? extends EntityLiving>, Class<? extends AbstractGravityAIHelper>> gAiConstructMap = new HashMap<Class<? extends EntityLiving>, Class<? extends AbstractGravityAIHelper>>();
  
  public EntityLookHelper lookHelperOrg;
  
  public EntityMoveHelper moveHelperOrg;
  
  public EntityJumpHelper jumpHelperOrg;
  
  public PathNavigate navigatorOrg;
  
  public EntitySenses sensesOrg;
  public GEntityLookHelper gLookHelper;
  public GEntityMoveHelper gMoveHelper;
  public GEntityJumpHelper gJumpHelper;
  public GPathNavigate gNavigator;
  public GEntitySenses gSenses;
  public EntityAITasks gTasks;
  public EntityAITasks gTargetTasks;
  
  public void initAI(EntityLiving entityLiving) {
    this.lookHelperOrg = entityLiving.getLookHelper();
    this.moveHelperOrg = entityLiving.getMoveHelper();
    this.jumpHelperOrg = entityLiving.getJumpHelper();
    this.navigatorOrg = entityLiving.getNavigator();
    this.sensesOrg = entityLiving.getEntitySenses();
    
    this.gLookHelper = new GEntityLookHelper(entityLiving);
    this.gMoveHelper = new GEntityMoveHelper(entityLiving);
    this.gJumpHelper = new GEntityJumpHelper(entityLiving);
    
    this.gNavigator = new GPathNavigate(entityLiving, entityLiving.worldObj);
    this.gSenses = new GEntitySenses(entityLiving);
    
    this.gTasks = new EntityAITasks((entityLiving.worldObj != null && entityLiving.worldObj.theProfiler != null) ? entityLiving.worldObj.theProfiler : null);
    this.gTargetTasks = new EntityAITasks((entityLiving.worldObj != null && entityLiving.worldObj.theProfiler != null) ? entityLiving.worldObj.theProfiler : null);
  }
  
  public static void injectGAIHelper(AbstractGravityAIHelper gAIHelper, EntityLiving entityLiving) {
    if (entityLiving.getNavigator() != gAIHelper.gNavigator) {
      SMCoreReflectionHelper.setLookHelper(entityLiving, (EntityLookHelper)gAIHelper.gLookHelper);
      SMCoreReflectionHelper.setMoveHelper(entityLiving, (EntityMoveHelper)gAIHelper.gMoveHelper);
      SMCoreReflectionHelper.setJumpHelper(entityLiving, (EntityJumpHelper)gAIHelper.gJumpHelper);
      
      SMCoreReflectionHelper.setNavigator(entityLiving, (PathNavigate)gAIHelper.gNavigator);
      SMCoreReflectionHelper.setSenses(entityLiving, (EntitySenses)gAIHelper.gSenses);
    } 
  }
  
  public static void resetOrgAIHelper(AbstractGravityAIHelper gAIHelper, EntityLiving entityLiving) {
    if (entityLiving.getNavigator() == gAIHelper.gNavigator) {
      SMCoreReflectionHelper.setLookHelper(entityLiving, gAIHelper.lookHelperOrg);
      SMCoreReflectionHelper.setMoveHelper(entityLiving, gAIHelper.moveHelperOrg);
      SMCoreReflectionHelper.setJumpHelper(entityLiving, gAIHelper.jumpHelperOrg);
      
      SMCoreReflectionHelper.setNavigator(entityLiving, gAIHelper.navigatorOrg);
      SMCoreReflectionHelper.setSenses(entityLiving, gAIHelper.sensesOrg);
    } 
  }
  
  public static class DefaultGravityAIHelper
    extends AbstractGravityAIHelper {
    public void initAI(EntityLiving entityLiving) {
      super.initAI(entityLiving);
      
      this.gTasks.addTask(9, new EntityAIWatchClosest2GEx(entityLiving, EntityPlayer.class, 3.0F, 1.0F));
      this.gTasks.addTask(9, (EntityAIBase)new EntityAIWander((EntityCreature)entityLiving, 0.6D));
      this.gTasks.addTask(10, new EntityAIWatchClosestGEx(entityLiving, EntityLiving.class, 8.0F));
    }
  }
}
