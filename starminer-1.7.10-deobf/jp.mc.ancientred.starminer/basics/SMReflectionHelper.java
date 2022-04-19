package jp.mc.ancientred.starminer.basics;

import cpw.mods.fml.relauncher.ReflectionHelper;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemReed;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.profiler.Profiler;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;

public class SMReflectionHelper
{
  public static Field field_hasMoved;
  public static Method field_setSize;
  public static Field field_sleeping;
  public static Field field_sleepTimer;
  
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
  
  public static Field field_allPlayersSleeping;
  public static Field field_field_150935_a;
  
  public static void setSize(EntityPlayer player, float width, float hight) {
    try {
      if (field_setSize == null) {
        field_setSize = ReflectionHelper.findMethod(Entity.class, player, new String[] { "setSize", "func_70105_a" }, new Class[] { float.class, float.class });
      }
      field_setSize.invoke(player, new Object[] { Float.valueOf(width), Float.valueOf(hight) });
    } catch (Exception ex) {
      ex.printStackTrace();
    } 
  }
  
  public static Field field_worldProvider;
  public static Field field_theProfiler;
  
  public static void setSleeping(EntityPlayer player) {
    try {
      if (field_sleeping == null) {
        field_sleeping = ReflectionHelper.findField(EntityPlayer.class, new String[] { "sleeping", "field_71083_bS" });
      }
      field_sleeping.setBoolean(player, true);
    } catch (Exception ex) {
      ex.printStackTrace();
    } 
  }

  public static void setSleepTimer(EntityPlayer player, int value) {
    try {
      if (field_sleepTimer == null) {
        field_sleepTimer = ReflectionHelper.findField(EntityPlayer.class, new String[] { "sleepTimer", "field_71076_b" });
      }
      field_sleepTimer.setInt(player, value);
    } catch (Exception ex) {
      ex.printStackTrace();
    } 
  }

  public static void setallPlayersSleeping(WorldServer worldServer, boolean value) {
    try {
      if (field_allPlayersSleeping == null) {
        field_allPlayersSleeping = ReflectionHelper.findField(WorldServer.class, new String[] { "allPlayersSleeping", "field_73068_P" });
      }
      field_allPlayersSleeping.setBoolean(worldServer, value);
    } catch (Exception ex) {
      ex.printStackTrace();
    } 
  }

  public static Block getField_150935_a(ItemReed itemReed) {
    try {
      if (field_field_150935_a == null) {
        field_field_150935_a = ReflectionHelper.findField(ItemReed.class, new String[] { "field_150935_a", "field_150935_a" });
      }
      return (Block)field_field_150935_a.get(itemReed);
    } catch (Exception ex) {
      ex.printStackTrace();
      
      return null;
    } 
  }

  public static void setWrappedWorldFinalField(World world, WorldProvider worldProvider, Profiler theProfiler) {
    try {
      if (field_worldProvider == null) {
        field_worldProvider = ReflectionHelper.findField(World.class, new String[] { "provider", "field_73011_w" });
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field_worldProvider, field_worldProvider.getModifiers() & 0xFFFFFFEF);
      } 
      if (field_theProfiler == null) {
        field_theProfiler = ReflectionHelper.findField(World.class, new String[] { "theProfiler", "field_72984_F" });
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field_theProfiler, field_theProfiler.getModifiers() & 0xFFFFFFEF);
      } 
      
      field_worldProvider.set(world, worldProvider);
      field_theProfiler.set(world, theProfiler);
    } catch (Exception ex) {
      ex.printStackTrace();
    } 
  }
}
