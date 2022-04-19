package jp.mc.ancientred.starminer.basics.tileentity.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Calendar;
import jp.mc.ancientred.starminer.api.GravityDirection;
import jp.mc.ancientred.starminer.basics.tileentity.TileEntityChestEx;
import net.minecraft.block.Block;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.model.ModelLargeChest;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class TileEntityChestRendererEx
  extends TileEntitySpecialRenderer
{
  private static final ResourceLocation textureTrappedDouble = new ResourceLocation("textures/entity/chest/trapped_double.png");
  private static final ResourceLocation textureChristmasDouble = new ResourceLocation("textures/entity/chest/christmas_double.png");
  private static final ResourceLocation textureNormalDouble = new ResourceLocation("textures/entity/chest/normal_double.png");
  private static final ResourceLocation textureTrapped = new ResourceLocation("textures/entity/chest/trapped.png");
  private static final ResourceLocation textureChristmas = new ResourceLocation("textures/entity/chest/christmas.png");
  private static final ResourceLocation textureNormal = new ResourceLocation("textures/entity/chest/normal.png");
  private ModelChest modelChest = new ModelChest();
  private ModelChest modelLargeChest = (ModelChest)new ModelLargeChest();
  
  private boolean isChristams;
  private static final String __OBFID = "CL_00000965";
  
  public TileEntityChestRendererEx() {
    Calendar calendar = Calendar.getInstance();
    
    if (calendar.get(2) + 1 == 12 && calendar.get(5) >= 24 && calendar.get(5) <= 26)
    {
      this.isChristams = true; } 
  }
  
  public void renderTileEntityAt(TileEntityChestEx tileEntityChestEx, double posX, double posY, double posZ, float partialTick) {
    int meta;
    ModelChest modelchest;
    GravityDirection gDir = tileEntityChestEx.getGravityDirection();

    if (!tileEntityChestEx.hasWorldObj()) {
      
      meta = 0;
    }
    else {
      
      Block block = tileEntityChestEx.getBlockType();
      meta = tileEntityChestEx.getBlockMetadata();
      
      if (block instanceof jp.mc.ancientred.starminer.basics.block.BlockChestEx && meta == 0)
      {
        meta = tileEntityChestEx.getBlockMetadata();
      }
    } 
    
    int adjacentChestInt = tileEntityChestEx.getAdjacentChestTo();
    
    if (adjacentChestInt == TileEntityChestEx.IS_adjacentChestZNeg) {
      return;
    }
    
    if (adjacentChestInt == TileEntityChestEx.IS_adjacentChestXNeg) {
      return;
    }

    if (adjacentChestInt == TileEntityChestEx.IS_adjacentChestNone) {

      modelchest = this.modelChest;
      
      if (tileEntityChestEx.getChestType() == 1)
      {
        
        bindTexture(textureTrapped);
      }
      else if (this.isChristams)
      {
        bindTexture(textureChristmas);
      }
      else
      {
        bindTexture(textureNormal);
      }
    
    }
    else {
      
      modelchest = this.modelLargeChest;
      
      if (tileEntityChestEx.getChestType() == 1) {

        
        bindTexture(textureTrappedDouble);
      }
      else if (this.isChristams) {
        
        bindTexture(textureChristmasDouble);
      }
      else {
        
        bindTexture(textureNormalDouble);
      } 
    } 
    
    GL11.glPushMatrix();
    GL11.glEnable(32826);
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    GL11.glTranslatef((float)posX, (float)posY + 1.0F, (float)posZ + 1.0F);
    GL11.glScalef(1.0F, -1.0F, -1.0F);
    GL11.glTranslatef(0.5F, 0.5F, 0.5F);
    short rotateAroundY = 0;
    
    GL11.glRotatef(-180.0F * gDir.rotX, 1.0F, 0.0F, 0.0F);
    GL11.glRotatef(180.0F * gDir.rotZ, 0.0F, 0.0F, 1.0F);
    
    if (meta == 2)
    {
      rotateAroundY = 180;
    }
    
    if (meta == 3)
    {
      rotateAroundY = 0;
    }
    
    if (meta == 4)
    {
      rotateAroundY = 90;
    }
    
    if (meta == 5)
    {
      rotateAroundY = -90;
    }
    
    if (meta == 2 && adjacentChestInt == TileEntityChestEx.IS_adjacentChestXPos)
    {
      GL11.glTranslatef(1.0F, 0.0F, 0.0F);
    }
    
    if (meta == 5 && adjacentChestInt == TileEntityChestEx.IS_adjacentChestZPos)
    {
      GL11.glTranslatef(0.0F, 0.0F, -1.0F);
    }
    
    GL11.glRotatef(rotateAroundY, 0.0F, 1.0F, 0.0F);
    
    GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
    float f1 = tileEntityChestEx.prevLidAngle + (tileEntityChestEx.lidAngle - tileEntityChestEx.prevLidAngle) * partialTick;

    f1 = 1.0F - f1;
    f1 = 1.0F - f1 * f1 * f1;
    modelchest.chestLid.rotateAngleX = -(f1 * 3.1415927F / 2.0F);
    modelchest.renderAll();
    GL11.glDisable(32826);
    GL11.glPopMatrix();
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
  }

  public void renderTileEntityAt(TileEntity p_147500_1_, double p_147500_2_, double p_147500_4_, double p_147500_6_, float p_147500_8_) {
    renderTileEntityAt((TileEntityChestEx)p_147500_1_, p_147500_2_, p_147500_4_, p_147500_6_, p_147500_8_);
  }
}
