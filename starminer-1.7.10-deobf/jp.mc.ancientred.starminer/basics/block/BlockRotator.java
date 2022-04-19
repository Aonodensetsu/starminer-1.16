package jp.mc.ancientred.starminer.basics.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.List;
import jp.mc.ancientred.starminer.api.Gravity;
import jp.mc.ancientred.starminer.basics.SMModContainer;
import jp.mc.ancientred.starminer.basics.SMReflectionHelper;
import jp.mc.ancientred.starminer.basics.dummy.DummyRotatedBlockAccess;
import jp.mc.ancientred.starminer.basics.dummy.DummyRotatedWorld;
import jp.mc.ancientred.starminer.basics.tileentity.TileEntityBlockRotator;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemReed;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockRotator
  extends Block
  implements ITileEntityProvider
{
  public static final float EXPAND_BND_SIZE = 0.01F;
  private static final double EXPAND_AABB = 10.0D;
  public boolean rotateBlockSelfFlg;
  
  public BlockRotator() {
    super(Material.glass);
    setHardness(1.0F);
    setResistance(1.0F);
    setLightLevel(1.0F);
    setStepSound(soundTypeGlass);
    setTextureName("starminer:rotator_yellow");
    
    setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
  }

  @SideOnly(Side.CLIENT)
  public void registerIcons(IIconRegister par1IconRegister) {
    this.blockIcon = par1IconRegister.registerIcon(getTextureName());
  }

  public boolean canRenderInPass(int pass) {
    return true;
  }
  
  public int getRenderBlockPass() {
    return 1;
  }

  public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z, EntityPlayer player) {
    return new ItemStack(SMModContainer.BlockRotatorBlock);
  }

  public TileEntity createNewTileEntity(World par1World, int metadata) {
    return (TileEntity)new TileEntityBlockRotator();
  }

  public void onBlockPlacedBy(World world, int parX, int parY, int parZ, EntityLivingBase parPlacer, ItemStack itemStack) {
    if (parPlacer != null && parPlacer instanceof EntityPlayer) {
      Gravity gravity = Gravity.getGravityProp((Entity)parPlacer);
      TileEntityBlockRotator te = getTileEntityBlockRotator((IBlockAccess)world, parX, parY, parZ);
      if (te != null) {
        te.setGravityDirection(gravity.gravityDirection);
      }
    } 
  }

  public void onNeighborBlockChange(World world, int parX, int parY, int parZ, Block par5Block) {
    TileEntityBlockRotator te = getTileEntityBlockRotator((IBlockAccess)world, parX, parY, parZ);
    if (te != null && 
      te.hasRelated()) {
      Block relatedBlock = world.getBlock(te.relatedBlockX, te.relatedBlockY, te.relatedBlockZ);
      if (relatedBlock instanceof BlockRotator) {
        TileEntityBlockRotator relatedTe = getTileEntityBlockRotator((IBlockAccess)world, te.relatedBlockX, te.relatedBlockY, te.relatedBlockZ);
        if (relatedTe != null && relatedTe.relatedBlockX == parX && relatedTe.relatedBlockY == parY && relatedTe.relatedBlockZ == parZ) {
          return;
        }
      } 

      world.setBlock(parX, parY, parZ, Blocks.air);
    } 
  }

  public void breakBlock(World world, int parX, int parY, int parZ, Block block, int meta) {
    if (!world.isRemote && world.getGameRules().getGameRuleBooleanValue("doTileDrops") && !isRestoringBlockSnapshots(world)) {
      TileEntityBlockRotator te = getTileEntityBlockRotator((IBlockAccess)world, parX, parY, parZ);
      if (te != null && !te.isSubBlock) {
        Item storedItem = te.getStoredItem();
        if (storedItem != null && storedItem != Items.cake) {
          int initialBlockMeta = te.getItemMetadata();
          ItemStack itemStack = new ItemStack(storedItem, 1, initialBlockMeta);
          if (((Boolean)this.captureDrops.get()).booleanValue()) {
            
            ((List<ItemStack>)this.capturedDrops.get()).add(itemStack);
            return;
          } 
          float f = 0.7F;
          double d0 = (world.rand.nextFloat() * f) + (1.0F - f) * 0.5D;
          double d1 = (world.rand.nextFloat() * f) + (1.0F - f) * 0.5D;
          double d2 = (world.rand.nextFloat() * f) + (1.0F - f) * 0.5D;
          EntityItem entityitem = new EntityItem(world, parX + d0, parY + d1, parZ + d2, itemStack);
          entityitem.delayBeforeCanPickup = 10;
          world.spawnEntityInWorld((Entity)entityitem);
        } 
      } 
    } 
    super.breakBlock(world, parX, parY, parZ, block, meta);
  }
  
  private boolean isRestoringBlockSnapshots(World world) {
    if (!SMModContainer.isIS1710OldWorld) {
      return world.restoringBlockSnapshots;
    }
    return false;
  }

  public boolean onBlockActivated(World world, int parX, int parY, int parZ, EntityPlayer par5EntityPlayer, int side, float par7, float par8, float par9) {
    TileEntityBlockRotator te = getTileEntityBlockRotator((IBlockAccess)world, parX, parY, parZ);
    if (te == null) return true;

    try { Block block = te.getStoredBlock();
      
      if (block != null && block != Blocks.air) {
        World toPass;
        
        if (block instanceof net.minecraft.block.BlockDoor) {
          DummyRotatedWorld dummyWorld = DummyRotatedWorld.get();
          toPass = (world == dummyWorld) ? world : dummyWorld.wrapp(world, te.getGravityDirection(), parX, parY, parZ);
        } else {
          toPass = world;
        } 
        return block.onBlockActivated(toPass, parX, parY, parZ, par5EntityPlayer, side, par7, par8, par9);
      }  }
    catch (Exception ex) { ex.printStackTrace(); }

    return false;
  }
  
  private boolean isVannilaBlock(Block block) {
    return block.getClass().getName().startsWith("net.minecraft");
  }

  public int getLightValue(IBlockAccess world, int parX, int parY, int parZ) {
    TileEntityBlockRotator te = getTileEntityBlockRotator(world, parX, parY, parZ);
    if (te != null) {
      Block block = te.getStoredBlock();
      if (block != null) {
        
        DummyRotatedBlockAccess dummyBlockAccess = DummyRotatedBlockAccess.get();
        IBlockAccess toPass = (world == dummyBlockAccess) ? world : dummyBlockAccess.wrapp(world, te.getGravityDirection(), parX, parY, parZ);
        return block.getLightValue(toPass, parX, parY, parZ);
      } 
    } 
    return this.lightValue;
  }
  
  public boolean renderAsNormalBlock() {
    return false;
  }
  
  public boolean isOpaqueCube() {
    return false;
  }
  
  public int getRenderType() {
    return 398378466;
  }

  @SideOnly(Side.CLIENT)
  public boolean shouldSideBeRendered(IBlockAccess world, int parX, int parY, int parZ, int side) {
    if (this.rotateBlockSelfFlg) {
      return (side != 0 && side != 1);
    }
    
    TileEntityBlockRotator te = getTileEntityBlockRotator(world, parX, parY, parZ);
    if (te != null) {
      Block block = te.getStoredBlock();
      if (block != null && block != Blocks.air) {
        
        DummyRotatedBlockAccess dummyBlockAccess = DummyRotatedBlockAccess.get();
        IBlockAccess toPass = (world == dummyBlockAccess) ? world : dummyBlockAccess.wrapp(world, te.getGravityDirection(), parX, parY, parZ);
        return block.shouldSideBeRendered(toPass, parX, parY, parZ, side);
      } 
    } 
    return super.shouldSideBeRendered(world, parX, parY, parZ, side);
  }

  public void addCollisionBoxesToList(World world, int parX, int parY, int parZ, AxisAlignedBB aabb, List<AxisAlignedBB> aabbList, Entity entity) {
    TileEntityBlockRotator te = getTileEntityBlockRotator((IBlockAccess)world, parX, parY, parZ);
    if (te != null) {
      Block block = te.getStoredBlock();
      if (block != null) {
        ArrayList<AxisAlignedBB> dummyList = thHoldDummyList.get();
        dummyList.clear();

        aabb.minX -= 10.0D; aabb.maxX += 10.0D;
        aabb.minY -= 10.0D; aabb.maxY += 10.0D;
        aabb.minZ -= 10.0D; aabb.maxZ += 10.0D;

        DummyRotatedWorld dummyWorld = DummyRotatedWorld.get();
        World toPass = (world == dummyWorld) ? world : dummyWorld.wrapp(world, te.getGravityDirection(), parX, parY, parZ);
        block.addCollisionBoxesToList(toPass, parX, parY, parZ, aabb, dummyList, entity);

        aabb.minX += 10.0D; aabb.maxX -= 10.0D;
        aabb.minY += 10.0D; aabb.maxY -= 10.0D;
        aabb.minZ += 10.0D; aabb.maxZ -= 10.0D;
        
        for (int i = 0; i < dummyList.size(); i++) {
          AxisAlignedBB axisalignedbb1 = dummyList.get(i);
          if (axisalignedbb1 != null) {
            axisalignedbb1 = te.getGravityDirection().rotateAABBAt(axisalignedbb1, parX, parY, parZ);
            if (aabb.intersectsWith(axisalignedbb1))
            {
              aabbList.add(axisalignedbb1);
            }
          } 
        } 
      } 
    } 
  }

  public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
    TileEntityBlockRotator te = getTileEntityBlockRotator((IBlockAccess)world, x, y, z);
    if (te != null) {
      Block block = te.getStoredBlock();
      if (block != null) {
        AxisAlignedBB storedBlockAABB = block.getCollisionBoundingBoxFromPool(world, x, y, z);
        if (storedBlockAABB == null) return null;
        
        return te.getGravityDirection().rotateAABBAt(storedBlockAABB, x, y, z);
      } 
    } 
    return AxisAlignedBB.getBoundingBox(x + this.minX, y + this.minY, z + this.minZ, x + this.maxX, y + this.maxY, z + this.maxZ);
  }

  public boolean isLadder(IBlockAccess world, int parX, int parY, int parZ, EntityLivingBase entity) {
    TileEntityBlockRotator te = getTileEntityBlockRotator(world, parX, parY, parZ);
    if (te != null) {
      Block block = te.getStoredBlock();
      if (block != null) {
        
        DummyRotatedBlockAccess dummyBlockAccess = DummyRotatedBlockAccess.get();
        IBlockAccess toPass = (world == dummyBlockAccess) ? world : dummyBlockAccess.wrapp(world, te.getGravityDirection(), parX, parY, parZ);
        return block.isLadder(toPass, parX, parY, parZ, entity);
      } 
    } 
    return false;
  }

  public static TileEntityBlockRotator getTileEntityBlockRotator(IBlockAccess world, int par2, int par3, int par4) {
    TileEntity te = world.getTileEntity(par2, par3, par4);
    if (te instanceof TileEntityBlockRotator) {
      return (TileEntityBlockRotator)te;
    }
    return null;
  }
  
  private Block convertItemToBlock(Item item) {
    Block block = null;
    if (item instanceof ItemReed) {
      
      block = SMReflectionHelper.getField_150935_a((ItemReed)item);
    } else if (item instanceof net.minecraft.item.ItemBlock) {
      
      block = Block.getBlockFromItem(item);
    } 
    return block;
  }

  private static ThreadLocal<ArrayList> thHoldDummyList = new ThreadLocal<ArrayList>() {
      protected ArrayList initialValue() {
        return new ArrayList();
      }
    };
}
