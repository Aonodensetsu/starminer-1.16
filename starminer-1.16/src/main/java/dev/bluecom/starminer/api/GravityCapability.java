package dev.bluecom.starminer.api;

import dev.bluecom.starminer.basics.common.CommonNetworkHandler;
import dev.bluecom.starminer.basics.common.PacketGravityCapability;
import dev.bluecom.starminer.basics.tileentity.TileEntityGravityCore;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.PacketDistributor;

public class GravityCapability implements IGravityCapability {
	private GravityDirection gravity = GravityDirection.UP_TO_DOWN_YN;
	private Entity host;
	private boolean zero = false;
	private boolean inverted = false;
	private boolean isAttracted = false;
	private BlockPos attractedPos = new BlockPos(0, 0, 0);
	private int ticksLeft = 0;
	
	public GravityCapability() {
		throw new IllegalArgumentException("Pass me an Entity");
	}
	
	public GravityCapability(LivingEntity entity) {
		this.host = entity;
	}
	
	@Override
	public void tick() {
		if (this.isAttracted) {
			if (this.getTicks() % 40 == 0) {
				TileEntity entity = host.level.getBlockEntity(new BlockPos(this.attractedPos.getX(), this.attractedPos.getY(), this.attractedPos.getZ()));
				if (entity instanceof TileEntityGravityCore) {
					TileEntityGravityCore tile = (TileEntityGravityCore) entity;
					this.setGravityDir(tile.getCurrentGravity(host));
					if (host instanceof PlayerEntity) {
						System.out.println("grav update: "+this.getAttracted());
					}
				}
			}
		}
		if (this.getTicks() > 0) {
			this.changeTicks(-1);
		} else if (this.getTicks() == 0) {
			this.setTicks(-1);
			this.loseAttractedBy();
		}
	}
	
	@Override
	public int getTicks() {
		return this.ticksLeft;
	}
	
	@Override
	public void setTicks(int tick) {
		this.ticksLeft = tick;
		this.updateClients();
	}
	
	@Override
	public void changeTicks(int tick) {
		this.setTicks(this.getTicks()+tick);
	}
	
	@Override
	public GravityDirection getGravityDir() {
		return this.gravity;
	}
	
	@Override
	public void setGravityDir(GravityDirection grav) {
		this.gravity = grav;
		this.updateClients();
		this.updatePlayerModel();
	}
	
	@Override
	public boolean getGravityZero() {
		return this.zero;
	}
	
	@Override
	public void setGravityZero(boolean zer) {
		this.zero = zer;
		this.updateClients();
	}
	
	@Override
	public boolean getGravityInverted() {
		return this.inverted;
	}

	@Override
	public void setGravityInverted(boolean inv) {
		this.inverted = inv;
		this.updateClients();
	}
	
	@Override
	public boolean getAttracted() {
		return this.isAttracted;
	}
	
	@Override
	public void setAttracted(boolean att) {
		this.isAttracted = att;
		this.updateClients();
	}
	
	@Override
	public BlockPos getAttractedPos() {
		return this.attractedPos;
	}
	
	@Override
	public void setAttractedPos(BlockPos position) {
		attractedPos = position;
		this.setAttracted(true);
	}
	
	@Override
	public boolean getAttractedBy(IAttractableTileEntity entity) {
		if (entity instanceof TileEntity) {
			TileEntity tile = (TileEntity) entity;
			if (tile.getBlockPos().getX() == this.getAttractedPos().getX() && tile.getBlockPos().getY() == this.getAttractedPos().getY() && tile.getBlockPos().getZ() == this.getAttractedPos().getZ()) {
				return true;
			}
		}
		return false;
	}
	@Override
	public void setAttractedBy(IAttractableTileEntity entity) {
		if (entity instanceof TileEntityGravityCore) {
			TileEntityGravityCore tile = (TileEntityGravityCore) entity;
			this.setAttractedPos(new BlockPos(tile.getBlockPos().getX(), tile.getBlockPos().getY(), tile.getBlockPos().getZ()));
			this.setGravityDir(tile.getCurrentGravity(host));
		}
	}
	
	@Override
	public void loseAttractedBy() {
		this.setAttractedPos(new BlockPos(0, 0, 0));
		this.setAttracted(false);
		this.setGravityDir(GravityDirection.UP_TO_DOWN_YN);
	}
	
	private void updatePlayerModel() {
		host.getBoundingBox();
	}
	
	public static GravityCapability getGravityProp(Entity entity) {
		LazyOptional<IGravityCapability> gravity = entity.getCapability(GravityProvider.GRAVITY);
		return (GravityCapability) gravity.orElseThrow(() -> new IllegalArgumentException("This should only be used on LivingEntities"));
	}
	
	@Override
	public void updateClients() {
		if (!host.level.isClientSide) {
			CommonNetworkHandler.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> host), this.getPacket());
		}
	}
	
	@Override
	public PacketGravityCapability getPacket() {
		return new PacketGravityCapability(gravity, host, zero, inverted, isAttracted, attractedPos, ticksLeft);
	}
	
	@Deprecated
	public void setTemporaryGravityDirection(GravityDirection paramGravityDirection, int paramInt) {}
	@Deprecated
	public void setTemporaryZeroGravity(int paramInt) {}
	@Deprecated
	public Vector3d getGravityFixedLook(float paramFloat1, float paramFloat2) { return new Vector3d(0, 0, 0); }
	@Deprecated
	public Vector3d getGravityFixedPlayerEyePoz(PlayerEntity paramEntityPlayer, float paramFloat) { return new Vector3d(0, 0, 0); }
	@Deprecated
	public void setGravityFixedPlayerShootVec(PlayerEntity paramEntityPlayer, Entity paramEntity, float paramFloat) {}
	@Deprecated
	public void setResistInOpaqueBlockDamegeTick(int paramInt) {}
}
