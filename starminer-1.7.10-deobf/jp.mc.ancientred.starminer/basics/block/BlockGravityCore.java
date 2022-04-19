package jp.mc.ancientred.starminer.basics.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Random;
import jp.mc.ancientred.starminer.basics.SMModContainer;
import jp.mc.ancientred.starminer.basics.tileentity.TileEntityGravityGenerator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockGravityCore
  extends BlockContainer
  implements ITileEntityProvider
{
private final Random random = new Random();
  
  @SideOnly(Side.CLIENT)
  protected IIcon normalIcon;
  @SideOnly(Side.CLIENT)
  protected IIcon itemIcon;
  
  public BlockGravityCore() {
    super(Material.rock);
    setHardness(1.0F);
  }
  
  public TileEntity createNewTileEntity(World par1World, int metadata) {
     return (TileEntity)new TileEntityGravityGenerator();
  }
  
  public boolean renderAsNormalBlock() {
     return false;
  }
  
  public boolean isOpaqueCube() {
     return false;
  }
  
  public int getRenderType() {
     return 4341803;
  }

  public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
     if (!par1World.isRemote)
    {
       par5EntityPlayer.openGui(SMModContainer.instance, SMModContainer.guiStarCoreId, par1World, par2, par3, par4);
    }
     return true;
  }
  
  @SideOnly(Side.CLIENT)
  public void registerIcons(IIconRegister par1IconRegister) {
     this.normalIcon = par1IconRegister.registerIcon("starminer:g_core");
     this.itemIcon = par1IconRegister.registerIcon("starminer:g_coreItem");
  }
  
  @SideOnly(Side.CLIENT)
  public IIcon getIcon(int par1, int par2) {
     return this.normalIcon;
  }
  
  @SideOnly(Side.CLIENT)
  public IIcon getCoreItemIcon() {
     return this.itemIcon;
  }

  public void breakBlock(World par1World, int par2, int par3, int par4, Block par5, int par6) {
     TileEntityGravityGenerator tileentitychest = (TileEntityGravityGenerator)par1World.getTileEntity(par2, par3, par4);
    
     if (tileentitychest != null) {
      
       for (int j1 = 0; j1 < tileentitychest.getSizeInventory(); j1++) {
        
         ItemStack itemstack = tileentitychest.getStackInSlot(j1);
        
         if (itemstack != null) {
          
           float f = this.random.nextFloat() * 0.8F + 0.1F;
           float f1 = this.random.nextFloat() * 0.8F + 0.1F;

           for (float f2 = this.random.nextFloat() * 0.8F + 0.1F; itemstack.stackSize > 0; par1World.spawnEntityInWorld((Entity)entityitem)) {
            
             int k1 = this.random.nextInt(21) + 10;
            
             if (k1 > itemstack.stackSize)
            {
               k1 = itemstack.stackSize;
            }
            
             itemstack.stackSize -= k1;
             EntityItem entityitem = new EntityItem(par1World, (par2 + f), (par3 + f1), (par4 + f2), new ItemStack(itemstack.getItem(), k1, itemstack.getMetadata()));
             float f3 = 0.05F;
             entityitem.motionX = ((float)this.random.nextGaussian() * f3);
             entityitem.motionY = ((float)this.random.nextGaussian() * f3 + 0.2F);
             entityitem.motionZ = ((float)this.random.nextGaussian() * f3);
            
             if (itemstack.hasTagCompound())
            {
               entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
            }
          } 
        } 
      } 
      
       par1World.updateNeighborsAboutBlockChange(par2, par3, par4, par5);
    } 
    
     super.breakBlock(par1World, par2, par3, par4, par5, par6);
  }
}
