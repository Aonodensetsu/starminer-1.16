package jp.mc.ancientred.starminer.basics.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Iterator;
import java.util.Random;
import jp.mc.ancientred.starminer.api.Gravity;
import jp.mc.ancientred.starminer.api.GravityDirection;
import jp.mc.ancientred.starminer.basics.tileentity.TileEntityChestEx;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockChestEx
  extends BlockContainer
{
  private final Random rand = new Random();
  public final int chestType;
  private static final int CHESTMOUTHFACING_NORTH = 2;
  
  public BlockChestEx(int type) {
    super(Material.wood);
    this.chestType = type;
    setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
  }

  private static final int CHESTMOUTHFACING_EAST = 5;
  private static final int CHESTMOUTHFACING_SOUTH = 3;
  private static final int CHESTMOUTHFACING_WEST = 4;
  
  public boolean isOpaqueCube() {
    return false;
  }

  public boolean renderAsNormalBlock() {
    return false;
  }

  public int getRenderType() {
    return 22;
  }

  public void setBlockBoundsBasedOnState(IBlockAccess world, int parX, int parY, int parZ) {
    TileEntityChestEx te = getTileEntityChestEx(world, parX, parY, parZ);
    GravityDirection gDir = te.getGravityDirection();
    GravityDirection dirOpposite = GravityDirection.turnWayForNormal(gDir);
    
    AxisAlignedBB aabb = null;
    if (te.hasRelated()) {
      dirOpposite.rotateXYZAt(te.conv, te.relatedBlockX, te.relatedBlockY, te.relatedBlockZ, parX, parY, parZ);
      
      if (conpareXYZ(te.conv, parX, parY, parZ - 1))
      {
        aabb = AxisAlignedBB.getBoundingBox(0.0625D, 0.0D, 0.0D, 0.9375D, 0.875D, 0.9375D);
      
      }
      else if (conpareXYZ(te.conv, parX, parY, parZ + 1))
      {
        aabb = AxisAlignedBB.getBoundingBox(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.875D, 1.0D);
      
      }
      else if (conpareXYZ(te.conv, parX - 1, parY, parZ))
      {
        aabb = AxisAlignedBB.getBoundingBox(0.0D, 0.0D, 0.0625D, 0.9375D, 0.875D, 0.9375D);
      
      }
      else if (conpareXYZ(te.conv, parX + 1, parY, parZ))
      {
        aabb = AxisAlignedBB.getBoundingBox(0.0625D, 0.0D, 0.0625D, 1.0D, 0.875D, 0.9375D);
      }
    
    }
    else {
      
      aabb = AxisAlignedBB.getBoundingBox(0.0625D, 0.0D, 0.0625D, 1.0D, 0.875D, 0.9375D);
    } 
    
    if (aabb != null) {
      gDir.rotateAABBAt(aabb, parX, parY, parZ);
    }
  }

  public void onBlockPlacedBy(World world, int parX, int parY, int parZ, EntityLivingBase entityLivingBase, ItemStack itemStack) {
    GravityDirection gDir = Gravity.getGravityDirection((Entity)entityLivingBase);
    int[] conv = new int[3];

    gDir.rotateXYZAt(conv, parX, parY, parZ - 1, parX, parY, parZ);
    Block blockNorth = world.getBlock(conv[0], conv[1], conv[2]);
    boolean canConbBlockNorth = canConbineWith(gDir, (IBlockAccess)world, blockNorth, conv[0], conv[1], conv[2], parX, parY, parZ);

    gDir.rotateXYZAt(conv, parX, parY, parZ + 1, parX, parY, parZ);
    Block blockSouth = world.getBlock(conv[0], conv[1], conv[2]);
    boolean canConbBlockSouth = canConbineWith(gDir, (IBlockAccess)world, blockSouth, conv[0], conv[1], conv[2], parX, parY, parZ);

    gDir.rotateXYZAt(conv, parX - 1, parY, parZ, parX, parY, parZ);
    Block blockWest = world.getBlock(conv[0], conv[1], conv[2]);
    boolean canConbBlockWest = canConbineWith(gDir, (IBlockAccess)world, blockWest, conv[0], conv[1], conv[2], parX, parY, parZ);

    gDir.rotateXYZAt(conv, parX + 1, parY, parZ, parX, parY, parZ);
    Block blockEast = world.getBlock(conv[0], conv[1], conv[2]);
    boolean canConbBlockEast = canConbineWith(gDir, (IBlockAccess)world, blockEast, conv[0], conv[1], conv[2], parX, parY, parZ);

    int direction4 = getGravityFixedDirectionFromEntity(entityLivingBase);
    
    byte meta = 0;
    if (direction4 == 0)
    {
      meta = 2;
    }
    
    if (direction4 == 1)
    {
      meta = 5;
    }
    
    if (direction4 == 2)
    {
      meta = 3;
    }
    
    if (direction4 == 3)
    {
      meta = 4;
    }
    
    if (!canConbBlockNorth && !canConbBlockSouth && !canConbBlockWest && !canConbBlockEast) {

      setRelatedWithThis((IBlockAccess)world, parX, parY, parZ, parX, parY, parZ, gDir, false);
      world.setBlockMetadataWithNotify(parX, parY, parZ, meta, 3);
    
    }
    else {
      
      if (canConbBlockNorth) {

        if (meta == 2 || meta == 3)
        {
          if (blockWest == Blocks.air) {
            meta = 4;
          } else {
            meta = 5;
          } 
        }
        gDir.rotateXYZAt(conv, parX, parY, parZ - 1, parX, parY, parZ);
      }
      else if (canConbBlockSouth) {

        if (meta == 2 || meta == 3)
        {
          if (blockEast == Blocks.air) {
            meta = 5;
          } else {
            meta = 4;
          } 
        }
        gDir.rotateXYZAt(conv, parX, parY, parZ + 1, parX, parY, parZ);
      }
      else if (canConbBlockWest) {

        if (meta == 5 || meta == 4)
        {
          if (blockNorth == Blocks.air) {
            meta = 2;
          } else {
            meta = 3;
          } 
        }
        gDir.rotateXYZAt(conv, parX - 1, parY, parZ, parX, parY, parZ);
      
      }
      else {
        
        if (meta == 5 || meta == 4)
        {
          if (blockSouth == Blocks.air) {
            meta = 3;
          } else {
            meta = 2;
          } 
        }
        gDir.rotateXYZAt(conv, parX + 1, parY, parZ, parX, parY, parZ);
      } 

      setRelatedWithThis((IBlockAccess)world, conv[0], conv[1], conv[2], parX, parY, parZ, gDir, false);
      setRelatedWithThis((IBlockAccess)world, parX, parY, parZ, conv[0], conv[1], conv[2], gDir, true);
      
      world.setBlockMetadataWithNotify(conv[0], conv[1], conv[2], meta, 3);
      world.setBlockMetadataWithNotify(parX, parY, parZ, meta, 3);
    } 

    if (itemStack.hasDisplayName())
    {
      ((TileEntityChestEx)world.getTileEntity(parX, parY, parZ)).setCustomName(itemStack.getDisplayName()); } 
  }
  
  private void setRelatedWithThis(IBlockAccess world, int targetX, int targetY, int targetZ, int thisX, int thisY, int thisZ, GravityDirection gDir, boolean isSubBlock) {
    TileEntityChestEx te = getTileEntityChestEx(world, targetX, targetY, targetZ);
    te.relatedBlockX = thisX;
    te.relatedBlockY = thisY;
    te.relatedBlockZ = thisZ;
    te.isSubBlock = isSubBlock;
    te.setGravityDirection(gDir);
  }

  public boolean canPlaceBlockAt(World p_149742_1_, int p_149742_2_, int p_149742_3_, int p_149742_4_) {
    return true;
  }

  public void onNeighborBlockChange(World world, int parX, int parY, int parZ, Block block) {
    TileEntityChestEx te = getTileEntityChestEx((IBlockAccess)world, parX, parY, parZ);
    if (te != null) {
      
      Block relatedBlock = world.getBlock(te.relatedBlockX, te.relatedBlockY, te.relatedBlockZ);
      if (relatedBlock == null || relatedBlock == Blocks.air) {
        te.relatedBlockX = parX;
        te.relatedBlockY = parY;
        te.relatedBlockZ = parZ;
        te.isSubBlock = false;
        
        world.markBlockForUpdate(parX, parY, parZ);
      } else {
        TileEntityChestEx relatedTe = getTileEntityChestEx((IBlockAccess)world, te.relatedBlockX, te.relatedBlockY, te.relatedBlockZ);
        if (relatedTe == null || relatedTe.relatedBlockX != parX || relatedTe.relatedBlockY != parY || relatedTe.relatedBlockZ != parZ) {
          te.relatedBlockX = parX;
          te.relatedBlockY = parY;
          te.relatedBlockZ = parZ;
          te.isSubBlock = false;
          
          world.markBlockForUpdate(parX, parY, parZ);
        } 
      } 
    } 
  }

  public void breakBlock(World world, int parX, int parY, int parZ, Block block, int meta) {
    TileEntityChestEx tileentitychest = (TileEntityChestEx)world.getTileEntity(parX, parY, parZ);
    
    if (tileentitychest != null) {
      
      for (int i1 = 0; i1 < tileentitychest.getSizeInventory(); i1++) {
        
        ItemStack itemstack = tileentitychest.getStackInSlot(i1);
        
        if (itemstack != null) {
          
          float f = this.rand.nextFloat() * 0.8F + 0.1F;
          float f1 = this.rand.nextFloat() * 0.8F + 0.1F;

          for (float f2 = this.rand.nextFloat() * 0.8F + 0.1F; itemstack.stackSize > 0; world.spawnEntityInWorld((Entity)entityitem)) {
            
            int j1 = this.rand.nextInt(21) + 10;
            
            if (j1 > itemstack.stackSize)
            {
              j1 = itemstack.stackSize;
            }
            
            itemstack.stackSize -= j1;
            EntityItem entityitem = new EntityItem(world, (parX + f), (parY + f1), (parZ + f2), new ItemStack(itemstack.getItem(), j1, itemstack.getMetadata()));
            float f3 = 0.05F;
            entityitem.motionX = ((float)this.rand.nextGaussian() * f3);
            entityitem.motionY = ((float)this.rand.nextGaussian() * f3 + 0.2F);
            entityitem.motionZ = ((float)this.rand.nextGaussian() * f3);
            
            if (itemstack.hasTagCompound())
            {
              entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
            }
          } 
        } 
      } 
      
      world.updateNeighborsAboutBlockChange(parX, parY, parZ, block);
    } 
    
    super.breakBlock(world, parX, parY, parZ, block, meta);
  }

  public boolean onBlockActivated(World world, int parX, int parY, int parZ, EntityPlayer entityPlayer, int side, float hitX, float hitY, float hitZ) {
    if (world.isRemote)
    {
      return true;
    }

    IInventory iinventory = conInventory(world, parX, parY, parZ);
    
    if (iinventory != null)
    {
      entityPlayer.displayGUIChest(iinventory);
    }
    
    return true;
  }

  public IInventory conInventory(World world, int parX, int parY, int parZ) {
    TileEntityChestEx te = getTileEntityChestEx((IBlockAccess)world, parX, parY, parZ);
    if (te.hasRelated() && world.getBlock(te.relatedBlockX, te.relatedBlockY, te.relatedBlockZ) == this) {
      if (te.isSubBlock) {
        return (IInventory)new InventoryLargeChest("container.chestDouble", (IInventory)world.getTileEntity(parX, parY, parZ), (IInventory)world.getTileEntity(te.relatedBlockX, te.relatedBlockY, te.relatedBlockZ));
      }
      return (IInventory)new InventoryLargeChest("container.chestDouble", (IInventory)world.getTileEntity(te.relatedBlockX, te.relatedBlockY, te.relatedBlockZ), (IInventory)world.getTileEntity(parX, parY, parZ));
    } 
    
    return (IInventory)world.getTileEntity(parX, parY, parZ);
  }

  public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
    TileEntityChestEx tileentitychest = new TileEntityChestEx();
    return (TileEntity)tileentitychest;
  }

  public boolean canProvidePower() {
    return (this.chestType == 1);
  }

  public int isProvidingWeakPower(IBlockAccess p_149709_1_, int p_149709_2_, int p_149709_3_, int p_149709_4_, int p_149709_5_) {
    if (!canProvidePower())
    {
      return 0;
    }

    int i1 = ((TileEntityChestEx)p_149709_1_.getTileEntity(p_149709_2_, p_149709_3_, p_149709_4_)).numPlayersUsing;
    return MathHelper.clamp_int(i1, 0, 15);
  }

  public int isProvidingStrongPower(IBlockAccess p_149748_1_, int p_149748_2_, int p_149748_3_, int p_149748_4_, int p_149748_5_) {
    return (p_149748_5_ == 1) ? isProvidingWeakPower(p_149748_1_, p_149748_2_, p_149748_3_, p_149748_4_, p_149748_5_) : 0;
  }
  
  private static boolean isOcelotSittingOnTop(World p_149953_0_, int p_149953_1_, int p_149953_2_, int p_149953_3_) {
    EntityOcelot entityocelot;
    Iterator<Entity> iterator = p_149953_0_.getEntitiesWithinAABB(EntityOcelot.class, AxisAlignedBB.getBoundingBox(p_149953_1_, (p_149953_2_ + 1), p_149953_3_, (p_149953_1_ + 1), (p_149953_2_ + 2), (p_149953_3_ + 1))).iterator();

    do {
      if (!iterator.hasNext())
      {
        return false;
      }
      
      Entity entity = iterator.next();
      entityocelot = (EntityOcelot)entity;
    }
    while (!entityocelot.isSitting());
    
    return true;
  }

  public boolean hasComparatorInputOverride() {
    return true;
  }

  public int getComparatorInputOverride(World p_149736_1_, int p_149736_2_, int p_149736_3_, int p_149736_4_, int p_149736_5_) {
    return Container.calcRedstoneFromInventory(conInventory(p_149736_1_, p_149736_2_, p_149736_3_, p_149736_4_));
  }

  @SideOnly(Side.CLIENT)
  public void registerIcons(IIconRegister p_149651_1_) {
    this.blockIcon = p_149651_1_.registerIcon("planks_oak");
  }

  private TileEntityChestEx getTileEntityChestEx(IBlockAccess world, int parX, int parY, int parZ) {
    TileEntity te = world.getTileEntity(parX, parY, parZ);
    if (te != null && te instanceof TileEntityChestEx) {
      return (TileEntityChestEx)te;
    }
    return null;
  }
  
  private GravityDirection getGDirection(IBlockAccess world, int parX, int parY, int parZ) {
    TileEntity te = world.getTileEntity(parX, parY, parZ);
    if (te != null && te instanceof TileEntityChestEx) {
      return ((TileEntityChestEx)te).getGravityDirection();
    }
    return GravityDirection.upTOdown_YN;
  }
  
  private boolean conpareXYZ(int[] conv, int parX, int parY, int parZ) {
    return (conv[0] == parX && conv[1] == parY && conv[2] == parZ);
  }
  
  private int getGravityFixedDirectionFromEntity(EntityLivingBase entityLivingBase) {
    GravityDirection gDir = Gravity.getGravityDirection((Entity)entityLivingBase);
    Vec3 playerLookVec = entityLivingBase.getLookVec();
    GravityDirection dirOpposite = GravityDirection.turnWayForNormal(gDir);
    dirOpposite.rotateVec3(playerLookVec);
    double pitch = -Math.asin(playerLookVec.yCoord) * 57.29577951308232D;
    double yaw = -90.0D + Math.atan2(playerLookVec.zCoord, playerLookVec.xCoord) * 180.0D / Math.PI;
    
    return MathHelper.floor_double(yaw * 4.0D / 360.0D + 0.5D) & 0x3;
  }
  
  public boolean canConbineWith(GravityDirection gDir, IBlockAccess world, Block block, int targetX, int targetY, int targetZ, int thisX, int thisY, int thisZ) {
    if (block != this) return false; 
    TileEntityChestEx te = getTileEntityChestEx(world, targetX, targetY, targetZ);
    if (te == null || te.hasRelated() || te.getGravityDirection() != gDir) return false; 
    return true;
  }
  
  public boolean isConbinedFrom(GravityDirection gDir, IBlockAccess world, Block block, int targetX, int targetY, int targetZ, int thisX, int thisY, int thisZ) {
    if (block != this) return false; 
    TileEntityChestEx te = getTileEntityChestEx(world, targetX, targetY, targetZ);
    if (te != null && te.hasRelated() && te.getGravityDirection() == gDir) {
      return (te.relatedBlockX == thisX && te.relatedBlockY == thisY && te.relatedBlockZ == thisZ);
    }
    return false;
  }
}
