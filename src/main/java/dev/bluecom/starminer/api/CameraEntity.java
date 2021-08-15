package dev.bluecom.starminer.api;

import dev.bluecom.starminer.basics.common.CommonRegistryHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.UUID;

public class CameraEntity extends Entity {
    public PlayerEntity host;
    private UUID resolver;
    private PlayerEntity servhost;

    public CameraEntity(EntityType<?> type, World world) {
        super(type, world);
        this.servhost = null;
    }

    public CameraEntity(PlayerEntity player) {
        super(CommonRegistryHandler.CAMERA_ENTITY.get(), player.level);
        this.servhost = player;
    }

    @Override
    public void tick() {
        if (this.level.isClientSide) {
            if (host == null) {
                if (resolver == null) { System.out.println("uuid empty, camera killed"); this.kill(); return; }
                this.host = this.level.getPlayerByUUID(resolver);
                System.out.println("made the host "+this.level.getPlayerByUUID(resolver));
            }
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundNBT nbt) {
        if (nbt.hasUUID("hostplayer")) {
            this.resolver = nbt.getUUID("hostplayer");
        } else {
            this.resolver = null;
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundNBT nbt) {
        if (servhost != null) {
            nbt.putUUID("hostplayer", servhost.getUUID());
        }
    }

    @Override protected void defineSynchedData() {}
    @Override public IPacket<?> getAddEntityPacket() { return NetworkHooks.getEntitySpawningPacket(this); }
}
