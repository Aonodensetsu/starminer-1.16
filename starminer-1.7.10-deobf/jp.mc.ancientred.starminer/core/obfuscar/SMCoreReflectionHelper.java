package jp.mc.ancientred.starminer.core.obfuscar;

import cpw.mods.fml.relauncher.ReflectionHelper;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityBodyHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityJumpHelper;
import net.minecraft.entity.ai.EntityLookHelper;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.ai.EntitySenses;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.pathfinding.PathNavigate;

public class SMCoreReflectionHelper
{
  private static final Object[] EMPTY_OBJARY = new Object[0];
  public static Method method_rotateCorpsePublic;
  public static Field field_fire;
  public static Field field_hasMoved;
  public static Field field_lookHelper;
  public static Field field_moveHelper;
  public static float[] method_rotateCorpsePublic_args = new float[3];
  
  public static final void initMethodAccessRotateCorpsePublic() {
    try {
      method_rotateCorpsePublic = RendererLivingEntity.class.getDeclaredMethod("rotateCorpsePublic", new Class[] { EntityLivingBase.class, float[].class });
    } catch (Exception ex) {
      ex.printStackTrace();
    } 
  }
  
  public static Field field_jumpHelper;
  public static Field field_bodyHelper;
  public static Field field_navigator;
  
  public static final void initFiledAccessFire() {
    try {
      field_fire = ReflectionHelper.findField(Entity.class, new String[] { "fire", "field_70151_c" });
    } catch (Exception ex) {
      ex.printStackTrace();
    } 
  }
  
  public static Field field_senses;
  public static Method method_despawnEntity;
  public static Method method_updateAITick;
  
  public static void ignoreHasMovedFlg(EntityPlayer par5EntityPlayer) {
    if (par5EntityPlayer instanceof EntityPlayerMP) {
      try {
        if (field_hasMoved == null) {
          field_hasMoved = ReflectionHelper.findField(NetHandlerPlayServer.class, new String[] { "hasMoved", "field_147380_r" });
        }
        field_hasMoved.setBoolean(((EntityPlayerMP)par5EntityPlayer).playerNetServerHandler, true);
      } catch (Exception ex) {
        ex.printStackTrace();
      } 
    }
  }

  public static void setLookHelper(EntityLiving entityLiving, EntityLookHelper lookHelper) {
    try {
      if (field_lookHelper == null) {
        field_lookHelper = ReflectionHelper.findField(EntityLiving.class, new String[] { "lookHelper", "field_70749_g" });
      }
      field_lookHelper.set(entityLiving, lookHelper);
    } catch (Exception ex) {
      ex.printStackTrace();
    } 
  }

  public static void setMoveHelper(EntityLiving entityLiving, EntityMoveHelper moveHelper) {
    try {
      if (field_moveHelper == null) {
        field_moveHelper = ReflectionHelper.findField(EntityLiving.class, new String[] { "moveHelper", "field_70765_h" });
      }
      field_moveHelper.set(entityLiving, moveHelper);
    } catch (Exception ex) {
      ex.printStackTrace();
    } 
  }

  public static void setJumpHelper(EntityLiving entityLiving, EntityJumpHelper jumpHelper) {
    try {
      if (field_jumpHelper == null) {
        field_jumpHelper = ReflectionHelper.findField(EntityLiving.class, new String[] { "jumpHelper", "field_70767_i" });
      }
      field_jumpHelper.set(entityLiving, jumpHelper);
    } catch (Exception ex) {
      ex.printStackTrace();
    } 
  }

  public static void setBodyHelper(EntityLiving entityLiving, EntityBodyHelper bodyHelper) {
    try {
      if (field_bodyHelper == null) {
        field_bodyHelper = ReflectionHelper.findField(EntityLiving.class, new String[] { "bodyHelper", "field_70762_j" });
      }
      field_bodyHelper.set(entityLiving, bodyHelper);
    } catch (Exception ex) {
      ex.printStackTrace();
    } 
  }

  public static void setNavigator(EntityLiving entityLiving, PathNavigate navigate) {
    try {
      if (field_navigator == null) {
        field_navigator = ReflectionHelper.findField(EntityLiving.class, new String[] { "navigator", "field_70699_by" });
      }
      field_navigator.set(entityLiving, navigate);
    } catch (Exception ex) {
      ex.printStackTrace();
    } 
  }

  public static void setSenses(EntityLiving entityLiving, EntitySenses senses) {
    try {
      if (field_senses == null) {
        field_senses = ReflectionHelper.findField(EntityLiving.class, new String[] { "senses", "field_70723_bA" });
      }
      field_senses.set(entityLiving, senses);
    } catch (Exception ex) {
      ex.printStackTrace();
    } 
  }

  public static void invokeDespawnEntity(EntityLiving entityLiving) {
    try {
      if (method_despawnEntity == null) {
        try {
          method_despawnEntity = EntityLiving.class.getDeclaredMethod("despawnEntity", new Class[0]);
        } catch (NoSuchMethodException ex) {
          method_despawnEntity = EntityLiving.class.getDeclaredMethod("func_70623_bb", new Class[0]);
        } 
        method_despawnEntity.setAccessible(true);
      } 
      method_despawnEntity.invoke(entityLiving, EMPTY_OBJARY);
    } catch (Exception ex) {
      ex.printStackTrace();
    } 
  }

  public static void invokeUpdateAITick(EntityLiving entityLiving) {
    try {
      if (method_updateAITick == null) {
        try {
          method_updateAITick = EntityLivingBase.class.getDeclaredMethod("updateAITick", new Class[0]);
        } catch (NoSuchMethodException ex) {
          method_updateAITick = EntityLivingBase.class.getDeclaredMethod("func_70629_bd", new Class[0]);
        } 
        method_updateAITick.setAccessible(true);
      } 
      method_updateAITick.invoke(entityLiving, EMPTY_OBJARY);
    } catch (Exception ex) {
      ex.printStackTrace();
    } 
  }
}
