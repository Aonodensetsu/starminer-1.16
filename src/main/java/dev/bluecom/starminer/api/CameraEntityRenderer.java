package dev.bluecom.starminer.api;

import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class CameraEntityRenderer extends EntityRenderer<CameraEntity> {

    public CameraEntityRenderer(EntityRendererManager p_i46179_1_) {
        super(p_i46179_1_);
    }

    @Override
    public boolean shouldRender(CameraEntity p_225626_1_, ClippingHelper p_225626_2_, double p_225626_3_, double p_225626_5_, double p_225626_7_) {
        return false;
    }

    @Override public ResourceLocation getTextureLocation(CameraEntity p_110775_1_) { return null; }
}
