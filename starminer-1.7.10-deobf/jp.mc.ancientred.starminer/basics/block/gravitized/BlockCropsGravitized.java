package jp.mc.ancientred.starminer.basics.block.gravitized;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Random;
import jp.mc.ancientred.starminer.basics.SMModContainer;
import net.minecraft.block.IGrowable;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockCropsGravitized
  extends BlockBushGravitized
  implements IGrowable
{
  public IIcon starMarkIcon;
  public static final double HEIGHT_D = 0.25D;
  public static final float HEIGHT_F = 0.25F;
  
  public BlockCropsGravitized() {
    super(0.5D, 0.25D);
  }

  public int getRenderType() {
    return 4341800;
  }

  public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random) {
    checkAndDropBlock(par1World, par2, par3, par4);
    
    if (par1World.getBlockLightValue(par2, par3, par4) >= 7) {
      
      int l = par1World.getBlockMetadata(par2, par3, par4);
      
      if (l < 7) {
        
        float f = 3.0F;
        
        if (par5Random.nextInt((int)(25.0F / f) + 1) == 0) {
          
          l++;
          par1World.setBlockMetadataWithNotify(par2, par3, par4, l, 2);
        } 
      } 
    } 
  }

  public void dropBlockAsItemWithChance(World par1World, int par2, int par3, int par4, int par5, float par6, int par7) {
    super.dropBlockAsItemWithChance(par1World, par2, par3, par4, par5, par6, 0);
  }

  public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
    ArrayList<ItemStack> ret = super.getDrops(world, x, y, z, metadata, fortune);
    
    if (metadata >= 7)
    {
      for (int n = 0; n < 3 + fortune; n++) {
        
        if (world.rand.nextInt(15) <= metadata)
        {
          ret.add(new ItemStack(getSeed(), 1, 0));
        }
      } 
    }
    
    return ret;
  }

  public Item getItemDropped(int par1, Random par2Random, int par3) {
    return (par1 == 7) ? getCrop() : getSeed();
  }

  public int quantityDropped(Random par1Random) {
    return 1;
  }

  public void fertilize(World par1World, int par2, int par3, int par4) {
    int l = par1World.getBlockMetadata(par2, par3, par4) + MathHelper.getRandomIntegerInRange(par1World.rand, 2, 5);
    
    if (l > 7)
    {
      l = 7;
    }
    
    par1World.setBlockMetadataWithNotify(par2, par3, par4, l, 2);
  }

  protected Item getSeed() {
    return SMModContainer.SeedGravizedItem;
  }

  protected Item getCrop() {
    return Items.wheat;
  }

  public boolean canFertilize(World parWorld, int parX, int parY, int parZ, boolean p_149851_5_) {
    return true;
  }

  public boolean shouldFertilize(World parWorld, Random parRand, int parX, int parY, int parZ) {
    return (parWorld.getBlockMetadata(parX, parY, parZ) != 7);
  }
  
  public void fertilize(World parWorld, Random parRand, int parX, int parY, int parZ) {
    fertilize(parWorld, parX, parY, parZ);
  }

  @SideOnly(Side.CLIENT)
  public Item getItem(World par1World, int par2, int par3, int par4) {
    return getSeed();
  }

  @SideOnly(Side.CLIENT)
  public IIcon getIcon(int par1, int par2) {
    return Blocks.wheat.getIcon(par1, par2);
  }

  @SideOnly(Side.CLIENT)
  public void registerIcons(IIconRegister par1IconRegister) {
    this.starMarkIcon = par1IconRegister.registerIcon("starminer:starmark");
  }
}
