package jp.mc.ancientred.starminer.basics.common;

import jp.mc.ancientred.starminer.api.IRotateSleepingViewHandler;
import jp.mc.ancientred.starminer.basics.SMModContainer;
import jp.mc.ancientred.starminer.basics.block.DirectionConst;
import jp.mc.ancientred.starminer.basics.block.bed.BlockStarBedHead;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class SleepingViewHandler implements IRotateSleepingViewHandler {
  public boolean rotateSleepingFPView() {
    EntityClientPlayerMP entityClientPlayerMP = (Minecraft.getMinecraft()).thePlayer;
    
    WorldClient worldClient = (Minecraft.getMinecraft()).theWorld;
    ChunkCoordinates chunkcoordinates = ((EntityPlayer)entityClientPlayerMP).playerLocation;
    Block block = worldClient.getBlock(chunkcoordinates.posX, chunkcoordinates.posY, chunkcoordinates.posZ);
    if (block == SMModContainer.StarBedHeadBlock) {
      int conDir = ((BlockStarBedHead)block).getConnectionDirection((IBlockAccess)worldClient, chunkcoordinates.posX, chunkcoordinates.posY, chunkcoordinates.posZ);
      if (conDir == -1) return true; 
      conDir = DirectionConst.OPPOSITE_CNV[conDir];
      int gravDir = ((BlockStarBedHead)block).getGravityDirection((IBlockAccess)worldClient, chunkcoordinates.posX, chunkcoordinates.posY, chunkcoordinates.posZ);
      
      float translateFixX = 0.0F;
      float translateFixY = 0.0F;
      float translateFixZ = 0.0F;
      
      switch (gravDir) {
        
        case 3:
          GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
          GL11.glRotatef(-90.0F, 0.0F, 0.0F, 1.0F);
          switch (conDir) {

            case 0:
              GL11.glRotatef(-180.0F, 1.0F, 0.0F, 0.0F);
              break;
            case 5:
              GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
              break;
            case 4:
              GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
              break;
          } 
          
          GL11.glTranslatef(0.5F, 0.0F, 0.0F);
          break;
        case 2:
          GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
          GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
          
          switch (conDir) {

            case 0:
              GL11.glRotatef(-180.0F, 1.0F, 0.0F, 0.0F);
              break;
            case 5:
              GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
              break;
            case 4:
              GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
              break;
          } 
          
          GL11.glTranslatef(-0.5F, 0.0F, 0.0F);
          break;
        case 5:
          GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
          switch (conDir) {
            case 3:
              GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
              break;
            case 2:
              GL11.glRotatef(-90.0F, 0.0F, 0.0F, 1.0F);
              break;

            case 0:
              GL11.glRotatef(-180.0F, 0.0F, 0.0F, 1.0F);
              break;
          } 
          
          GL11.glTranslatef(0.0F, 0.0F, 0.5F);
          break;
        case 4:
          GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
          GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
          switch (conDir) {
            case 3:
              GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
              break;
            case 2:
              GL11.glRotatef(-90.0F, 0.0F, 0.0F, 1.0F);
              break;

            case 0:
              GL11.glRotatef(-180.0F, 0.0F, 0.0F, 1.0F);
              break;
          } 
          
          GL11.glTranslatef(0.0F, 0.0F, -0.5F);
          break;
        
        case 1:
          GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
          switch (conDir) {
            case 3:
              GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
              break;
            case 2:
              GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
              break;
            case 5:
              GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
              break;
          } 

          GL11.glTranslatef(0.0F, 0.5F, 0.0F);
          break;
        case 0:
          switch (conDir) {
            case 3:
              GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
              break;
            case 2:
              GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
              break;

            case 4:
              GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
              break;
          } 
          
          GL11.glTranslatef(0.0F, -0.5F, 0.0F);
          break;
      } 
      
      return true;
    } 
    
    return false;
  }
  
  public boolean rotateTPPlayerSleeping(EntityPlayer player) {
    World world = player.worldObj;
    ChunkCoordinates chunkcoordinates = player.playerLocation;
    Block block = world.getBlock(chunkcoordinates.posX, chunkcoordinates.posY, chunkcoordinates.posZ);
    if (block == SMModContainer.StarBedHeadBlock) {
      int conDir = ((BlockStarBedHead)block).getConnectionDirection((IBlockAccess)world, chunkcoordinates.posX, chunkcoordinates.posY, chunkcoordinates.posZ);
      if (conDir == -1) return false; 
      conDir = DirectionConst.OPPOSITE_CNV[conDir];
      int gravDir = ((BlockStarBedHead)block).getGravityDirection((IBlockAccess)world, chunkcoordinates.posX, chunkcoordinates.posY, chunkcoordinates.posZ);
      
      switch (conDir) {
        case 3:
          GL11.glRotatef(-90.0F, 0.0F, 0.0F, 1.0F);
          break;
        case 2:
          GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
          break;

        case 0:
          GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
          break;
        case 5:
          GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
          break;
        case 4:
          GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
          break;
      } 
      return true;
    } 
    
    return false;
  }
}
