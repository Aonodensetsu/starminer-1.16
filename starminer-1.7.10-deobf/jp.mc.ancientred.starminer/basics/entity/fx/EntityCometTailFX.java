package jp.mc.ancientred.starminer.basics.entity.fx;

import jp.mc.ancientred.starminer.basics.common.SMRenderHelper;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.World;

public class EntityCometTailFX extends EntityFX {
  float initialParticleScale;
  
  public EntityCometTailFX(World world, double posX, double posY, double posZ) {
    this(world, posX, posY, posZ, 1.0F);
  }
  
  public EntityCometTailFX(World world, double posX, double posY, double posZ, float scale) {
    super(world, posX, posY, posZ, 0.0D, 0.0D, 0.0D);
    this.particleRed = this.particleGreen = this.particleBlue = (float)(Math.random() * 0.30000001192092896D + 0.6000000238418579D);
    this.particleScale *= 0.75F;
    this.particleScale *= scale;
    this.initialParticleScale = this.particleScale;
    this.particleMaxAge = (int)(18.0D / (Math.random() * 0.8D + 0.6D));
    this.particleMaxAge = (int)(this.particleMaxAge * scale);
    this.noClip = true;
    setParticleTextureIndex(65);
    onUpdate();
  }
  
  public void renderParticle(Tessellator tes, float tickPartial, float p_70539_3_, float p_70539_4_, float p_70539_5_, float p_70539_6_, float p_70539_7_) {
    float agedScale = (this.particleAge + tickPartial) / this.particleMaxAge * 32.0F;
    
    if (agedScale < 0.0F) {
      agedScale = 0.0F;
    }
    
    if (agedScale > 1.0F) {
      agedScale = 1.0F;
    }
    
    this.particleScale = this.initialParticleScale * agedScale;
    renderParticleGRoatFix(tes, tickPartial, p_70539_3_, p_70539_4_, p_70539_5_, p_70539_6_, p_70539_7_);
  }
  
  public void renderParticleGRoatFix(Tessellator tes, float tickPartial, float p_70539_3_, float p_70539_4_, float p_70539_5_, float p_70539_6_, float p_70539_7_) {
    SMRenderHelper.ensureValues(tickPartial);
    
    float rotationX = SMRenderHelper.rotationX;
    float rotationZ = SMRenderHelper.rotationZ;
    float rotationYZ = SMRenderHelper.rotationYZ;
    float rotationXY = SMRenderHelper.rotationXY;
    float rotationXZ = SMRenderHelper.rotationXZ;
    
    float texUMN = this.particleTextureIndexX / 16.0F;
    float texUMX = texUMN + 0.0624375F;
    float texVMN = this.particleTextureIndexY / 16.0F;
    float texVMX = texVMN + 0.0624375F;
    float scale = 0.1F * this.particleScale;
    
    if (this.particleIcon != null) {
      
      texUMN = this.particleIcon.getMinU();
      texUMX = this.particleIcon.getMaxU();
      texVMN = this.particleIcon.getMinV();
      texVMX = this.particleIcon.getMaxV();
    } 
    
    float fX = (float)(this.prevPosX + (this.posX - this.prevPosX) * tickPartial - interpPosX);
    float fY = (float)(this.prevPosY + (this.posY - this.prevPosY) * tickPartial - interpPosY);
    float fZ = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * tickPartial - interpPosZ);
    tes.setColorRGBA_F(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha);

    
    tes.addVertexWithUV((fX - rotationX * scale - rotationYZ * scale), (fY - rotationXZ * scale), (fZ - rotationZ * scale - rotationXY * scale), texUMX, texVMX);
    tes.addVertexWithUV((fX - rotationX * scale + rotationYZ * scale), (fY + rotationXZ * scale), (fZ - rotationZ * scale + rotationXY * scale), texUMX, texVMN);
    tes.addVertexWithUV((fX + rotationX * scale + rotationYZ * scale), (fY + rotationXZ * scale), (fZ + rotationZ * scale + rotationXY * scale), texUMN, texVMN);
    tes.addVertexWithUV((fX + rotationX * scale - rotationYZ * scale), (fY - rotationXZ * scale), (fZ + rotationZ * scale - rotationXY * scale), texUMN, texVMX);
  }

  public void onUpdate() {
    this.prevPosX = this.posX;
    this.prevPosY = this.posY;
    this.prevPosZ = this.posZ;
    
    if (this.particleAge++ >= this.particleMaxAge) {
      setDead();
    }

    moveEntity(this.motionX, this.motionY, this.motionZ);

    this.motionX *= 0.65D;
    this.motionY *= 0.65D;
    this.motionZ *= 0.65D;
  }
}
