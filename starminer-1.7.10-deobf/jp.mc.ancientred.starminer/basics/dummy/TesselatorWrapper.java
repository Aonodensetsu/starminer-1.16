package jp.mc.ancientred.starminer.basics.dummy;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.mc.ancientred.starminer.api.GravityDirection;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.shader.TesselatorVertexState;

@SideOnly(Side.CLIENT)
public class TesselatorWrapper
  extends Tessellator
{
  private Tessellator wrapped;
  private GravityDirection gdir;
  private double centerX;
  private double centerY;
  private double centerZ;
  private double[] conv = new double[3];

  public Tessellator wrap(Tessellator wrapped, GravityDirection gdir, double centerX, double centerY, double centerZ) {
    this.wrapped = wrapped;
    this.gdir = gdir;
    this.centerX = centerX;
    this.centerY = centerY;
    this.centerZ = centerZ;
    return this;
  }
  public void addVertexWithUV(double parX, double parY, double parZ, double texU, double texV) {
    double[] rotated = this.gdir.rotateXYZAt(this.conv, parX, parY, parZ, this.centerX, this.centerY, this.centerZ);
    parX = rotated[0]; parY = rotated[1]; parZ = rotated[2];
    
    this.wrapped.addVertexWithUV(parX, parY, parZ, texU, texV);
  }

  public void addVertex(double parX, double parY, double parZ) {
    double[] rotated = this.gdir.rotateXYZAt(this.conv, parX, parY, parZ, this.centerX, this.centerY, this.centerZ);
    parX = rotated[0]; parY = rotated[1]; parZ = rotated[2];
    
    this.wrapped.addVertex(parX, parY, parZ);
  }

  public void setNormal(float parX, float parY, float parZ) {
    double[] rotated = this.gdir.rotateXYZAt(this.conv, parX, parY, parZ, 0.0D, 0.0D, 0.0D);
    parX = (float)rotated[0]; parY = (float)rotated[1]; parZ = (float)rotated[2];
    
    this.wrapped.setNormal(parX, parY, parZ);
  }

  public int draw() {
    return this.wrapped.draw();
  }

  public TesselatorVertexState getVertexState(float p_147564_1_, float p_147564_2_, float p_147564_3_) {
    return this.wrapped.getVertexState(p_147564_1_, p_147564_2_, p_147564_3_);
  }

  public void setVertexState(TesselatorVertexState p_147565_1_) {
    this.wrapped.setVertexState(p_147565_1_);
  }

  public void startDrawingQuads() {
    this.wrapped.startDrawingQuads();
  }

  public void startDrawing(int p_78371_1_) {
    this.wrapped.startDrawing(p_78371_1_);
  }

  public void setTextureUV(double p_78385_1_, double p_78385_3_) {
    this.wrapped.setTextureUV(p_78385_1_, p_78385_3_);
  }

  public void setBrightness(int p_78380_1_) {
    this.wrapped.setBrightness(p_78380_1_);
  }

  public void setColorOpaque_F(float p_78386_1_, float p_78386_2_, float p_78386_3_) {
    this.wrapped.setColorOpaque_F(p_78386_1_, p_78386_2_, p_78386_3_);
  }

  public void setColorRGBA_F(float p_78369_1_, float p_78369_2_, float p_78369_3_, float p_78369_4_) {
    this.wrapped.setColorRGBA_F(p_78369_1_, p_78369_2_, p_78369_3_, p_78369_4_);
  }

  public void setColorOpaque(int p_78376_1_, int p_78376_2_, int p_78376_3_) {
    this.wrapped.setColorOpaque(p_78376_1_, p_78376_2_, p_78376_3_);
  }

  public void setColorRGBA(int p_78370_1_, int p_78370_2_, int p_78370_3_, int p_78370_4_) {
    this.wrapped.setColorRGBA(p_78370_1_, p_78370_2_, p_78370_3_, p_78370_4_);
  }

  
  public void func_154352_a(byte p_154352_1_, byte p_154352_2_, byte p_154352_3_) {
    this.wrapped.func_154352_a(p_154352_1_, p_154352_2_, p_154352_3_);
  }

  public void setColorOpaque_I(int p_78378_1_) {
    this.wrapped.setColorOpaque_I(p_78378_1_);
  }

  public void setColorRGBA_I(int p_78384_1_, int p_78384_2_) {
    this.wrapped.setColorRGBA_I(p_78384_1_, p_78384_2_);
  }

  public void disableColor() {
    this.wrapped.disableColor();
  }

  public void setTranslation(double p_78373_1_, double p_78373_3_, double p_78373_5_) {
    this.wrapped.setTranslation(p_78373_1_, p_78373_3_, p_78373_5_);
  }

  public void addTranslation(float p_78372_1_, float p_78372_2_, float p_78372_3_) {
    this.wrapped.addTranslation(p_78372_1_, p_78372_2_, p_78372_3_);
  }
}
