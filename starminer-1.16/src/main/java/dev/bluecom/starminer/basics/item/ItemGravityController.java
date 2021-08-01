package dev.bluecom.starminer.basics.item;

import dev.bluecom.starminer.api.GravityCapability;
import dev.bluecom.starminer.api.GravityProvider;
import dev.bluecom.starminer.api.IGravityCapability;
import dev.bluecom.starminer.basics.SMModContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;

public class ItemGravityController extends Item {
	private float gravstate = 0;
	
	public ItemGravityController(Properties properties) {
		super(properties);
	}
	
	public float getGrav() {
		return this.gravstate;
	}
	
	@Override
	public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		if (!world.isClientSide) {
			LazyOptional<IGravityCapability> gravity = player.getCapability(GravityProvider.GRAVITY);
			GravityCapability cap = (GravityCapability) gravity.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!"));
			cap.setGravity(null, null, !cap.getGravityInverted());
			this.gravstate = cap.getGravityInverted() ? 1 : 0;
			IItemPropertyGetter prop = ItemModelsProperties.getProperty(player.getItemInHand(hand).getItem(), new ResourceLocation(SMModContainer.MODID, "gravitystate"));
			prop.call(player.getItemInHand(hand), null, null);
		}
		return super.use(world, player, hand);
	}
}
