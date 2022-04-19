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

public class BlockStarBedHead
  extends Block
{
  public static final double HEIGHT_D = 0.5625D;
  public static final float HEIGHT_F = 0.5625F;
  @SideOnly(Side.CLIENT)
  public IIcon bedTopIcon;
  @SideOnly(Side.CLIENT)
  public IIcon bedEndIcon;
  @SideOnly(Side.CLIENT)
  private IIcon bedSideIcon;
  
  public BlockStarBedHead() {
    super(Material.cloth);
    setHardness(0.2F);
    disableStats();
  }

  public boolean isBed(IBlockAccess world, int x, int y, int z, EntityLivingBase player) {
    return true;
  }
  public int getGravityDirection(IBlockAccess world, int x, int y, int z) {
    int meta = world.getBlockMetadata(x, y, z);
    return meta & 0x7;
  }
  
  public int getConnectionDirection(IBlockAccess world, int x, int y, int z) {
    int[] bodyRel = DirectionConst.getBlockBedHeadNeighborBody(world, x, y, z);
    if (bodyRel != null && world.getBlock(x + bodyRel[0], y + bodyRel[1], z + bodyRel[2]) == SMModContainer.StarBedBodyBlock) {
      
      int meta = world.getBlockMetadata(x + bodyRel[0], y + bodyRel[1], z + bodyRel[2]);
      return meta & 0x7;
    } 
    return -1;
  }

  public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
    if (par1World.isRemote)
    {
      return true;
    }

    int meta = par1World.getBlockMetadata(par2, par3, par4);
    
    if (par1World.provider.canRespawnHere() && par1World.getBiomeGenForCoords(par2, par4) != BiomeGenBase.hell) {
      
      if (isBedOccupiedHead(meta)) {
        
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
        
        setBedOccupiedHead((IBlockAccess)par1World, par2, par3, par4, false);
      } 
      
      EntityPlayer.EnumStatus enumstatus = par5EntityPlayer.sleepInBedAt(par2, par3, par4);
      SMReflectionHelper.ignoreHasMovedFlg(par5EntityPlayer);
      
      if (enumstatus == EntityPlayer.EnumStatus.OK) {
        
        setBedOccupiedHead((IBlockAccess)par1World, par2, par3, par4, true);
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
    int[] bodyRel = DirectionConst.getBlockBedHeadNeighborBody((IBlockAccess)par1World, par2, par3, par4);
    if (bodyRel == null || par1World.getBlock(par2 + bodyRel[0], par3 + bodyRel[1], par4 + bodyRel[2]) != SMModContainer.StarBedBodyBlock)
    {
      par1World.setBlockToAir(par2, par3, par4);
    }
  }

  public Item getItemDropped(int par1, Random par2Random, int par3) {
    return SMModContainer.StarBedItem;
  }

  public ChunkCoordinates getBedSpawnPosition(IBlockAccess world, int x, int y, int z, EntityPlayer player) {
    return BlockStarBedBody.getNearestEmptyChunkCoordinates(world, x, y, z, 0);
  }

  public int getMobilityFlag() {
    return 1;
  }

  public static boolean isBedOccupiedHead(int par0) {
    return ((par0 & 0x8) != 0);
  }

  public static void setBedOccupiedHead(IBlockAccess world, int par1, int par2, int par3, boolean doSet) {
    if (world instanceof World) {
      int meta = world.getBlockMetadata(par1, par2, par3);
      meta &= 0x7;
      if (doSet)
      {
        meta |= 0x8;
      }
      ((World)world).setBlockMetadataWithNotify(par1, par2, par3, meta, 4);
    } 
  }

  public void setBedOccupied(IBlockAccess world, int x, int y, int z, EntityPlayer player, boolean occupied) {
    if (world instanceof World) {
      setBedOccupiedHead(world, x, y, z, occupied);
    }
  }

  public static void setPositionForStarBedSleepingPlayer(EntityPlayer player, int par1, int par2, int par3, int gravDir, int connDir) {
    double move = 0.3D;
    double setPad = 1.3D;
    double padX = 0.0D;
    double padY = 0.0D;
    double padZ = 0.0D;
    
    switch (connDir) {
      case 3:
        padX = setPad;
        break;
      case 2:
        padX = -setPad;
        break;
      case 5:
        padZ = setPad;
        break;
      case 4:
        padZ = -setPad;
        break;
      case 1:
        padY = setPad;
        break;
      case 0:
        padY = -setPad;
        break;
    } 
    
    switch (gravDir) {
      case 3:
        player.setPositionAndRotation(padX + par1 + 0.5D - move, padY + par2 + 0.5D, padZ + par3 + 0.5D, 0.0F, 0.0F);
        break;
      case 2:
        player.setPositionAndRotation(padX + par1 + 0.5D + move, padY + par2 + 0.5D, padZ + par3 + 0.5D, 0.0F, 0.0F);
        break;
      case 5:
        player.setPositionAndRotation(padX + par1 + 0.5D, padY + par2 + 0.5D, padZ + par3 + 0.5D - move, 0.0F, 0.0F);
        break;
      case 4:
        player.setPositionAndRotation(padX + par1 + 0.5D, padY + par2 + 0.5D, padZ + par3 + 0.5D + move, 0.0F, 0.0F);
        break;
      case 1:
        player.setPositionAndRotation(padX + par1 + 0.5D, padY + par2 + 0.5D - move, padZ + par3 + 0.5D, 0.0F, 0.0F);
        break;
      case 0:
        player.setPositionAndRotation(padX + par1 + 0.5D, padY + par2 + 0.5D + move, padZ + par3 + 0.5D, 0.0F, 0.0F);
        break;
    } 
  }

  @SideOnly(Side.CLIENT)
  public Item getItem(World par1World, int par2, int par3, int par4) {
    return SMModContainer.StarBedItem;
  }

  public void onBlockHarvested(World par1World, int par2, int par3, int par4, int par5, EntityPlayer par6EntityPlayer) {
    if (par6EntityPlayer.capabilities.isCreativeMode) {
      
      int[] bodyRel = DirectionConst.getBlockBedHeadNeighborBody((IBlockAccess)par1World, par2, par3, par4);
      if (bodyRel == null || par1World.getBlock(par2 + bodyRel[0], par3 + bodyRel[1], par4 + bodyRel[2]) != SMModContainer.StarBedBodyBlock)
      {
        par1World.setBlockToAir(par2, par3, par4);
      }
    } 
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
  public void registerIcons(IIconRegister par1IconRegister) {
    this.bedTopIcon = par1IconRegister.registerIcon("bed_head_top");
    this.bedEndIcon = par1IconRegister.registerIcon("bed_head_end");
    this.bedSideIcon = par1IconRegister.registerIcon("bed_head_side");
  }

  @SideOnly(Side.CLIENT)
  public IIcon getIcon(int par1, int par2) {
    return this.bedTopIcon;
  }
}
