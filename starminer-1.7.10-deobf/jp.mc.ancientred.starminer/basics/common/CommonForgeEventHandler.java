package jp.mc.ancientred.starminer.basics.common;

import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import java.util.List;
import java.util.Random;
import jp.mc.ancientred.starminer.api.Gravity;
import jp.mc.ancientred.starminer.api.GravityDirection;
import jp.mc.ancientred.starminer.basics.Config;
import jp.mc.ancientred.starminer.basics.SMModContainer;
import jp.mc.ancientred.starminer.basics.SMReflectionHelper;
import jp.mc.ancientred.starminer.basics.block.bed.BlockStarBedHead;
import jp.mc.ancientred.starminer.basics.dimention.DimentionTeleportHandler;
import jp.mc.ancientred.starminer.basics.dimention.renderer.BurningScreenEffectRenderer;
import jp.mc.ancientred.starminer.basics.entity.EntityGProjectile;
import jp.mc.ancientred.starminer.basics.packet.SMPacketSender;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.world.WorldEvent;

public class CommonForgeEventHandler
{
  private Random rand = new Random();
  
  public DimentionTeleportHandler teleportHanlder = new DimentionTeleportHandler();
  
  @SubscribeEvent
  public void handleLivingUpdate(LivingEvent.LivingUpdateEvent event) {
    if (event.entity != null && !event.entity.worldObj.isRemote && 
      event.entityLiving instanceof EntityPlayerMP && 
      this.teleportHanlder.onUpdateEntityEnd((EntityPlayerMP)event.entityLiving))
    {
      event.setCanceled(true);
    }
  }

  @SubscribeEvent
  public boolean handlePopulateChunkEvent(PopulateChunkEvent event) {
    if (Config.generateStars && 
      event instanceof PopulateChunkEvent.Post && 
      event.world.provider instanceof net.minecraft.world.WorldProviderSurface) {
      SMModContainer.starGenerator.generate(event.chunkProvider, event.world, event.chunkX, event.chunkZ, null);
      SMModContainer.starGenerator.generateStructuresInChunk(event.world, event.rand, event.chunkX, event.chunkZ);
    } 

    return true;
  }
  
  @SubscribeEvent
  public void handlePlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
    try {
      if (event.player instanceof EntityPlayerMP)
      {
        SMPacketSender.sendSkyMapPacketToPlayer((EntityPlayerMP)event.player);
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    } 
  }
  
  @SubscribeEvent
  public void handleWorldTickEvent(TickEvent.WorldTickEvent event) {
    if (event.phase == TickEvent.Phase.START && 
      event.world instanceof WorldServer) {
      
      WorldServer worldServer = (WorldServer)event.world;
      SMModContainer.proxy.doWakeupAll(worldServer);
    } 
  }

  @SubscribeEvent
  public void handleRenderTickEvent(TickEvent.RenderTickEvent event) {
    if (event.phase == TickEvent.Phase.START) {
      SMRenderHelper.isValueCalculatedOnThisRender = false;
    }
  }
  
  @SubscribeEvent
  public void handleDrawSceen(RenderGameOverlayEvent.Pre event) {
    if (event.type == RenderGameOverlayEvent.ElementType.HELMET) {
      BurningScreenEffectRenderer.renderBurningScreen(event.resolution.getScaledWidth(), event.resolution.getScaledHeight(), event.partialTicks);
    }
  }
  
  @SubscribeEvent
  public void handleUseHoeEvent(UseHoeEvent event) {
    Block block = event.world.getBlock(event.x, event.y, event.z);
    if (block == SMModContainer.DirtGrassExBlock) {
      int meta = event.world.getBlockMetadata(event.x, event.y, event.z);
      if ((meta & 0x8) == 0) {
        event.world.playSoundEffect((event.x + 0.5F), (event.y + 0.5F), (event.z + 0.5F), SMModContainer.DirtGrassExBlock.stepSound.getStepSound(), (SMModContainer.DirtGrassExBlock.stepSound.getVolume() + 1.0F) / 2.0F, SMModContainer.DirtGrassExBlock.stepSound.getFrequency() * 0.8F);

        if (!event.world.isRemote) {
          event.world.setBlockMetadataWithNotify(event.x, event.y, event.z, meta | 0x8, 2);
          event.setResult(Event.Result.ALLOW);
        } 
      } 
    } 
  }
  
  @SubscribeEvent
  public void handleWorldLoadEvent(WorldEvent.Load event) {
    SMModContainer.proxy.handleWorldLoadEvent(event);
  }
  
  @SubscribeEvent
  public void handlePlayerSleepInBed(PlayerSleepInBedEvent event) {
    EntityPlayer player = event.entityPlayer;
    int tarX = event.x;
    int tarY = event.y;
    int tarZ = event.z;
    
    Block block = player.worldObj.getBlock(tarX, tarY, tarZ);
    if (block != SMModContainer.StarBedHeadBlock) {
      return;
    }
    
    EntityPlayer.EnumStatus res = sleepInBedAt(player, tarX, tarY, tarZ);
    event.result = res;
  }
  
  private EntityPlayer.EnumStatus sleepInBedAt(EntityPlayer player, int par1, int par2, int par3) {
    Block block = player.worldObj.getBlock(par1, par2, par3);
    
    int gravDir = ((BlockStarBedHead)block).getGravityDirection((IBlockAccess)player.worldObj, par1, par2, par3);
    int connDir = ((BlockStarBedHead)block).getConnectionDirection((IBlockAccess)player.worldObj, par1, par2, par3);
    
    if (!player.worldObj.isRemote) {
      
      Gravity gravity = Gravity.getGravityProp((Entity)player);
      switch (gravity.gravityDirection) {
        case northTOsouth_ZP:
          if (gravDir != 5) return EntityPlayer.EnumStatus.OTHER_PROBLEM; 
          break;
        case southTOnorth_ZN:
          if (gravDir != 4) return EntityPlayer.EnumStatus.OTHER_PROBLEM; 
          break;
        case westTOeast_XP:
          if (gravDir != 3) return EntityPlayer.EnumStatus.OTHER_PROBLEM; 
          break;
        case eastTOwest_XN:
          if (gravDir != 2) return EntityPlayer.EnumStatus.OTHER_PROBLEM; 
          break;
        case downTOup_YP:
          if (gravDir != 1) return EntityPlayer.EnumStatus.OTHER_PROBLEM; 
          break;
        case upTOdown_YN:
          if (gravDir != 0) return EntityPlayer.EnumStatus.OTHER_PROBLEM;
          
          break;
      } 
      if (player.isPlayerSleeping() || !player.isEntityAlive())
      {
        return EntityPlayer.EnumStatus.OTHER_PROBLEM;
      }
      
      if (!player.worldObj.provider.isSurfaceWorld())
      {
        return EntityPlayer.EnumStatus.NOT_POSSIBLE_HERE;
      }
      
      if (player.worldObj.isDaytime())
      {
        return EntityPlayer.EnumStatus.NOT_POSSIBLE_NOW;
      }
      
      if (Math.abs(player.posX - par1) > 3.0D || Math.abs(player.posY - par2) > 3.0D || Math.abs(player.posZ - par3) > 3.0D)
      {
        return EntityPlayer.EnumStatus.TOO_FAR_AWAY;
      }
      
      double d0 = 8.0D;
      double d1 = 5.0D;
      List list = player.worldObj.getEntitiesWithinAABB(EntityMob.class, AxisAlignedBB.getBoundingBox(par1 - d0, par2 - d1, par3 - d0, par1 + d0, par2 + d1, par3 + d0));

      if (!list.isEmpty())
      {
        return EntityPlayer.EnumStatus.NOT_SAFE;
      }
    } 
    
    if (player.isRiding())
    {
      player.mountEntity((Entity)null);
    }
    
    SMReflectionHelper.setSize(player, 0.2F, 0.2F);
    player.yOffset = 0.2F;
    
    BlockStarBedHead.setPositionForStarBedSleepingPlayer(player, par1, par2, par3, gravDir, connDir);

    
    SMReflectionHelper.setSleeping(player);
    SMReflectionHelper.setSleepTimer(player, 0);
    
    player.playerLocation = new ChunkCoordinates(par1, par2, par3);
    player.motionX = player.motionZ = player.motionY = 0.0D;
    
    if (!player.worldObj.isRemote)
    {
      player.worldObj.updateAllPlayersSleepingFlag();
    }
    
    return EntityPlayer.EnumStatus.OK;
  }
  
  @SubscribeEvent
  public void handleArrowNockEvent(ArrowNockEvent event) {
    EntityPlayer shooter = event.entityPlayer;
    if (shooter.inventory.hasItem(SMModContainer.GArrowItem)) {
      
      shooter.setItemInUse(event.result, Items.bow.getMaxItemUseDuration(event.result));
      event.setCanceled(true);
    } 
  }

  @SubscribeEvent
  public void handleArrowLooseEvent(ArrowLooseEvent event) {
    EntityPlayer shooter = event.entityPlayer;
    World wordl = shooter.worldObj;
    ItemStack itemStackBow = event.bow;
    int itemUseDuration = event.charge;
    
    boolean infinity = (shooter.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, itemStackBow) > 0);
    
    if (shooter.inventory.hasItem(SMModContainer.GArrowItem)) {
      float f = itemUseDuration / 20.0F;
      f = (f * f + f * 2.0F) / 3.0F;
      
      if (f < 0.1D) {
        return;
      }

      if (f > 1.0F)
      {
        f = 1.0F;
      }

      EntityGProjectile entityGarrow = new EntityGProjectile(wordl, shooter, f * 2.0F, EntityGProjectile.GProjectileType.gArrow);
      
      if (f == 1.0F)
      {
        entityGarrow.setIsCritical(true);
      }
      
      int k = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, itemStackBow);
      
      if (k > 0)
      {
        entityGarrow.setDamage(entityGarrow.getDamage() + k * 0.5D + 0.5D);
      }
      
      int l = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, itemStackBow);
      
      if (l > 0)
      {
        entityGarrow.setKnockbackStrength(l);
      }
      
      if (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, itemStackBow) > 0)
      {
        entityGarrow.setFire(100);
      }
      
      itemStackBow.damageItem(1, (EntityLivingBase)shooter);
      wordl.playSoundAtEntity((Entity)shooter, "random.bow", 1.0F, 1.0F / (this.rand.nextFloat() * 0.4F + 1.2F) + f * 0.5F);
      
      if (infinity) {
        
        entityGarrow.canBePickedUp = 2;
      }
      else {
        
        shooter.inventory.consumeInventoryItem(SMModContainer.GArrowItem);
      } 
      
      if (!wordl.isRemote)
      {
        wordl.spawnEntityInWorld((Entity)entityGarrow);
      }
      
      event.setCanceled(true);
    } 
  }
}
