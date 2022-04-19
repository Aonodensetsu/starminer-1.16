package jp.mc.ancientred.starminer.basics.item.crop;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.mc.ancientred.starminer.basics.SMModContainer;
import jp.mc.ancientred.starminer.basics.block.DirectionConst;
import jp.mc.ancientred.starminer.basics.block.gravitized.IGravitizedPlants;
import jp.mc.ancientred.starminer.basics.item.ItemStarContoler;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;

public class ItemSeedFoodGravitized
  extends ItemFood
  implements IPlantable
{
  private Block cropBlock;
  
  public ItemSeedFoodGravitized(int par2, float par3, Block cropblock) {
    super(par2, par3, false);
    this.cropBlock = cropblock;
  }
  
  public ItemSeedFoodGravitized(Block cropblock) {
    this(getHealAmountForBlock(cropblock), getSaturationModifierForBlock(cropblock), cropblock);
  }
  private static int getHealAmountForBlock(Block block) {
    if (block == SMModContainer.CarrotGravitizedBlock) {
      return 4;
    }
    if (block == SMModContainer.PotatoGravitizedBlock) {
      return 1;
    }
    return 1;
  }
  private static float getSaturationModifierForBlock(Block block) {
    if (block == SMModContainer.CarrotGravitizedBlock) {
      return 0.6F;
    }
    if (block == SMModContainer.PotatoGravitizedBlock) {
      return 0.3F;
    }
    return 1.0F;
  }

  public EnumPlantType getPlantType(IBlockAccess world, int x, int y, int z) {
    return EnumPlantType.Crop;
  }

  public Block getPlant(IBlockAccess world, int x, int y, int z) {
    return this.cropBlock;
  }

  public int getPlantMetadata(IBlockAccess world, int x, int y, int z) {
    return 0;
  }

  public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int x, int y, int z, int side, float xOffset, float yOffset, float zOffset) {
    int meta = getMetadata(par1ItemStack.getMetadata());
    if (this.cropBlock instanceof IGravitizedPlants && 
      !((IGravitizedPlants)this.cropBlock).allowPlantOn(par3World.getBlock(x, y, z), meta)) {
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
      
      par3World.setBlock(tgtX, tgtY, tgtZ, this.cropBlock, meta, 3);
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
