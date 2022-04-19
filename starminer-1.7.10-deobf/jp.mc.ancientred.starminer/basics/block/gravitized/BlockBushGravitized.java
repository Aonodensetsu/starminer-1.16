package jp.mc.ancientred.starminer.basics.block.gravitized;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.mc.ancientred.starminer.basics.SMModContainer;
import jp.mc.ancientred.starminer.basics.block.DirectionConst;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockBushGravitized
  extends BlockBush
  implements IGravitizedPlants
{
  public double W_D = 0.2D;
  public float W_F = 0.2F;
  
  public double HEIGHT_D = 0.6000000000000001D;
  public float HEIGHT_F = 0.6F;

  public BlockBushGravitized(Material par2Material, double wd, double heightD) {
    this.W_D = wd;
    this.W_F = (float)wd;
    this.HEIGHT_D = heightD;
    this.HEIGHT_F = (float)heightD;
    
    setHardness(0.0F);
    setStepSound(Block.soundTypeGrass);
  }

  protected BlockBushGravitized(double wd, double heightD) {
    this(Material.plants, wd, heightD);
  }

  public BlockBushGravitized() {
    this(0.2D, 0.6000000000000001D);
  }
  
  public boolean allowPlantOn(Block block, int meta) {
    return (Blocks.dirt == block || Blocks.grass == block || Blocks.farmland == block || SMModContainer.DirtGrassExBlock == block);
  }

  public int getRenderType() {
    return 4341801;
  }

  public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4) {
    Block block = par1World.getBlock(par2, par3, par4);
    return (block == null || block.isReplaceable((IBlockAccess)par1World, par2, par3, par4));
  }

  public boolean canBlockStay(World par1World, int par2, int par3, int par4) {
    int meta = par1World.getBlockMetadata(par2, par3, par4); int dir;
    if ((dir = DirectionConst.getPlantGravityDirection((IBlockAccess)par1World, par2, par3, par4)) == -1) {
      return DirectionConst.tryStayable(par1World, par2, par3, par4, meta, this);
    }
    
    if (!DirectionConst.isStayableAtOppositeSide(par1World, par2, par3, par4, dir, meta, this)) {
      return DirectionConst.tryStayable(par1World, par2, par3, par4, meta, this);
    }
    return true;
  }
  
  private boolean tryGravitizeCorrectly() {
    return false;
  }
  
  public int onBlockPlaced(World par1World, int x, int y, int z, int side, float par6, float par7, float par8, int par9) {
    int dir = 0;
    int gAirX = x;
    int gAirY = y;
    int gAirZ = z;
    switch (side) {
      case 0:
        dir = 1;
        gAirY--;
        break;
      case 1:
        dir = 0;
        gAirY++;
        break;
      case 2:
        dir = 5;
        gAirZ--;
        break;
      case 3:
        dir = 4;
        gAirZ++;
        break;
      case 4:
        dir = 3;
        gAirX--;
        break;
      case 5:
        dir = 2;
        gAirX++;
        break;
    } 
    
    if (par1World.isAirBlock(gAirX, gAirY, gAirZ)) {
      DirectionConst.setDummyAirBlockWithMeta(par1World, gAirX, gAirY, gAirZ, dir + 1, 3);
    }
    return super.onBlockPlaced(par1World, x, y, z, side, par6, par7, par8, par9);
  }

  
  @SideOnly(Side.CLIENT)
  public AxisAlignedBB getSelectedBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
    int dir;
    if ((dir = DirectionConst.getPlantGravityDirection((IBlockAccess)par1World, par2, par3, par4)) == -1) {
      dir = 0;
    }
    double f = this.W_D;
    double lw = this.HEIGHT_D;
    double hi = 1.0D - this.HEIGHT_D;
    switch (dir) {
      case 1:
        return AxisAlignedBB.getBoundingBox(par2 + 0.5D - f, par3 + hi, par4 + 0.5D - f, par2 + 0.5D + f, par3 + 1.0D, par4 + 0.5D + f);

      case 0:
        return AxisAlignedBB.getBoundingBox(par2 + 0.5D - f, par3 + 0.0D, par4 + 0.5D - f, par2 + 0.5D + f, par3 + lw, par4 + 0.5D + f);

      case 3:
        return AxisAlignedBB.getBoundingBox(par2 + hi, par3 + 0.5D - f, par4 + 0.5D - f, par2 + 1.0D, par3 + 0.5D + f, par4 + 0.5D + f);

      case 2:
        return AxisAlignedBB.getBoundingBox(par2 + 0.0D, par3 + 0.5D - f, par4 + 0.5D - f, par2 + lw, par3 + 0.5D + f, par4 + 0.5D + f);

      case 5:
        return AxisAlignedBB.getBoundingBox(par2 + 0.5D - f, par3 + 0.5D - f, par4 + hi, par2 + 0.5D + f, par3 + 0.5D + f, par4 + 1.0D);

      case 4:
        return AxisAlignedBB.getBoundingBox(par2 + 0.5D - f, par3 + 0.5D - f, par4 + 0.0D, par2 + 0.5D + f, par3 + 0.5D + f, par4 + lw);
    } 

    return AxisAlignedBB.getBoundingBox(par2 + 0.0D, par3 + 0.0D, par4 + 0.0D, par2 + 1.0D, par3 + 1.0D, par4 + 1.0D);
  }

  public void setBlockBoundsBasedOnState(IBlockAccess par1World, int par2, int par3, int par4) {
    int dir;
    if ((dir = DirectionConst.getPlantGravityDirection(par1World, par2, par3, par4)) == -1) {
      dir = 0;
    }
    setBoundBasedOnAirMeataAbove(dir);
  }
  
  private void setBoundBasedOnAirMeataAbove(int dir) {
    float f = this.W_F;
    float lw = this.HEIGHT_F;
    float hi = 1.0F - this.HEIGHT_F;
    
    switch (dir) {
      case 1:
        setBlockBounds(0.5F - f, hi, 0.5F - f, 0.5F + f, 1.0F, 0.5F + f);
        break;
      case 0:
        setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, lw, 0.5F + f);
        break;
      case 3:
        setBlockBounds(hi, 0.5F - f, 0.5F - f, 1.0F, 0.5F + f, 0.5F + f);
        break;
      case 2:
        setBlockBounds(0.0F, 0.5F - f, 0.5F - f, lw, 0.5F + f, 0.5F + f);
        break;
      case 5:
        setBlockBounds(0.5F - f, 0.5F - f, hi, 0.5F + f, 0.5F + f, 1.0F);
        break;
      case 4:
        setBlockBounds(0.5F - f, 0.5F - f, 0.0F, 0.5F + f, 0.5F + f, lw);
        break;
    } 
  }
}
