package dev.bluecom.starminer.basics.item;

import dev.bluecom.starminer.api.GravityCapability;
import dev.bluecom.starminer.api.GravityDirection;
import dev.bluecom.starminer.api.GravityProvider;
import dev.bluecom.starminer.api.IGravityCapability;
import dev.bluecom.starminer.basics.SMModContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
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
		return super.use(world, player, hand);
	}
	
	@Override
	public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
		World world = context.getLevel();
		if (!world.isClientSide) {
			LazyOptional<IGravityCapability> gravity = context.getPlayer().getCapability(GravityProvider.GRAVITY);
			GravityCapability cap = (GravityCapability) gravity.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!"));
			cap.setGravity(null, null, !cap.getGravityInverted());
			this.gravstate = cap.getGravityInverted() ? 1 : 0;
			IItemPropertyGetter prop = ItemModelsProperties.getProperty(stack.getItem(), new ResourceLocation(SMModContainer.MODID, "gravitystate"));
			prop.call(stack, null, null);
		}
		return super.onItemUseFirst(stack, context);
	}
}
