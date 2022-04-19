package jp.mc.ancientred.starminer.basics.item.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.mc.ancientred.starminer.basics.SMModContainer;
import jp.mc.ancientred.starminer.basics.block.DirectionConst;
import jp.mc.ancientred.starminer.basics.block.gravitized.BlockCropsGravitized;
import jp.mc.ancientred.starminer.basics.block.gravitized.IGravitizedPlants;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class ItemColoredForTallGrass
  extends ItemBlock
{
  private final Block theBlock;
  private String[] blockNames = new String[] { "shrub", "grass", "fern" };
  
  public ItemColoredForTallGrass(Block block) {
    super(block);
    this.theBlock = block;
    
    setMaxDurability(0);
    setHasSubtypes(true);
  }
  
  @SideOnly(Side.CLIENT)
  public int getColorFromItemStack(ItemStack par1ItemStack, int par2) {
    return this.theBlock.getRenderColor(par1ItemStack.getMetadata());
  }
  
  public int getMetadata(int par1) {
    return par1;
  }
  
  public ItemColoredForTallGrass setBlockNames(String[] par1ArrayOfStr) {
    this.blockNames = par1ArrayOfStr;
    return this;
  }
  
  @SideOnly(Side.CLIENT)
  public IIcon getIconFromDamage(int par1) {
    return this.theBlock.getIcon(0, par1);
  }
  
  public String getUnlocalizedName(ItemStack par1ItemStack) {
    if (this.blockNames == null)
    {
      return super.getUnlocalizedName(par1ItemStack);
    }
    
    int i = par1ItemStack.getMetadata();
    return (i >= 0 && i < this.blockNames.length) ? (super.getUnlocalizedName(par1ItemStack) + "." + this.blockNames[i]) : super.getUnlocalizedName(par1ItemStack);
  }
  
  public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int x, int y, int z, int side, float xOffset, float yOffset, float zOffset) {
    int meta = getMetadata(par1ItemStack.getMetadata());
    if (this.theBlock instanceof IGravitizedPlants && 
      !((IGravitizedPlants)this.theBlock).allowPlantOn(par3World.getBlock(x, y, z), meta)) {
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
      
      par3World.setBlock(tgtX, tgtY, tgtZ, this.theBlock, meta, 3);
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
