package jp.mc.ancientred.starminer.basics.dummy;

import com.google.common.collect.ImmutableSetMultimap;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import jp.mc.ancientred.starminer.api.GravityDirection;
import jp.mc.ancientred.starminer.basics.SMModContainer;
import jp.mc.ancientred.starminer.basics.SMReflectionHelper;
import jp.mc.ancientred.starminer.basics.tileentity.TileEntityBlockRotator;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.command.IEntitySelector;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.profiler.Profiler;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.Explosion;
import net.minecraft.world.GameRules;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.IWorldAccess;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.util.ForgeDirection;

public class DummyRotatedWorld
  extends World
{
  private World wrapped;
  private GravityDirection gdir;
  private int centerX;
  private int centerY;
  private int centerZ;
  private int[] conv = new int[3];
  
  private DummyRotatedWorld() {
    super((ISaveHandler)null, (String)null, (WorldSettings)null, (WorldProvider)null, (Profiler)null);
  }

  public static final DummyRotatedWorld get() {
    return thHoldThis.get();
  }
  
  private static ThreadLocal<DummyRotatedWorld> thHoldThis = new ThreadLocal<DummyRotatedWorld>() {
      protected DummyRotatedWorld initialValue() {
        return new DummyRotatedWorld();
      }
    };

  public World wrapp(World world, GravityDirection gdir, int centerX, int centerY, int centerZ) {
    this.centerX = centerX;
    this.centerY = centerY;
    this.centerZ = centerZ;
    this.gdir = gdir;
    this.wrapped = world;

    this.scheduledUpdatesAreImmediate = world.scheduledUpdatesAreImmediate;
    this.loadedEntityList = world.loadedEntityList;
    this.loadedTileEntityList = world.loadedTileEntityList;
    this.playerEntities = world.playerEntities;
    this.weatherEffects = world.weatherEffects;
    this.skylightSubtracted = world.skylightSubtracted;
    this.prevRainingStrength = world.prevRainingStrength;
    this.rainingStrength = world.rainingStrength;
    this.prevThunderingStrength = world.prevThunderingStrength;
    this.thunderingStrength = world.thunderingStrength;
    this.lastLightningBolt = world.lastLightningBolt;
    this.difficultySetting = world.difficultySetting;
    this.rand = world.rand;
    this.findingSpawnPoint = world.findingSpawnPoint;
    this.mapStorage = world.mapStorage;
    this.villageCollectionObj = world.villageCollectionObj;
    this.isRemote = world.isRemote;
    
    if (!SMModContainer.isIS1710OldWorld) {
      setNewForgeWorldFiled(world);
    }
    
    SMReflectionHelper.setWrappedWorldFinalField(this, world.provider, world.theProfiler);
    
    return this;
  }

  private void setNewForgeWorldFiled(World world) {
    this.restoringBlockSnapshots = world.restoringBlockSnapshots;
    this.captureBlockSnapshots = world.captureBlockSnapshots;
    this.capturedBlockSnapshots = world.capturedBlockSnapshots;
  }
  
  private TileEntityBlockRotator getTileEntityBlockRotator(IBlockAccess world, int par2, int par3, int par4) {
    TileEntity te = this.wrapped.getTileEntity(par2, par3, par4);
    if (te instanceof TileEntityBlockRotator) {
      return (TileEntityBlockRotator)te;
    }
    return null;
  }
  
  public int[] rotateOnCurrentState(int parX, int parY, int parZ) {
    return this.gdir.rotateXYZAt(this.conv, parX, parY, parZ, this.centerX, this.centerY, this.centerZ);
  }

  public BiomeGenBase getBiomeGenForCoords(int p_72807_1_, int p_72807_2_) {
    return this.wrapped.getBiomeGenForCoords(p_72807_1_, p_72807_2_);
  }
  
  public BiomeGenBase getBiomeGenForCoordsBody(int p_72807_1_, int p_72807_2_) {
    return this.wrapped.getBiomeGenForCoordsBody(p_72807_1_, p_72807_2_);
  }
  
  public WorldChunkManager getWorldChunkManager() {
    return this.wrapped.getWorldChunkManager();
  }
  
  public void setSpawnLocation() {
    this.wrapped.setSpawnLocation();
  }
  
  public boolean doChunksNearChunkExist(int p_72873_1_, int p_72873_2_, int p_72873_3_, int p_72873_4_) {
    return this.wrapped.doChunksNearChunkExist(p_72873_1_, p_72873_2_, p_72873_3_, p_72873_4_);
  }
  
  public boolean checkChunksExist(int p_72904_1_, int p_72904_2_, int p_72904_3_, int p_72904_4_, int p_72904_5_, int p_72904_6_) {
    return this.wrapped.checkChunksExist(p_72904_1_, p_72904_2_, p_72904_3_, p_72904_4_, p_72904_5_, p_72904_6_);
  }
  
  public Chunk getChunkFromBlockCoords(int p_72938_1_, int p_72938_2_) {
    return this.wrapped.getChunkFromBlockCoords(p_72938_1_, p_72938_2_);
  }
  
  public Chunk getChunkFromChunkCoords(int p_72964_1_, int p_72964_2_) {
    return this.wrapped.getChunkFromChunkCoords(p_72964_1_, p_72964_2_);
  }

  public void markBlockForRenderUpdate(int p_147479_1_, int p_147479_2_, int p_147479_3_) {
    this.wrapped.markBlockForRenderUpdate(p_147479_1_, p_147479_2_, p_147479_3_);
  }
  
  public boolean isDaytime() {
    return this.wrapped.isDaytime();
  }
  
  public MovingObjectPosition rayTraceBlocks(Vec3 p_72933_1_, Vec3 p_72933_2_) {
    return this.wrapped.rayTraceBlocks(p_72933_1_, p_72933_2_);
  }
  
  public MovingObjectPosition rayTraceBlocks(Vec3 p_72901_1_, Vec3 p_72901_2_, boolean p_72901_3_) {
    return this.wrapped.rayTraceBlocks(p_72901_1_, p_72901_2_, p_72901_3_);
  }
  
  public MovingObjectPosition rayTraceBlocks(Vec3 p_147447_1_, Vec3 p_147447_2_, boolean p_147447_3_, boolean p_147447_4_, boolean p_147447_5_) {
    return this.wrapped.rayTraceBlocks(p_147447_1_, p_147447_2_, p_147447_3_, p_147447_4_, p_147447_5_);
  }
  
  public void playSoundAtEntity(Entity p_72956_1_, String p_72956_2_, float p_72956_3_, float p_72956_4_) {
    this.wrapped.playSoundAtEntity(p_72956_1_, p_72956_2_, p_72956_3_, p_72956_4_);
  }
  
  public void playSoundToNearExcept(EntityPlayer p_85173_1_, String p_85173_2_, float p_85173_3_, float p_85173_4_) {
    this.wrapped.playSoundToNearExcept(p_85173_1_, p_85173_2_, p_85173_3_, p_85173_4_);
  }
  
  public void playSoundEffect(double p_72908_1_, double p_72908_3_, double p_72908_5_, String p_72908_7_, float p_72908_8_, float p_72908_9_) {
    this.wrapped.playSoundEffect(p_72908_1_, p_72908_3_, p_72908_5_, p_72908_7_, p_72908_8_, p_72908_9_);
  }
  
  public void playSound(double p_72980_1_, double p_72980_3_, double p_72980_5_, String p_72980_7_, float p_72980_8_, float p_72980_9_, boolean p_72980_10_) {
    this.wrapped.playSound(p_72980_1_, p_72980_3_, p_72980_5_, p_72980_7_, p_72980_8_, p_72980_9_, p_72980_10_);
  }
  
  public void playRecord(String p_72934_1_, int p_72934_2_, int p_72934_3_, int p_72934_4_) {
    this.wrapped.playRecord(p_72934_1_, p_72934_2_, p_72934_3_, p_72934_4_);
  }
  
  public void spawnParticle(String p_72869_1_, double p_72869_2_, double p_72869_4_, double p_72869_6_, double p_72869_8_, double p_72869_10_, double p_72869_12_) {
    this.wrapped.spawnParticle(p_72869_1_, p_72869_2_, p_72869_4_, p_72869_6_, p_72869_8_, p_72869_10_, p_72869_12_);
  }

  public void func_147446_b(int p_147446_1_, int p_147446_2_, int p_147446_3_, Block p_147446_4_, int p_147446_5_, int p_147446_6_) {
    this.wrapped.func_147446_b(p_147446_1_, p_147446_2_, p_147446_3_, p_147446_4_, p_147446_5_, p_147446_6_);
  }

  public boolean addWeatherEffect(Entity p_72942_1_) {
    return this.wrapped.addWeatherEffect(p_72942_1_);
  }
  
  public boolean spawnEntityInWorld(Entity p_72838_1_) {
    return this.wrapped.spawnEntityInWorld(p_72838_1_);
  }
  
  public void onEntityAdded(Entity p_72923_1_) {
    this.wrapped.onEntityAdded(p_72923_1_);
  }
  
  public void onEntityRemoved(Entity p_72847_1_) {
    this.wrapped.onEntityRemoved(p_72847_1_);
  }
  
  public void removeEntity(Entity p_72900_1_) {
    this.wrapped.removeEntity(p_72900_1_);
  }
  
  public void removePlayerEntityDangerously(Entity p_72973_1_) {
    this.wrapped.removePlayerEntityDangerously(p_72973_1_);
  }
  
  public void addWorldAccess(IWorldAccess p_72954_1_) {
    this.wrapped.addWorldAccess(p_72954_1_);
  }
  
  public List getCollidingBoundingBoxes(Entity p_72945_1_, AxisAlignedBB p_72945_2_) {
    return this.wrapped.getCollidingBoundingBoxes(p_72945_1_, p_72945_2_);
  }
  
  public List func_147461_a(AxisAlignedBB p_147461_1_) {
    return this.wrapped.func_147461_a(p_147461_1_);
  }
  
  public int calculateSkylightSubtracted(float p_72967_1_) {
    return this.wrapped.calculateSkylightSubtracted(p_72967_1_);
  }
  
  public float getSunBrightnessFactor(float p_72967_1_) {
    return this.wrapped.getSunBrightnessFactor(p_72967_1_);
  }
  
  public void removeWorldAccess(IWorldAccess p_72848_1_) {
    this.wrapped.removeWorldAccess(p_72848_1_);
  }
  
  public float getSunBrightness(float p_72971_1_) {
    return this.wrapped.getSunBrightness(p_72971_1_);
  }
  
  public float getSunBrightnessBody(float p_72971_1_) {
    return this.wrapped.getSunBrightnessBody(p_72971_1_);
  }
  
  public Vec3 getSkyColor(Entity p_72833_1_, float p_72833_2_) {
    return this.wrapped.getSkyColor(p_72833_1_, p_72833_2_);
  }
  
  public Vec3 getSkyColorBody(Entity p_72833_1_, float p_72833_2_) {
    return this.wrapped.getSkyColorBody(p_72833_1_, p_72833_2_);
  }
  
  public float getCelestialAngle(float p_72826_1_) {
    return this.wrapped.getCelestialAngle(p_72826_1_);
  }
  
  public int getMoonPhase() {
    return this.wrapped.getMoonPhase();
  }
  
  public float getCurrentMoonPhaseFactor() {
    return this.wrapped.getCurrentMoonPhaseFactor();
  }
  
  public float getCurrentMoonPhaseFactorBody() {
    return this.wrapped.getCurrentMoonPhaseFactorBody();
  }
  
  public float getCelestialAngleRadians(float p_72929_1_) {
    return this.wrapped.getCelestialAngleRadians(p_72929_1_);
  }
  
  public Vec3 getCloudColour(float p_72824_1_) {
    return this.wrapped.getCloudColour(p_72824_1_);
  }
  
  public Vec3 drawCloudsBody(float p_72824_1_) {
    return this.wrapped.drawCloudsBody(p_72824_1_);
  }
  
  public Vec3 getFogColor(float p_72948_1_) {
    return this.wrapped.getFogColor(p_72948_1_);
  }
  
  public int getPrecipitationHeight(int p_72874_1_, int p_72874_2_) {
    return this.wrapped.getPrecipitationHeight(p_72874_1_, p_72874_2_);
  }
  
  public int getTopSolidOrLiquidBlock(int p_72825_1_, int p_72825_2_) {
    return this.wrapped.getTopSolidOrLiquidBlock(p_72825_1_, p_72825_2_);
  }
  
  public float getStarBrightness(float p_72880_1_) {
    return this.wrapped.getStarBrightness(p_72880_1_);
  }
  
  public float getStarBrightnessBody(float par1) {
    return this.wrapped.getStarBrightnessBody(par1);
  }
  
  public void updateEntities() {
    this.wrapped.updateEntities();
  }
  
  public void func_147448_a(Collection p_147448_1_) {
    this.wrapped.func_147448_a(p_147448_1_);
  }
  
  public void updateEntity(Entity p_72870_1_) {
    this.wrapped.updateEntity(p_72870_1_);
  }
  
  public void updateEntityWithOptionalForce(Entity p_72866_1_, boolean p_72866_2_) {
    this.wrapped.updateEntityWithOptionalForce(p_72866_1_, p_72866_2_);
  }
  
  public boolean checkNoEntityCollision(AxisAlignedBB p_72855_1_) {
    return this.wrapped.checkNoEntityCollision(p_72855_1_);
  }
  
  public boolean checkNoEntityCollision(AxisAlignedBB p_72917_1_, Entity p_72917_2_) {
    return this.wrapped.checkNoEntityCollision(p_72917_1_, p_72917_2_);
  }
  
  public boolean checkBlockCollision(AxisAlignedBB p_72829_1_) {
    return this.wrapped.checkBlockCollision(p_72829_1_);
  }
  
  public boolean isAnyLiquid(AxisAlignedBB p_72953_1_) {
    return this.wrapped.isAnyLiquid(p_72953_1_);
  }
  
  public boolean func_147470_e(AxisAlignedBB p_147470_1_) {
    return this.wrapped.func_147470_e(p_147470_1_);
  }
  
  public boolean handleMaterialAcceleration(AxisAlignedBB p_72918_1_, Material p_72918_2_, Entity p_72918_3_) {
    return this.wrapped.handleMaterialAcceleration(p_72918_1_, p_72918_2_, p_72918_3_);
  }
  
  public boolean isMaterialInBB(AxisAlignedBB p_72875_1_, Material p_72875_2_) {
    return this.wrapped.isMaterialInBB(p_72875_1_, p_72875_2_);
  }
  
  public boolean isAABBInMaterial(AxisAlignedBB p_72830_1_, Material p_72830_2_) {
    return this.wrapped.isAABBInMaterial(p_72830_1_, p_72830_2_);
  }
  
  public Explosion createExplosion(Entity p_72876_1_, double p_72876_2_, double p_72876_4_, double p_72876_6_, float p_72876_8_, boolean p_72876_9_) {
    return this.wrapped.createExplosion(p_72876_1_, p_72876_2_, p_72876_4_, p_72876_6_, p_72876_8_, p_72876_9_);
  }

  
  public Explosion newExplosion(Entity p_72885_1_, double p_72885_2_, double p_72885_4_, double p_72885_6_, float p_72885_8_, boolean p_72885_9_, boolean p_72885_10_) {
    return this.wrapped.newExplosion(p_72885_1_, p_72885_2_, p_72885_4_, p_72885_6_, p_72885_8_, p_72885_9_, p_72885_10_);
  }
  
  public float getBlockDensity(Vec3 p_72842_1_, AxisAlignedBB p_72842_2_) {
    return this.wrapped.getBlockDensity(p_72842_1_, p_72842_2_);
  }
  
  public boolean extinguishFire(EntityPlayer p_72886_1_, int p_72886_2_, int p_72886_3_, int p_72886_4_, int p_72886_5_) {
    return this.wrapped.extinguishFire(p_72886_1_, p_72886_2_, p_72886_3_, p_72886_4_, p_72886_5_);
  }
  
  public String getDebugLoadedEntities() {
    return this.wrapped.getDebugLoadedEntities();
  }
  
  public String getProviderName() {
    return this.wrapped.getProviderName();
  }

  
  public void setTileEntity(int p_147455_1_, int p_147455_2_, int p_147455_3_, TileEntity p_147455_4_) {
    this.wrapped.setTileEntity(p_147455_1_, p_147455_2_, p_147455_3_, p_147455_4_);
  }

  
  public void removeTileEntity(int p_147475_1_, int p_147475_2_, int p_147475_3_) {
    this.wrapped.removeTileEntity(p_147475_1_, p_147475_2_, p_147475_3_);
  }
  
  public void markTileEntityForRemoval(TileEntity p_147457_1_) {
    this.wrapped.markTileEntityForRemoval(p_147457_1_);
  }
  
  public boolean isBlockFullCube(int p_147469_1_, int p_147469_2_, int p_147469_3_) {
    return this.wrapped.isBlockFullCube(p_147469_1_, p_147469_2_, p_147469_3_);
  }
  
  public void calculateInitialSkylight() {
    this.wrapped.calculateInitialSkylight();
  }
  
  public void setAllowedSpawnTypes(boolean p_72891_1_, boolean p_72891_2_) {
    this.wrapped.setAllowedSpawnTypes(p_72891_1_, p_72891_2_);
  }
  
  public void tick() {
    this.wrapped.tick();
  }
  
  public void calculateInitialWeatherBody() {
    this.wrapped.calculateInitialWeatherBody();
  }
  
  public void updateWeatherBody() {
    this.wrapped.updateWeatherBody();
  }
  
  public boolean updateAllLightTypes(int p_147451_1_, int p_147451_2_, int p_147451_3_) {
    return this.wrapped.updateAllLightTypes(p_147451_1_, p_147451_2_, p_147451_3_);
  }
  
  public boolean updateLightByType(EnumSkyBlock p_147463_1_, int p_147463_2_, int p_147463_3_, int p_147463_4_) {
    return this.wrapped.updateLightByType(p_147463_1_, p_147463_2_, p_147463_3_, p_147463_4_);
  }
  
  public boolean tickUpdates(boolean p_72955_1_) {
    return this.wrapped.tickUpdates(p_72955_1_);
  }
  
  public List getPendingBlockUpdates(Chunk p_72920_1_, boolean p_72920_2_) {
    return this.wrapped.getPendingBlockUpdates(p_72920_1_, p_72920_2_);
  }

  
  public List getEntitiesWithinAABBExcludingEntity(Entity p_72839_1_, AxisAlignedBB p_72839_2_) {
    return this.wrapped.getEntitiesWithinAABBExcludingEntity(p_72839_1_, p_72839_2_);
  }
  
  public List getEntitiesWithinAABBExcludingEntity(Entity p_94576_1_, AxisAlignedBB p_94576_2_, IEntitySelector p_94576_3_) {
    return this.wrapped.getEntitiesWithinAABBExcludingEntity(p_94576_1_, p_94576_2_, p_94576_3_);
  }

  
  public List getEntitiesWithinAABB(Class p_72872_1_, AxisAlignedBB p_72872_2_) {
    return this.wrapped.getEntitiesWithinAABB(p_72872_1_, p_72872_2_);
  }
  
  public List selectEntitiesWithinAABB(Class p_82733_1_, AxisAlignedBB p_82733_2_, IEntitySelector p_82733_3_) {
    return this.wrapped.selectEntitiesWithinAABB(p_82733_1_, p_82733_2_, p_82733_3_);
  }
  
  public Entity findNearestEntityWithinAABB(Class p_72857_1_, AxisAlignedBB p_72857_2_, Entity p_72857_3_) {
    return this.wrapped.findNearestEntityWithinAABB(p_72857_1_, p_72857_2_, p_72857_3_);
  }
  
  public List getLoadedEntityList() {
    return this.wrapped.getLoadedEntityList();
  }
  
  public void markTileEntityChunkModified(int p_147476_1_, int p_147476_2_, int p_147476_3_, TileEntity p_147476_4_) {
    this.wrapped.markTileEntityChunkModified(p_147476_1_, p_147476_2_, p_147476_3_, p_147476_4_);
  }
  
  public int countEntities(Class p_72907_1_) {
    return this.wrapped.countEntities(p_72907_1_);
  }
  
  public void addLoadedEntities(List p_72868_1_) {
    this.wrapped.addLoadedEntities(p_72868_1_);
  }
  
  public void unloadEntities(List p_72828_1_) {
    this.wrapped.unloadEntities(p_72828_1_);
  }
  
  public boolean canPlaceEntityOnSide(Block p_147472_1_, int p_147472_2_, int p_147472_3_, int p_147472_4_, boolean p_147472_5_, int p_147472_6_, Entity p_147472_7_, ItemStack p_147472_8_) {
    return this.wrapped.canPlaceEntityOnSide(p_147472_1_, p_147472_2_, p_147472_3_, p_147472_4_, p_147472_5_, p_147472_6_, p_147472_7_, p_147472_8_);
  }
  
  public PathEntity getPathEntityToEntity(Entity p_72865_1_, Entity p_72865_2_, float p_72865_3_, boolean p_72865_4_, boolean p_72865_5_, boolean p_72865_6_, boolean p_72865_7_) {
    return this.wrapped.getPathEntityToEntity(p_72865_1_, p_72865_2_, p_72865_3_, p_72865_4_, p_72865_5_, p_72865_6_, p_72865_7_);
  }
  
  public PathEntity getEntityPathToXYZ(Entity p_72844_1_, int p_72844_2_, int p_72844_3_, int p_72844_4_, float p_72844_5_, boolean p_72844_6_, boolean p_72844_7_, boolean p_72844_8_, boolean p_72844_9_) {
    return this.wrapped.getEntityPathToXYZ(p_72844_1_, p_72844_2_, p_72844_3_, p_72844_4_, p_72844_5_, p_72844_6_, p_72844_7_, p_72844_8_, p_72844_9_);
  }

  public EntityPlayer getClosestPlayerToEntity(Entity p_72890_1_, double p_72890_2_) {
    return this.wrapped.getClosestPlayerToEntity(p_72890_1_, p_72890_2_);
  }
  
  public EntityPlayer getClosestPlayer(double p_72977_1_, double p_72977_3_, double p_72977_5_, double p_72977_7_) {
    return this.wrapped.getClosestPlayer(p_72977_1_, p_72977_3_, p_72977_5_, p_72977_7_);
  }
  
  public EntityPlayer getClosestVulnerablePlayerToEntity(Entity p_72856_1_, double p_72856_2_) {
    return this.wrapped.getClosestVulnerablePlayerToEntity(p_72856_1_, p_72856_2_);
  }
  
  public EntityPlayer getClosestVulnerablePlayer(double p_72846_1_, double p_72846_3_, double p_72846_5_, double p_72846_7_) {
    return this.wrapped.getClosestVulnerablePlayer(p_72846_1_, p_72846_3_, p_72846_5_, p_72846_7_);
  }
  
  public EntityPlayer getPlayerEntityByName(String p_72924_1_) {
    return this.wrapped.getPlayerEntityByName(p_72924_1_);
  }
  
  public EntityPlayer getPlayerEntityByUUID(UUID p_152378_1_) {
    return this.wrapped.getPlayerEntityByUUID(p_152378_1_);
  }
  
  public void sendQuittingDisconnectingPacket() {
    this.wrapped.sendQuittingDisconnectingPacket();
  }
  
  public void checkSessionLock() throws MinecraftException {
    this.wrapped.checkSessionLock();
  }
  
  public void func_82738_a(long p_82738_1_) {
    this.wrapped.func_82738_a(p_82738_1_);
  }
  
  public long getSeed() {
    return this.wrapped.getSeed();
  }
  
  public long getTotalWorldTime() {
    return this.wrapped.getTotalWorldTime();
  }
  
  public long getWorldTime() {
    return this.wrapped.getWorldTime();
  }
  
  public void setWorldTime(long p_72877_1_) {
    this.wrapped.setWorldTime(p_72877_1_);
  }
  
  public ChunkCoordinates getSpawnPoint() {
    return this.wrapped.getSpawnPoint();
  }
  
  public void setSpawnLocation(int p_72950_1_, int p_72950_2_, int p_72950_3_) {
    this.wrapped.setSpawnLocation(p_72950_1_, p_72950_2_, p_72950_3_);
  }
  
  public void joinEntityInSurroundings(Entity p_72897_1_) {
    this.wrapped.joinEntityInSurroundings(p_72897_1_);
  }
  
  public void setEntityState(Entity p_72960_1_, byte p_72960_2_) {
    this.wrapped.setEntityState(p_72960_1_, p_72960_2_);
  }
  
  public IChunkProvider getChunkProvider() {
    return this.wrapped.getChunkProvider();
  }
  
  public void addBlockEvent(int p_147452_1_, int p_147452_2_, int p_147452_3_, Block p_147452_4_, int p_147452_5_, int p_147452_6_) {
    this.wrapped.addBlockEvent(p_147452_1_, p_147452_2_, p_147452_3_, p_147452_4_, p_147452_5_, p_147452_6_);
  }
  
  public ISaveHandler getSaveHandler() {
    return this.wrapped.getSaveHandler();
  }
  
  public WorldInfo getWorldInfo() {
    return this.wrapped.getWorldInfo();
  }
  
  public GameRules getGameRules() {
    return this.wrapped.getGameRules();
  }
  
  public void updateAllPlayersSleepingFlag() {
    this.wrapped.updateAllPlayersSleepingFlag();
  }
  
  public float getWeightedThunderStrength(float p_72819_1_) {
    return this.wrapped.getWeightedThunderStrength(p_72819_1_);
  }
  
  public void setThunderStrength(float p_147442_1_) {
    this.wrapped.setThunderStrength(p_147442_1_);
  }
  
  public float getRainStrength(float p_72867_1_) {
    return this.wrapped.getRainStrength(p_72867_1_);
  }
  
  public void setRainStrength(float p_72894_1_) {
    this.wrapped.setRainStrength(p_72894_1_);
  }
  
  public boolean isThundering() {
    return this.wrapped.isThundering();
  }
  
  public boolean isRaining() {
    return this.wrapped.isRaining();
  }
  
  public void setItemData(String p_72823_1_, WorldSavedData p_72823_2_) {
    this.wrapped.setItemData(p_72823_1_, p_72823_2_);
  }
  
  public WorldSavedData loadItemData(Class p_72943_1_, String p_72943_2_) {
    return this.wrapped.loadItemData(p_72943_1_, p_72943_2_);
  }
  
  public int getUniqueDataId(String p_72841_1_) {
    return this.wrapped.getUniqueDataId(p_72841_1_);
  }
  
  public void playBroadcastSound(int p_82739_1_, int p_82739_2_, int p_82739_3_, int p_82739_4_, int p_82739_5_) {
    this.wrapped.playBroadcastSound(p_82739_1_, p_82739_2_, p_82739_3_, p_82739_4_, p_82739_5_);
  }
  
  public void playAuxSFX(int p_72926_1_, int p_72926_2_, int p_72926_3_, int p_72926_4_, int p_72926_5_) {
    this.wrapped.playAuxSFX(p_72926_1_, p_72926_2_, p_72926_3_, p_72926_4_, p_72926_5_);
  }
  
  public void playAuxSFXAtEntity(EntityPlayer p_72889_1_, int p_72889_2_, int p_72889_3_, int p_72889_4_, int p_72889_5_, int p_72889_6_) {
    this.wrapped.playAuxSFXAtEntity(p_72889_1_, p_72889_2_, p_72889_3_, p_72889_4_, p_72889_5_, p_72889_6_);
  }
  
  public int getHeight() {
    return this.wrapped.getHeight();
  }
  
  public int getActualHeight() {
    return this.wrapped.getActualHeight();
  }
  
  public Random setRandomSeed(int p_72843_1_, int p_72843_2_, int p_72843_3_) {
    return this.wrapped.setRandomSeed(p_72843_1_, p_72843_2_, p_72843_3_);
  }
  
  public ChunkPosition findClosestStructure(String p_147440_1_, int p_147440_2_, int p_147440_3_, int p_147440_4_) {
    return this.wrapped.findClosestStructure(p_147440_1_, p_147440_2_, p_147440_3_, p_147440_4_);
  }
  
  public boolean extendedLevelsInChunkCache() {
    return this.wrapped.extendedLevelsInChunkCache();
  }
  
  public double getHorizon() {
    return this.wrapped.getHorizon();
  }
  
  public CrashReportCategory addWorldInfoToCrashReport(CrashReport p_72914_1_) {
    return this.wrapped.addWorldInfoToCrashReport(p_72914_1_);
  }
  
  public void destroyBlockInWorldPartially(int p_147443_1_, int p_147443_2_, int p_147443_3_, int p_147443_4_, int p_147443_5_) {
    this.wrapped.destroyBlockInWorldPartially(p_147443_1_, p_147443_2_, p_147443_3_, p_147443_4_, p_147443_5_);
  }
  
  public Calendar getCurrentDate() {
    return this.wrapped.getCurrentDate();
  }
  
  public void makeFireworks(double p_92088_1_, double p_92088_3_, double p_92088_5_, double p_92088_7_, double p_92088_9_, double p_92088_11_, NBTTagCompound p_92088_13_) {
    this.wrapped.makeFireworks(p_92088_1_, p_92088_3_, p_92088_5_, p_92088_7_, p_92088_9_, p_92088_11_, p_92088_13_);
  }
  
  public Scoreboard getScoreboard() {
    return this.wrapped.getScoreboard();
  }

  public void updateNeighborsAboutBlockChange(int p_147453_1_, int p_147453_2_, int p_147453_3_, Block p_147453_4_) {
    this.wrapped.updateNeighborsAboutBlockChange(p_147453_1_, p_147453_2_, p_147453_3_, p_147453_4_);
  }
  
  public float getTensionFactorForBlock(double p_147462_1_, double p_147462_3_, double p_147462_5_) {
    return this.wrapped.getTensionFactorForBlock(p_147462_1_, p_147462_3_, p_147462_5_);
  }
  
  public float func_147473_B(int p_147473_1_, int p_147473_2_, int p_147473_3_) {
    return this.wrapped.func_147473_B(p_147473_1_, p_147473_2_, p_147473_3_);
  }
  
  public void func_147450_X() {
    this.wrapped.func_147450_X();
  }
  
  public void addTileEntity(TileEntity entity) {
    this.wrapped.addTileEntity(entity);
  }
  
  public ImmutableSetMultimap<ChunkCoordIntPair, ForgeChunkManager.Ticket> getPersistentChunks() {
    return this.wrapped.getPersistentChunks();
  }

  public int countEntities(EnumCreatureType type, boolean forSpawnCount) {
    return this.wrapped.countEntities(type, forSpawnCount);
  }
  
  protected IChunkProvider createChunkProvider() {
    return null;
  }
  
  protected int getRenderDistanceChunks() {
    return 0;
  }
  
  public Entity getEntityByID(int p_73045_1_) {
    return this.wrapped.getEntityByID(p_73045_1_);
  }

  public boolean setBlock(int parX, int parY, int parZ, Block p_147465_4_, int p_147465_5_, int p_147465_6_) {
    int[] rotated = this.gdir.rotateXYZAt(this.conv, parX, parY, parZ, this.centerX, this.centerY, this.centerZ);
    parX = rotated[0]; parY = rotated[1]; parZ = rotated[2];
    
    return this.wrapped.setBlock(parX, parY, parZ, p_147465_4_, p_147465_5_, p_147465_6_);
  }
  
  public void markAndNotifyBlock(int parX, int parY, int parZ, Chunk chunk, Block oldBlock, Block newBlock, int flag) {
    int[] rotated = this.gdir.rotateXYZAt(this.conv, parX, parY, parZ, this.centerX, this.centerY, this.centerZ);
    parX = rotated[0]; parY = rotated[1]; parZ = rotated[2];
    
    this.wrapped.markAndNotifyBlock(parX, parY, parZ, chunk, oldBlock, newBlock, flag);
  }
  
  public boolean setBlockMetadataWithNotify(int parX, int parY, int parZ, int p_72921_4_, int p_72921_5_) {
    int[] rotated = this.gdir.rotateXYZAt(this.conv, parX, parY, parZ, this.centerX, this.centerY, this.centerZ);
    parX = rotated[0]; parY = rotated[1]; parZ = rotated[2];
    
    return this.wrapped.setBlockMetadataWithNotify(parX, parY, parZ, p_72921_4_, p_72921_5_);
  }
  
  public boolean setBlockToAir(int parX, int parY, int parZ) {
    int[] rotated = this.gdir.rotateXYZAt(this.conv, parX, parY, parZ, this.centerX, this.centerY, this.centerZ);
    parX = rotated[0]; parY = rotated[1]; parZ = rotated[2];
    
    return this.wrapped.setBlockToAir(parX, parY, parZ);
  }
  
  public boolean breakBlock(int parX, int parY, int parZ, boolean p_147480_4_) {
    int[] rotated = this.gdir.rotateXYZAt(this.conv, parX, parY, parZ, this.centerX, this.centerY, this.centerZ);
    parX = rotated[0]; parY = rotated[1]; parZ = rotated[2];
    
    return this.wrapped.breakBlock(parX, parY, parZ, p_147480_4_);
  }
  
  public boolean setBlock(int parX, int parY, int parZ, Block p_147449_4_) {
    int[] rotated = this.gdir.rotateXYZAt(this.conv, parX, parY, parZ, this.centerX, this.centerY, this.centerZ);
    parX = rotated[0]; parY = rotated[1]; parZ = rotated[2];
    
    return this.wrapped.setBlock(parX, parY, parZ, p_147449_4_);
  }
  
  public void markBlockForUpdate(int parX, int parY, int parZ) {
    int[] rotated = this.gdir.rotateXYZAt(this.conv, parX, parY, parZ, this.centerX, this.centerY, this.centerZ);
    parX = rotated[0]; parY = rotated[1]; parZ = rotated[2];
    
    this.wrapped.markBlockForUpdate(parX, parY, parZ);
  }
  
  public void notifyBlockChange(int parX, int parY, int parZ, Block p_147444_4_) {
    int[] rotated = this.gdir.rotateXYZAt(this.conv, parX, parY, parZ, this.centerX, this.centerY, this.centerZ);
    parX = rotated[0]; parY = rotated[1]; parZ = rotated[2];
    
    this.wrapped.notifyBlockChange(parX, parY, parZ, p_147444_4_);
  }
  
  public void markBlocksDirtyVertical(int parX, int parZ, int parYMin, int parYMax) {
    this.wrapped.markBlocksDirtyVertical(parX, parZ, parYMin, parYMax);
  }

  public void markBlockRangeForRenderUpdate(int parXMin, int parYMin, int parZMin, int parXMax, int parYMax, int parZMax) {
    int[] rotated = this.gdir.rotateXYZAt(this.conv, parXMin, parYMin, parZMin, this.centerX, this.centerY, this.centerZ);
    parXMin = rotated[0]; parYMin = rotated[1]; parZMin = rotated[2];
    
    rotated = this.gdir.rotateXYZAt(this.conv, parXMax, parYMax, parZMax, this.centerX, this.centerY, this.centerZ);
    parXMax = rotated[0]; parYMax = rotated[1]; parZMax = rotated[2];
    
    this.wrapped.markBlockRangeForRenderUpdate(parXMin, parYMin, parZMin, parXMax, parYMax, parZMax);
  }
  
  public void notifyBlocksOfNeighborChange(int parX, int parY, int parZ, Block p_147459_4_) {
    int[] rotated = this.gdir.rotateXYZAt(this.conv, parX, parY, parZ, this.centerX, this.centerY, this.centerZ);
    parX = rotated[0]; parY = rotated[1]; parZ = rotated[2];
    
    this.wrapped.notifyBlocksOfNeighborChange(parX, parY, parZ, p_147459_4_);
  }
  
  public void notifyBlocksOfNeighborChange(int parX, int parY, int parZ, Block p_147441_4_, int p_147441_5_) {
    int[] rotated = this.gdir.rotateXYZAt(this.conv, parX, parY, parZ, this.centerX, this.centerY, this.centerZ);
    parX = rotated[0]; parY = rotated[1]; parZ = rotated[2];
    
    this.wrapped.notifyBlocksOfNeighborChange(parX, parY, parZ, p_147441_4_, p_147441_5_);
  }
  
  public void notifyBlockOfNeighborChange(int parX, int parY, int parZ, Block p_147460_4_) {
    int[] rotated = this.gdir.rotateXYZAt(this.conv, parX, parY, parZ, this.centerX, this.centerY, this.centerZ);
    parX = rotated[0]; parY = rotated[1]; parZ = rotated[2];
    
    this.wrapped.notifyBlockOfNeighborChange(parX, parY, parZ, p_147460_4_);
  }
  
  public void setLightValue(EnumSkyBlock p_72915_1_, int parX, int parY, int parZ, int p_72915_5_) {
    int[] rotated = this.gdir.rotateXYZAt(this.conv, parX, parY, parZ, this.centerX, this.centerY, this.centerZ);
    parX = rotated[0]; parY = rotated[1]; parZ = rotated[2];
    
    this.wrapped.setLightValue(p_72915_1_, parX, parY, parZ, p_72915_5_);
  }

  public void scheduleBlockUpdate(int parX, int parY, int parZ, Block block, int tickRate) {
    int[] rotated = this.gdir.rotateXYZAt(this.conv, parX, parY, parZ, this.centerX, this.centerY, this.centerZ);
    parX = rotated[0]; parY = rotated[1]; parZ = rotated[2];
    
    this.wrapped.scheduleBlockUpdate(parX, parY, parZ, block, tickRate);
  }
  
  public void scheduleBlockUpdateWithPriority(int parX, int parY, int parZ, Block block, int tickRate, int priority) {
    int[] rotated = this.gdir.rotateXYZAt(this.conv, parX, parY, parZ, this.centerX, this.centerY, this.centerZ);
    parX = rotated[0]; parY = rotated[1]; parZ = rotated[2];
    
    this.wrapped.scheduleBlockUpdateWithPriority(parX, parY, parZ, block, tickRate, priority);
  }

  public Block getBlock(int parX, int parY, int parZ) {
    int[] rotated = this.gdir.rotateXYZAt(this.conv, parX, parY, parZ, this.centerX, this.centerY, this.centerZ);
    parX = rotated[0]; parY = rotated[1]; parZ = rotated[2];
    
    TileEntityBlockRotator te = getTileEntityBlockRotator((IBlockAccess)this.wrapped, parX, parY, parZ);
    if (te != null) {
      Block storedBlock = te.getStoredBlock();
      if (storedBlock != null) return storedBlock; 
    } 
    return this.wrapped.getBlock(parX, parY, parZ);
  }
  
  public TileEntity getTileEntity(int parX, int parY, int parZ) {
    int[] rotated = this.gdir.rotateXYZAt(this.conv, parX, parY, parZ, this.centerX, this.centerY, this.centerZ);
    parX = rotated[0]; parY = rotated[1]; parZ = rotated[2];
    
    return this.wrapped.getTileEntity(parX, parY, parZ);
  }
  
  public boolean isAirBlock(int parX, int parY, int parZ) {
    int[] rotated = this.gdir.rotateXYZAt(this.conv, parX, parY, parZ, this.centerX, this.centerY, this.centerZ);
    parX = rotated[0]; parY = rotated[1]; parZ = rotated[2];
    
    return this.wrapped.isAirBlock(parX, parY, parZ);
  }
  
  public boolean blockExists(int parX, int parY, int parZ) {
    int[] rotated = this.gdir.rotateXYZAt(this.conv, parX, parY, parZ, this.centerX, this.centerY, this.centerZ);
    parX = rotated[0]; parY = rotated[1]; parZ = rotated[2];
    
    return this.wrapped.blockExists(parX, parY, parZ);
  }
  
  public int getBlockMetadata(int parX, int parY, int parZ) {
    int[] rotated = this.gdir.rotateXYZAt(this.conv, parX, parY, parZ, this.centerX, this.centerY, this.centerZ);
    parX = rotated[0]; parY = rotated[1]; parZ = rotated[2];
    
    return this.wrapped.getBlockMetadata(parX, parY, parZ);
  }
  
  public int getBlockLightOpacity(int parX, int parY, int parZ) {
    int[] rotated = this.gdir.rotateXYZAt(this.conv, parX, parY, parZ, this.centerX, this.centerY, this.centerZ);
    parX = rotated[0]; parY = rotated[1]; parZ = rotated[2];
    
    return this.wrapped.getBlockLightOpacity(parX, parY, parZ);
  }
  
  public boolean isSideSolid(int parX, int parY, int parZ, ForgeDirection side) {
    int[] rotated = this.gdir.rotateXYZAt(this.conv, parX, parY, parZ, this.centerX, this.centerY, this.centerZ);
    parX = rotated[0]; parY = rotated[1]; parZ = rotated[2];

    side = ForgeDirection.values()[this.gdir.forgeSideRot[side.ordinal()]];
    
    return this.wrapped.isSideSolid(parX, parY, parZ, side);
  }
  
  public boolean isSideSolid(int parX, int parY, int parZ, ForgeDirection side, boolean _default) {
    int[] rotated = this.gdir.rotateXYZAt(this.conv, parX, parY, parZ, this.centerX, this.centerY, this.centerZ);
    parX = rotated[0]; parY = rotated[1]; parZ = rotated[2];

    side = ForgeDirection.values()[this.gdir.forgeSideRot[side.ordinal()]];
    
    return this.wrapped.isSideSolid(parX, parY, parZ, side, _default);
  }
  
  public boolean isBlockFreezable(int parX, int parY, int parZ) {
    int[] rotated = this.gdir.rotateXYZAt(this.conv, parX, parY, parZ, this.centerX, this.centerY, this.centerZ);
    parX = rotated[0]; parY = rotated[1]; parZ = rotated[2];
    
    return this.wrapped.isBlockFreezable(parX, parY, parZ);
  }
  
  public boolean isBlockFreezableNaturally(int parX, int parY, int parZ) {
    int[] rotated = this.gdir.rotateXYZAt(this.conv, parX, parY, parZ, this.centerX, this.centerY, this.centerZ);
    parX = rotated[0]; parY = rotated[1]; parZ = rotated[2];
    
    return this.wrapped.isBlockFreezableNaturally(parX, parY, parZ);
  }
  
  public boolean canBlockFreeze(int parX, int parY, int parZ, boolean p_72834_4_) {
    int[] rotated = this.gdir.rotateXYZAt(this.conv, parX, parY, parZ, this.centerX, this.centerY, this.centerZ);
    parX = rotated[0]; parY = rotated[1]; parZ = rotated[2];
    
    return this.wrapped.canBlockFreeze(parX, parY, parZ, p_72834_4_);
  }
  
  public boolean canBlockFreezeBody(int parX, int parY, int parZ, boolean p_72834_4_) {
    int[] rotated = this.gdir.rotateXYZAt(this.conv, parX, parY, parZ, this.centerX, this.centerY, this.centerZ);
    parX = rotated[0]; parY = rotated[1]; parZ = rotated[2];
    
    return this.wrapped.canBlockFreezeBody(parX, parY, parZ, p_72834_4_);
  }
  
  public boolean canSnowAt(int parX, int parY, int parZ, boolean p_147478_4_) {
    int[] rotated = this.gdir.rotateXYZAt(this.conv, parX, parY, parZ, this.centerX, this.centerY, this.centerZ);
    parX = rotated[0]; parY = rotated[1]; parZ = rotated[2];
    
    return this.wrapped.canSnowAt(parX, parY, parZ, p_147478_4_);
  }
  
  public boolean canSnowAtBody(int parX, int parY, int parZ, boolean p_147478_4_) {
    int[] rotated = this.gdir.rotateXYZAt(this.conv, parX, parY, parZ, this.centerX, this.centerY, this.centerZ);
    parX = rotated[0]; parY = rotated[1]; parZ = rotated[2];
    
    return this.wrapped.canSnowAtBody(parX, parY, parZ, p_147478_4_);
  }
  
  public boolean isRainingAt(int parX, int parY, int parZ) {
    int[] rotated = this.gdir.rotateXYZAt(this.conv, parX, parY, parZ, this.centerX, this.centerY, this.centerZ);
    parX = rotated[0]; parY = rotated[1]; parZ = rotated[2];
    
    return this.wrapped.isRainingAt(parX, parY, parZ);
  }
  
  public boolean isBlockHighHumidity(int parX, int parY, int parZ) {
    int[] rotated = this.gdir.rotateXYZAt(this.conv, parX, parY, parZ, this.centerX, this.centerY, this.centerZ);
    parX = rotated[0]; parY = rotated[1]; parZ = rotated[2];
    
    return this.wrapped.isBlockHighHumidity(parX, parY, parZ);
  }
  
  public boolean canMineBlock(EntityPlayer p_72962_1_, int parX, int parY, int parZ) {
    int[] rotated = this.gdir.rotateXYZAt(this.conv, parX, parY, parZ, this.centerX, this.centerY, this.centerZ);
    parX = rotated[0]; parY = rotated[1]; parZ = rotated[2];
    
    return this.wrapped.canMineBlock(p_72962_1_, parX, parY, parZ);
  }
  
  public boolean canMineBlockBody(EntityPlayer par1EntityPlayer, int parX, int parY, int parZ) {
    int[] rotated = this.gdir.rotateXYZAt(this.conv, parX, parY, parZ, this.centerX, this.centerY, this.centerZ);
    parX = rotated[0]; parY = rotated[1]; parZ = rotated[2];
    
    return this.wrapped.canMineBlockBody(par1EntityPlayer, parX, parY, parZ);
  }

  public boolean isBlockTickScheduledThisTick(int parX, int parY, int parZ, Block p_147477_4_) {
    int[] rotated = this.gdir.rotateXYZAt(this.conv, parX, parY, parZ, this.centerX, this.centerY, this.centerZ);
    parX = rotated[0]; parY = rotated[1]; parZ = rotated[2];
    
    return this.wrapped.isBlockTickScheduledThisTick(parX, parY, parZ, p_147477_4_);
  }
  
  public boolean canBlockSeeTheSky(int parX, int parY, int parZ) {
    int[] rotated = this.gdir.rotateXYZAt(this.conv, parX, parY, parZ, this.centerX, this.centerY, this.centerZ);
    parX = rotated[0]; parY = rotated[1]; parZ = rotated[2];
    
    return this.wrapped.canBlockSeeTheSky(parX, parY, parZ);
  }
  
  public int getFullBlockLightValue(int parX, int parY, int parZ) {
    int[] rotated = this.gdir.rotateXYZAt(this.conv, parX, parY, parZ, this.centerX, this.centerY, this.centerZ);
    parX = rotated[0]; parY = rotated[1]; parZ = rotated[2];
    
    return this.wrapped.getFullBlockLightValue(parX, parY, parZ);
  }
  
  public int getBlockLightValue(int parX, int parY, int parZ) {
    int[] rotated = this.gdir.rotateXYZAt(this.conv, parX, parY, parZ, this.centerX, this.centerY, this.centerZ);
    parX = rotated[0]; parY = rotated[1]; parZ = rotated[2];
    
    return this.wrapped.getBlockLightValue(parX, parY, parZ);
  }
  
  public int getBlockLightValue_do(int parX, int parY, int parZ, boolean p_72849_4_) {
    int[] rotated = this.gdir.rotateXYZAt(this.conv, parX, parY, parZ, this.centerX, this.centerY, this.centerZ);
    parX = rotated[0]; parY = rotated[1]; parZ = rotated[2];
    
    return this.wrapped.getBlockLightValue_do(parX, parY, parZ, p_72849_4_);
  }
  
  public int getSkyBlockTypeBrightness(EnumSkyBlock p_72925_1_, int parX, int parY, int parZ) {
    int[] rotated = this.gdir.rotateXYZAt(this.conv, parX, parY, parZ, this.centerX, this.centerY, this.centerZ);
    parX = rotated[0]; parY = rotated[1]; parZ = rotated[2];
    
    return this.wrapped.getSkyBlockTypeBrightness(p_72925_1_, parX, parY, parZ);
  }
  
  public int getSavedLightValue(EnumSkyBlock p_72972_1_, int parX, int parY, int parZ) {
    int[] rotated = this.gdir.rotateXYZAt(this.conv, parX, parY, parZ, this.centerX, this.centerY, this.centerZ);
    parX = rotated[0]; parY = rotated[1]; parZ = rotated[2];
    
    return this.wrapped.getSavedLightValue(p_72972_1_, parX, parY, parZ);
  }
  
  public int getLightBrightnessForSkyBlocks(int parX, int parY, int parZ, int p_72802_4_) {
    int[] rotated = this.gdir.rotateXYZAt(this.conv, parX, parY, parZ, this.centerX, this.centerY, this.centerZ);
    parX = rotated[0]; parY = rotated[1]; parZ = rotated[2];
    
    return this.wrapped.getLightBrightnessForSkyBlocks(parX, parY, parZ, p_72802_4_);
  }
  
  public float getLightBrightness(int parX, int parY, int parZ) {
    int[] rotated = this.gdir.rotateXYZAt(this.conv, parX, parY, parZ, this.centerX, this.centerY, this.centerZ);
    parX = rotated[0]; parY = rotated[1]; parZ = rotated[2];
    
    return this.wrapped.getLightBrightness(parX, parY, parZ);
  }
  
  public boolean isBlockNormalCubeDefault(int parX, int parY, int parZ, boolean p_147445_4_) {
    int[] rotated = this.gdir.rotateXYZAt(this.conv, parX, parY, parZ, this.centerX, this.centerY, this.centerZ);
    parX = rotated[0]; parY = rotated[1]; parZ = rotated[2];
    
    return this.wrapped.isBlockNormalCubeDefault(parX, parY, parZ, p_147445_4_);
  }

  public Block getTopBlock(int parX, int parZ) {
    return this.wrapped.getTopBlock(parX, parZ);
  }
  
  public int getHeightValue(int parX, int parZ) {
    return this.wrapped.getHeightValue(parX, parZ);
  }
  
  public int getChunkHeightMapMinimum(int parX, int parZ) {
    return this.wrapped.getChunkHeightMapMinimum(parX, parZ);
  }

  public int isBlockProvidingPowerTo(int p_72879_1_, int p_72879_2_, int p_72879_3_, int p_72879_4_) {
    return this.wrapped.isBlockProvidingPowerTo(p_72879_1_, p_72879_2_, p_72879_3_, p_72879_4_);
  }
  
  public int getBlockPowerInput(int p_94577_1_, int p_94577_2_, int p_94577_3_) {
    return this.wrapped.getBlockPowerInput(p_94577_1_, p_94577_2_, p_94577_3_);
  }
  
  public boolean getIndirectPowerOutput(int p_94574_1_, int p_94574_2_, int p_94574_3_, int p_94574_4_) {
    return this.wrapped.getIndirectPowerOutput(p_94574_1_, p_94574_2_, p_94574_3_, p_94574_4_);
  }
  
  public int getIndirectPowerLevelTo(int p_72878_1_, int p_72878_2_, int p_72878_3_, int p_72878_4_) {
    return this.wrapped.getIndirectPowerLevelTo(p_72878_1_, p_72878_2_, p_72878_3_, p_72878_4_);
  }
  
  public boolean isBlockIndirectlyGettingPowered(int p_72864_1_, int p_72864_2_, int p_72864_3_) {
    return this.wrapped.isBlockIndirectlyGettingPowered(p_72864_1_, p_72864_2_, p_72864_3_);
  }

  public int getStrongestIndirectPower(int p_94572_1_, int p_94572_2_, int p_94572_3_) {
    return this.wrapped.getStrongestIndirectPower(p_94572_1_, p_94572_2_, p_94572_3_);
  }
}
