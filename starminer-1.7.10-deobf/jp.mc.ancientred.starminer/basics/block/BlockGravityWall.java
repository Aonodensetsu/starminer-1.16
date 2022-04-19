package jp.mc.ancientred.starminer.basics.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.mc.ancientred.starminer.api.Gravity;
import jp.mc.ancientred.starminer.api.GravityDirection;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockGravityWall
  extends Block
{
  public static final double HEIGHT_D = 0.0625D;
  public static final float HEIGHT_F = 0.0625F;
  private static final int GWALL_AFFECT_TICK = 15;
  
  public BlockGravityWall() {
    super(Material.rock);
    setHardness(9.0F);
    setResistance(2.0F);
    func_111047_d(0);
  }
  
  public boolean renderAsNormalBlock() {
    return false;
  }
  
  public boolean isOpaqueCube() {
    return false;
  }
  
  public int getRenderType() {
    return 4341804;
  }

  @SideOnly(Side.CLIENT)
  public int getRenderBlockPass() {
    return 1;
  }

  public int onBlockPlaced(World par1World, int par2, int par3, int par4, int side, float par6, float par7, float par8, int par9) {
    int j1 = par9;
    
    if (side == 0)
    {
      j1 = 1;
    }
    if (side == 1)
    {
      j1 = 0;
    }
    
    if (side == 2)
    {
      j1 = 5;
    }
    
    if (side == 3)
    {
      j1 = 4;
    }
    
    if (side == 4)
    {
      j1 = 3;
    }
    
    if (side == 5)
    {
      j1 = 2;
    }
    
    return j1;
  }

  public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
    double lw = 0.0625D;
    double hi = 0.9375D;
    switch (par1World.getBlockMetadata(par2, par3, par4)) {
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

  public void setBlockBoundsForItemRender() {
    func_111047_d(0);
  }

  public void setBlockBoundsBasedOnState(IBlockAccess par1IWorld, int par2, int par3, int par4) {
    func_111047_d(par1IWorld.getBlockMetadata(par2, par3, par4));
  }
  
  protected void func_111047_d(int par1) {
    float lw = 0.0625F;
    float hi = 0.9375F;
    switch (par1) {
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
  
  public void onEntityCollidedWithBlock(World par1World, int par2, int par3, int par4, Entity par5Entity) {
    if (par5Entity instanceof net.minecraft.entity.player.EntityPlayer) {
      Gravity gravity = Gravity.getGravityProp(par5Entity);
      if (par1World.isRemote) {
        
        par5Entity.fallDistance = 0.0F;
        
        if (gravity != null) {
          switch (par1World.getBlockMetadata(par2, par3, par4)) {
            case 1:
              gravity.setTemporaryGravityDirection(GravityDirection.downTOup_YP, 15);
              break;
            case 0:
              gravity.setTemporaryGravityDirection(GravityDirection.upTOdown_YN, 15);
              break;
            case 3:
              gravity.setTemporaryGravityDirection(GravityDirection.westTOeast_XP, 15);
              break;
            case 2:
              gravity.setTemporaryGravityDirection(GravityDirection.eastTOwest_XN, 15);
              break;
            case 5:
              gravity.setTemporaryGravityDirection(GravityDirection.northTOsouth_ZP, 15);
              break;
            case 4:
              gravity.setTemporaryGravityDirection(GravityDirection.southTOnorth_ZN, 15);
              break;
          } 
        
        }
      } else {
        par5Entity.fallDistance = 0.0F;

        if (gravity != null) gravity.acceptExceptionalGravityTick = 45; 
      } 
    } 
  }
}
