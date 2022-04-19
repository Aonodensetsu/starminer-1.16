package jp.mc.ancientred.starminer.basics.block.gravitized;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.mc.ancientred.starminer.basics.SMModContainer;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;

public class BlockCarrotGravitized
  extends BlockCropsGravitized
{
  protected Item getSeed() {
    return SMModContainer.CarrotGravizedItem;
  }

  protected Item getCrop() {
    return SMModContainer.CarrotGravizedItem;
  }

  @SideOnly(Side.CLIENT)
  public IIcon getIcon(int par1, int par2) {
    return Blocks.carrots.getIcon(par1, par2);
  }
  
  @SideOnly(Side.CLIENT)
  public void registerIcons(IIconRegister par1IconRegister) {}
}
