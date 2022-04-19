package jp.mc.ancientred.starminer.basics.block;

import jp.mc.ancientred.starminer.basics.Config;
import jp.mc.ancientred.starminer.basics.SMModContainer;
import jp.mc.ancientred.starminer.basics.SMReflectionHelper;
import jp.mc.ancientred.starminer.basics.common.VecUtils;
import jp.mc.ancientred.starminer.basics.entity.EntityStarSquid;
import jp.mc.ancientred.starminer.basics.tileentity.TileEntityNavigator;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class BlockManBazooka
  extends Block
{
  public BlockManBazooka() {
    super(Material.rock);
    setHardness(2.0F);
    setResistance(50.0F);
  }

  public boolean renderAsNormalBlock() {
    return false;
  }
  
  public boolean isOpaqueCube() {
    return false;
  }
  
  public int getRenderType() {
    return 4341806;
  }
  
  public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
    Vec3 shootPoint = getSpawnPoint(par1World, par2, par3, par4);
    if (shootPoint == null) return true;
    
    if (!par1World.isRemote) {
      EntityStarSquid entityStarSquid = new EntityStarSquid(par1World, true);
      
      entityStarSquid.setLocationAndAngles(shootPoint.xCoord, shootPoint.yCoord, shootPoint.zCoord, par5EntityPlayer.rotationYaw, par5EntityPlayer.rotationPitch);
      
      par1World.spawnEntityInWorld((Entity)entityStarSquid);
      par5EntityPlayer.mountEntity((Entity)entityStarSquid);
      SMReflectionHelper.ignoreHasMovedFlg(par5EntityPlayer);
      
      TileEntityNavigator teNavi = null;
      int i;
      label43: for (i = par2 - 1; i <= par2 + 1; i++) {
        for (int j = par3 - 1; j <= par3 + 1; j++) {
          for (int k = par4 - 1; k <= par4 + 1; k++) {
            if (SMModContainer.NavigatorBlock == par1World.getBlock(i, j, k) && ((BlockNavigator)SMModContainer.NavigatorBlock).isOn(par1World.getBlockMetadata(i, j, k))) {
              
              teNavi = (TileEntityNavigator)par1World.getTileEntity(i, j, k);
              
              break label43;
            } 
          } 
        } 
      } 
      if (teNavi != null) {
        
        ((Entity)entityStarSquid).motionX = teNavi.lookX * Config.bazookaStartSpeed;
        ((Entity)entityStarSquid).motionY = teNavi.lookY * Config.bazookaStartSpeed;
        ((Entity)entityStarSquid).motionZ = teNavi.lookZ * Config.bazookaStartSpeed;
      } else {
        
        int meta = par1World.getBlockMetadata(par2, par3, par4);
        switch (meta) {
          case 0:
            ((Entity)entityStarSquid).motionY = 1.3D;
            break;
          case 1:
            ((Entity)entityStarSquid).motionY = -1.3D;
            break;
          case 2:
            ((Entity)entityStarSquid).motionX = 1.3D;
            break;
          case 3:
            ((Entity)entityStarSquid).motionX = -1.3D;
            break;
          case 4:
            ((Entity)entityStarSquid).motionZ = 1.3D;
            break;
          case 5:
            ((Entity)entityStarSquid).motionZ = -1.3D;
            break;
        } 

      } 
    } else {
      par5EntityPlayer.playSound("fireworks.largeBlast", 0.8F, 1.2F);
    } 
    return true;
  }

  private Vec3 getSpawnPoint(World par1World, int par2, int par3, int par4) {
    int meta = par1World.getBlockMetadata(par2, par3, par4);
    int x = par2;
    int y = par3;
    int z = par4;
    for (int i = 5; i >= 1; i--) {
      switch (meta) {
        case 0:
          y = par3 + i;
          break;
        case 1:
          y = par3 - i;
          break;
        case 2:
          x = par2 + i;
          break;
        case 3:
          x = par2 - i;
          break;
        case 4:
          z = par4 + i;
          break;
        case 5:
          z = par4 - i;
          break;
      } 

      if (par1World.isAirBlock(x, y, z)) {
        return VecUtils.createVec3(x + 0.5D, y + 0.5D, z + 0.5D);
      }
    } 
    return null;
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
    double lw = 0.2D;
    double hi = 0.8D;
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
}
