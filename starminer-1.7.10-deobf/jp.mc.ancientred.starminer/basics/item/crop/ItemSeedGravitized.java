package jp.mc.ancientred.starminer.basics.item.crop;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.mc.ancientred.starminer.basics.SMModContainer;
import jp.mc.ancientred.starminer.basics.block.DirectionConst;
import jp.mc.ancientred.starminer.basics.block.gravitized.IGravitizedPlants;
import jp.mc.ancientred.starminer.basics.item.ItemStarContoler;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class ItemSeedGravitized extends ItemSeeds {
  private Block blockTypePrivate;
  
  public ItemSeedGravitized(Block par1Block, Block par2Block) {
    super(par1Block, par2Block);
    this.blockTypePrivate = par1Block;
  }
  
  public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int x, int y, int z, int side, float xOffset, float yOffset, float zOffset) {
    int meta = getMetadata(par1ItemStack.getMetadata());
    if (this.blockTypePrivate instanceof IGravitizedPlants && 
      !((IGravitizedPlants)this.blockTypePrivate).allowPlantOn(par3World.getBlock(x, y, z), meta)) {
      return false;
    }
    
    int dir = 0;
    int tgtX = x;
    int tgtY = y;
    int tgtZ = z;
    int gAirX = x;
    int gAirY = y;
    int gAirZ = z;
    switch (side) {
      case 0:
        tgtY--;
        dir = 1;
        gAirY -= 2;
        break;
      case 1:
        dir = 0;
        tgtY++;
        gAirY += 2;
        break;
      case 2:
        dir = 5;
        tgtZ--;
        gAirZ -= 2;
        break;
      case 3:
        dir = 4;
        tgtZ++;
        gAirZ += 2;
        break;
      case 4:
        dir = 3;
        tgtX--;
        gAirX -= 2;
        break;
      case 5:
        dir = 2;
        tgtX++;
        gAirX += 2;
        break;
      default:
        return false;
    } 
    
    if (canEditAndAir(par1ItemStack, par2EntityPlayer, par3World, tgtX, tgtY, tgtZ, side) && canEditAndAir(par1ItemStack, par2EntityPlayer, par3World, gAirX, gAirY, gAirZ, side)) {
      
      par3World.setBlock(tgtX, tgtY, tgtZ, this.blockTypePrivate, meta, 3);
      DirectionConst.setDummyAirBlockWithMeta(par3World, gAirX, gAirY, gAirZ, dir + 1, 3);
      par1ItemStack.stackSize--;
      return true;
    } 
    return false;
  }
  public static final boolean canEditAndAir(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int x, int y, int z, int side) {
    return (par2EntityPlayer.canPlayerEdit(x, y, z, side, par1ItemStack) && par3World.isAirBlock(x, y, z));
  }

  @SideOnly(Side.CLIENT)
  public boolean requiresMultipleRenderPasses() {
    return true;
  }
  
  @SideOnly(Side.CLIENT)
  public int getRenderPasses(int metadata) {
    return 2;
  }
  
  @SideOnly(Side.CLIENT)
  public IIcon getIcon(ItemStack stack, int pass) {
    if (pass == 1) {
      return ((ItemStarContoler)SMModContainer.StarControlerItem).starMarkIcon;
    }
    return super.getIcon(stack, pass);
  }
}
