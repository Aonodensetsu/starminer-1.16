package dev.bluecom.starminer.api.camera;

import dev.bluecom.starminer.basics.common.CommonRegistryHandler;
import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.UUID;

public class CameraEntity extends Entity implements IEntityAdditionalSpawnData {
    public PlayerEntity host;
    private UUID resolver;

    public CameraEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    public CameraEntity(PlayerEntity player) {
        super(CommonRegistryHandler.CAMERA_ENTITY.get(), player.level);
        this.setPos(player.position().x, player.position().y, player.position().z);
    }

    @Override
    public void tick() {
        if (this.level.isClientSide) { // client side
            if (host == null) {
                if (resolver == null) { System.out.println("uuid empty, camera killed"); this.kill(); return; }
                this.host = this.level.getPlayerByUUID(resolver);
            }
        }
    }

    @Override
    public void writeSpawnData(PacketBuffer buffer) {
        buffer.writeUUID(this.level.getNearestPlayer(this, 1).getUUID());
    }

    @Override
    public void readSpawnData(PacketBuffer buffer) {
        this.resolver = buffer.readUUID();
    }

    @Override public boolean save(CompoundNBT nbt) {
        return false;
    }

    @Override protected void readAdditionalSaveData(CompoundNBT nbt) {}
    @Override protected void addAdditionalSaveData(CompoundNBT nbt) {}

    @Override protected void defineSynchedData() {}
    @Override public IPacket<?> getAddEntityPacket() { return NetworkHooks.getEntitySpawningPacket(this); }

    public static class Renderer extends EntityRenderer<CameraEntity> {
        public Renderer(EntityRendererManager p_i46179_1_) { super(p_i46179_1_); }
        @Override public boolean shouldRender(CameraEntity p_225626_1_, ClippingHelper p_225626_2_, double p_225626_3_, double p_225626_5_, double p_225626_7_) { return false; }
        @Override public ResourceLocation getTextureLocation(CameraEntity p_110775_1_) { return null; }
    }
}
