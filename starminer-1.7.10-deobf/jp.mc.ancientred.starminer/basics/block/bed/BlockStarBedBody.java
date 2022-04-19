package jp.mc.ancientred.starminer.basics.block.bed;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Iterator;
import java.util.Random;
import jp.mc.ancientred.starminer.basics.SMModContainer;
import jp.mc.ancientred.starminer.basics.SMReflectionHelper;
import jp.mc.ancientred.starminer.basics.block.DirectionConst;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockStarBedBody
  extends Block
{
  public static final double HEIGHT_D = 0.5625D;
  public static final float HEIGHT_F = 0.5625F;
  
  public BlockStarBedBody() {
    super(Material.cloth);
    setHardness(0.2F);
    disableStats();
  }

  public boolean isBed(IBlockAccess world, int x, int y, int z, EntityLivingBase player) {
    return true;
  }
  public int getGravityDirection(IBlockAccess world, int x, int y, int z) {
    int meta = world.getBlockMetadata(x, y, z);
    int[] headRel = DirectionConst.CHECKNEIGHBOR_LIST[meta & 0x7];
    Block block;
    if ((block = world.getBlock(x + headRel[0], y + headRel[1], z + headRel[2])) == SMModContainer.StarBedHeadBlock) {
      return ((BlockStarBedHead)block).getGravityDirection(world, x + headRel[0], y + headRel[1], z + headRel[2]);
    }
    return -1;
  }
  
  public int getConnectionDirection(IBlockAccess world, int x, int y, int z) {
    int meta = world.getBlockMetadata(x, y, z);
    return meta & 0x7;
  }

  public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
    if (par1World.isRemote)
    {
      return true;
    }

    int meta = par1World.getBlockMetadata(par2, par3, par4);
    int[] headRel = DirectionConst.CHECKNEIGHBOR_LIST[meta & 0x7];
    
    par2 += headRel[0];
    par3 += headRel[1];
    par4 += headRel[2];
    
    if (par1World.getBlock(par2, par3, par4) != SMModContainer.StarBedHeadBlock)
    {
      
      return true;
    }
    
    meta = par1World.getBlockMetadata(par2, par3, par4);
    
    if (par1World.provider.canRespawnHere() && par1World.getBiomeGenForCoords(par2, par4) != BiomeGenBase.hell) {
      
      if (BlockStarBedHead.isBedOccupiedHead(meta)) {
        
        EntityPlayer entityplayer1 = null;
        Iterator<EntityPlayer> iterator = par1World.playerEntities.iterator();
        
        while (iterator.hasNext()) {
          
          EntityPlayer entityplayer2 = iterator.next();
          
          if (entityplayer2.isPlayerSleeping()) {
            
            ChunkCoordinates chunkcoordinates = entityplayer2.playerLocation;
            
            if (chunkcoordinates.posX == par2 && chunkcoordinates.posY == par3 && chunkcoordinates.posZ == par4)
            {
              entityplayer1 = entityplayer2;
            }
          } 
        } 
        
        if (entityplayer1 != null) {

          par5EntityPlayer.addChatComponentMessage((IChatComponent)new ChatComponentTranslation("tile.bed.occupied", new Object[0]));
          return true;
        } 
        
        BlockStarBedHead.setBedOccupiedHead((IBlockAccess)par1World, par2, par3, par4, false);
      } 
      
      EntityPlayer.EnumStatus enumstatus = par5EntityPlayer.sleepInBedAt(par2, par3, par4);
      SMReflectionHelper.ignoreHasMovedFlg(par5EntityPlayer);
      
      if (enumstatus == EntityPlayer.EnumStatus.OK) {
        
        BlockStarBedHead.setBedOccupiedHead((IBlockAccess)par1World, par2, par3, par4, true);
        return true;
      } 

      if (enumstatus == EntityPlayer.EnumStatus.NOT_POSSIBLE_NOW) {

        par5EntityPlayer.addChatComponentMessage((IChatComponent)new ChatComponentTranslation("tile.bed.noSleep", new Object[0]));
      }
      else if (enumstatus == EntityPlayer.EnumStatus.NOT_SAFE) {

        par5EntityPlayer.addChatComponentMessage((IChatComponent)new ChatComponentTranslation("tile.bed.notSafe", new Object[0]));
      } 
      
      return true;
    } 

    double d0 = par2 + 0.5D;
    double d1 = par3 + 0.5D;
    double d2 = par4 + 0.5D;
    par1World.setBlockToAir(par2, par3, par4);
    par1World.newExplosion((Entity)null, (par2 + 0.5F), (par3 + 0.5F), (par4 + 0.5F), 8.0F, true, true);
    return true;
  }

  public boolean isOpaqueCube() {
    return false;
  }

  public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
    double lw = 0.5625D;
    double hi = 0.4375D;
    int dir = getGravityDirection((IBlockAccess)par1World, par2, par3, par4);
    switch (dir) {
      case 1:
        return AxisAlignedBB.getBoundingBox(par2 + 0.0D, par3 + hi, par4 + 0.0D, par2 + 1.0D, par3 + 1.0D, par4 + 1.0D);

      case 0:
        return AxisAlignedBB.getBoundingBox(par2 + 0.0D, par3 + 0.0D, par4 + 0.0D, par2 + 1.0D, par3 + lw, par4 + 1.0D);

      case 3:
        return AxisAlignedBB.getBoundingBox(par2 + hi, par3 + 0.0D, par4 + 0.0D, par2 + 1.0D, par3 + 1.0D, par4 + 1.0D);

      case 2:
        return AxisAlignedBB.getBoundingBox(par2 + 0.0D, par3 + 0.0D, par4 + 0.0D, par2 + lw, par3 + 1.0D, par4 + 1.0D);

      case 5:
        return AxisAlignedBB.getBoundingBox(par2 + 0.0D, par3 + 0.0D, par4 + hi, par2 + 1.0D, par3 + 1.0D, par4 + 1.0D);

      case 4:
        return AxisAlignedBB.getBoundingBox(par2 + 0.0D, par3 + 0.0D, par4 + 0.0D, par2 + 1.0D, par3 + 1.0D, par4 + lw);
    } 

    return AxisAlignedBB.getBoundingBox(par2 + 0.0D, par3 + 0.0D, par4 + 0.0D, par2 + 1.0D, par3 + 1.0D, par4 + 1.0D);
  }

  @SideOnly(Side.CLIENT)
  public AxisAlignedBB getSelectedBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
    return getCollisionBoundingBoxFromPool(par1World, par2, par3, par4);
  }

  public void setBlockBoundsBasedOnState(IBlockAccess par1IWorld, int par2, int par3, int par4) {
    int dir = getGravityDirection(par1IWorld, par2, par3, par4);
    float lw = 0.5625F;
    float hi = 0.4375F;
    switch (dir) {
      case 1:
        setBlockBounds(0.0F, hi, 0.0F, 1.0F, 1.0F, 1.0F);
        break;
      case 0:
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, lw, 1.0F);
        break;
      case 3:
        setBlockBounds(hi, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        break;
      case 2:
        setBlockBounds(0.0F, 0.0F, 0.0F, lw, 1.0F, 1.0F);
        break;
      case 5:
        setBlockBounds(0.0F, 0.0F, hi, 1.0F, 1.0F, 1.0F);
        break;
      case 4:
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, lw);
        break;
    } 
  }

  public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, Block parBlock) {
    int meta = par1World.getBlockMetadata(par2, par3, par4);
    int[] headRel = DirectionConst.CHECKNEIGHBOR_LIST[meta & 0x7];
    
    if (par1World.getBlock(par2 + headRel[0], par3 + headRel[1], par4 + headRel[2]) != SMModContainer.StarBedHeadBlock)
    {
      par1World.setBlockToAir(par2, par3, par4);
    }
  }

  public Item getItemDropped(int par1, Random par2Random, int par3) {
    return SMModContainer.StarBedItem;
  }

  public ChunkCoordinates getBedSpawnPosition(IBlockAccess world, int x, int y, int z, EntityPlayer player) {
    return getNearestEmptyChunkCoordinates(world, x, y, z, 0);
  }
  private static final int[] CHECK_HEIGHT_LIST = new int[] { 0, -1, 1, 2 };
  @SideOnly(Side.CLIENT)
  public IIcon bedTopIcon;
  @SideOnly(Side.CLIENT)
  public IIcon bedEndIcon;
  
  public static ChunkCoordinates getNearestEmptyChunkCoordinates(IBlockAccess world, int parX, int parY, int parZ, int par4) { Block block = world.getBlock(parX, parY, parZ);
    if (block != SMModContainer.StarBedHeadBlock) {
      return null;
    }
    int gravDir = ((BlockStarBedHead)block).getGravityDirection(world, parX, parY, parZ);
    
    for (int i = 0; i < CHECK_HEIGHT_LIST.length; i++) {
      int range, height = CHECK_HEIGHT_LIST[i];
      switch (gravDir) {
        case 3:
          for (range = 1; range <= 3; range++) {
            for (int z = -range; z <= range; z++) {
              for (int y = -range; y <= range; y++) {
                if (world.isSideSolid(parX + 1 - height, parY + y, parZ + z, ForgeDirection.WEST, false) && isAirBlockOrGravityWall(world, parX - height, parY + y, parZ + z) && world.isAirBlock(parX - 1 - height, parY + y, parZ + z))
                {
                  return new ChunkCoordinates(parX - 1 - height, parY + y, parZ + z);
                }
              } 
            } 
          } 
          break;
        case 2:
          for (range = 1; range <= 3; range++) {
            for (int z = -range; z <= range; z++) {
              for (int y = -range; y <= range; y++) {
                if (world.isSideSolid(parX - 1 + height, parY + y, parZ + z, ForgeDirection.EAST, false) && isAirBlockOrGravityWall(world, parX + height, parY + y, parZ + z) && world.isAirBlock(parX + 1 + height, parY + y, parZ + z))
                {
                  return new ChunkCoordinates(parX + 1 + height, parY + y, parZ + z);
                }
              } 
            } 
          } 
          break;
        case 5:
          for (range = 1; range <= 3; range++) {
            for (int x = -range; x <= range; x++) {
              for (int y = -range; y <= range; y++) {
                if (world.isSideSolid(parX + x, parY + y, parZ + 1 - height, ForgeDirection.NORTH, false) && isAirBlockOrGravityWall(world, parX + x, parY + y, parZ - height) && world.isAirBlock(parX + x, parY + y, parZ - 1 - height))
                {
                  return new ChunkCoordinates(parX + x, parY + y, parZ - 1 - height);
                }
              } 
            } 
          } 
          break;
        case 4:
          for (range = 1; range <= 3; range++) {
            for (int x = -range; x <= range; x++) {
              for (int y = -range; y <= range; y++) {
                if (world.isSideSolid(parX + x, parY + y, parZ - 1 + height, ForgeDirection.SOUTH, false) && isAirBlockOrGravityWall(world, parX + x, parY + y, parZ + height) && world.isAirBlock(parX + x, parY + y, parZ + 1 + height))
                {
                  return new ChunkCoordinates(parX + x, parY + y, parZ + 1 + height);
                }
              } 
            } 
          } 
          break;
        case 1:
          for (range = 1; range <= 3; range++) {
            for (int x = -range; x <= range; x++) {
              for (int z = -range; z <= range; z++) {
                if (world.isSideSolid(parX + x, parY + 1 - height, parZ + z, ForgeDirection.DOWN, false) && isAirBlockOrGravityWall(world, parX + x, parY - height, parZ + z) && world.isAirBlock(parX + x, parY - 1 - height, parZ + z))
                {
                  return new ChunkCoordinates(parX + x, parY - 1 - height, parZ + z);
                }
              } 
            } 
          } 
          break;
        case 0:
          for (range = 1; range <= 3; range++) {
            for (int x = -range; x <= range; x++) {
              for (int z = -range; z <= range; z++) {
                if (world.isSideSolid(parX + x, parY - 1 + height, parZ + z, ForgeDirection.UP, false) && isAirBlockOrGravityWall(world, parX + x, parY + height, parZ + z) && world.isAirBlock(parX + x, parY + 1 + height, parZ + z))
                {
                  return new ChunkCoordinates(parX + x, parY + 1 + height, parZ + z);
                }
              } 
            } 
          } 
          break;
      } 
    
    } 
    return null; } @SideOnly(Side.CLIENT)
  private IIcon bedSideIcon; @SideOnly(Side.CLIENT)
  private IIcon bedStarIcon; public static final boolean isAirBlockOrGravityWall(IBlockAccess world, int parX, int parY, int parZ) {
    return (world.isAirBlock(parX, parY, parZ) || world.getBlock(parX, parY, parZ) == SMModContainer.GravityWallBlock);
  }

  public int getMobilityFlag() {
    return 1;
  }

  public void setBedOccupied(IBlockAccess world, int x, int y, int z, EntityPlayer player, boolean occupied) {
    int meta = world.getBlockMetadata(x, y, z);
    int[] headRel = DirectionConst.CHECKNEIGHBOR_LIST[meta & 0x7];
    Block block;
    if ((block = world.getBlock(x + headRel[0], y + headRel[1], z + headRel[2])) == SMModContainer.StarBedHeadBlock) {
      BlockStarBedHead.setBedOccupiedHead(world, x + headRel[0], y + headRel[1], z + headRel[2], occupied);
    }
  }

  @SideOnly(Side.CLIENT)
  public Item getItem(World par1World, int par2, int par3, int par4) {
    return SMModContainer.StarBedItem;
  }

  @SideOnly(Side.CLIENT)
  public int getRenderType() {
    return 4341808;
  }

  @SideOnly(Side.CLIENT)
  public boolean renderAsNormalBlock() {
    return false;
  }

  @SideOnly(Side.CLIENT)
  public IIcon getBedTopIcon() {
    return this.bedTopIcon;
  }

  @SideOnly(Side.CLIENT)
  public IIcon getBedEndIcon() {
    return this.bedEndIcon;
  }

  @SideOnly(Side.CLIENT)
  public IIcon getBedSideIcon() {
    return this.bedSideIcon;
  }

  @SideOnly(Side.CLIENT)
  public IIcon getBedStarIcon() {
    return this.bedStarIcon;
  }

  @SideOnly(Side.CLIENT)
  public void registerIcons(IIconRegister par1IconRegister) {
    this.bedTopIcon = par1IconRegister.registerIcon("bed_feet_top");
    this.bedEndIcon = par1IconRegister.registerIcon("bed_feet_end");
    this.bedSideIcon = par1IconRegister.registerIcon("bed_feet_side");
    this.bedStarIcon = par1IconRegister.registerIcon("starminer:bedstar");
  }

  @SideOnly(Side.CLIENT)
  public IIcon getIcon(int par1, int par2) {
    return this.bedTopIcon;
  }
}
