package jp.mc.ancientred.starminer.basics.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderStarSquid
  extends RenderLiving
{
  private static final ResourceLocation squidTextures = new ResourceLocation("textures/entity/squid.png");

  
  public RenderStarSquid(ModelBase par1ModelBase, float par2) {
    super(par1ModelBase, par2);
  }

  public void renderLivingSquid(EntityStarSquid par1EntityMySquid, double par2, double par4, double par6, float par8, float par9) {
    doRender((EntityLiving)par1EntityMySquid, par2, par4, par6, par8, par9);
  }

  protected ResourceLocation getSquidTextures(EntityStarSquid par1EntityMySquid) {
    return squidTextures;
  }

  protected void rotateSquidsCorpse(EntityStarSquid par1EntityMySquid, float par2, float par3, float par4) {
    float f3 = par1EntityMySquid.prevSquidPitch + (par1EntityMySquid.squidPitch - par1EntityMySquid.prevSquidPitch) * par4;
    float f4 = par1EntityMySquid.prevSquidYaw + (par1EntityMySquid.squidYaw - par1EntityMySquid.prevSquidYaw) * par4;
    GL11.glTranslatef(0.0F, 0.5F, 0.0F);
    GL11.glRotatef(180.0F - par3, 0.0F, 1.0F, 0.0F);
    GL11.glRotatef(f3, 1.0F, 0.0F, 0.0F);
    GL11.glRotatef(f4, 0.0F, 1.0F, 0.0F);
    GL11.glTranslatef(0.0F, -1.2F, 0.0F);
  }

  protected float handleRotationFloat(EntityStarSquid par1EntityMySquid, float par2) {
    return par1EntityMySquid.prevTentacleAngle + (par1EntityMySquid.tentacleAngle - par1EntityMySquid.prevTentacleAngle) * par2;
  }

  public void doRenderLiving(EntityLiving par1EntityLiving, double par2, double par4, double par6, float par8, float par9) {
    renderLivingSquid((EntityStarSquid)par1EntityLiving, par2, par4, par6, par8, par9);
  }

  protected float handleRotationFloat(EntityLivingBase par1EntityLivingBase, float par2) {
    return handleRotationFloat((EntityStarSquid)par1EntityLivingBase, par2);
  }

  protected void rotateCorpse(EntityLivingBase par1EntityLivingBase, float par2, float par3, float par4) {
    rotateSquidsCorpse((EntityStarSquid)par1EntityLivingBase, par2, par3, par4);
  }

  public void renderPlayer(EntityLivingBase par1EntityLivingBase, double par2, double par4, double par6, float par8, float par9) {
    renderLivingSquid((EntityStarSquid)par1EntityLivingBase, par2, par4, par6, par8, par9);
  }

  protected ResourceLocation getEntityTexture(Entity par1Entity) {
    return getSquidTextures((EntityStarSquid)par1Entity);
  }

  public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
    renderLivingSquid((EntityStarSquid)par1Entity, par2, par4, par6, par8, par9);
  }
}
