package jp.mc.ancientred.starminer.basics.dimention;

import cpw.mods.fml.common.FMLCommonHandler;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import jp.mc.ancientred.starminer.api.IMountTeleportHandler;
import jp.mc.ancientred.starminer.basics.Config;
import jp.mc.ancientred.starminer.basics.SMModContainer;
import jp.mc.ancientred.starminer.basics.entity.EntityReRideAttacher;
import jp.mc.ancientred.starminer.basics.packet.SMPacketSender;
import jp.mc.ancientred.starminer.basics.tileentity.TileEntityGravityGenerator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S1DPacketEntityEffect;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.util.DamageSource;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class DimentionTeleportHandler
  implements IMountTeleportHandler.IEntityTransfer
{
  public boolean onUpdateEntityEnd(EntityPlayerMP player) {
    boolean teleported = false;
    if (player.dimension == SMModContainer.GeostationaryOrbitDimentionId) {
      
      if (player.posY < -20.0D) {
        if (!player.isImmuneToFire() && !isRidingEntityImmuneToFire(player)) {
          player.attackEntityFrom(DamageSource.inFire, 4.0F);
          player.setFire(10);
        } 
        if (player.posY < -64.0D) {
          player.timeUntilPortal = player.getPortalCooldown();
          player.lastTickPosY = player.posY = 288.0D;
          transferPlayerToDimention(player, 0);
          teleported = true;
        } 
      } else if (player.posY > 384.0D) {
        
        player.attackEntityFrom(DamageSource.outOfWorld, 4.0F);
      }
    
    } else if (player.dimension == 0) {
      
      if (player.posY > 288.0D && (
        Config.ticketFreeForTeleport || TileEntityGravityGenerator.isGravityReverse((Entity)player, true))) {
        player.timeUntilPortal = player.getPortalCooldown();
        player.lastTickPosY = player.posY = -10.0D;
        transferPlayerToDimention(player, SMModContainer.GeostationaryOrbitDimentionId);
        teleported = true;
      } 
    } 
    
    return teleported;
  }

  private boolean isRidingEntityImmuneToFire(EntityPlayerMP player) {
    return (player.ridingEntity != null && player.ridingEntity.isImmuneToFire());
  }

  public void transferPlayerToDimention(EntityPlayerMP player, int dstDimentionId) {
    if (!player.worldObj.isRemote && !player.isDead) {
      
      WorldServer worldserverSrc = player.mcServer.worldServerForDimension(player.dimension);
      WorldServer worldserverDst = player.mcServer.worldServerForDimension(dstDimentionId);
      TeleporterForSpace teleporter = new TeleporterForSpace(worldserverDst);
      
      Entity newRiding = null;
      
      try {
        if (player.isRiding()) {
          if (player.ridingEntity instanceof IMountTeleportHandler) {
            List list = new ArrayList();
            
            if (((IMountTeleportHandler)player.ridingEntity).handleSpaceTeleport(this, teleporter, dstDimentionId, player.posX, player.posY, player.posZ, list)) {


              
              if (list.size() != 0 && list.size() % 2 == 0) {
                for (int i = 0; i < list.size(); i += 2) {
                  Object objA = list.get(i);
                  Object objB = list.get(i + 1);
                  if (objA instanceof Entity && objB instanceof Entity) {
                    Entity entityRider = (Entity)objA;
                    Entity entityRidden = (Entity)objB;

                    
                    EntityReRideAttacher reRideAttacher = new EntityReRideAttacher(entityRider.worldObj);
                    reRideAttacher.setToRideEntityAtServer(entityRider);
                    reRideAttacher.setToBeRiddenEntityAtServer(entityRidden);
                    entityRider.worldObj.spawnEntityInWorld((Entity)reRideAttacher);
                  } 
                } 
              }

              return;
            } 
          } else {
            Entity ridingEntity = player.ridingEntity;
            player.ridingEntity.riddenByEntity = null;
            player.ridingEntity = null;
            ridingEntity.setLocationAndAngles(player.posX, player.posY, player.posZ, player.rotationYaw, player.rotationPitch);
            newRiding = travelEntityToDimension(ridingEntity, dstDimentionId, teleporter);
            
            if (newRiding != null) {
              
              EntityReRideAttacher reRideAttacher = new EntityReRideAttacher(newRiding.worldObj);
              reRideAttacher.setToRideEntityAtServer((Entity)player);
              reRideAttacher.setToBeRiddenEntityAtServer(newRiding);
              double motionY = (newRiding.worldObj.provider instanceof jp.mc.ancientred.starminer.api.IZeroGravityWorldProvider) ? 0.10000000149011612D : 0.0D;
              newRiding.motionY = motionY;
              newRiding.worldObj.spawnEntityInWorld((Entity)reRideAttacher);
            } 
          } 
        }
      } catch (Exception ex) {
        newRiding = null;
      } 

      travelPlayerToDimension((EntityPlayer)player, dstDimentionId, teleporter);

      if (newRiding != null) {
        player.mountEntity(newRiding);
      }
    } 
  }

  public Entity travelEntityToDimension(Entity entitySrc, int dstDimentionId, Teleporter teleporter) {
    MinecraftServer minecraftserver = MinecraftServer.getServer();
    int srcDimentionId = entitySrc.dimension;
    WorldServer worldserver = minecraftserver.worldServerForDimension(srcDimentionId);
    WorldServer worldserver1 = minecraftserver.worldServerForDimension(dstDimentionId);
    entitySrc.dimension = dstDimentionId;
    
    if (srcDimentionId == 1 && dstDimentionId == 1) {
      
      worldserver1 = minecraftserver.worldServerForDimension(0);
      entitySrc.dimension = 0;
    } 
    
    entitySrc.worldObj.removeEntity(entitySrc);
    entitySrc.isDead = false;
    minecraftserver.getConfigurationManager().transferEntityToWorld(entitySrc, srcDimentionId, worldserver, worldserver1, teleporter);
    Entity entityDst = EntityList.createEntityByName(EntityList.getEntityString(entitySrc), (World)worldserver1);
    
    if (entityDst != null) {
      
      entityDst.copyDataFrom(entitySrc, true);
      entityDst.forceSpawn = true;
      worldserver1.spawnEntityInWorld(entityDst);
      entityDst.setLocationAndAngles(entitySrc.posX, entitySrc.posY, entitySrc.posZ, entitySrc.rotationYaw, entitySrc.rotationPitch);
    } 
    
    entitySrc.isDead = true;
    worldserver.resetUpdateEntityTick();
    worldserver1.resetUpdateEntityTick();
    
    return entityDst;
  }

  public void travelPlayerToDimension(EntityPlayer entityPlayer, int dstDimentionId, Teleporter teleporter) {
    if (!(entityPlayer instanceof EntityPlayerMP))
      return;  EntityPlayerMP player = (EntityPlayerMP)entityPlayer;
    MinecraftServer mcServer = player.mcServer;
    ServerConfigurationManager serverConfigurationManager = mcServer.getConfigurationManager();
    
    int srcDimId = player.dimension;
    WorldServer worldserver = mcServer.worldServerForDimension(player.dimension);
    player.dimension = dstDimentionId;
    WorldServer worldserver1 = mcServer.worldServerForDimension(player.dimension);

    player.playerNetServerHandler.setPlayerLocation(player.posX, player.posY, player.posZ, player.rotationYaw, player.rotationPitch);

    SMPacketSender.sendRespawnPacketToPlayer(player, player.dimension, player.worldObj.difficultySetting, player.worldObj.getWorldInfo().getTerrainType(), player.theItemInWorldManager.getGameType());

    player.playerNetServerHandler.setPlayerLocation(player.posX, player.posY, player.posZ, player.rotationYaw, player.rotationPitch);
    
    worldserver.removePlayerEntityDangerously((Entity)player);
    player.isDead = false;
    serverConfigurationManager.transferEntityToWorld((Entity)player, srcDimId, worldserver, worldserver1, teleporter);
    serverConfigurationManager.func_72375_a(player, worldserver);
    
    player.theItemInWorldManager.setWorld(worldserver1);
    serverConfigurationManager.updateTimeAndWeatherForPlayer(player, worldserver1);
    serverConfigurationManager.syncPlayerInventory(player);
    Iterator<PotionEffect> iterator = player.getActivePotionEffects().iterator();
    
    while (iterator.hasNext()) {
      
      PotionEffect potioneffect = iterator.next();
      player.playerNetServerHandler.sendPacket((Packet)new S1DPacketEntityEffect(player.getEntityId(), potioneffect));
    } 
    FMLCommonHandler.instance().firePlayerChangedDimensionEvent((EntityPlayer)player, srcDimId, dstDimentionId);
  }
}
