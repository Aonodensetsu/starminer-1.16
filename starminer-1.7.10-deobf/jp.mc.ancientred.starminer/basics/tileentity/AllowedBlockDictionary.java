package jp.mc.ancientred.starminer.basics.tileentity;

import java.util.HashMap;
import jp.mc.ancientred.starminer.basics.SMModContainer;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

public class AllowedBlockDictionary
{
  public static final boolean isAllowed(Block block) {
    if (block == null) return false; 
    return placableBlockMap.containsKey(block);
  }
  public static final HashMap<Block, Object> placableBlockMap = new HashMap<Block, Object>();
  public static final Object ALLOWED = new Object();
  
  static {
    placableBlockMap.put(SMModContainer.OuterCoreBlock, ALLOWED);
    placableBlockMap.put(SMModContainer.InnerCoreBlock, ALLOWED);
    placableBlockMap.put(SMModContainer.DirtGrassExBlock, ALLOWED);

    placableBlockMap.put(Blocks.stone, ALLOWED);
    
    placableBlockMap.put(Blocks.dirt, ALLOWED);
    placableBlockMap.put(Blocks.cobblestone, ALLOWED);
    placableBlockMap.put(Blocks.planks, ALLOWED);
    
    placableBlockMap.put(Blocks.bedrock, ALLOWED);

    placableBlockMap.put(Blocks.sand, ALLOWED);
    placableBlockMap.put(Blocks.gravel, ALLOWED);
    placableBlockMap.put(Blocks.gold_ore, ALLOWED);
    placableBlockMap.put(Blocks.iron_ore, ALLOWED);
    placableBlockMap.put(Blocks.coal_ore, ALLOWED);
    placableBlockMap.put(Blocks.log, ALLOWED);
    placableBlockMap.put(Blocks.log2, ALLOWED);
    placableBlockMap.put(Blocks.leaves, ALLOWED);
    placableBlockMap.put(Blocks.leaves2, ALLOWED);
    placableBlockMap.put(Blocks.sponge, ALLOWED);
    placableBlockMap.put(Blocks.glass, ALLOWED);
    placableBlockMap.put(Blocks.lapis_ore, ALLOWED);
    placableBlockMap.put(Blocks.lapis_block, ALLOWED);
    
    placableBlockMap.put(Blocks.sandstone, ALLOWED);
    
    placableBlockMap.put(Blocks.web, ALLOWED);

    placableBlockMap.put(Blocks.wool, ALLOWED);

    placableBlockMap.put(Blocks.gold_block, ALLOWED);
    placableBlockMap.put(Blocks.iron_block, ALLOWED);

    placableBlockMap.put(Blocks.brick_block, ALLOWED);
    placableBlockMap.put(Blocks.tnt, ALLOWED);
    placableBlockMap.put(Blocks.bookshelf, ALLOWED);
    placableBlockMap.put(Blocks.mossy_cobblestone, ALLOWED);
    placableBlockMap.put(Blocks.obsidian, ALLOWED);
    
    placableBlockMap.put(Blocks.diamond_ore, ALLOWED);
    placableBlockMap.put(Blocks.diamond_block, ALLOWED);

    placableBlockMap.put(Blocks.redstone_ore, ALLOWED);

    placableBlockMap.put(Blocks.snow_layer, ALLOWED);
    placableBlockMap.put(Blocks.ice, ALLOWED);

    placableBlockMap.put(Blocks.clay, ALLOWED);

    placableBlockMap.put(Blocks.pumpkin, ALLOWED);
    placableBlockMap.put(Blocks.netherrack, ALLOWED);
    placableBlockMap.put(Blocks.soul_sand, ALLOWED);
    placableBlockMap.put(Blocks.glowstone, ALLOWED);
    
    placableBlockMap.put(Blocks.lit_pumpkin, ALLOWED);
    placableBlockMap.put(Blocks.cake, ALLOWED);
    
    placableBlockMap.put(Blocks.stonebrick, ALLOWED);
    
    placableBlockMap.put(Blocks.melon_block, ALLOWED);
    
    placableBlockMap.put(Blocks.mycelium, ALLOWED);
    
    placableBlockMap.put(Blocks.nether_brick, ALLOWED);
    
    placableBlockMap.put(Blocks.end_stone, ALLOWED);

    placableBlockMap.put(Blocks.emerald_ore, ALLOWED);

    placableBlockMap.put(Blocks.emerald_block, ALLOWED);

    placableBlockMap.put(Blocks.anvil, ALLOWED);

    placableBlockMap.put(Blocks.redstone_block, ALLOWED);
    placableBlockMap.put(Blocks.quartz_ore, ALLOWED);
    
    placableBlockMap.put(Blocks.quartz_block, ALLOWED);

    placableBlockMap.put(Blocks.stained_hardened_clay, ALLOWED);
    placableBlockMap.put(Blocks.hay_block, ALLOWED);
    
    placableBlockMap.put(Blocks.hardened_clay, ALLOWED);
    placableBlockMap.put(Blocks.coal_block, ALLOWED);
    placableBlockMap.put(Blocks.packed_ice, ALLOWED);

    placableBlockMap.put(Blocks.stained_glass, ALLOWED);
  }
}
