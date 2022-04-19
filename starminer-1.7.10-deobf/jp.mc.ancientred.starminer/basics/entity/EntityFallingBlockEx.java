package jp.mc.ancientred.starminer.basics.entity;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.material.Material;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityFallingBlockEx
  extends EntityArrow
  implements IEntityAdditionalSpawnData {
  private Block block;
  public int blockMeta;
  public int fallTime;
  public double exceedRange;
  public double spawnPosX;
  public double spawnPosY;
  public double spawnPosZ;
  public boolean shouldDropItem;
  private boolean isBreakingAnvil;
  private boolean isAnvil;
  private int fallHurtMax;
  private float fallHurtAmount;
  
  public EntityFallingBlockEx(World p_i1706_1_) {
    super(p_i1706_1_);
    this.shouldDropItem = true;
    this.fallHurtMax = 40;
    this.fallHurtAmount = 2.0F;
  }
  
  public EntityFallingBlockEx(World world, double posX, double posY, double posZ, Block block) {
    this(world, posX, posY, posZ, block, 0);
  }
  
  public EntityFallingBlockEx(World world, double posX, double poxY, double posZ, Block block, int meta) {
    super(world);
    this.shouldDropItem = true;
    this.exceedRange = 3.0D;
    this.fallHurtMax = 40;
    this.fallHurtAmount = 2.0F;
    this.block = block;
    this.blockMeta = meta;
    this.preventEntitySpawning = true;
    setSize(0.2F, 0.2F);
    this.yOffset = this.height / 2.0F;
    setPosition(posX, poxY, posZ);
    this.motionX = 0.0D;
    this.motionY = 0.0D;
    this.motionZ = 0.0D;
    this.spawnPosX = this.prevPosX = posX;
    this.spawnPosY = this.prevPosY = poxY;
    this.spawnPosZ = this.prevPosZ = posZ;
  }
  
  public void setExceedRange(double distance) {
    this.exceedRange = distance;
    if (this.exceedRange == 0.0D) {
      this.exceedRange = 5.0D + this.worldObj.rand.nextDouble() * 5.0D;
    }
  }

  protected boolean canTriggerWalking() {
    return false;
  }

  protected void entityInit() {}

  public boolean canBeCollidedWith() {
    return !this.isDead;
  }

  public void onUpdate() {
    if (this.block.getMaterial() == Material.air) {
      
      setDead();
    }
    else {
      
      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;
      this.fallTime++;
      
      moveEntity(this.motionX, this.motionY, this.motionZ);
      this.motionX *= 0.9800000190734863D;
      this.motionY *= 0.9800000190734863D;
      this.motionZ *= 0.9800000190734863D;
      
      if (!this.worldObj.isRemote) {
        
        int i = MathHelper.floor_double(this.posX);
        int j = MathHelper.floor_double(this.posY);
        int k = MathHelper.floor_double(this.posZ);
        
        double farFromSpawn = MathHelper.sqrt_double(getDistanceSq(this.spawnPosX, this.spawnPosY, this.spawnPosZ));
        boolean exceedRange = (farFromSpawn > this.exceedRange);
        
        if (exceedRange || this.onGround || this.isCollidedHorizontally || this.isCollidedVertically) {
          
          this.motionX *= 0.699999988079071D;
          this.motionZ *= 0.699999988079071D;
          this.motionY *= -0.5D;
          
          if (this.worldObj.getBlock(i, j, k) != Blocks.piston_extension) {
            
            setDead();
            
            if (this.worldObj.setBlock(i, j, k, this.block, this.blockMeta, 3)) {
              
              if (this.block instanceof BlockFalling)
              {
                ((BlockFalling)this.block).playSoundWhenFallen(this.worldObj, i, j, k, this.blockMeta);
              }
            }
            else if (this.shouldDropItem && !this.isBreakingAnvil) {
              
              entityDropItem(new ItemStack(this.block, 1, this.block.damageDropped(this.blockMeta)), 0.0F);
            } 
          } 
        } 
      } 
    } 
  }

  public void writeEntityToNBT(NBTTagCompound p_70014_1_) {
    p_70014_1_.setDouble("exceedR", this.exceedRange);
    p_70014_1_.setDouble("spwPosX", this.spawnPosX);
    p_70014_1_.setDouble("spwPosY", this.spawnPosY);
    p_70014_1_.setDouble("spwPosZ", this.spawnPosZ);
    p_70014_1_.setByte("Tile", (byte)Block.getIdFromBlock(this.block));
    p_70014_1_.setInteger("TileID", Block.getIdFromBlock(this.block));
    p_70014_1_.setByte("Data", (byte)this.blockMeta);
    p_70014_1_.setByte("Time", (byte)this.fallTime);
    p_70014_1_.setBoolean("DropItem", this.shouldDropItem);
    p_70014_1_.setBoolean("HurtEntities", this.isAnvil);
    p_70014_1_.setFloat("FallHurtAmount", this.fallHurtAmount);
    p_70014_1_.setInteger("FallHurtMax", this.fallHurtMax);
  }

  public void readEntityFromNBT(NBTTagCompound p_70037_1_) {
    this.exceedRange = p_70037_1_.getDouble("exceedR");
    this.spawnPosX = p_70037_1_.getDouble("spwPosX");
    this.spawnPosY = p_70037_1_.getDouble("spwPosY");
    this.spawnPosZ = p_70037_1_.getDouble("spwPosZ");
    
    if (p_70037_1_.hasKey("TileID", 99)) {
      
      this.block = Block.getBlockById(p_70037_1_.getInteger("TileID"));
    }
    else {
      
      this.block = Block.getBlockById(p_70037_1_.getByte("Tile") & 0xFF);
    } 
    
    this.blockMeta = p_70037_1_.getByte("Data") & 0xFF;
    this.fallTime = p_70037_1_.getByte("Time") & 0xFF;
    
    if (p_70037_1_.hasKey("HurtEntities", 99)) {
      
      this.isAnvil = p_70037_1_.getBoolean("HurtEntities");
      this.fallHurtAmount = p_70037_1_.getFloat("FallHurtAmount");
      this.fallHurtMax = p_70037_1_.getInteger("FallHurtMax");
    }
    else if (this.block == Blocks.anvil) {
      
      this.isAnvil = true;
    } 
    
    if (p_70037_1_.hasKey("DropItem", 99))
    {
      this.shouldDropItem = p_70037_1_.getBoolean("DropItem");
    }
  }

  public void setHurtEntities(boolean p_145806_1_) {
    this.isAnvil = p_145806_1_;
  }

  public void addEntityCrashInfo(CrashReportCategory p_85029_1_) {
    super.addEntityCrashInfo(p_85029_1_);
    p_85029_1_.addCrashSection("Immitating block ID", Integer.valueOf(Block.getIdFromBlock(this.block)));
    p_85029_1_.addCrashSection("Immitating block data", Integer.valueOf(this.blockMeta));
  }

  @SideOnly(Side.CLIENT)
  public float getShadowSize() {
    return 0.0F;
  }

  @SideOnly(Side.CLIENT)
  public World getWorldObj() {
    return this.worldObj;
  }

  @SideOnly(Side.CLIENT)
  public boolean canRenderOnFire() {
    return false;
  }

  public Block getBlock() {
    return this.block;
  }

  public void writeSpawnData(ByteBuf buffer) {
    buffer.writeDouble(this.exceedRange);
    buffer.writeDouble(this.spawnPosX);
    buffer.writeDouble(this.spawnPosY);
    buffer.writeDouble(this.spawnPosZ);
    buffer.writeInt(Block.getIdFromBlock(this.block));
    buffer.writeInt(this.blockMeta);
    buffer.writeInt(this.fallTime);
    buffer.writeBoolean(this.shouldDropItem);
    buffer.writeBoolean(this.isAnvil);
    buffer.writeFloat(this.fallHurtAmount);
    buffer.writeInt(this.fallHurtMax);
  }

  public void readSpawnData(ByteBuf additionalData) {
    this.exceedRange = additionalData.readDouble();
    this.spawnPosX = additionalData.readDouble();
    this.spawnPosY = additionalData.readDouble();
    this.spawnPosZ = additionalData.readDouble();
    this.block = Block.getBlockById(additionalData.readInt());
    this.blockMeta = additionalData.readInt();
    this.fallTime = additionalData.readInt();
    this.shouldDropItem = additionalData.readBoolean();
    this.isAnvil = additionalData.readBoolean();
    this.fallHurtAmount = additionalData.readFloat();
    this.fallHurtMax = additionalData.readInt();
  }
}
