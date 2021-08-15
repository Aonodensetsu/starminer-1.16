package dev.bluecom.starminer.api;

import dev.bluecom.starminer.basics.common.CommonRegistryHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;
import java.util.UUID;

public class CameraEntity extends Entity implements IEntityAdditionalSpawnData {
    public PlayerEntity host;
    private UUID resolver;
    private int resolver2;
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
            }
        }
    }

    @Override
    public void writeSpawnData(PacketBuffer buffer) {
        buffer.writeUUID(servhost.getUUID());
        buffer.writeInt(servhost.getId());
    }

    @Override
    public void readSpawnData(PacketBuffer buffer) {
        this.resolver = buffer.readUUID();
        this.resolver2 = buffer.readInt();
    }

    @Override
    public boolean save(CompoundNBT nbt) {
        return false;
    }

    @Override protected void readAdditionalSaveData(CompoundNBT nbt) {}
    @Override protected void addAdditionalSaveData(CompoundNBT nbt) {}
    @Override protected void defineSynchedData() {}
    @Override public IPacket<?> getAddEntityPacket() { return NetworkHooks.getEntitySpawningPacket(this); }
}
