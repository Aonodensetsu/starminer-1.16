package jp.mc.ancientred.starminer.basics.item.block;

import jp.mc.ancientred.starminer.basics.SMModContainer;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemBlockForGWall
  extends ItemBlock
{
  public ItemBlockForGWall(Block block) {
    super(block);
  }

  public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10) {
    Block block = par3World.getBlock(par4, par5, par6);
    if (block == SMModContainer.GravityWallBlock) {
      return false;
    }
    return super.onItemUse(par1ItemStack, par2EntityPlayer, par3World, par4, par5, par6, par7, par8, par9, par10);
  }
}
