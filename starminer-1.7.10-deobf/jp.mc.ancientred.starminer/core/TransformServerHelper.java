package jp.mc.ancientred.starminer.core;

import jp.mc.ancientred.starminer.core.common.VecUtils;
import jp.mc.ancientred.starminer.core.entity.EntityLivingGravitized;
import jp.mc.ancientred.starminer.core.entity.ExtendedPropertyGravity;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class TransformServerHelper
{
  public static boolean jumpOverKickIllegalStance() {
    return CoreConfig.skipIllegalStanceCheck;
  }
  public static boolean jumpOverKickFloatTooLong(NetHandlerPlayServer handler) {
    if (handler.playerEntity == null) return false; 
    if (handler.playerEntity.worldObj == null) return false;
    
    if (handler.playerEntity.worldObj.provider instanceof jp.mc.ancientred.starminer.api.IZeroGravityWorldProvider)
    {
      return true;
    }
    
    if (ExtendedPropertyGravity.isEntityAbnormalGravity((Entity)handler.playerEntity))
    {
      return true;
    }
    
    return false;
  }
  public static double pullGravityYInGravity(Entity entity) {
    if (entity.worldObj.provider instanceof jp.mc.ancientred.starminer.api.IZeroGravityWorldProvider) {
      return 0.0D;
    }
    return 0.03999999910593033D;
  }
  public static MovingObjectPosition getMovingObjectPositionFromPlayerByGravity(World par1World, EntityPlayer par2EntityPlayer, boolean par3) {
    double d3;
    float f = 1.0F;
    float f1 = par2EntityPlayer.prevRotationPitch + (par2EntityPlayer.rotationPitch - par2EntityPlayer.prevRotationPitch) * f;
    float f2 = par2EntityPlayer.prevRotationYaw + (par2EntityPlayer.rotationYaw - par2EntityPlayer.prevRotationYaw) * f;
    double d0 = par2EntityPlayer.prevPosX + (par2EntityPlayer.posX - par2EntityPlayer.prevPosX) * f;
    double d1 = par2EntityPlayer.prevPosY + (par2EntityPlayer.posY - par2EntityPlayer.prevPosY) * f + (par1World.isRemote ? (par2EntityPlayer.getEyeHeight() - par2EntityPlayer.getDefaultEyeHeight()) : par2EntityPlayer.getEyeHeight());
    double d2 = par2EntityPlayer.prevPosZ + (par2EntityPlayer.posZ - par2EntityPlayer.prevPosZ) * f;
    
    Vec3 vec3 = VecUtils.createVec3(d0, d1, d2);

    if (par2EntityPlayer instanceof EntityPlayerMP) {
      
      d3 = ((EntityPlayerMP)par2EntityPlayer).theItemInWorldManager.getBlockReachDistance();
      vec3 = TransformUtils.fixEyePositionByGravityServer(par2EntityPlayer, vec3);
    } else {
      d3 = 5.0D;
      vec3 = TransformUtils.fixEyePositionByGravityClient((Entity)par2EntityPlayer, vec3);
    } 
    
    Vec3 vecLook = par2EntityPlayer.getLook(1.0F);
    Vec3 vec31 = vec3.addVector(vecLook.xCoord * d3, vecLook.yCoord * d3, vecLook.zCoord * d3);
    return par1World.rayTraceBlocks(vec3, vec31, par3, !par3, false);
  }
  
  public static boolean blockLiquidOnBlockAddedHook(Block block, World par1World, int par2, int par3, int par4) {
    if (par1World.provider instanceof jp.mc.ancientred.starminer.api.IZeroGravityWorldProvider && 
      block != null) {
      if (block.getMaterial() == Material.water) {
        Chunk chunk = par1World.getChunkFromChunkCoords(par2 >> 4, par4 >> 4);
        chunk.setBlockIDWithMetadata(par2 & 0xF, par3, par4 & 0xF, Blocks.packed_ice, 0);
        
        return true;
      } 
      if (block.getMaterial() == Material.lava) {
        Chunk chunk = par1World.getChunkFromChunkCoords(par2 >> 4, par4 >> 4);
        chunk.setBlockIDWithMetadata(par2 & 0xF, par3, par4 & 0xF, Blocks.obsidian, 0);
        
        return true;
      } 
    } 
    
    return false;
  }
  
  public static boolean updateAITasksByGravity(EntityLivingBase entityLivingBase) {
    if (entityLivingBase instanceof EntityLivingGravitized) {
      EntityLivingGravitized entityLivingg = (EntityLivingGravitized)entityLivingBase;
      if (entityLivingg.updateAITasksForGravity())
      {
        return true;
      }
      
      return false;
    } 

    return false;
  }
}
