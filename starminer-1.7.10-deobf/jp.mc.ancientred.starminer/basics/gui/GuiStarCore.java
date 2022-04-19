package jp.mc.ancientred.starminer.basics.gui;

import cpw.mods.fml.client.config.GuiCheckBox;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import jp.mc.ancientred.starminer.basics.packet.SMPacketSender;
import jp.mc.ancientred.starminer.basics.tileentity.TileEntityGravityGenerator;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;

public class GuiStarCore
  extends GuiContainer
{
  private static final ResourceLocation resource = new ResourceLocation("starminer:textures/gui/GuiStarCore.png");
  
  public static final String REMOTE = "REMOTE";
  
  public static final int ButtonAddGrvOneID = 1;
  
  public static final int ButtonSubGrvOneID = 2;
  public static final int ButtonAddGrvTenID = 3;
  public static final int ButtonSubGrvTenID = 4;
  public static final int ButtonAddRadOneID = 5;
  public static final int ButtonSubRadOneID = 6;
  public static final int ButtonAddRadTenID = 7;
  public static final int ButtonSubRadTenID = 8;
  public static final int ButtonToggleTypeID = 9;
  public static final int CheckBoxUseBufferID = 10;
  private GuiButton ButtonAddGrvOne;
  private GuiButton ButtonSubGrvOne;
  private GuiButton ButtonAddGrvTen;
  private GuiButton ButtonSubGrvTen;
  private GuiButton ButtonAddRadOne;
  private GuiButton ButtonSubRadOne;
  private GuiButton ButtonAddRadTen;
  private GuiButton ButtonSubRadTen;
  private GuiButton ButtonToggleType;
  private GuiCheckBox CheckBoxUseBuffer;
  private int inventoryRows;
  private WeakReference<EntityPlayer> player;
  TileEntityGravityGenerator par3InvStarCore;
  private List floatLabelList = new ArrayList();
  private static final String[] TYPENAMES = new String[] { "starInfo.type.sph", "starInfo.type.cub", "starInfo.type.xcyl", "starInfo.type.ycyl", "starInfo.type.zcyl" };

  public GuiStarCore(EntityPlayer par1Player, TileEntityGravityGenerator par3InvStarCore) {
    super(new ContainerStarCore(par1Player, par3InvStarCore));
    this.par3InvStarCore = par3InvStarCore;
    this.player = new WeakReference<EntityPlayer>(par1Player);
    this.allowUserInput = false;
    short short1 = 222;
    int i = short1 - 108;
    this.inventoryRows = par3InvStarCore.getSizeInventory() / 9;
    this.ySize = 222;
  }

  public void onGuiClosed() {
    super.onGuiClosed();
  }

  public void initGui() {
    super.initGui();
    int xLeft = (this.width - this.xSize) / 2;
    int yTop = (this.height - this.ySize) / 2;
    int grvButtonXLeft = xLeft + 8;
    int grvButtonWidth = 17;
    int grvButtonHeight = 20;
    int grvButtonShiftH = grvButtonWidth + 1;
    int grvButtonFirstRowY = yTop + 8;
    int grvButtonSecondRowY = yTop + 55 - grvButtonHeight;
    this.buttonList.add(this.ButtonSubRadTen = new GuiButton(8, grvButtonXLeft, grvButtonFirstRowY, grvButtonWidth, grvButtonHeight, "-5"));
    this.buttonList.add(this.ButtonSubRadOne = new GuiButton(6, grvButtonXLeft + grvButtonShiftH, grvButtonFirstRowY, grvButtonWidth, grvButtonHeight, "-1"));
    this.buttonList.add(this.ButtonSubGrvTen = new GuiButton(4, grvButtonXLeft, grvButtonSecondRowY, grvButtonWidth, grvButtonHeight, "-5"));
    this.buttonList.add(this.ButtonSubGrvOne = new GuiButton(2, grvButtonXLeft + grvButtonShiftH, grvButtonSecondRowY, grvButtonWidth, grvButtonHeight, "-1"));
    
    grvButtonXLeft = xLeft + 97;
    this.buttonList.add(this.ButtonAddRadOne = new GuiButton(5, grvButtonXLeft, grvButtonFirstRowY, grvButtonWidth, grvButtonHeight, "+1"));
    this.buttonList.add(this.ButtonAddRadTen = new GuiButton(7, grvButtonXLeft + grvButtonShiftH, grvButtonFirstRowY, grvButtonWidth, grvButtonHeight, "+5"));
    this.buttonList.add(this.ButtonAddGrvOne = new GuiButton(1, grvButtonXLeft, grvButtonSecondRowY, grvButtonWidth, grvButtonHeight, "+1"));
    this.buttonList.add(this.ButtonAddGrvTen = new GuiButton(3, grvButtonXLeft + grvButtonShiftH, grvButtonSecondRowY, grvButtonWidth, grvButtonHeight, "+5"));
    
    this.buttonList.add(this.ButtonToggleType = new GuiButton(9, xLeft + 135, grvButtonFirstRowY, 30, grvButtonHeight, StatCollector.translateToLocal(TYPENAMES[this.par3InvStarCore.type])));
    
    this.buttonList.add(this.CheckBoxUseBuffer = new GuiCheckBox(10, xLeft + 145, yTop + 58, "", this.par3InvStarCore.useBufferArea));
    if (this.floatLabelList.isEmpty()) {
      this.floatLabelList.add(StatCollector.translateToLocal("starInfo.bufferzone"));
    }
  }

  public void updateScreen() {
    super.updateScreen();
    int xLeft = (this.width - this.xSize) / 2;
    int yTop = (this.height - this.ySize) / 2;
    
    this.ButtonToggleType.displayString = StatCollector.translateToLocal(TYPENAMES[this.par3InvStarCore.type]);
    if (this.par3InvStarCore.type == 2 || this.par3InvStarCore.type == 3 || this.par3InvStarCore.type == 4) {

      
      this.CheckBoxUseBuffer.visible = true;
      this.CheckBoxUseBuffer.setIsChecked(this.par3InvStarCore.useBufferArea);
    } else {
      this.CheckBoxUseBuffer.visible = false;
    } 
  }

  public void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    
    this.fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 94, 4210752);
    this.fontRendererObj.drawString(StatCollector.translateToLocal("starInfo.inventory"), 8, this.ySize - 162, 4210752);
    
    int centerX = 70;
    String string = StatCollector.translateToLocal("starInfo.titleSSize");
    int centered = centerX - this.fontRendererObj.getStringWidth(string) / 2;
    this.fontRendererObj.drawString(string, centered, this.ySize - 212, 15658734);
    
    string = StatCollector.translateToLocalFormatted("starInfo.gSizeInfo", new Object[] { Integer.valueOf((int)this.par3InvStarCore.starRad) });
    centered = centerX - this.fontRendererObj.getStringWidth(string) / 2;
    this.fontRendererObj.drawString(string, centered, this.ySize - 202, 15658734);
    
    string = StatCollector.translateToLocal("starInfo.titleGrange");
    centered = centerX - this.fontRendererObj.getStringWidth(string) / 2;
    this.fontRendererObj.drawString(string, centered, this.ySize - 187, 15658734);
    
    string = StatCollector.translateToLocalFormatted("starInfo.gRangeInfo", new Object[] { Integer.valueOf((int)this.par3InvStarCore.gravityRange) });
    centered = centerX - this.fontRendererObj.getStringWidth(string) / 2;
    this.fontRendererObj.drawString(string, centered, this.ySize - 177, 15658734);
    
    if (this.CheckBoxUseBuffer.visible && isMouseHoveringOn(mouseX, mouseY, (GuiButton)this.CheckBoxUseBuffer)) {
      int w = this.fontRendererObj.getStringWidth(this.floatLabelList.get(0));
      drawHoveringText(this.floatLabelList, 145 - w / 2 - 3, this.ySize - 137, this.fontRendererObj);
    } 
  }

  private boolean isMouseHoveringOn(int mouseX, int mouseY, GuiButton guiButton) {
    return (mouseX >= guiButton.xPosition && mouseY >= guiButton.yPosition && mouseX < guiButton.xPosition + guiButton.width && mouseY < guiButton.yPosition + guiButton.height);
  }

  protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    this.mc.getTextureManager().bindTexture(resource);
    int k = (this.width - this.xSize) / 2;
    int l = (this.height - this.ySize) / 2;
    drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
  }

  protected void actionPerformed(GuiButton par1GuiButton) {
    EntityPlayer player;
    if ((player = this.player.get()) != null) {
      NetHandlerPlayClient nethandlerplayclient = this.mc.getNetHandler();
      nethandlerplayclient.addToSendQueue(SMPacketSender.createGUIActPacket(par1GuiButton.id));
    } 
  }
}
