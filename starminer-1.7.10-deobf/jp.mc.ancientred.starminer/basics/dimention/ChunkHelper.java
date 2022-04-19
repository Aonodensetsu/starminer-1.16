package jp.mc.ancientred.starminer.basics.dimention;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

public class ChunkHelper
{
  public static final void fillinChunk(Chunk chunk, World world, Block[] blocksData, byte[] metadata, int chunkX, int chunkZ) {
    ExtendedBlockStorage[] storageArrays = chunk.getBlockStorageArray();
    int max = blocksData.length / 256;
    
    for (int y = 0; y < max; y++) {
      
      for (int z = 0; z < 16; z++) {
        
        for (int x = 0; x < 16; x++) {
          
          int idx = y << 8 | z << 4 | x;
          Block id = blocksData[idx];
          int meta = metadata[idx];
          
          if (id != null || meta != 0) {
            
            int storageBlock = y >> 4;
            
            if (storageArrays[storageBlock] == null)
            {
              storageArrays[storageBlock] = new ExtendedBlockStorage(storageBlock << 4, !world.provider.hasNoSky);
            }
            
            storageArrays[storageBlock].setExtBlockID(x, y & 0xF, z, id);
            if (id == Blocks.glowstone) {
              storageArrays[storageBlock].setExtBlocklightValue(x, y & 0xF, z, 15);
            }
            storageArrays[storageBlock].setExtBlockMetadata(x, y & 0xF, z, meta);
          } 
        } 
      } 
    } 
  }
}
