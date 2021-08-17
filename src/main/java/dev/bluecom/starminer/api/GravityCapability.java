package dev.bluecom.starminer.api;

import dev.bluecom.starminer.api.util.GravityDirection;
import dev.bluecom.starminer.basics.network.CommonNetworkHandler;
import dev.bluecom.starminer.basics.network.PacketGravityCapability;
import dev.bluecom.starminer.basics.tileentities.TileEntityGravityCore;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.PacketDistributor;

import javax.annotation.Nonnull;

public class GravityCapability implements IGravityCapability {
	public static final int DEFAULT_TIMEOUT = 130;
	private GravityDirection gravity = GravityDirection.UP_TO_DOWN_YN;
	private GravityDirection prevgravity = GravityDirection.UP_TO_DOWN_YN;
	private final Entity host;
	private boolean zero = false;
	private boolean inverted = false;
	private boolean isAttracted = false;
	private double angle = 0;
	private Vector3d eyePos = Vector3d.ZERO;
	private BlockPos attractedPos = new BlockPos(0, 0, 0);
	private int ticksLeft = 0;
	
	public GravityCapability() {
		throw new IllegalArgumentException("Pass me an Entity");
	}
	
	public GravityCapability(LivingEntity entity) {
		this.host = entity;
	}
	
	@Override
	public void tickServer() {
		if (this.isAttracted) {
			this.moveEntity();
			if (this.getTicks() % 40 == 0) {
				if (host instanceof PlayerEntity) {
					System.out.println("grav update: " + this.getGravityDir());
				}
				TileEntity entity = host.level.getBlockEntity(new BlockPos(this.attractedPos.getX(), this.attractedPos.getY(), this.attractedPos.getZ()));
				if (entity instanceof TileEntityGravityCore) {
					TileEntityGravityCore tile = (TileEntityGravityCore) entity;
					if (!tile.inGravityRange(host)) { this.loseAttractedBy(); }
					this.setGravityDir(tile.getCurrentGravity(host));
				}
			}
		}
		if (this.getTicks() > 0) {
			this.changeTicks(-1);
		} else if (this.getTicks() == 0) {
			this.setTicks(-1);
			this.loseAttractedBy();
		}
		this.updateClients();
	}

	@Override
	public void tickClient() {
		if (this.host instanceof PlayerEntity) {
			if (this.isAttracted) { // move player
				host.setDeltaMovement(host.getDeltaMovement().add(0, 0.08, 0));
				host.setDeltaMovement(host.getDeltaMovement().add(this.gravity.down().scale(0.08)));
			}
		}
	}

	private void moveEntity() {
		if (this.host instanceof PlayerEntity) return;
		host.setDeltaMovement(host.getDeltaMovement().add(0, 0.08, 0));
		host.setDeltaMovement(host.getDeltaMovement().add(this.gravity.down().scale(0.08)));
	}

	@Override
	public int getTicks() {
		return this.ticksLeft;
	}
	
	@Override
	public void setTicks(int tick) {
		this.ticksLeft = tick;
	}
	
	@Override
	public void changeTicks(int tick) {
		this.setTicks(this.getTicks()+tick);
	}

	@Override
	public boolean hasTransitionAngle() {
		return this.angle != 0 ? true : false;
	}

	@Override
	public double getTransitionAngle() {
		return this.angle;
	}

	@Override
	public void setTransitionAngle(double a) {
		this.angle = a;
	}

	@Override
	public Vector3d getEyePos() {
		return this.eyePos;
	}

	@Override
	public void setEyePos(Vector3d vec) {
		this.eyePos = vec;
	}

	@Override
	public GravityDirection getGravityDir() {
		return this.gravity;
	}
	
	@Override
	public void setGravityDir(@Nonnull GravityDirection grav) {
		//if (host instanceof PlayerEntity) {
		//	Vector3d oldEyePos = host.getPosition(1).add(0, host.getEyeHeight(), 0);
		//	this.prevgravity = this.gravity;
		//	this.gravity = grav;
		//	this.gravity.postModifyPlayerOnGravityChange((PlayerEntity) host, this.prevgravity, oldEyePos);
		//	if (host.level.isClientSide) {
		//		Vector3d newEyePos = host.getPosition(1).add(0, host.getEyeHeight(), 0);
		//		Vector3d eyesDiff = newEyePos.subtract(oldEyePos);
		//		this.setEyePos(eyesDiff);
		//	}
		//} else {
			this.prevgravity = this.gravity;
			this.gravity = grav;
			this.updatePlayerModel();
		//}
	}

	@Override
	public GravityDirection getPreviousDir() {
		return this.prevgravity;
	}

	@Override
	public boolean getGravityZero() {
		return this.zero;
	}
	
	@Override
	public void setGravityZero(boolean zer) {
		this.zero = zer;
	}
	
	@Override
	public boolean getGravityInverted() {
		return this.inverted;
	}

	@Override
	public void setGravityInverted(boolean inv) {
		this.inverted = inv;
	}
	
	@Override
	public boolean getAttracted() {
		return this.isAttracted;
	}
	
	@Override
	public void setAttracted(boolean att) {
		this.isAttracted = att;
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
			return tile.getBlockPos().getX() == this.getAttractedPos().getX() && tile.getBlockPos().getY() == this.getAttractedPos().getY() && tile.getBlockPos().getZ() == this.getAttractedPos().getZ();
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
		//TODO
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

	//public void setTemporaryGravityDirection(GravityDirection paramGravityDirection, int paramInt) {}
	//public void setTemporaryZeroGravity(int paramInt) {}
	//public Vector3d getGravityFixedLook(float paramFloat1, float paramFloat2) { return new Vector3d(0, 0, 0); }
	//public Vector3d getGravityFixedPlayerEyePoz(PlayerEntity paramEntityPlayer, float paramFloat) { return new Vector3d(0, 0, 0); }
	//public void setResistInOpaqueBlockDamageTick(int paramInt) {}
}
