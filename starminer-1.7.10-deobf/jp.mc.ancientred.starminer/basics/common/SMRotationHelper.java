package jp.mc.ancientred.starminer.basics.common;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.util.ForgeDirection;

public class SMRotationHelper
{
  private enum BlockType
  {
    LOG,
    DISPENSER,
    BED,
    RAIL,
    RAIL_POWERED,
    RAIL_ASCENDING,
    RAIL_CORNER,
    TORCH,
    STAIR,
    CHEST,
    SIGNPOST,
    DOOR,
    LEVER,
    BUTTON,
    REDSTONE_REPEATER,
    TRAPDOOR,
    MUSHROOM_CAP,
    MUSHROOM_CAP_CORNER,
    MUSHROOM_CAP_SIDE,
    VINE,
    SKULL,
    ANVIL;
  }
  
  private static final ForgeDirection[] UP_DOWN_AXES = new ForgeDirection[] { ForgeDirection.UP, ForgeDirection.DOWN };
  private static final Map<BlockType, BiMap<Integer, ForgeDirection>> MAPPINGS = new HashMap<BlockType, BiMap<Integer, ForgeDirection>>();
  
  public static ForgeDirection[] getValidVanillaBlockRotations(Block block) {
    return (block instanceof net.minecraft.block.BlockBed || block instanceof net.minecraft.block.BlockPumpkin || block instanceof net.minecraft.block.BlockFenceGate || block instanceof net.minecraft.block.BlockEndPortalFrame || block instanceof net.minecraft.block.BlockTripWireHook || block instanceof net.minecraft.block.BlockCocoa || block instanceof net.minecraft.block.BlockRailPowered || block instanceof net.minecraft.block.BlockRailDetector || block instanceof net.minecraft.block.BlockStairs || block instanceof net.minecraft.block.BlockChest || block instanceof net.minecraft.block.BlockEnderChest || block instanceof net.minecraft.block.BlockFurnace || block instanceof net.minecraft.block.BlockLadder || block == Blocks.wall_sign || block == Blocks.standing_sign || block instanceof net.minecraft.block.BlockDoor || block instanceof net.minecraft.block.BlockRail || block instanceof net.minecraft.block.BlockButton || block instanceof net.minecraft.block.BlockRedstoneRepeater || block instanceof net.minecraft.block.BlockRedstoneComparator || block instanceof net.minecraft.block.BlockTrapDoor || block instanceof net.minecraft.block.BlockHugeMushroom || block instanceof net.minecraft.block.BlockVine || block instanceof net.minecraft.block.BlockSkull || block instanceof net.minecraft.block.BlockAnvil) ? UP_DOWN_AXES : ForgeDirection.VALID_DIRECTIONS;
  }
  
  public static int rotateVanillaBlock(Block block, int orgMeta, ForgeDirection axis) {
    if (axis == ForgeDirection.UP || axis == ForgeDirection.DOWN) {
      
      if (block instanceof net.minecraft.block.BlockBed || block instanceof net.minecraft.block.BlockPumpkin || block instanceof net.minecraft.block.BlockFenceGate || block instanceof net.minecraft.block.BlockEndPortalFrame || block instanceof net.minecraft.block.BlockTripWireHook || block instanceof net.minecraft.block.BlockCocoa)
      {
        return rotateBlock(orgMeta, axis, 3, BlockType.BED);
      }
      if (block instanceof net.minecraft.block.BlockRail)
      {
        return rotateBlock(orgMeta, axis, 15, BlockType.RAIL);
      }
      if (block instanceof net.minecraft.block.BlockRailPowered || block instanceof net.minecraft.block.BlockRailDetector)
      {
        return rotateBlock(orgMeta, axis, 7, BlockType.RAIL_POWERED);
      }
      if (block instanceof net.minecraft.block.BlockStairs)
      {
        return rotateBlock(orgMeta, axis, 3, BlockType.STAIR);
      }
      if (block instanceof net.minecraft.block.BlockChest || block instanceof net.minecraft.block.BlockEnderChest || block instanceof net.minecraft.block.BlockFurnace || block instanceof net.minecraft.block.BlockLadder || block == Blocks.wall_sign)
      {
        return rotateBlock(orgMeta, axis, 7, BlockType.CHEST);
      }
      if (block == Blocks.standing_sign)
      {
        return rotateBlock(orgMeta, axis, 15, BlockType.SIGNPOST);
      }
      if (block instanceof net.minecraft.block.BlockDoor)
      {
        return rotateBlock(orgMeta, axis, 3, BlockType.DOOR);
      }
      if (block instanceof net.minecraft.block.BlockButton)
      {
        return rotateBlock(orgMeta, axis, 7, BlockType.BUTTON);
      }
      if (block instanceof net.minecraft.block.BlockRedstoneRepeater || block instanceof net.minecraft.block.BlockRedstoneComparator)
      {
        return rotateBlock(orgMeta, axis, 3, BlockType.REDSTONE_REPEATER);
      }
      if (block instanceof net.minecraft.block.BlockTrapDoor)
      {
        return rotateBlock(orgMeta, axis, 3, BlockType.TRAPDOOR);
      }
      if (block instanceof net.minecraft.block.BlockHugeMushroom)
      {
        return rotateBlock(orgMeta, axis, 15, BlockType.MUSHROOM_CAP);
      }
      if (block instanceof net.minecraft.block.BlockVine)
      {
        return rotateBlock(orgMeta, axis, 15, BlockType.VINE);
      }
      if (block instanceof net.minecraft.block.BlockSkull)
      {
        return rotateBlock(orgMeta, axis, 7, BlockType.SKULL);
      }
      if (block instanceof net.minecraft.block.BlockAnvil)
      {
        return rotateBlock(orgMeta, axis, 1, BlockType.ANVIL);
      }
    } 
    
    if (block instanceof net.minecraft.block.BlockLog)
    {
      return rotateBlock(orgMeta, axis, 12, BlockType.LOG);
    }
    if (block instanceof net.minecraft.block.BlockDispenser || block instanceof net.minecraft.block.BlockPistonBase || block instanceof net.minecraft.block.BlockPistonExtension || block instanceof net.minecraft.block.BlockHopper)
    {
      return rotateBlock(orgMeta, axis, 7, BlockType.DISPENSER);
    }
    if (block instanceof net.minecraft.block.BlockTorch)
    {
      return rotateBlock(orgMeta, axis, 15, BlockType.TORCH);
    }
    if (block instanceof net.minecraft.block.BlockLever)
    {
      return rotateBlock(orgMeta, axis, 7, BlockType.LEVER);
    }
    
    return -1;
  }

  private static int rotateBlock(int orgMeta, ForgeDirection axis, int mask, BlockType blockType) {
    int rotMeta = orgMeta;
    if (blockType == BlockType.DOOR && (rotMeta & 0x8) == 8)
    {
      return -1;
    }
    int masked = rotMeta & (mask ^ 0xFFFFFFFF);
    int meta = rotateMetadata(axis, blockType, rotMeta & mask);
    if (meta == -1)
    {
      return -1;
    }
    
    return meta & mask | masked;
  }

  private static int rotateMetadata(ForgeDirection axis, BlockType blockType, int meta) {
    if (blockType == BlockType.RAIL || blockType == BlockType.RAIL_POWERED) {
      
      if (meta == 0 || meta == 1)
      {
        return (meta ^ 0xFFFFFFFF) & 0x1;
      }
      if (meta >= 2 && meta <= 5)
      {
        blockType = BlockType.RAIL_ASCENDING;
      }
      if (meta >= 6 && meta <= 9 && blockType == BlockType.RAIL)
      {
        blockType = BlockType.RAIL_CORNER;
      }
    } 
    if (blockType == BlockType.SIGNPOST)
    {
      return (axis == ForgeDirection.UP) ? ((meta + 4) % 16) : ((meta + 12) % 16);
    }
    if (blockType == BlockType.LEVER && (axis == ForgeDirection.UP || axis == ForgeDirection.DOWN))
    {
      switch (meta) {
        
        case 5:
          return 6;
        case 6:
          return 5;
        case 7:
          return 0;
        case 0:
          return 7;
      } 
    }
    if (blockType == BlockType.MUSHROOM_CAP)
    {
      if (meta % 2 == 0) {
        
        blockType = BlockType.MUSHROOM_CAP_SIDE;
      }
      else {
        
        blockType = BlockType.MUSHROOM_CAP_CORNER;
      } 
    }
    if (blockType == BlockType.VINE)
    {
      return meta << 1 | (meta & 0x8) >> 3;
    }
    
    ForgeDirection orientation = metadataToDirection(blockType, meta);
    ForgeDirection rotated = orientation.getRotation(axis);
    return directionToMetadata(blockType, rotated);
  }

  private static ForgeDirection metadataToDirection(BlockType blockType, int meta) {
    if (blockType == BlockType.LEVER)
    {
      if (meta == 6) {
        
        meta = 5;
      }
      else if (meta == 0) {
        
        meta = 7;
      } 
    }
    
    if (MAPPINGS.containsKey(blockType)) {
      
      BiMap<Integer, ForgeDirection> biMap = MAPPINGS.get(blockType);
      if (biMap.containsKey(Integer.valueOf(meta)))
      {
        return (ForgeDirection)biMap.get(Integer.valueOf(meta));
      }
    } 
    
    if (blockType == BlockType.TORCH)
    {
      return ForgeDirection.getOrientation(6 - meta);
    }
    if (blockType == BlockType.STAIR)
    {
      return ForgeDirection.getOrientation(5 - meta);
    }
    if (blockType == BlockType.CHEST || blockType == BlockType.DISPENSER || blockType == BlockType.SKULL)
    {
      return ForgeDirection.getOrientation(meta);
    }
    if (blockType == BlockType.BUTTON)
    {
      return ForgeDirection.getOrientation(6 - meta);
    }
    if (blockType == BlockType.TRAPDOOR)
    {
      return ForgeDirection.getOrientation(meta + 2).getOpposite();
    }
    
    return ForgeDirection.UNKNOWN;
  }

  private static int directionToMetadata(BlockType blockType, ForgeDirection direction) {
    if ((blockType == BlockType.LOG || blockType == BlockType.ANVIL) && direction.offsetX + direction.offsetY + direction.offsetZ < 0)
    {
      direction = direction.getOpposite();
    }
    
    if (MAPPINGS.containsKey(blockType)) {
      
      BiMap<ForgeDirection, Integer> biMap = ((BiMap)MAPPINGS.get(blockType)).inverse();
      if (biMap.containsKey(direction))
      {
        return ((Integer)biMap.get(direction)).intValue();
      }
    } 
    
    if (blockType == BlockType.TORCH)
    {
      if (direction.ordinal() >= 1)
      {
        return 6 - direction.ordinal();
      }
    }
    if (blockType == BlockType.STAIR)
    {
      return 5 - direction.ordinal();
    }
    if (blockType == BlockType.CHEST || blockType == BlockType.DISPENSER || blockType == BlockType.SKULL)
    {
      return direction.ordinal();
    }
    if (blockType == BlockType.BUTTON)
    {
      if (direction.ordinal() >= 2)
      {
        return 6 - direction.ordinal();
      }
    }
    if (blockType == BlockType.TRAPDOOR)
    {
      return direction.getOpposite().ordinal() - 2;
    }
    
    return -1;
  }

  static {
    HashBiMap hashBiMap = HashBiMap.create(3);
    hashBiMap.put(Integer.valueOf(0), ForgeDirection.UP);
    hashBiMap.put(Integer.valueOf(4), ForgeDirection.EAST);
    hashBiMap.put(Integer.valueOf(8), ForgeDirection.SOUTH);
    MAPPINGS.put(BlockType.LOG, hashBiMap);
    
    hashBiMap = HashBiMap.create(4);
    hashBiMap.put(Integer.valueOf(0), ForgeDirection.SOUTH);
    hashBiMap.put(Integer.valueOf(1), ForgeDirection.WEST);
    hashBiMap.put(Integer.valueOf(2), ForgeDirection.NORTH);
    hashBiMap.put(Integer.valueOf(3), ForgeDirection.EAST);
    MAPPINGS.put(BlockType.BED, hashBiMap);
    
    hashBiMap = HashBiMap.create(4);
    hashBiMap.put(Integer.valueOf(2), ForgeDirection.EAST);
    hashBiMap.put(Integer.valueOf(3), ForgeDirection.WEST);
    hashBiMap.put(Integer.valueOf(4), ForgeDirection.NORTH);
    hashBiMap.put(Integer.valueOf(5), ForgeDirection.SOUTH);
    MAPPINGS.put(BlockType.RAIL_ASCENDING, hashBiMap);
    
    hashBiMap = HashBiMap.create(4);
    hashBiMap.put(Integer.valueOf(6), ForgeDirection.WEST);
    hashBiMap.put(Integer.valueOf(7), ForgeDirection.NORTH);
    hashBiMap.put(Integer.valueOf(8), ForgeDirection.EAST);
    hashBiMap.put(Integer.valueOf(9), ForgeDirection.SOUTH);
    MAPPINGS.put(BlockType.RAIL_CORNER, hashBiMap);
    
    hashBiMap = HashBiMap.create(6);
    hashBiMap.put(Integer.valueOf(1), ForgeDirection.EAST);
    hashBiMap.put(Integer.valueOf(2), ForgeDirection.WEST);
    hashBiMap.put(Integer.valueOf(3), ForgeDirection.SOUTH);
    hashBiMap.put(Integer.valueOf(4), ForgeDirection.NORTH);
    hashBiMap.put(Integer.valueOf(5), ForgeDirection.UP);
    hashBiMap.put(Integer.valueOf(7), ForgeDirection.DOWN);
    MAPPINGS.put(BlockType.LEVER, hashBiMap);
    
    hashBiMap = HashBiMap.create(4);
    hashBiMap.put(Integer.valueOf(0), ForgeDirection.WEST);
    hashBiMap.put(Integer.valueOf(1), ForgeDirection.NORTH);
    hashBiMap.put(Integer.valueOf(2), ForgeDirection.EAST);
    hashBiMap.put(Integer.valueOf(3), ForgeDirection.SOUTH);
    MAPPINGS.put(BlockType.DOOR, hashBiMap);
    
    hashBiMap = HashBiMap.create(4);
    hashBiMap.put(Integer.valueOf(0), ForgeDirection.NORTH);
    hashBiMap.put(Integer.valueOf(1), ForgeDirection.EAST);
    hashBiMap.put(Integer.valueOf(2), ForgeDirection.SOUTH);
    hashBiMap.put(Integer.valueOf(3), ForgeDirection.WEST);
    MAPPINGS.put(BlockType.REDSTONE_REPEATER, hashBiMap);
    
    hashBiMap = HashBiMap.create(4);
    hashBiMap.put(Integer.valueOf(1), ForgeDirection.EAST);
    hashBiMap.put(Integer.valueOf(3), ForgeDirection.SOUTH);
    hashBiMap.put(Integer.valueOf(7), ForgeDirection.NORTH);
    hashBiMap.put(Integer.valueOf(9), ForgeDirection.WEST);
    MAPPINGS.put(BlockType.MUSHROOM_CAP_CORNER, hashBiMap);
    
    hashBiMap = HashBiMap.create(4);
    hashBiMap.put(Integer.valueOf(2), ForgeDirection.NORTH);
    hashBiMap.put(Integer.valueOf(4), ForgeDirection.WEST);
    hashBiMap.put(Integer.valueOf(6), ForgeDirection.EAST);
    hashBiMap.put(Integer.valueOf(8), ForgeDirection.SOUTH);
    MAPPINGS.put(BlockType.MUSHROOM_CAP_SIDE, hashBiMap);
    
    hashBiMap = HashBiMap.create(2);
    hashBiMap.put(Integer.valueOf(0), ForgeDirection.SOUTH);
    hashBiMap.put(Integer.valueOf(1), ForgeDirection.EAST);
    MAPPINGS.put(BlockType.ANVIL, hashBiMap);
  }
}
