package jp.mc.ancientred.starminer.basics.dimention;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.mc.ancientred.starminer.api.IZeroGravityWorldProvider;
import jp.mc.ancientred.starminer.basics.SMModContainer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;

public class WorldProviderSpace
  extends WorldProvider
  implements IZeroGravityWorldProvider
{
  public static int cloudTickCounter = 0;

  public void registerWorldChunkManager() {
    this.worldChunkMgr = new WorldChunkManagerSpace(BiomeGenBase.sky, 0.5F, 0.0F);
    
    this.hasNoSky = false;
  }

  public String getDimensionName() {
    return "GeostationaryOrbit";
  }

  public IChunkProvider createChunkGenerator() {
    return new ChunkProviderSpace(this.worldObj, this.worldObj.getSeed());
  }

  public boolean canRespawnHere() {
    return true;
  }

  public boolean isSurfaceWorld() {
    return true;
  }

  public double getHorizon() {
    return 100.0D;
  }
  
  @SideOnly(Side.CLIENT)
  public boolean doesXZShowFog(int par1, int par2) {
    return false;
  }
  
  public void updateWeather() {
    super.updateWeather();
    
    if (this.worldObj.isRemote) {
      
      cloudTickCounter++;

      IChunkProvider chunkProvider = this.worldObj.getChunkProvider();
      if (chunkProvider instanceof net.minecraft.client.multiplayer.ChunkProviderClient) {
        SMModContainer.proxy.canselLightGapUpdate(chunkProvider);
      }
    } 
  }
  
  @SideOnly(Side.CLIENT)
  public Vec3 drawClouds(float partialTicks) {
    float f1 = this.worldObj.getCelestialAngle(partialTicks);
    float f2 = MathHelper.cos(f1 * 3.1415927F * 2.0F) * 2.0F + 0.5F;
    
    if (f2 < 0.0F)
    {
      f2 = 0.0F;
    }
    
    if (f2 > 1.0F)
    {
      f2 = 1.0F;
    }
    
    float f3 = 1.0F;
    float f4 = 1.0F;
    float f5 = 1.0F;
    float f6 = 1.0F;
    
    f3 *= f2 * 0.9F + 0.1F;
    f4 *= f2 * 0.9F + 0.1F;
    f5 *= f2 * 0.85F + 0.15F;
    
    return Vec3.createVectorHelper(f3, f4, f5);
  }
}
