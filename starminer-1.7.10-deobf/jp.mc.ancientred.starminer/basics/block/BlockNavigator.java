package jp.mc.ancientred.starminer.basics.block;

import jp.mc.ancientred.starminer.basics.tileentity.TileEntityNavigator;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockNavigator
  extends Block
  implements ITileEntityProvider
{
  public BlockNavigator() {
    super(Material.rock);
    setHardness(1.0F);
    setResistance(1.0F);
    setLightLevel(1.0F);
    
    this.minX = 0.1875D;
    this.minY = 0.1875D;
    this.minZ = 0.1875D;
    this.maxX = 0.8125D;
    this.maxY = 0.8125D;
    this.maxZ = 0.8125D;
  }
  
  public TileEntity createNewTileEntity(World par1World, int metadata) {
    return (TileEntity)new TileEntityNavigator();
  }
  public boolean isOn(int blockMetadata) {
    return ((blockMetadata & 0x1) != 0);
  }

  public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, Block par5Block) {
    int meta = par1World.getBlockMetadata(par2, par3, par4);
    boolean isPowerOn = ((meta & 0x1) != 0);
    boolean isHavingPower = par1World.isBlockIndirectlyGettingPowered(par2, par3, par4);
    
    if (isPowerOn != isHavingPower) {
      par1World.setBlockMetadataWithNotify(par2, par3, par4, isHavingPower ? 1 : 0, 2);
      par1World.markBlockRangeForRenderUpdate(par2, par3, par4, par2, par3, par4);
    } 
  }

  public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
    if (!par1World.isRemote) {
      
      if (!doesPlayerHasTorchOnHand(par5EntityPlayer)) return false;

      
      TileEntity te = par1World.getTileEntity(par2, par3, par4);
      if (!(te instanceof TileEntityNavigator)) return false; 
      TileEntityNavigator teNavi = (TileEntityNavigator)te;
      teNavi.activate();
      par1World.markBlockForUpdate(par2, par3, par4);
    } 
    return true;
  }

  public boolean renderAsNormalBlock() {
    return false;
  }
  
  public boolean isOpaqueCube() {
    return false;
  }
  
  public int getRenderType() {
    return 4341807;
  }
  public static final boolean doesPlayerHasTorchOnHand(EntityPlayer parEntityPlayer) {
    ItemStack itemNow = parEntityPlayer.getCurrentEquippedItem();
    return (itemNow != null && itemNow.getItem() == Item.getItemFromBlock(Blocks.torch));
  }
}
