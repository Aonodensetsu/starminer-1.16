package jp.mc.ancientred.starminer.basics.dummy;

import jp.mc.ancientred.starminer.api.GravityDirection;
import jp.mc.ancientred.starminer.basics.tileentity.TileEntityBlockRotator;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.util.ForgeDirection;

public class DummyRotatedBlockAccess
  implements IBlockAccess
{
  private IBlockAccess wrappedAccess;
  private GravityDirection gdir;
  private int centerX;
  private int centerY;
  private int centerZ;
  private int[] conv = new int[3];

  public static final DummyRotatedBlockAccess get() {
    return thHoldThis.get();
  }
  
  private static ThreadLocal<DummyRotatedBlockAccess> thHoldThis = new ThreadLocal<DummyRotatedBlockAccess>() {
      protected DummyRotatedBlockAccess initialValue() {
        return new DummyRotatedBlockAccess();
      }
    };
  
  public IBlockAccess wrapp(IBlockAccess world, GravityDirection gdir, int centerX, int centerY, int centerZ) {
    this.centerX = centerX;
    this.centerY = centerY;
    this.centerZ = centerZ;
    this.gdir = gdir;
    this.wrappedAccess = world;
    return this;
  }

  public Block getBlock(int parX, int parY, int parZ) {
    int[] rotated = this.gdir.rotateXYZAt(this.conv, parX, parY, parZ, this.centerX, this.centerY, this.centerZ);
    parX = rotated[0]; parY = rotated[1]; parZ = rotated[2];
    
    TileEntityBlockRotator te = getTileEntityBlockRotator(this.wrappedAccess, parX, parY, parZ);
    if (te != null) {
      Block storedBlock = te.getStoredBlock();
      if (storedBlock != null) return storedBlock; 
    } 
    return this.wrappedAccess.getBlock(parX, parY, parZ);
  }

  public TileEntity getTileEntity(int parX, int parY, int parZ) {
    int[] rotated = this.gdir.rotateXYZAt(this.conv, parX, parY, parZ, this.centerX, this.centerY, this.centerZ);
    parX = rotated[0]; parY = rotated[1]; parZ = rotated[2];
    
    return this.wrappedAccess.getTileEntity(parX, parY, parZ);
  }

  public int getLightBrightnessForSkyBlocks(int parX, int parY, int parZ, int p_72802_4_) {
    int[] rotated = this.gdir.rotateXYZAt(this.conv, parX, parY, parZ, this.centerX, this.centerY, this.centerZ);
    parX = rotated[0]; parY = rotated[1]; parZ = rotated[2];
    
    return this.wrappedAccess.getLightBrightnessForSkyBlocks(parX, parY, parZ, p_72802_4_);
  }

  public int getBlockMetadata(int parX, int parY, int parZ) {
    int[] rotated = this.gdir.rotateXYZAt(this.conv, parX, parY, parZ, this.centerX, this.centerY, this.centerZ);
    parX = rotated[0]; parY = rotated[1]; parZ = rotated[2];
    
    return this.wrappedAccess.getBlockMetadata(parX, parY, parZ);
  }

  public int isBlockProvidingPowerTo(int parX, int parY, int parZ, int direction) {
    int[] rotated = this.gdir.rotateXYZAt(this.conv, parX, parY, parZ, this.centerX, this.centerY, this.centerZ);
    parX = rotated[0]; parY = rotated[1]; parZ = rotated[2];
    
    return this.wrappedAccess.isBlockProvidingPowerTo(parX, parY, parZ, direction);
  }

  public boolean isAirBlock(int parX, int parY, int parZ) {
    int[] rotated = this.gdir.rotateXYZAt(this.conv, parX, parY, parZ, this.centerX, this.centerY, this.centerZ);
    parX = rotated[0]; parY = rotated[1]; parZ = rotated[2];
    
    return this.wrappedAccess.isAirBlock(parX, parY, parZ);
  }

  public BiomeGenBase getBiomeGenForCoords(int p_72807_1_, int p_72807_2_) {
    return this.wrappedAccess.getBiomeGenForCoords(p_72807_1_, p_72807_2_);
  }

  public int getHeight() {
    return this.wrappedAccess.getHeight();
  }

  public boolean extendedLevelsInChunkCache() {
    return this.wrappedAccess.extendedLevelsInChunkCache();
  }

  public boolean isSideSolid(int parX, int parY, int parZ, ForgeDirection side, boolean _default) {
    int[] rotated = this.gdir.rotateXYZAt(this.conv, parX, parY, parZ, this.centerX, this.centerY, this.centerZ);
    parX = rotated[0]; parY = rotated[1]; parZ = rotated[2];
    
    return this.wrappedAccess.isSideSolid(parX, parY, parZ, side, _default);
  }
  private TileEntityBlockRotator getTileEntityBlockRotator(IBlockAccess world, int par2, int par3, int par4) {
    TileEntity te = this.wrappedAccess.getTileEntity(par2, par3, par4);
    if (te instanceof TileEntityBlockRotator) {
      return (TileEntityBlockRotator)te;
    }
    return null;
  }
  
  private DummyRotatedBlockAccess() {}
}
