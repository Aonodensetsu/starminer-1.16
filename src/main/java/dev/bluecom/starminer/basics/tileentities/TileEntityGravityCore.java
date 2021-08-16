package dev.bluecom.starminer.basics.tileentities;

import dev.bluecom.starminer.api.GravityCapability;
import dev.bluecom.starminer.api.IAttractableTileEntity;
import dev.bluecom.starminer.api.util.GravityDirection;
import dev.bluecom.starminer.basics.common.CommonRegistryHandler;
import dev.bluecom.starminer.basics.items.ItemGravityController;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.passive.AmbientEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.List;

public class TileEntityGravityCore extends TileEntity implements ITickableTileEntity, IAttractableTileEntity {
	private final ItemStackHandler itemHandler = new ItemStackHandler(27) {
		@Override
		protected void onContentsChanged(int slot) {
			setChanged();
		}
	};
	private final LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler);

	private int gravRad = 0;
	private int starRad = 0;
	private int gType = 0;
	private int invType = 0;
	public boolean workFast = false;
	public boolean useBufferArea = true;
	public short workStateX = 0;
	public short workStateY = 0;
	public short workStateZ = 0;
	
	public TileEntityGravityCore() {
		super(CommonRegistryHandler.TILE_GRAVITY_CORE.get());
	}
	
	public int getInvType() {
		return invType;
	}
	
	public TranslationTextComponent getInvTypeMessage() {
		switch (invType) {
			case 0:
				return new TranslationTextComponent("screen.starminer.normal");
			case 1:
				return new TranslationTextComponent("screen.starminer.inverted");
			case 2:
				return new TranslationTextComponent("screen.starminer.zero");
			default:
				return new TranslationTextComponent("screen.starminer.notfound");
		}
	}

	public void nextInvType() {
		invType += 1;
		if (invType > 2) {
			invType = 0;
		}
	}
	
	public int getGravityType() {
		return gType;
	}
	
	public TranslationTextComponent getGravityTypeMessage() {
		switch (gType) {
			case 0:
				return new TranslationTextComponent("screen.starminer.sphere");
			case 1:
				return new TranslationTextComponent("screen.starminer.cube");
			case 2:
				return new TranslationTextComponent("screen.starminer.xcylinder");
			case 3:
				return new TranslationTextComponent("screen.starminer.ycylinder");
			case 4:
				return new TranslationTextComponent("screen.starminer.zcylinder");
			default:
				return new TranslationTextComponent("screen.starminer.notfound");
		}
	}
	
	public void nextGravityType() {
		gType += 1;
		if (gType > 4) {
			gType = 0;
		}
	}
	
	public int getGravityRadius() {
		return gravRad;
	}
	
	public void setGravityRadius(int grad) {
		gravRad = grad;
		if (gravRad < 0) {
			gravRad = 0;
		}
		if (gravRad > 32) {
			gravRad = 32;
		}
	}
	
	public void changeRadius(int g, int s) {
		setGravityRadius(gravRad+g);
		setStarRadius(starRad+s);
	}
	
	public int getStarRadius() {
		return starRad;
	}
	
	@SuppressWarnings("SpellCheckingInspection")
	public void setStarRadius(int srad) {
		starRad = srad;
		if (starRad < 0) {
			starRad = 0;
		}
		if (starRad > 32) {
			starRad = 32;
		}
	}
	
	public boolean canPlayerAccessInventory(PlayerEntity player) {
		if (level.getBlockEntity(worldPosition) != this) { return false; }
		return player.blockPosition().distSqr(new Vector3i(worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5)) < 64;
	}

	@Override
	public void setRemoved() {
		super.setRemoved();
		handler.invalidate();
	}
	
	@Override
	public void load(BlockState block, CompoundNBT nbt) {
		itemHandler.deserializeNBT(nbt.getCompound("inv"));
		gravRad = nbt.getInt("gravRad");
		starRad = nbt.getInt("starRad");
		gType = nbt.getInt("gType");
		invType = nbt.getInt("invType");
		super.load(block, nbt);
	}
	
	@Override
	public CompoundNBT save(CompoundNBT nbt) {
		nbt.put("inv", itemHandler.serializeNBT());
		nbt.putInt("gravRad", gravRad);
		nbt.putInt("starRad", starRad);
		nbt.putInt("gType", gType);
		nbt.putInt("invType", invType);
		return super.save(nbt);
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
		CompoundNBT nbt = pkt.getTag();
		gravRad = nbt.getInt("gravRad");
		starRad = nbt.getInt("starRad");
		gType = nbt.getInt("gType");
		invType = nbt.getInt("invType");
	}
	
	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		CompoundNBT nbt = new CompoundNBT();
		nbt.putInt("gravRad", gravRad);
		nbt.putInt("starRad", starRad);
		nbt.putInt("gType", gType);
		nbt.putInt("invType", invType);
		return new SUpdateTileEntityPacket(this.getBlockPos(), -1, nbt);
	}
	
	@Override
	public CompoundNBT getUpdateTag() {
		CompoundNBT nbt = super.getUpdateTag();
		nbt.putInt("gravRad", gravRad);
		nbt.putInt("starRad", starRad);
		nbt.putInt("gType", gType);
		nbt.putInt("invType", invType);
		return nbt;
	}
	
	@Override
	public void handleUpdateTag(BlockState state, CompoundNBT tag) {
		gravRad = tag.getInt("gravRad");
		starRad = tag.getInt("starRad");
		gType = tag.getInt("gType");
		invType = tag.getInt("invType");
		super.handleUpdateTag(state, tag);
	}
	
	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return handler.cast();
		}
		return super.getCapability(cap, side);
	}
	
	@Override
	public void tick() {
		if (!level.isClientSide) {
			long totalTime = level.getGameTime();
			if (totalTime % 120 == 0L) {
				addEffectToPlayer();
			}
			long workSpeed = workFast ? 5L : 40L;
			if (totalTime % workSpeed == 0L) {
				doTerraformWork();
			}
			if (totalTime % 100L == 0L) {
				doGravitizeWork();
				doAnimalModSpawning();
			}
		}
	}
	
	private void addEffectToPlayer() {
		AxisAlignedBB axisalignedbb = AxisAlignedBB.unitCubeFromLowerCorner(new Vector3d(worldPosition.getX(), worldPosition.getY(), worldPosition.getZ())).inflate(gravRad);
		List<LivingEntity> list = level.getEntitiesOfClass(LivingEntity.class, axisalignedbb);

		for (LivingEntity entityG : list) {
			if (entityG instanceof AmbientEntity) { continue; }
			if (entityG instanceof EnderDragonEntity) { continue; }
			if (entityG instanceof WitherEntity) { continue; }
			if (entityG.isSleeping()) { continue; }

			if (inGravityRange(entityG)) {
				GravityCapability gravity = GravityCapability.getGravityProp(entityG);
				if (!gravity.getAttracted()) {
					gravity.setTicks(130);
					gravity.setAttractedBy(this);
					continue;
				}
				if (gravity.getAttractedBy(this)) {
					gravity.setTicks(130);
				}
			}
		}
	}

	@Override
	public boolean inGravityRange(Entity entityG) {
		GravityCapability gravity = GravityCapability.getGravityProp(entityG);
		double entityPosX, entityPosY, entityPosZ;
		double dWidthHalf = (entityG.getBbWidth() / 2.0F);
		AxisAlignedBB bb = entityG.getBoundingBox();

		switch (gravity.getGravityDir()) {
			case DOWN_TO_UP_YP:
				entityPosX = (bb.maxX + bb.minX) / 2.0D;
				entityPosY = bb.maxY - dWidthHalf;
				entityPosZ = (bb.maxZ + bb.minZ) / 2.0D;
				break;
			case EAST_TO_WEST_XN:
				entityPosX = bb.minX + dWidthHalf;
				entityPosY = (bb.maxY + bb.minY) / 2.0D;
				entityPosZ = (bb.maxZ + bb.minZ) / 2.0D;
				break;
			case NORTH_TO_SOUTH_ZP:
				entityPosX = (bb.maxX + bb.minX) / 2.0D;
				entityPosY = (bb.maxY + bb.minY) / 2.0D;
				entityPosZ = bb.maxZ - dWidthHalf;
				break;
			case SOUTH_TO_NORTH_ZN:
				entityPosX = (bb.maxX + bb.minX) / 2.0D;
				entityPosY = (bb.maxY + bb.minY) / 2.0D;
				entityPosZ = bb.minZ + dWidthHalf;
				break;
			case WEST_TO_EAST_XP:
				entityPosX = bb.maxX - dWidthHalf;
				entityPosY = (bb.maxY + bb.minY) / 2.0D;
				entityPosZ = (bb.maxZ + bb.minZ) / 2.0D;
				break;
			default:
				entityPosX = (bb.maxX + bb.minX) / 2.0D;
				entityPosY = bb.minY + dWidthHalf;
				entityPosZ = (bb.maxZ + bb.minZ) / 2.0D;
				break;
		}

		double centerX = worldPosition.getX() + 0.5D;
		double centerY = worldPosition.getY() + 0.5D;
		double centerZ = worldPosition.getZ() + 0.5D;

		double xRel = centerX - entityPosX;
		double yRel = centerY - entityPosY;
		double zRel = centerZ - entityPosZ;

		switch (gType) {
			case 1:
				return checkAttractedRangeCube(centerX, centerY, centerZ, entityPosX, entityPosY, entityPosZ, this.gravRad);
			case 2:
				return checkAttractedRangeXCylinder(centerX, entityPosX, yRel, zRel, this.gravRad);
			case 3:
				return checkAttractedRangeYCylinder(centerY, entityPosY, xRel, zRel, this.gravRad);
			case 4:
				return checkAttractedRangeZCylinder(centerZ, entityPosZ, xRel, yRel, this.gravRad);
			default:
				return checkAttractedRangeSphere(xRel, yRel, zRel, this.gravRad);
		}
	}
	
	private static boolean checkAttractedRangeSphere(double xRel, double yRel, double zRel, int grav) {
		return Math.sqrt(xRel*xRel+yRel*yRel+zRel*zRel) <= grav;
	}
	
	private static boolean checkAttractedRangeCube(double centerX, double centerY, double centerZ, double entityX, double entityY, double entityZ, int grav) {
		return (entityX <= centerX + grav && entityX >= centerX - grav && entityY <= centerY + grav && entityY >= centerY - grav && entityZ <= centerZ + grav && entityZ >= centerZ - grav);
	}
	
	private static boolean checkAttractedRangeXCylinder(double centerX, double entityX, double yRel, double zRel, int grav) {
		return (entityX <= centerX + grav && entityX >= centerX - grav && Math.sqrt(yRel * yRel + zRel * zRel) <= grav);
	}
	
	private static boolean checkAttractedRangeYCylinder(double centerY, double entityY, double xRel, double zRel, int grav) {
		return (entityY <= centerY + grav && entityY >= centerY - grav && Math.sqrt(xRel * xRel + zRel * zRel) <= grav);
	}
	
	private static boolean checkAttractedRangeZCylinder(double centerZ, double entityZ, double xRel, double yRel, int grav) {
		return (entityZ <= centerZ + grav && entityZ >= centerZ - grav && Math.sqrt(xRel * xRel + yRel * yRel) <= grav);
	}
	
	@Override
	public GravityDirection getCurrentGravity(Entity entity) {
		double centerX = worldPosition.getX() + 0.5D;
		double centerY = worldPosition.getY() + 0.5D;
		double centerZ = worldPosition.getZ() + 0.5D;
		double dWidthHalf = entity.getBbWidth()/2.0F;
		AxisAlignedBB bb = entity.getBoundingBox();
		double entityX, entityY, entityZ, xRel, yRel, zRel;
		GravityDirection gDir;
		boolean reverse;
		GravityCapability cap = GravityCapability.getGravityProp(entity);
		switch (cap.getGravityDir()) {
		case DOWN_TO_UP_YP:
			entityX = (bb.maxX+bb.minX)/2.0D; 
			entityY = bb.maxY-dWidthHalf; 
			entityZ = (bb.maxZ+bb.minZ)/2.0D; 
			xRel = entityX-centerX; 
			yRel = entityY-centerY; 
			zRel = entityZ-centerZ;
			gDir = cap.getGravityDir();
			reverse = isGravityReverse(entity);
			return getGravityDirection(xRel, yRel, zRel, gDir, reverse);
		case EAST_TO_WEST_XN:
			entityX = bb.minX+dWidthHalf; 
			entityY = (bb.maxY+bb.minY)/2.0D; 
			entityZ = (bb.maxZ+bb.minZ)/2.0D; 
			xRel = entityX-centerX; 
			yRel = entityY-centerY; 
			zRel = entityZ-centerZ; 
			gDir = cap.getGravityDir(); 
			reverse = isGravityReverse(entity); 
			return getGravityDirection(xRel, yRel, zRel, gDir, reverse);
		case NORTH_TO_SOUTH_ZP:
			entityX = (bb.maxX+bb.minX)/2.0D;
			entityY = (bb.maxY+bb.minY)/2.0D;
	        entityZ = bb.maxZ-dWidthHalf;
	        xRel = entityX-centerX;
	        yRel = entityY-centerY;
	        zRel = entityZ-centerZ;
	        gDir = cap.getGravityDir();
	        reverse = isGravityReverse(entity);
	        return getGravityDirection(xRel, yRel, zRel, gDir, reverse);
		case SOUTH_TO_NORTH_ZN:
			entityX = (bb.maxX+bb.minX)/2.0D; 
			entityY = (bb.maxY+bb.minY)/2.0D; 
			entityZ = bb.minZ+dWidthHalf; 
			xRel = entityX-centerX; 
			yRel = entityY-centerY; 
			zRel = entityZ-centerZ; 
			gDir = cap.getGravityDir(); 
			reverse = isGravityReverse(entity); 
			return getGravityDirection(xRel, yRel, zRel, gDir, reverse);
		case WEST_TO_EAST_XP:
			entityX = bb.maxX-dWidthHalf; 
			entityY = (bb.maxY+bb.minY)/2.0D; 
			entityZ = (bb.maxZ+bb.minZ)/2.0D; 
			xRel = entityX-centerX; 
			yRel = entityY-centerY; 
			zRel = entityZ-centerZ; 
			gDir = cap.getGravityDir(); 
			reverse = isGravityReverse(entity);
			return getGravityDirection(xRel, yRel, zRel, gDir, reverse);
		default:
			entityX = (bb.maxX+bb.minX)/2.0D; 
			entityY = bb.minY+dWidthHalf; 
			entityZ = (bb.maxZ+bb.minZ)/2.0D; 
			xRel = entityX-centerX; 
			yRel = entityY-centerY; 
			zRel = entityZ-centerZ; 
			gDir = cap.getGravityDir();  
			reverse = isGravityReverse(entity); 
			return getGravityDirection(xRel, yRel, zRel, gDir, reverse);
		}
	}
	
	private boolean isGravityReverse(Entity entity) {
		if (entity instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) entity;
			for (int i=0; i < 9; i++) {
				ItemStack itemstack = player.inventory.items.get(i);
				if (itemstack.getItem() == CommonRegistryHandler.ITEM_GRAVITY_CONTROLLER.get()) {
					ItemGravityController controller = (ItemGravityController) itemstack.getItem();
					if (controller.gravstate == 1.0F) { 
						return true; 
					}
				}
			}
		}
		return false;
	}
	
	private GravityDirection getGravityDirection(double xRel, double yRel, double zRel, GravityDirection gDir, boolean reverse) {
		GravityDirection gravityDirNew;
		double axisDiff;
		switch (gType) {
			case 2:
				axisDiff = Math.abs(zRel)-Math.abs(yRel);
				if (this.useBufferArea && gDir != null && Math.abs(axisDiff) < 1.5D) {
					return gDir;
				}
				if (axisDiff > 0.0D) {
					if (zRel < 0.0D) {
						gravityDirNew = reverse ? GravityDirection.NORTH_TO_SOUTH_ZP : GravityDirection.SOUTH_TO_NORTH_ZN;
					} else {
						gravityDirNew = reverse ? GravityDirection.SOUTH_TO_NORTH_ZN : GravityDirection.NORTH_TO_SOUTH_ZP;
					}
				} else if (yRel < 0.0D) {
					gravityDirNew = reverse ? GravityDirection.DOWN_TO_UP_YP : GravityDirection.UP_TO_DOWN_YN;
				} else {
					gravityDirNew = reverse ? GravityDirection.UP_TO_DOWN_YN : GravityDirection.DOWN_TO_UP_YP;
				} 
				return gravityDirNew;
			case 3:
				axisDiff = Math.abs(xRel)-Math.abs(zRel); 
				if (this.useBufferArea && gDir != null && Math.abs(axisDiff) < 1.5D) {
					return gDir;
				}
				if (axisDiff > 0.0D) { 
					if (xRel < 0.0D) { 
						gravityDirNew = reverse ? GravityDirection.WEST_TO_EAST_XP : GravityDirection.EAST_TO_WEST_XN; 
					} else { 
						gravityDirNew = reverse ? GravityDirection.EAST_TO_WEST_XN : GravityDirection.WEST_TO_EAST_XP; 
					}  
				} else if (zRel < 0.0D) { 
					gravityDirNew = reverse ? GravityDirection.NORTH_TO_SOUTH_ZP : GravityDirection.SOUTH_TO_NORTH_ZN; 
				} else { 
					gravityDirNew = reverse ? GravityDirection.SOUTH_TO_NORTH_ZN : GravityDirection.NORTH_TO_SOUTH_ZP; 
				} 
				return gravityDirNew;
			case 4:
				axisDiff = Math.abs(xRel)-Math.abs(yRel); 
				if (this.useBufferArea && gDir != null && Math.abs(axisDiff) < 1.5D) {
					return gDir;
				}
				if (axisDiff > 0.0D) { 
					if (xRel < 0.0D) { 
						gravityDirNew = reverse ? GravityDirection.WEST_TO_EAST_XP : GravityDirection.EAST_TO_WEST_XN; 
					} else { 
						gravityDirNew = reverse ? GravityDirection.EAST_TO_WEST_XN : GravityDirection.WEST_TO_EAST_XP; 
					}  
				} else if (yRel < 0.0D) { 
					gravityDirNew = reverse ? GravityDirection.DOWN_TO_UP_YP : GravityDirection.UP_TO_DOWN_YN; 
				} else { 
					gravityDirNew = reverse ? GravityDirection.UP_TO_DOWN_YN : GravityDirection.DOWN_TO_UP_YP; 
				}  
				return gravityDirNew;
			default:
				if (Math.abs(xRel) > Math.abs(zRel) && Math.abs(xRel) > Math.abs(yRel)) { 
					if (xRel < 0.0D) { 
						gravityDirNew = !reverse ? GravityDirection.WEST_TO_EAST_XP : GravityDirection.EAST_TO_WEST_XN; 
					} else { 
						gravityDirNew = !reverse ? GravityDirection.EAST_TO_WEST_XN : GravityDirection.WEST_TO_EAST_XP; 
					}  
				} else if (Math.abs(zRel) >= Math.abs(xRel) && Math.abs(zRel) > Math.abs(yRel)) { 
					if (zRel < 0.0D) { 
						gravityDirNew = !reverse ? GravityDirection.NORTH_TO_SOUTH_ZP : GravityDirection.SOUTH_TO_NORTH_ZN; 
					} else { 
						gravityDirNew = !reverse ? GravityDirection.SOUTH_TO_NORTH_ZN : GravityDirection.NORTH_TO_SOUTH_ZP; 
					}  
				} else if (yRel < 0.0D) { 
					gravityDirNew = !reverse ? GravityDirection.DOWN_TO_UP_YP : GravityDirection.UP_TO_DOWN_YN; 
				} else { 
					gravityDirNew = !reverse ? GravityDirection.UP_TO_DOWN_YN : GravityDirection.DOWN_TO_UP_YP; 
				}  
				return gravityDirNew;
		}
	}

	private void doTerraformWork() {
		int itemIndex = getItemStackForTerraform();
		if (itemIndex == -1) {
			workFast = false;
			return;
		}
		if (gType == 1) { // why can it not build a cube?
			workFast = false;
			return;
		}
		if (workStateY < -starRad || workStateY + worldPosition.getY() < 0) {
			workFast = false;
			return;
		}
		workFast = true;
		if (calculateNextWorkSpot()) {
			putBlock(itemIndex);
		}
	}
	
	private void putBlock(int itemIndex) {
		BlockPos pos = new BlockPos(worldPosition.getX()+workStateX, worldPosition.getY()+workStateY, worldPosition.getZ()+workStateZ);
		ItemStack itemstack = itemHandler.getStackInSlot(itemIndex);
		Item item = itemstack.getItem();
		if (item instanceof BlockItem) {
			BlockItem blockitem = (BlockItem) item;
			Block block = blockitem.getBlock();
			BlockState blockstate = block.defaultBlockState();
			if (level.setBlockAndUpdate(pos, blockstate)) {
				itemHandler.setStackInSlot(itemIndex, new ItemStack(itemstack.getItem(), itemstack.getCount()-1));
			}
		}
	}

	private boolean calculateNextWorkSpot() {
		int radPlusOne = starRad+1;
		int radSubOne = starRad-1;
		double radPow = starRad*starRad;
		double radSubPow = radSubOne*radSubOne;
		int count = 0;
		workStateX = (short)-radPlusOne;
		workStateZ = (short)-radPlusOne;
		while ((workStateX = (short)(workStateX+1)) <= radPlusOne || (workStateZ = (short)(workStateZ+1)) <= radPlusOne || (workStateY = (short)(workStateY-1)) >= -radPlusOne && workStateY+worldPosition.getY() >= 0) {
			if (workStateX != 0 || workStateY != 0 || workStateZ != 0) {
				double distancePow;
				switch (gType) {
					case 1:
						distancePow = workStateX+workStateY+workStateZ;
						if (distancePow < radPow && distancePow >= radSubPow) {
							return true;
						}
						break;
					case 2:
						distancePow = workStateY * workStateY + workStateZ * workStateZ;
						if (distancePow < radPow && distancePow >= radSubPow) {
							return true;
			            }
			            break;
					case 3:
						distancePow = workStateX * workStateX + workStateZ * workStateZ;
						if (distancePow < radPow && distancePow >= radSubPow) {
							return true;
						}
						break;
					case 4:
						distancePow = workStateX * workStateX + workStateY * workStateY;
			            if (distancePow < radPow && distancePow >= radSubPow) {
			            	return true;
			            }
			            break;
					default:
			            distancePow = workStateX * workStateX + workStateY * workStateY + workStateZ * workStateZ;
			            if (distancePow < radPow && distancePow >= radSubPow) {
			            	return true;
			            }
			            break;
				}
			}
			if (++count > 50) {
				return false;
			}
		}
		return false;
	}

	private int getItemStackForTerraform() {
		for (int i=0; i<itemHandler.getSlots(); i++) {
			if (!itemHandler.getStackInSlot(i).isEmpty()) {
				Item item = itemHandler.getStackInSlot(i).getItem();
				if (item instanceof BlockItem) {
					BlockItem blockitem = (BlockItem) item;
					if (level.dimension() != CommonRegistryHandler.DIMENSION_ZERO_GRAVITY || blockitem.getBlock() != Blocks.ICE) {
						return i;
					}
				}
			}
		}
		return -1;
	}

	private void doGravitizeWork() {
//		if (level.random.nextInt(24) == 0) {
//			ItemStack itemStack = itemHandler.getStackInSlot(itemHandler.getSlots()-1);
//			if (itemStack != null) {
//				Item newItem = null;
//				Item item = itemStack.getItem();
//				if (item == Items.wheat_seeds) {
//					newItem = CommonRegistryHandler.ITEM_SEED_GRAVITIZED.get();
//				}
//				if (item == Items.carrot) {
//					newItem = CommonRegistryHandler.ITEM_CARROT_GRAVITIZED.get();
//				}
//				if (item == Items.potato) {
//					newItem = CommonRegistryHandler.ITEM_POTATO_GRAVITIZED.get();
//				}
//				Block block = Block.getBlockFromItem(item);
//				if (block != null) {
//					if (block == Blocks.sapling) {
//						newItem = Item.getItemFromBlock(CommonRegistryHandler.BLOCK_SAPLING_GRAVITIZED.get());
//					}
//					if (block == Blocks.yellow_flower) {
//						newItem = Item.getItemFromBlock(CommonRegistryHandler.BLOCK_YELLOW_PLANT_GRAVITIZED.get());
//					}
//					if (block == Blocks.red_flower) {
//						newItem = Item.getItemFromBlock(CommonRegistryHandler.BLOCK_RED_PLANT_GRAVITIZED.get());
//					}
//				} 
//				if (newItem != null) {
//					itemHandler.getStackInSlot(itemHandler.getSlots()-1) = new ItemStack(newItem, itemStack.getCount(), itemStack.getTag());
//				}
//			} 
//		} 
	}
	
	private void doAnimalModSpawning() {
		//TODO
	}
	
	public void resetWorkState() {
		int radPlsOne = starRad + 1;
		workStateX = (short)-radPlsOne;
		workStateZ = (short)-radPlsOne;
		workStateY = (short)radPlsOne;
		if (workStateY + worldPosition.getY() > 255) {
			workStateY = (short)(255 - worldPosition.getY());
		}
	}
}
