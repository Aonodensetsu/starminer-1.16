package jp.mc.ancientred.starminer.basics.item;

import java.util.List;
import jp.mc.ancientred.starminer.api.Gravity;
import jp.mc.ancientred.starminer.api.GravityDirection;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class ItemEarthStone
  extends Item
{
  private static final int HEAVYSTONE_AFFECT_TICK = 15;
  
  public ItemEarthStone() {
    setTextureName("snowball");
    setMaxStackSize(1);
  }

  public void onUpdate(ItemStack itemStack, World world, Entity entity, int p_77663_4_, boolean p_77663_5_) {
    if (world.provider instanceof jp.mc.ancientred.starminer.api.IZeroGravityWorldProvider && entity instanceof EntityPlayer) {
      Gravity gravity = Gravity.getGravityProp(entity);
      if (gravity.isZeroGravity()) {
        
        entity.fallDistance = 0.0F;
        if (world.isRemote)
        
        { if (gravity != null) gravity.setTemporaryGravityDirection(GravityDirection.upTOdown_YN, 15);
           }
        
        else if (gravity != null) { gravity.acceptExceptionalGravityTick = 45; }
      
      } 
    } 
  }
  
  public void addInformation(ItemStack itemStack, EntityPlayer player, List<String> list, boolean par4) {
    list.add(StatCollector.translateToLocal("starInfo.creativeonly"));
  }
  
  public int getColorFromItemStack(ItemStack p_82790_1_, int p_82790_2_) {
    return 6710886;
  }
}
