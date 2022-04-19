package jp.mc.ancientred.starminer.basics.block.gravitized;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import java.util.Random;
import jp.mc.ancientred.starminer.basics.block.DirectionConst;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.event.terraingen.TerrainGen;

public class BlockSaplingGravitized
  extends BlockBushGravitized
  implements IGrowable
{
  public BlockSaplingGravitized() {
    super(0.4D, 0.8D);
    setHardness(0.0F);
    setStepSound(soundTypeGrass);
  }

  public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random) {
    if (!par1World.isRemote) {
      
      super.updateTick(par1World, par2, par3, par4, par5Random);
      
      if (par1World.getBlockLightValue(par2, par3, par4) >= 7 && par5Random.nextInt(7) == 0)
      {
        markOrGrowMarked(par1World, par2, par3, par4, par5Random);
      }
    } 
  }

  public void markOrGrowMarked(World par1World, int par2, int par3, int par4, Random par5Random) {
    int l = par1World.getBlockMetadata(par2, par3, par4);
    
    if ((l & 0x8) == 0) {
      
      par1World.setBlockMetadataWithNotify(par2, par3, par4, l | 0x8, 4);
    }
    else {
      
      growTree(par1World, par2, par3, par4, par5Random);
    } 
  }
  
  public void growTree(World parWorld, int parX, int parY, int parZ, Random parRand) {
    if (!TerrainGen.saplingGrowTree(parWorld, parRand, parX, parY, parZ))
      return; 
    int dir;
    if ((dir = DirectionConst.getPlantGravityDirection((IBlockAccess)parWorld, parX, parY, parZ)) == -1) {
      return;
    }
    
    int meta = parWorld.getBlockMetadata(parX, parY, parZ) & 0x7;
    Object treeGen = null;
    int xRel = 0;
    int zRel = 0;
    boolean willGrowHugeTree = false;

    int multipOrAddTreeHight = (parWorld.provider instanceof jp.mc.ancientred.starminer.api.IZeroGravityWorldProvider) ? 2 : 1;
    
    switch (meta) {

      default:
        treeGen = new WorldGenTreesG(true, 4 * multipOrAddTreeHight, 0, 0, false, dir);
        break;

      case 1:
        if (!willGrowHugeTree) {
          
          zRel = 0;
          xRel = 0;
          treeGen = new WorldGenTaiga2G(true, 6 + multipOrAddTreeHight, dir);
        } 
        break;
      
      case 2:
        treeGen = new WorldGenForestG(true, 5 + multipOrAddTreeHight, dir);
        break;

      case 3:
        if (!willGrowHugeTree) {
          
          zRel = 0;
          xRel = 0;
          treeGen = new WorldGenTreesG(true, 4 * multipOrAddTreeHight + parRand.nextInt(7), 3, 3, false, dir);
        } 
        break;
      
      case 4:
        treeGen = new WorldGenSavannaTreeG(true, 5 + multipOrAddTreeHight, dir);
        break;

      case 5:
        if (!willGrowHugeTree) {

          zRel = 0;
          xRel = 0;
          treeGen = new WorldGenTreesG(true, 4 * multipOrAddTreeHight + parRand.nextInt(7), 1, 1, false, dir, true);
        } 
        break;
    } 
    Block block = Blocks.air;
    
    if (willGrowHugeTree) {
      
      parWorld.setBlock(parX + xRel, parY, parZ + zRel, block, 0, 4);
      parWorld.setBlock(parX + xRel + 1, parY, parZ + zRel, block, 0, 4);
      parWorld.setBlock(parX + xRel, parY, parZ + zRel + 1, block, 0, 4);
      parWorld.setBlock(parX + xRel + 1, parY, parZ + zRel + 1, block, 0, 4);
    }
    else {
      
      parWorld.setBlock(parX, parY, parZ, block, 0, 4);
    } 
    
    if (!((WorldGenerator)treeGen).generate(parWorld, parRand, parX + xRel, parY, parZ + zRel))
    {
      if (willGrowHugeTree) {
        
        parWorld.setBlock(parX + xRel, parY, parZ + zRel, (Block)this, meta, 4);
        parWorld.setBlock(parX + xRel + 1, parY, parZ + zRel, (Block)this, meta, 4);
        parWorld.setBlock(parX + xRel, parY, parZ + zRel + 1, (Block)this, meta, 4);
        parWorld.setBlock(parX + xRel + 1, parY, parZ + zRel + 1, (Block)this, meta, 4);
      }
      else {
        
        parWorld.setBlock(parX, parY, parZ, (Block)this, meta, 4);
      } 
    }
  }
  
  public boolean func_149880_a(World parWorld, int parX, int parY, int parZ, int parMeta) {
    return (parWorld.getBlock(parX, parY, parZ) == this && (parWorld.getBlockMetadata(parX, parY, parZ) & 0x7) == parMeta);
  }

  public int damageDropped(int p_149692_1_) {
    return MathHelper.clamp_int(p_149692_1_ & 0x7, 0, 5);
  }

  public boolean canFertilize(World parWorld, int parX, int parY, int parZ, boolean p_149851_5_) {
    return true;
  }

  public boolean shouldFertilize(World parWorld, Random parRand, int parX, int parY, int parZ) {
    return (parWorld.rand.nextFloat() < 0.45D);
  }
  
  public void fertilize(World parWorld, Random parRand, int parX, int parY, int parZ) {
    markOrGrowMarked(parWorld, parX, parY, parZ, parRand);
  }

  @SideOnly(Side.CLIENT)
  public void getSubBlocks(Item p_149666_1_, CreativeTabs p_149666_2_, List<ItemStack> p_149666_3_) {
    p_149666_3_.add(new ItemStack(p_149666_1_, 1, 0));
    p_149666_3_.add(new ItemStack(p_149666_1_, 1, 1));
    p_149666_3_.add(new ItemStack(p_149666_1_, 1, 2));
    p_149666_3_.add(new ItemStack(p_149666_1_, 1, 3));
    p_149666_3_.add(new ItemStack(p_149666_1_, 1, 4));
    p_149666_3_.add(new ItemStack(p_149666_1_, 1, 5));
  }

  @SideOnly(Side.CLIENT)
  public IIcon getIcon(int par1, int par2) {
    return Blocks.sapling.getIcon(par1, par2);
  }
  
  @SideOnly(Side.CLIENT)
  public void registerIcons(IIconRegister par1IconRegister) {}
}
