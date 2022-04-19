package jp.mc.ancientred.starminer.basics.tileentity;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import jp.mc.ancientred.starminer.api.Gravity;
import jp.mc.ancientred.starminer.api.GravityDirection;
import jp.mc.ancientred.starminer.api.IAttractableTileEntity;
import jp.mc.ancientred.starminer.basics.Config;
import jp.mc.ancientred.starminer.basics.SMModContainer;
import jp.mc.ancientred.starminer.basics.item.ItemLifeSoup;
import jp.mc.ancientred.starminer.basics.packet.SMPacketSender;
import jp.mc.ancientred.starminer.core.entity.EntityLivingGravitized;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class TileEntityGravityGenerator
  extends TileEntity
  implements IInventory, IAttractableTileEntity
{
  private static final String GRAVITY_NBT_KEY = "stmn.gravRange";
  private static final String STARRAD_NBT_KEY = "stmn.starRad";
  private static final String TYPE_NBT_KEY = "stmn.type";
  private static final String WORKX_NBT_KEY = "stmn.wkX";
  private static final String WORKY_NBT_KEY = "stmn.wkY";
  private static final String WORKZ_NBT_KEY = "stmn.wkZ";
  private static final String USEBUF_NBT_KEY = "stmn.usb";
  public static final int GTYPE_SPHERE = 0;
  public static final int GTYPE_SQUARE = 1;
  public static final int GTYPE_XCYLINDER = 2;
  public static final int GTYPE_YCYLINDER = 3;
  public static final int GTYPE_ZCYLINDER = 4;
  public static final int TYPE_NUM = 5;
  public static final int RESEND_ATTRACT_PACKET_TICK = 60;
  public static final int RESEND_ATTRACT_PACKET_TICK_ITEM = 120;
  private static final int SPAWN_SEARCH_HIGHT = 6;
  private static final int SPAWN_SEARCH_LOW = 3;
  public double gravityRange = 0.0D;
  
  public double starRad = 0.0D;
  
  public int type = 0;
  
  public short workStateX = 0;
  public short workStateY = 0;
  public short workStateZ = 0;
  
  public boolean workFast = false;
  
  public boolean useBufferArea = true;

  public void updateEntity() {
    if (!this.worldObj.isRemote) {
      long totalTime = this.worldObj.getTotalWorldTime();
      if (totalTime % Config.attractCheckTick == 0L) {
        addEffectsToPlayersOnServer();
      }
      long workSpeed = this.workFast ? 5L : 40L;
      if (totalTime % workSpeed == 0L) {
        doTerraformWork();
      }
      if (totalTime % 100L == 0L) {
        doGravitizeWork();
        
        doAnimalMobSpawning();
      } 
    } 
  }

  private void addEffectsToPlayersOnServer() {
    double centerX = this.xCoord + 0.5D;
    double centerY = this.yCoord + 0.5D;
    double centerZ = this.zCoord + 0.5D;
    
    AxisAlignedBB axisalignedbb = AxisAlignedBB.getBoundingBox(this.xCoord, this.yCoord, this.zCoord, (this.xCoord + 1), (this.yCoord + 1), (this.zCoord + 1)).expand(this.gravityRange, this.gravityRange, this.gravityRange);

    List list = this.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb);
    Iterator<EntityLivingBase> iterator = list.iterator();
    
    while (iterator.hasNext()) {
      EntityLivingBase entityG = iterator.next();

      if (entityG instanceof net.minecraft.entity.passive.EntityAmbientCreature) {
        continue;
      }
      if (entityG instanceof net.minecraft.entity.boss.IBossDisplayData) {
        continue;
      }
      if (entityG.isPlayerSleeping()) {
        continue;
      }
      Gravity gravity = Gravity.getGravityProp((Entity)entityG);
      
      if (gravity == null) {
        continue;
      }
      if (inGravityRange((Entity)entityG, gravity.gravityDirection, centerX, centerY, centerZ, this.gravityRange, this.type)) {

        if (!gravity.isAttracted()) {
          
          gravity.attractUpdateTickCount = 60;
          gravity.setAttractedBy(this); continue;
        }  if (gravity.isAttractedBy(this) && gravity.attractUpdateTickCount <= 0) {

          gravity.attractUpdateTickCount = 60;
          gravity.setAttractedBy(this);
        } 
      } 
    } 
  }

  public static boolean inGravityRange(Entity entity, GravityDirection gDirection, double attractCenterX, double attractCenterY, double attractCenterZ, double gravityRange, int type) {
    double entityPosX, entityPosY, entityPosZ, dWidthHalf = (entity.width / 2.0F);
    AxisAlignedBB bb = entity.boundingBox;
    switch (Gravity.getGravityDirection(entity)) {
      case northTOsouth_ZP:
        entityPosX = (bb.maxX + bb.minX) / 2.0D;
        entityPosY = (bb.maxY + bb.minY) / 2.0D;
        entityPosZ = bb.maxZ - dWidthHalf;
        break;
      case southTOnorth_ZN:
        entityPosX = (bb.maxX + bb.minX) / 2.0D;
        entityPosY = (bb.maxY + bb.minY) / 2.0D;
        entityPosZ = bb.minZ + dWidthHalf;
        break;
      case westTOeast_XP:
        entityPosX = bb.maxX - dWidthHalf;
        entityPosY = (bb.maxY + bb.minY) / 2.0D;
        entityPosZ = (bb.maxZ + bb.minZ) / 2.0D;
        break;
      case eastTOwest_XN:
        entityPosX = bb.minX + dWidthHalf;
        entityPosY = (bb.maxY + bb.minY) / 2.0D;
        entityPosZ = (bb.maxZ + bb.minZ) / 2.0D;
        break;
      case downTOup_YP:
        entityPosX = (bb.maxX + bb.minX) / 2.0D;
        entityPosY = bb.maxY - dWidthHalf;
        entityPosZ = (bb.maxZ + bb.minZ) / 2.0D;
        break;
      default:
        entityPosX = (bb.maxX + bb.minX) / 2.0D;
        entityPosY = bb.minY + dWidthHalf;
        entityPosZ = (bb.maxZ + bb.minZ) / 2.0D;
        break;
    } 
    
    switch (type) {
      case 1:
        return checkAttractedRangeSquare(attractCenterX, attractCenterY, attractCenterZ, entityPosX, entityPosY, entityPosZ, gravityRange);

      case 2:
        return checkAttractedRangeXbaseCylinder(attractCenterX, attractCenterY, attractCenterZ, entityPosX, entityPosY, entityPosZ, gravityRange);

      case 3:
        return checkAttractedRangeYbaseCylinder(attractCenterX, attractCenterY, attractCenterZ, entityPosX, entityPosY, entityPosZ, gravityRange);

      case 4:
        return checkAttractedRangeZbaseCylinder(attractCenterX, attractCenterY, attractCenterZ, entityPosX, entityPosY, entityPosZ, gravityRange);
    } 

    return checkAttractedRangeSphere(attractCenterX, attractCenterY, attractCenterZ, entityPosX, entityPosY, entityPosZ, gravityRange);
  }

  private static boolean checkAttractedRangeSphere(double centerX, double centerY, double centerZ, double playerX, double playerY, double playerZ, double argAttarctedRange) {
    double xRel = centerX - playerX;
    double zRel = centerZ - playerZ;
    double yRel = centerY - playerY;
    
    return (Math.sqrt(xRel * xRel + yRel * yRel + zRel * zRel) <= argAttarctedRange);
  }

  private static boolean checkAttractedRangeSquare(double centerX, double centerY, double centerZ, double playerX, double playerY, double playerZ, double argAttarctedRange) {
    return (playerX <= centerX + argAttarctedRange && playerX >= centerX - argAttarctedRange && playerY <= centerY + argAttarctedRange && playerY >= centerY - argAttarctedRange && playerZ <= centerZ + argAttarctedRange && playerZ >= centerZ - argAttarctedRange);
  }

  private static boolean checkAttractedRangeXbaseCylinder(double centerX, double centerY, double centerZ, double playerX, double playerY, double playerZ, double argAttarctedRange) {
    double yRel = centerY - playerY;
    double zRel = centerZ - playerZ;
    
    return (playerX <= centerX + argAttarctedRange && playerX >= centerX - argAttarctedRange && Math.sqrt(yRel * yRel + zRel * zRel) <= argAttarctedRange);
  }

  private static boolean checkAttractedRangeYbaseCylinder(double centerX, double centerY, double centerZ, double playerX, double playerY, double playerZ, double argAttarctedRange) {
    double xRel = centerX - playerX;
    double zRel = centerZ - playerZ;
    
    return (playerY <= centerY + argAttarctedRange && playerY >= centerY - argAttarctedRange && Math.sqrt(xRel * xRel + zRel * zRel) <= argAttarctedRange);
  }

  private static boolean checkAttractedRangeZbaseCylinder(double centerX, double centerY, double centerZ, double playerX, double playerY, double playerZ, double argAttarctedRange) {
    double xRel = centerX - playerX;
    double yRel = centerY - playerY;
    
    return (playerZ <= centerZ + argAttarctedRange && playerZ >= centerZ - argAttarctedRange && Math.sqrt(xRel * xRel + yRel * yRel) <= argAttarctedRange);
  }

  public boolean isStillInAttractedState(Entity entity) {
    return inGravityRange(entity, Gravity.getGravityDirection(entity), this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D, this.gravityRange, this.type);
  }

  public GravityDirection getCurrentGravity(Entity entity) {
    double centerX = this.xCoord + 0.5D;
    double centerY = this.yCoord + 0.5D;
    double centerZ = this.zCoord + 0.5D;

    double dWidthHalf = (entity.width / 2.0F);
    AxisAlignedBB bb = entity.boundingBox;
    switch (Gravity.getGravityDirection(entity))
    { case northTOsouth_ZP:
        entityPosX = (bb.maxX + bb.minX) / 2.0D;
        entityPosY = (bb.maxY + bb.minY) / 2.0D;
        entityPosZ = bb.maxZ - dWidthHalf;

        xRel = entityPosX - centerX;
        yRel = entityPosY - centerY;
        zRel = entityPosZ - centerZ;
        
        gDir = Gravity.getGravityDirection(entity);

        this; reverse = isGravityReverse(entity, false);
        
        return getGravityDirection(xRel, yRel, zRel, reverse, gDir);
	case southTOnorth_ZN: entityPosX = (bb.maxX + bb.minX) / 2.0D; entityPosY = (bb.maxY + bb.minY) / 2.0D; entityPosZ = bb.minZ + dWidthHalf; xRel = entityPosX - centerX; yRel = entityPosY - centerY; zRel = entityPosZ - centerZ; gDir = Gravity.getGravityDirection(entity); this; reverse = isGravityReverse(entity, false); return getGravityDirection(xRel, yRel, zRel, reverse, gDir);
	case westTOeast_XP: entityPosX = bb.maxX - dWidthHalf; entityPosY = (bb.maxY + bb.minY) / 2.0D; entityPosZ = (bb.maxZ + bb.minZ) / 2.0D; xRel = entityPosX - centerX; yRel = entityPosY - centerY; zRel = entityPosZ - centerZ; gDir = Gravity.getGravityDirection(entity); this; reverse = isGravityReverse(entity, false); return getGravityDirection(xRel, yRel, zRel, reverse, gDir);
	case eastTOwest_XN: entityPosX = bb.minX + dWidthHalf; entityPosY = (bb.maxY + bb.minY) / 2.0D; entityPosZ = (bb.maxZ + bb.minZ) / 2.0D; xRel = entityPosX - centerX; yRel = entityPosY - centerY; zRel = entityPosZ - centerZ; gDir = Gravity.getGravityDirection(entity); this; reverse = isGravityReverse(entity, false); return getGravityDirection(xRel, yRel, zRel, reverse, gDir);
	case downTOup_YP: entityPosX = (bb.maxX + bb.minX) / 2.0D; entityPosY = bb.maxY - dWidthHalf; entityPosZ = (bb.maxZ + bb.minZ) / 2.0D; xRel = entityPosX - centerX; yRel = entityPosY - centerY; zRel = entityPosZ - centerZ; gDir = Gravity.getGravityDirection(entity); this; reverse = isGravityReverse(entity, false); return getGravityDirection(xRel, yRel, zRel, reverse, gDir); }  
	double entityPosX = (bb.maxX + bb.minX) / 2.0D; 
	double entityPosY = bb.minY + dWidthHalf; 
	double entityPosZ = (bb.maxZ + bb.minZ) / 2.0D; 
	double xRel = entityPosX - centerX; 
	double yRel = entityPosY - centerY; 
	double zRel = entityPosZ - centerZ; 
	GravityDirection gDir = Gravity.getGravityDirection(entity); 
	this; 
	boolean reverse = isGravityReverse(entity, false); 
	return getGravityDirection(xRel, yRel, zRel, reverse, gDir);
  }
  private GravityDirection getGravityDirection(double xRel, double yRel, double zRel, boolean reverse, GravityDirection gDirNow) {
    GravityDirection gravityDirNew;
    double axisDiff, grangeHalf = this.gravityRange / 3.0D;
    
    switch (this.type)
    
    { 
      
      case 2:
        axisDiff = Math.abs(zRel) - Math.abs(yRel);
        if (this.useBufferArea && gDirNow != null && Math.abs(axisDiff) < 1.5D) {
          return gDirNow;
        }
        
        if (axisDiff > 0.0D) {
          
          if (zRel < 0.0D) {
            gravityDirNew = reverse ? GravityDirection.northTOsouth_ZP : GravityDirection.southTOnorth_ZN;
          } else {
            gravityDirNew = reverse ? GravityDirection.southTOnorth_ZN : GravityDirection.northTOsouth_ZP;
          }
        
        }
        else if (yRel < 0.0D) {
          gravityDirNew = reverse ? GravityDirection.downTOup_YP : GravityDirection.upTOdown_YN;
        } else {
          gravityDirNew = reverse ? GravityDirection.upTOdown_YN : GravityDirection.downTOup_YP;
        } 

        return gravityDirNew;
		case 3: axisDiff = Math.abs(xRel) - Math.abs(zRel); if (this.useBufferArea && gDirNow != null && Math.abs(axisDiff) < 1.5D) return gDirNow;  if (axisDiff > 0.0D) { if (xRel < 0.0D) { gravityDirNew = reverse ? GravityDirection.westTOeast_XP : GravityDirection.eastTOwest_XN; } else { gravityDirNew = reverse ? GravityDirection.eastTOwest_XN : GravityDirection.westTOeast_XP; }  } else if (zRel < 0.0D) { gravityDirNew = reverse ? GravityDirection.northTOsouth_ZP : GravityDirection.southTOnorth_ZN; } else { gravityDirNew = reverse ? GravityDirection.southTOnorth_ZN : GravityDirection.northTOsouth_ZP; }  return gravityDirNew;
		case 4: axisDiff = Math.abs(xRel) - Math.abs(yRel); if (this.useBufferArea && gDirNow != null && Math.abs(axisDiff) < 1.5D) return gDirNow;  if (axisDiff > 0.0D) { if (xRel < 0.0D) { gravityDirNew = reverse ? GravityDirection.westTOeast_XP : GravityDirection.eastTOwest_XN; } else { gravityDirNew = reverse ? GravityDirection.eastTOwest_XN : GravityDirection.westTOeast_XP; }  } else if (yRel < 0.0D) { gravityDirNew = reverse ? GravityDirection.downTOup_YP : GravityDirection.upTOdown_YN; } else { gravityDirNew = reverse ? GravityDirection.upTOdown_YN : GravityDirection.downTOup_YP; }  return gravityDirNew; }  
		if (Math.abs(xRel) > Math.abs(zRel) && Math.abs(xRel) > Math.abs(yRel)) { if (xRel < 0.0D) { gravityDirNew = !reverse ? GravityDirection.westTOeast_XP : GravityDirection.eastTOwest_XN; } else { gravityDirNew = !reverse ? GravityDirection.eastTOwest_XN : GravityDirection.westTOeast_XP; }  } else if (Math.abs(zRel) >= Math.abs(xRel) && Math.abs(zRel) > Math.abs(yRel)) { if (zRel < 0.0D) { gravityDirNew = !reverse ? GravityDirection.northTOsouth_ZP : GravityDirection.southTOnorth_ZN; } else { gravityDirNew = !reverse ? GravityDirection.southTOnorth_ZN : GravityDirection.northTOsouth_ZP; }  } else if (yRel < 0.0D) { gravityDirNew = !reverse ? GravityDirection.downTOup_YP : GravityDirection.upTOdown_YN; } else { gravityDirNew = !reverse ? GravityDirection.upTOdown_YN : GravityDirection.downTOup_YP; }  return gravityDirNew;
  }

  public static boolean isGravityReverse(Entity entity, boolean setReverseOff) {
    if (entity instanceof EntityPlayer) {
      EntityPlayer player = (EntityPlayer)entity;
      
      for (int i = 0; i < 9; i++) {
        ItemStack itemStack = player.inventory.mainInventory[i];
        if (itemStack != null && itemStack.getItem() == SMModContainer.GravityControllerItem) {
          if (itemStack.hasTagCompound()) {
            NBTTagCompound tag = itemStack.getTagCompound();
            boolean gcon = tag.getBoolean("stmn.g_reverse");
            if (gcon && setReverseOff) {
              tag.setBoolean("stmn.g_reverse", false);
            }
            return gcon;
          } 
          return false;
        } 
      } 
    } 
    
    return false;
  }

  private void doTerraformWork() {
    int itemStackIndexToUse = getItemStackForTerraform();

    if (itemStackIndexToUse == -1) {
      this.workFast = false;
      
      return;
    } 
    
    if (this.type == 1) {
      this.workFast = false;
      
      return;
    } 
    
    if (this.workStateY < -((int)this.starRad) || this.workStateY + this.yCoord < 0) {
      
      this.workFast = false;
      
      return;
    } 
    
    this.workFast = true;

    
    if (calclulateNextWorkSpot())
    {

      putBlock(itemStackIndexToUse);
    }
  }

  private void putBlock(int itemStackIndexToUse) {
    this.worldObj.setBlock(this.xCoord + this.workStateX, this.yCoord + this.workStateY, this.zCoord + this.workStateZ, Block.getBlockFromItem(this.gCoreItemStacks[itemStackIndexToUse].getItem()), this.gCoreItemStacks[itemStackIndexToUse].getMetadata(), 3);

    if (--(this.gCoreItemStacks[itemStackIndexToUse]).stackSize <= 0) {
      this.gCoreItemStacks[itemStackIndexToUse] = null;
    }
  }

  public void resetWorkState() {
    int radPlsOne = (int)this.starRad + 1;
    this.workStateX = (short)-radPlsOne;
    this.workStateZ = (short)-radPlsOne;
    this.workStateY = (short)radPlsOne;
    if (this.workStateY + this.yCoord > 255) {
      this.workStateY = (short)(255 - this.yCoord);
    }
  }

  private boolean calclulateNextWorkSpot() {
    int intRad = (int)this.starRad;
    
    int radPlsOne = intRad + 1;
    int radSub1 = intRad - 1;
    double radPow = (intRad * intRad);
    double radSub1Pow = (radSub1 * radSub1);

    int lookedCount = 0;

    this.workStateX = (short)-radPlsOne;
    
    this.workStateZ = (short)-radPlsOne;
    while ((this.workStateX = (short)(this.workStateX + 1)) <= radPlsOne || (this.workStateZ = (short)(this.workStateZ + 1)) <= radPlsOne || ((this.workStateY = (short)(this.workStateY - 1)) >= -radPlsOne && this.workStateY + this.yCoord >= 0)) {

      if (this.workStateX != 0 || this.workStateY != 0 || this.workStateZ != 0) {
        double distancePow; double distanceX; double distanceY; double distanceZ; switch (this.type) {
          case 2:
            distanceY = this.workStateY;
            distanceZ = this.workStateZ;
            
            distancePow = distanceY * distanceY + distanceZ * distanceZ;
            if (distancePow < radPow && 
              distancePow >= radSub1Pow) {
              return true;
            }
            break;
          
          case 3:
            distanceX = this.workStateX;
            distanceZ = this.workStateZ;
            
            distancePow = distanceX * distanceX + distanceZ * distanceZ;
            if (distancePow < radPow && 
              distancePow >= radSub1Pow) {
              return true;
            }
            break;
          
          case 4:
            distanceX = this.workStateX;
            distanceY = this.workStateY;
            
            distancePow = distanceX * distanceX + distanceY * distanceY;
            if (distancePow < radPow && 
              distancePow >= radSub1Pow) {
              return true;
            }
            break;
          
          default:
            distanceX = this.workStateX;
            distanceY = this.workStateY;
            distanceZ = this.workStateZ;
            
            distancePow = distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ;
            if (distancePow < radPow && 
              distancePow >= radSub1Pow) {
              return true;
            }
            break;
        } 
      
      } 
      if (++lookedCount > 50) return false;
    
    } 
    return false;
  }

  private int getItemStackForTerraform() {
    for (int i = 0; i < 27; i++) {
      if (this.gCoreItemStacks[i] != null) {
        Item item = this.gCoreItemStacks[i].getItem();
        if (item != null) {
          Block block = Block.getBlockFromItem(item);
          if (block != null && block != Blocks.air && AllowedBlockDictionary.isAllowed(block))
          {
            
            if (!(this.worldObj.provider instanceof jp.mc.ancientred.starminer.api.IZeroGravityWorldProvider) || block != Blocks.ice)
            {

              return i; } 
          }
        } 
      } 
    } 
    return -1;
  }

  private void doGravitizeWork() {
    if (this.worldObj.rand.nextInt(24) == 0) {
      ItemStack itemStack = this.gCoreItemStacks[this.gCoreItemStacks.length - 1];
      if (itemStack != null) {
        Item newItem = null;
        Item item = itemStack.getItem();
        if (item == Items.wheat_seeds) {
          newItem = SMModContainer.SeedGravizedItem;
        }
        if (item == Items.carrot) {
          newItem = SMModContainer.CarrotGravizedItem;
        }
        if (item == Items.potato) {
          newItem = SMModContainer.PotatoGravizedItem;
        }
        Block block = Block.getBlockFromItem(item);
        if (block != null) {
          if (block == Blocks.sapling) {
            newItem = Item.getItemFromBlock(SMModContainer.SaplingGravitizedBlock);
          }
          if (block == Blocks.yellow_flower) {
            newItem = Item.getItemFromBlock(SMModContainer.PlantYelGravitizedBlock);
          }
          if (block == Blocks.red_flower) {
            newItem = Item.getItemFromBlock(SMModContainer.PlantRedGravitizedBlock);
          }
        } 
        if (newItem != null) {
          this.gCoreItemStacks[this.gCoreItemStacks.length - 1] = new ItemStack(newItem, itemStack.stackSize, itemStack.getMetadata());
        }
      } 
    } 
  }

  public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
    super.readFromNBT(par1NBTTagCompound);
    this.gravityRange = par1NBTTagCompound.getDouble("stmn.gravRange");
    this.type = par1NBTTagCompound.getInteger("stmn.type");
    if (par1NBTTagCompound.hasKey("stmn.starRad")) {
      this.starRad = par1NBTTagCompound.getDouble("stmn.starRad");
    } else {
      this.starRad = this.gravityRange - 6.0D;
    } 
    if (this.starRad < 0.0D) this.starRad = 0.0D; 
    if (par1NBTTagCompound.hasKey("stmn.wkX")) {
      this.workStateX = par1NBTTagCompound.getShort("stmn.wkX");
      this.workStateY = par1NBTTagCompound.getShort("stmn.wkY");
      this.workStateZ = par1NBTTagCompound.getShort("stmn.wkZ");
    } else {
      resetWorkState();
    } 
    this.useBufferArea = par1NBTTagCompound.getBoolean("stmn.usb");

    
    if (par1NBTTagCompound.hasKey("Items")) {
      NBTTagList nbttaglist = par1NBTTagCompound.getTagList("Items", 10);
      for (int i = 0; i < nbttaglist.tagCount(); i++) {
        
        NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
        byte b0 = nbttagcompound1.getByte("Slot");
        
        if (b0 >= 0 && b0 < this.gCoreItemStacks.length)
        {
          this.gCoreItemStacks[b0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
        }
      } 
    } 
  }

  public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
    super.writeToNBT(par1NBTTagCompound);
    par1NBTTagCompound.setDouble("stmn.gravRange", this.gravityRange);
    par1NBTTagCompound.setInteger("stmn.type", this.type);
    par1NBTTagCompound.setDouble("stmn.starRad", this.starRad);
    par1NBTTagCompound.setShort("stmn.wkX", this.workStateX);
    par1NBTTagCompound.setShort("stmn.wkY", this.workStateY);
    par1NBTTagCompound.setShort("stmn.wkZ", this.workStateZ);
    par1NBTTagCompound.setBoolean("stmn.usb", this.useBufferArea);

    NBTTagList nbttaglist = new NBTTagList();
    for (int i = 0; i < this.gCoreItemStacks.length; i++) {
      
      if (this.gCoreItemStacks[i] != null) {
        
        NBTTagCompound nbttagcompound1 = new NBTTagCompound();
        nbttagcompound1.setByte("Slot", (byte)i);
        this.gCoreItemStacks[i].writeToNBT(nbttagcompound1);
        nbttaglist.appendTag((NBTBase)nbttagcompound1);
      } 
    } 
    par1NBTTagCompound.setTag("Items", (NBTBase)nbttaglist);
  }

  public Packet getDescriptionPacket() {
    return SMPacketSender.createTEGcoreDescriptionPacket(this.xCoord, this.yCoord, this.zCoord, this.gravityRange, this.starRad, this.type, this.useBufferArea);
  }

  public void fixInValidRange() {
    if (this.gravityRange < 0.0D) {
      this.gravityRange = 0.0D;
    }
    if (this.gravityRange > Config.maxGravityRad) {
      this.gravityRange = Config.maxGravityRad;
    }
    
    if (this.starRad < 0.0D) {
      this.starRad = 0.0D;
    }
    if (this.starRad > Config.maxStarRad) {
      this.starRad = Config.maxStarRad;
    }
  }

  private ItemStack[] gCoreItemStacks = new ItemStack[28];

  public int getSizeInventory() {
    return this.gCoreItemStacks.length;
  }
  
  public ItemStack getStackInSlot(int par1) {
    return this.gCoreItemStacks[par1];
  }
  
  public ItemStack decrStackSize(int par1, int par2) {
    if (this.gCoreItemStacks[par1] != null) {

      if ((this.gCoreItemStacks[par1]).stackSize <= par2) {
        
        ItemStack itemStack = this.gCoreItemStacks[par1];
        this.gCoreItemStacks[par1] = null;
        return itemStack;
      } 

      ItemStack itemstack = this.gCoreItemStacks[par1].splitStack(par2);
      
      if ((this.gCoreItemStacks[par1]).stackSize == 0)
      {
        this.gCoreItemStacks[par1] = null;
      }
      
      return itemstack;
    } 

    return null;
  }

  public ItemStack getStackInSlotOnClosing(int par1) {
    if (this.gCoreItemStacks[par1] != null) {
      
      ItemStack itemstack = this.gCoreItemStacks[par1];
      this.gCoreItemStacks[par1] = null;
      return itemstack;
    } 

    return null;
  }

  public void setInventorySlotContents(int par1, ItemStack par2ItemStack) {
    this.gCoreItemStacks[par1] = par2ItemStack;
    
    if (par2ItemStack != null && par2ItemStack.stackSize > getInventoryStackLimit())
    {
      par2ItemStack.stackSize = getInventoryStackLimit();
    }
  }

  public String getInventoryName() {
    return "";
  }
  
  public boolean isCustomInventoryName() {
    return true;
  }
  
  public int getInventoryStackLimit() {
    return 64;
  }
  
  public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer) {
    return (this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this) ? false : ((par1EntityPlayer.getDistance(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D) < 64.0D));
  }

  public void openChest() {}

  public void closeChest() {}

  public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
    return true;
  }

  public boolean isEntityMateableNow(EntityLivingBase entityLivingBase) {
    if (entityLivingBase instanceof EntityVillager) {
      
      ItemStack itemStack = this.gCoreItemStacks[this.gCoreItemStacks.length - 1];
      return ItemLifeSoup.isStackLifeSeedVillager(itemStack);
    } 
    return false;
  }
  
  public void notifyMate(EntityLivingBase entityLivingBase) {
    if (entityLivingBase instanceof EntityVillager) {
      
      ItemStack itemStack = this.gCoreItemStacks[this.gCoreItemStacks.length - 1];
      if (ItemLifeSoup.isStackLifeSeedVillager(itemStack)) {
        itemStack.stackSize--;
        if (itemStack.stackSize <= 0) {
          this.gCoreItemStacks[this.gCoreItemStacks.length - 1] = null;
        }
      } 
    } 
  }

  private void doAnimalMobSpawning() {
    if (this.type == 1) {
      return;
    }
    if (!this.worldObj.getGameRules().getGameRuleBooleanValue("doMobSpawning"))
      return; 
    if (this.worldObj.rand.nextInt(6) != 0)
      return;  try {
      double relX, relY, relZ, angleRad2, cos, radPls1; int zz, xx;
      ItemStack itemStack = this.gCoreItemStacks[this.gCoreItemStacks.length - 1];
      
      boolean isDirtySoup = ItemLifeSoup.isStackLifeSeedAnimalDirty(itemStack);
      if (!isDirtySoup && !ItemLifeSoup.isStackLifeSeedAnimal(itemStack)) {
        return;
      }

      Random rand = this.worldObj.rand;
      double rad = this.starRad;
      double radSub1 = rad - 1.0D;
      if (rad <= 3.0D)
        return;  boolean hasSpawnedMob = false;
      
      double angleRad = rand.nextDouble() * Math.PI * 2.0D;
      switch (this.type) {
        case 2:
          relX = rand.nextDouble() * rad * 2.0D - rad;
          relY = Math.sin(angleRad) * radSub1;
          relZ = Math.cos(angleRad) * radSub1;
          break;
        
        case 3:
          relX = Math.sin(angleRad) * radSub1;
          relY = rand.nextDouble() * rad * 2.0D - rad;
          relZ = Math.cos(angleRad) * radSub1;
          break;
        
        case 4:
          relX = Math.sin(angleRad) * radSub1;
          relY = Math.cos(angleRad) * radSub1;
          relZ = rand.nextDouble() * rad * 2.0D - rad;
          break;
        
        default:
          angleRad2 = rand.nextDouble() * Math.PI * 2.0D;
          cos = Math.cos(angleRad2);
          radPls1 = rad + 1.0D;
          relY = Math.sin(angleRad2) * radPls1;
          relX = Math.cos(angleRad) * cos * radPls1;
          relZ = Math.sin(angleRad) * cos * radPls1;
          break;
      } 

      int checkY = (int)(relY + this.yCoord + 0.5D);
      if (checkY <= 0 || checkY >= 255)
        return; 
      GravityDirection gDir = getGravityDirection(relX, relY, relZ, false, (GravityDirection)null);
      int targetX = MathHelper.floor_double(relX + this.xCoord + 0.5D);
      int targetY = checkY;
      int targetZ = MathHelper.floor_double(relZ + this.zCoord + 0.5D);
      
      World world = this.worldObj;
      int vSearchDirectionDwn = -1;
      
      int fullAirLayerCount = 0;
      switch (gDir) { case northTOsouth_ZP:
          vSearchDirectionDwn = 1;
        case southTOnorth_ZN:
          searchStart = targetZ + -vSearchDirectionDwn * 6;
          searchEnd = targetZ + vSearchDirectionDwn * 3;
          if (vSearchDirectionDwn > 0)
          { if (searchStart <= this.zCoord && searchEnd >= this.zCoord) searchStart = this.zCoord + 1;
             }
          else if (searchStart >= this.zCoord && searchEnd <= this.zCoord) { searchStart = this.zCoord - 1; }

          validatorWay = (vSearchDirectionDwn > 0);
          for (zz = searchStart; (validatorWay && zz <= searchEnd) || (!validatorWay && zz >= searchEnd); zz += vSearchDirectionDwn) {
            if (fullAirLayerCount > 2 && 
              world.getBlock(targetX, targetY, zz).getMaterial().isSolid()) {
              
              if (tryMobSpawnInPoint(gDir, targetX, targetY, zz - vSearchDirectionDwn, isDirtySoup) && 
                --itemStack.stackSize <= 0) {
                this.gCoreItemStacks[this.gCoreItemStacks.length - 1] = null;
              }
              
              return;
            } 
            
            int i;
            label159: for (i = targetX - 1; i <= targetX + 1; i++) {
              for (int j = targetY - 1; j <= targetY + 1; j++) {
                Block block = world.getBlock(i, j, zz);
                if (block.getMaterial().blocksMovement()) {
                  fullAirLayerCount = 0;
                  
                  break label159;
                } 
              } 
            } 
            fullAirLayerCount++;
          } 
          return;
        case westTOeast_XP:
          vSearchDirectionDwn = 1;
        case eastTOwest_XN:
          searchStart = targetX + -vSearchDirectionDwn * 6;
          searchEnd = targetX + vSearchDirectionDwn * 3;
          if (vSearchDirectionDwn > 0)
          { if (searchStart <= this.xCoord && searchEnd >= this.xCoord) searchStart = this.xCoord + 1;
             }
          else if (searchStart >= this.xCoord && searchEnd <= this.xCoord) { searchStart = this.xCoord - 1; }

          validatorWay = (vSearchDirectionDwn > 0);
          for (xx = searchStart; (validatorWay && xx <= searchEnd) || (!validatorWay && xx >= searchEnd); xx += vSearchDirectionDwn) {
            if (fullAirLayerCount > 2 && 
              world.getBlock(xx, targetY, targetZ).getMaterial().isSolid()) {
              
              if (tryMobSpawnInPoint(gDir, xx - vSearchDirectionDwn, targetY, targetZ, isDirtySoup) && 
                --itemStack.stackSize <= 0) {
                this.gCoreItemStacks[this.gCoreItemStacks.length - 1] = null;
              }
              
              return;
            } 
            
            int i;
            label160: for (i = targetY - 1; i <= targetY + 1; i++) {
              for (int j = targetZ - 1; j <= targetZ + 1; j++) {
                Block block = world.getBlock(xx, i, j);
                if (block.getMaterial().blocksMovement()) {
                  fullAirLayerCount = 0;
                  
                  break label160;
                } 
              } 
            } 
            fullAirLayerCount++;
          } 
          return;
        case downTOup_YP:
          vSearchDirectionDwn = 1; break; }
      
      int searchStart = targetY + -vSearchDirectionDwn * 6;
      int searchEnd = targetY + vSearchDirectionDwn * 3;
      if (vSearchDirectionDwn > 0)
      { if (searchStart <= this.yCoord && searchEnd >= this.yCoord) searchStart = this.yCoord + 1;
         }
      else if (searchStart >= this.yCoord && searchEnd <= this.yCoord) { searchStart = this.yCoord - 1; }

      boolean validatorWay = (vSearchDirectionDwn > 0); int yy;
      for (yy = searchStart; (validatorWay && yy <= searchEnd) || (!validatorWay && yy >= searchEnd); yy += vSearchDirectionDwn) {
        if (fullAirLayerCount > 2 && 
          world.getBlock(targetX, yy, targetZ).getMaterial().isSolid()) {
          
          if (tryMobSpawnInPoint(gDir, targetX, yy - vSearchDirectionDwn, targetZ, isDirtySoup) && 
            --itemStack.stackSize <= 0) {
            this.gCoreItemStacks[this.gCoreItemStacks.length - 1] = null;
          }
          
          return;
        } 
        
        int i;
        label161: for (i = targetX - 1; i <= targetX + 1; i++) {
          for (int j = targetZ - 1; j <= targetZ + 1; j++) {
            Block block = world.getBlock(i, yy, j);
            if (block.getMaterial().blocksMovement()) {
              fullAirLayerCount = 0;
              
              break label161;
            } 
          } 
        } 
        fullAirLayerCount++;

      }

    }
    catch (Exception ex) {}
  }
  private static final Class[] PURE_ANIMALS_SPAWN_LIST = new Class[] { EntityVillager.class, EntityChicken.class, EntityCow.class, EntityMooshroom.class, EntityPig.class, EntitySheep.class, EntityHorse.class, EntityOcelot.class, EntityWolf.class };

  private static final Class[] DIRTY_ANIMALS_SPAWN_LIST = new Class[] { EntityVillager.class, EntityChicken.class, EntityCow.class, EntityMooshroom.class, EntityPig.class, EntitySheep.class, EntityHorse.class, EntityOcelot.class, EntityWolf.class, EntityCreeper.class, EntityCreeper.class, EntityCreeper.class, EntityCreeper.class, EntityCreeper.class, EntityCreeper.class, EntityCreeper.class, EntityCreeper.class, EntityCreeper.class };
  
  private boolean tryMobSpawnInPoint(GravityDirection gDir, int targetX, int targetY, int targetZ, boolean isDirtySoup) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
    Class[] targetList = isDirtySoup ? DIRTY_ANIMALS_SPAWN_LIST : PURE_ANIMALS_SPAWN_LIST;
    
    EntityLiving entityliving = targetList[this.worldObj.rand.nextInt(targetList.length)].getConstructor(new Class[] { World.class }).newInstance(new Object[] { this.worldObj });
    entityliving.setLocationAndAngles(targetX + 0.5D, targetY + 0.5D, targetZ + 0.5D, MathHelper.wrapAngleTo180_float(this.worldObj.rand.nextFloat() * 360.0F), 0.0F);
    entityliving.rotationYawHead = entityliving.rotationYaw;
    entityliving.renderYawOffset = entityliving.rotationYaw;
    entityliving.onSpawnWithEgg((IEntityLivingData)null);
    Object cast = entityliving;
    Gravity gravity = Gravity.getGravityProp((Entity)entityliving);
    if (cast instanceof EntityLivingGravitized && gravity != null) {
      ((EntityLivingGravitized)cast).preSetSpawnGravity(gDir, this.xCoord, this.yCoord, this.zCoord);
    }

    return this.worldObj.spawnEntityInWorld((Entity)entityliving);
  }
  
  public void setDirtyLifeSoup(int itemCnt) {
    this.gCoreItemStacks[this.gCoreItemStacks.length - 1] = new ItemStack(SMModContainer.LifeSoupItem, itemCnt, 1);
  }
}
