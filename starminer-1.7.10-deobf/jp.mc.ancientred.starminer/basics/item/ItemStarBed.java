package jp.mc.ancientred.starminer.basics.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.mc.ancientred.starminer.api.Gravity;
import jp.mc.ancientred.starminer.api.GravityDirection;
import jp.mc.ancientred.starminer.basics.SMModContainer;
import jp.mc.ancientred.starminer.basics.block.DirectionConst;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class ItemStarBed
  extends Item {
  public ItemStarBed() {
    setTextureName("bed");
    setMaxStackSize(1);
  }

  public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10) {
    if (par3World.isRemote)
    {
      return true;
    }

    Gravity gravity = Gravity.getGravityProp((Entity)par2EntityPlayer);
    Vec3 lookVec = par2EntityPlayer.getLook(1.0F);

    int gravityDir = -1;

    int connectDir = -1;
    
    int[] bodyPlace = { 0, 0, 0 };
    int[] surfaceCheck = { 0, 0, 0 };
    
    ForgeDirection surfaceCheckDir = ForgeDirection.UNKNOWN;
    
    boolean skipCascade = false;

    
    switch (gravity.gravityDirection) {
      
      case northTOsouth_ZP:
        if (par7 != 2) return false; 
        bodyPlace[2] = -1;

        surfaceCheck[2] = 1;
        surfaceCheckDir = ForgeDirection.NORTH;

        gravityDir = 5;

        skipCascade = true;
      
      case southTOnorth_ZN:
        if (!skipCascade) {
          
          if (par7 != 3) return false; 
          bodyPlace[2] = 1;

          surfaceCheck[2] = -1;
          surfaceCheckDir = ForgeDirection.SOUTH;

          gravityDir = 4;
        } 

        if (Math.abs(lookVec.xCoord) > Math.abs(lookVec.yCoord)) {
          if (lookVec.xCoord > 0.0D) {
            
            connectDir = 2;
            break;
          } 
          connectDir = 3;
          break;
        } 
        if (lookVec.yCoord > 0.0D) {
          
          connectDir = 0;
          break;
        } 
        connectDir = 1;
        break;

      case westTOeast_XP:
        if (par7 != 4) return false; 
        bodyPlace[0] = -1;

        surfaceCheck[0] = 1;
        surfaceCheckDir = ForgeDirection.WEST;

        gravityDir = 3;

        skipCascade = true;
      
      case eastTOwest_XN:
        if (!skipCascade) {
          
          if (par7 != 5) return false; 
          bodyPlace[0] = 1;

          surfaceCheck[0] = -1;
          surfaceCheckDir = ForgeDirection.EAST;

          gravityDir = 2;
        } 

        if (Math.abs(lookVec.zCoord) > Math.abs(lookVec.yCoord)) {
          if (lookVec.zCoord > 0.0D) {
            
            connectDir = 4;
            break;
          } 
          connectDir = 5;
          break;
        } 
        if (lookVec.yCoord > 0.0D) {
          
          connectDir = 0;
          break;
        } 
        connectDir = 1;
        break;

      case downTOup_YP:
        if (par7 != 0) return false; 
        bodyPlace[1] = -1;

        surfaceCheck[1] = 1;
        surfaceCheckDir = ForgeDirection.DOWN;

        gravityDir = 1;

        skipCascade = true;
      
      case upTOdown_YN:
        if (!skipCascade) {
          
          if (par7 != 1) return false; 
          bodyPlace[1] = 1;

          surfaceCheck[1] = -1;
          surfaceCheckDir = ForgeDirection.UP;

          gravityDir = 0;
        } 

        if (Math.abs(lookVec.zCoord) > Math.abs(lookVec.xCoord)) {
          if (lookVec.zCoord > 0.0D) {
            
            connectDir = 4;
            break;
          } 
          connectDir = 5;
          break;
        } 
        if (lookVec.xCoord > 0.0D) {
          
          connectDir = 2;
          break;
        } 
        connectDir = 3;
        break;
    } 

    if (connectDir == -1) return false;

    int[] neighbourForHead = DirectionConst.CHECKNEIGHBOR_LIST[connectDir];
    
    int bodyX = par4 + bodyPlace[0];
    int bodyY = par5 + bodyPlace[1];
    int bodyZ = par6 + bodyPlace[2];
    
    int headX = bodyX + neighbourForHead[0];
    int headY = bodyY + neighbourForHead[1];
    int headZ = bodyZ + neighbourForHead[2];
    
    if (par2EntityPlayer.canPlayerEdit(bodyX, bodyY, bodyZ, par7, par1ItemStack) && par2EntityPlayer.canPlayerEdit(headX, headY, headZ, par7, par1ItemStack)) {

      if (par3World.isAirBlock(bodyX, bodyY, bodyZ) && par3World.isAirBlock(headX, headY, headZ) && par3World.isSideSolid(bodyX + surfaceCheck[0], bodyY + surfaceCheck[1], bodyZ + surfaceCheck[2], surfaceCheckDir) && par3World.isSideSolid(headX + surfaceCheck[0], headY + surfaceCheck[1], headZ + surfaceCheck[2], surfaceCheckDir)) {

        par3World.setBlock(bodyX, bodyY, bodyZ, SMModContainer.StarBedBodyBlock, connectDir, 3);
        
        if (par3World.getBlock(bodyX, bodyY, bodyZ) == SMModContainer.StarBedBodyBlock)
        {
          par3World.setBlock(headX, headY, headZ, SMModContainer.StarBedHeadBlock, gravityDir, 3);
        }
        
        par1ItemStack.stackSize--;
        return true;
      } 

      return false;
    } 

    return false;
  }

  @SideOnly(Side.CLIENT)
  public boolean requiresMultipleRenderPasses() {
    return true;
  }
  
  @SideOnly(Side.CLIENT)
  public int getRenderPasses(int metadata) {
    return 2;
  }
  
  @SideOnly(Side.CLIENT)
  public IIcon getIcon(ItemStack stack, int pass) {
    if (pass == 1)
    {
      return ((ItemStarContoler)SMModContainer.StarControlerItem).starMarkIcon;
    }
    return super.getIcon(stack, pass);
  }
}
