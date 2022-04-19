package jp.mc.ancientred.starminer.basics.item.block;

import jp.mc.ancientred.starminer.api.Gravity;
import jp.mc.ancientred.starminer.api.GravityDirection;
import jp.mc.ancientred.starminer.basics.Config;
import jp.mc.ancientred.starminer.basics.SMModContainer;
import jp.mc.ancientred.starminer.basics.SMReflectionHelper;
import jp.mc.ancientred.starminer.basics.block.BlockRotator;
import jp.mc.ancientred.starminer.basics.dummy.DummyRotatedWorld;
import jp.mc.ancientred.starminer.basics.tileentity.TileEntityBlockRotator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemReed;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class ItemBlockForRotator
  extends ItemBlock
{
  public ItemBlockForRotator(Block block) {
    super(block);
  }

  public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int parX, int parY, int parZ, int side, float hitX, float hitY, float hitZ) {
    Block currentBlock = world.getBlock(parX, parY, parZ);
    
    if (currentBlock == Blocks.snow_layer && (world.getBlockMetadata(parX, parY, parZ) & 0x7) < 1) {
      
      side = 1;
    }
    else if (currentBlock != Blocks.vine && currentBlock != Blocks.tallgrass && currentBlock != Blocks.deadbush && !currentBlock.isReplaceable((IBlockAccess)world, parX, parY, parZ)) {
      
      if (side == 0)
      {
        parY--;
      }
      
      if (side == 1)
      {
        parY++;
      }
      
      if (side == 2)
      {
        parZ--;
      }
      
      if (side == 3)
      {
        parZ++;
      }
      
      if (side == 4)
      {
        parX--;
      }
      
      if (side == 5)
      {
        parX++;
      }
    } 
    
    if (itemStack.stackSize == 0)
    {
      return false;
    }
    if (!player.canPlayerEdit(parX, parY, parZ, side, itemStack))
    {
      return false;
    }
    if (parY == 255 && this.blockInstance.getMaterial().isSolid())
    {
      return false;
    }
    if (world.canPlaceEntityOnSide(this.blockInstance, parX, parY, parZ, false, side, (Entity)player, itemStack)) {
      
      GravityDirection gDir = Gravity.getGravityDirection((Entity)player);
      
      ItemStack[] mainInv = player.inventory.mainInventory;
      
      Block blockToInclude = null;
      ItemStack targetStack = null;
      int slotNum;
      for (slotNum = 0; slotNum < 9; slotNum++) {
        Item item; if (mainInv[slotNum] != null && (item = mainInv[slotNum].getItem()) != null) {
          blockToInclude = convertItemToBlock(item);
          
          if (blockToInclude != null && blockToInclude != Blocks.air && !blockToInclude.hasTileEntity(0) && !(blockToInclude instanceof net.minecraft.block.ITileEntityProvider) && !(blockToInclude instanceof BlockRotator) && (!Config.enableFakeRotatorOnlyVannilaBlock || isVannilaBlock(blockToInclude))) {

            targetStack = mainInv[slotNum];
            
            break;
          } 
        } 
      } 
      if (targetStack == null) return false;
      
      int itemMeta = targetStack.getMetadata();

      int convSide = gDir.forgeSideRot[side];
      
      GravityDirection dirOpposite = GravityDirection.turnWayForNormal(gDir);
      float[] conv = new float[3];
      conv = dirOpposite.rotateXYZAt(conv, hitX, hitY, hitZ, 0.5F, 0.5F, 0.5F);

      World dummy = DummyRotatedWorld.get().wrapp(world, gDir, parX, parY, parZ);
      int meta = blockToInclude.onBlockPlaced(dummy, parX, parY, parZ, convSide, conv[0], conv[1], conv[2], itemMeta);

      Vec3 playerLookVec = player.getLookVec();
      dirOpposite.rotateVec3(playerLookVec);
      float rotationPitchSaved = player.rotationPitch;
      float rotationYawSaved = player.rotationYaw;
      
      try { double pitch = -Math.asin(playerLookVec.yCoord) * 57.29577951308232D;
        double yaw = -90.0D + Math.atan2(playerLookVec.zCoord, playerLookVec.xCoord) * 180.0D / Math.PI;
        player.rotationPitch = (float)pitch;
        player.rotationYaw = (float)yaw;
        
        Block blockRotToSet = SMModContainer.BlockRotatorBlock;

        if (placeBlockAtEx(gDir, blockToInclude, blockRotToSet, itemStack, targetStack, player, world, dummy, parX, parY, parZ, side, hitX, hitY, hitZ, meta)) {
          
          world.playSoundEffect((parX + 0.5F), (parY + 0.5F), (parZ + 0.5F), this.blockInstance.stepSound.getPlaceSound(), (this.blockInstance.stepSound.getVolume() + 1.0F) / 2.0F, this.blockInstance.stepSound.getFrequency() * 0.8F);
          itemStack.stackSize--;
          
          if (!player.capabilities.isCreativeMode && --targetStack.stackSize == 0)
          {
            player.inventory.setInventorySlotContents(slotNum, (ItemStack)null); } 
        }  }
      catch (Exception ex)
      { ex.printStackTrace(); }
      finally { player.rotationPitch = rotationPitchSaved;
        player.rotationYaw = rotationYawSaved; }

      
      return true;
    } 
    
    return false;
  }

  private boolean placeBlockAtEx(GravityDirection gDir, Block blockToInclude, Block blockRotToSet, ItemStack stack, ItemStack targetStack, EntityPlayer player, World world, World dummy, int parX, int parY, int parZ, int side, float hitX, float hitY, float hitZ, int metadata) {
    if (blockToInclude instanceof BlockDoor) {
      return placeDoorAtEx(gDir, (BlockDoor)blockToInclude, blockRotToSet, stack, targetStack, player, world, dummy, parX, parY, parZ, side, hitX, hitY, hitZ, metadata);
    }
    if (blockToInclude instanceof BlockDoublePlant) {
      return placeBlockDoublePlantAtEx(gDir, (BlockDoublePlant)blockToInclude, blockRotToSet, stack, targetStack, player, world, dummy, parX, parY, parZ, side, hitX, hitY, hitZ, metadata);
    }

    if (!world.setBlock(parX, parY, parZ, blockRotToSet, metadata, 3))
    {
      
      return false;
    }
    
    if (world.getBlock(parX, parY, parZ) == blockRotToSet) {

      
      blockToInclude.onBlockPlacedBy(dummy, parX, parY, parZ, (EntityLivingBase)player, stack);
      blockToInclude.onPostBlockPlaced(dummy, parX, parY, parZ, metadata);
    } 

    TileEntityBlockRotator te = BlockRotator.getTileEntityBlockRotator((IBlockAccess)world, parX, parY, parZ);
    te.setStoredBlock(blockToInclude);
    te.setStoredItem(targetStack.getItem());
    te.setItemMetadata(targetStack.getMetadata());
    
    te.setGravityDirection(gDir);
    te.isSubBlock = false;
    te.relatedBlockX = parX;
    te.relatedBlockY = parY;
    te.relatedBlockZ = parZ;
    
    return true;
  }

  private boolean placeBlockDoublePlantAtEx(GravityDirection gDir, BlockDoublePlant blockToInclude, Block blockRotToSet, ItemStack stack, ItemStack targetStack, EntityPlayer player, World world, World dummy, int parX, int parY, int parZ, int side, float hitX, float hitY, float hitZ, int metadata) {
    int l = ((MathHelper.floor_double((player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 0x3) + 2) % 4;

    int[] rotatedSubPos = ((DummyRotatedWorld)dummy).rotateOnCurrentState(parX, parY + 1, parZ);
    if (!world.isAirBlock(rotatedSubPos[0], rotatedSubPos[1], rotatedSubPos[2]))
    {
      return false;
    }

    world.setBlock(parX, parY, parZ, blockRotToSet, metadata, 3);
    world.setBlock(rotatedSubPos[0], rotatedSubPos[1], rotatedSubPos[2], blockRotToSet, 0x8 | l, 2);

    TileEntityBlockRotator te = BlockRotator.getTileEntityBlockRotator((IBlockAccess)world, parX, parY, parZ);
    te.setStoredBlock((Block)blockToInclude);
    te.setStoredItem(targetStack.getItem());
    te.setItemMetadata(targetStack.getMetadata());
    
    te.setGravityDirection(gDir);
    te.isSubBlock = false;
    te.relatedBlockX = rotatedSubPos[0];
    te.relatedBlockY = rotatedSubPos[1];
    te.relatedBlockZ = rotatedSubPos[2];

    te = BlockRotator.getTileEntityBlockRotator((IBlockAccess)world, rotatedSubPos[0], rotatedSubPos[1], rotatedSubPos[2]);
    te.setStoredBlock((Block)blockToInclude);
    te.setStoredItem(targetStack.getItem());
    te.setItemMetadata(targetStack.getMetadata());
    
    te.setGravityDirection(gDir);
    te.isSubBlock = true;
    te.relatedBlockX = parX;
    te.relatedBlockY = parY;
    te.relatedBlockZ = parZ;
    
    return true;
  }

  private boolean placeDoorAtEx(GravityDirection gDir, BlockDoor blockToInclude, Block blockRotToSet, ItemStack stack, ItemStack targetStack, EntityPlayer player, World world, World dummy, int parX, int parY, int parZ, int side, float hitX, float hitY, float hitZ, int metadata) {
    int direction4Way = MathHelper.floor_double(((player.rotationYaw + 180.0F) * 4.0F / 360.0F) - 0.5D) & 0x3;
    byte fixX = 0;
    byte fixZ = 0;
    
    if (direction4Way == 0)
    {
      fixZ = 1;
    }
    
    if (direction4Way == 1)
    {
      fixX = -1;
    }
    
    if (direction4Way == 2)
    {
      fixZ = -1;
    }
    
    if (direction4Way == 3)
    {
      fixX = 1;
    }
    
    int i1 = (dummy.getBlock(parX - fixX, parY, parZ - fixZ).isNormalCube() ? 1 : 0) + (dummy.getBlock(parX - fixX, parY + 1, parZ - fixZ).isNormalCube() ? 1 : 0);
    int j1 = (dummy.getBlock(parX + fixX, parY, parZ + fixZ).isNormalCube() ? 1 : 0) + (dummy.getBlock(parX + fixX, parY + 1, parZ + fixZ).isNormalCube() ? 1 : 0);
    boolean flag = (dummy.getBlock(parX - fixX, parY, parZ - fixZ) == blockToInclude || dummy.getBlock(parX - fixX, parY + 1, parZ - fixZ) == blockToInclude);
    boolean flag1 = (dummy.getBlock(parX + fixX, parY, parZ + fixZ) == blockToInclude || dummy.getBlock(parX + fixX, parY + 1, parZ + fixZ) == blockToInclude);
    boolean isFlipped = false;
    
    if (flag && !flag1) {
      
      isFlipped = true;
    }
    else if (j1 > i1) {
      
      isFlipped = true;
    } 
    
    int[] rotatedHeadPos = ((DummyRotatedWorld)dummy).rotateOnCurrentState(parX, parY + 1, parZ);
    if (!world.isAirBlock(rotatedHeadPos[0], rotatedHeadPos[1], rotatedHeadPos[2]))
    {
      return false;
    }

    world.setBlock(parX, parY, parZ, blockRotToSet, direction4Way, 2);
    world.setBlock(rotatedHeadPos[0], rotatedHeadPos[1], rotatedHeadPos[2], blockRotToSet, 0x8 | (isFlipped ? 1 : 0), 2);

    TileEntityBlockRotator te = BlockRotator.getTileEntityBlockRotator((IBlockAccess)world, parX, parY, parZ);
    te.setStoredBlock((Block)blockToInclude);
    te.setStoredItem(targetStack.getItem());
    te.setItemMetadata(targetStack.getMetadata());

    te.setGravityDirection(gDir);
    te.isSubBlock = false;
    te.relatedBlockX = rotatedHeadPos[0];
    te.relatedBlockY = rotatedHeadPos[1];
    te.relatedBlockZ = rotatedHeadPos[2];

    te = BlockRotator.getTileEntityBlockRotator((IBlockAccess)world, rotatedHeadPos[0], rotatedHeadPos[1], rotatedHeadPos[2]);
    te.setStoredBlock((Block)blockToInclude);
    te.setStoredItem(targetStack.getItem());
    te.setItemMetadata(targetStack.getMetadata());
    
    te.setGravityDirection(gDir);
    te.isSubBlock = true;
    te.relatedBlockX = parX;
    te.relatedBlockY = parY;
    te.relatedBlockZ = parZ;

    world.notifyBlocksOfNeighborChange(parX, parY, parZ, blockRotToSet);
    world.notifyBlocksOfNeighborChange(rotatedHeadPos[0], rotatedHeadPos[1], rotatedHeadPos[2], blockRotToSet);
    
    return true;
  }
  
  private boolean isVannilaBlock(Block block) {
    return block.getClass().getName().startsWith("net.minecraft");
  }
  
  private Block convertItemToBlock(Item item) {
    Block block = null;
    if (item instanceof ItemReed) {
      
      block = SMReflectionHelper.getField_150935_a((ItemReed)item);
    } else if (item instanceof ItemBlock) {
      
      block = Block.getBlockFromItem(item);
    } else if (item instanceof net.minecraft.item.ItemDoor) {
      
      if (item == Items.wooden_door) {
        block = Blocks.wooden_door;
      } else if (item == Items.iron_door) {
        block = Blocks.iron_door;
      } 
    } 
    return block;
  }
}
