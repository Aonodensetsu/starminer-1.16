package jp.mc.ancientred.starminer.basics.item.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.mc.ancientred.starminer.basics.SMModContainer;
import jp.mc.ancientred.starminer.basics.block.DirectionConst;
import jp.mc.ancientred.starminer.basics.block.gravitized.BlockCropsGravitized;
import jp.mc.ancientred.starminer.basics.block.gravitized.BlockFlowerGravitized;
import jp.mc.ancientred.starminer.basics.block.gravitized.BlockSaplingGravitized;
import jp.mc.ancientred.starminer.basics.block.gravitized.IGravitizedPlants;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemMultiTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class ItemMultiTextureTileForG
  extends ItemMultiTexture
{
  public ItemMultiTextureTileForG(Block par1Block, BlockSaplingGravitized par2Block, String[] namelist) {
    super(par1Block, (Block)par2Block, namelist);
  }
  public ItemMultiTextureTileForG(Block par1Block, BlockFlowerGravitized par2Block, String[] namelist) {
    super(par1Block, (Block)par2Block, namelist);
  }
  
  public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int x, int y, int z, int side, float xOffset, float yOffset, float zOffset) {
    Block thisBlockId = this.field_150941_b;
    int meta = getMetadata(par1ItemStack.getMetadata());
    if (thisBlockId instanceof IGravitizedPlants && 
      !((IGravitizedPlants)thisBlockId).allowPlantOn(par3World.getBlock(x, y, z), meta)) {
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
      
      par3World.setBlock(tgtX, tgtY, tgtZ, thisBlockId, meta, 3);
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
    if (pass == 1)
    {
      return ((BlockCropsGravitized)SMModContainer.CropGravitizedBlock).starMarkIcon;
    }
    return super.getIcon(stack, pass);
  }
}
