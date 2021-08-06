package dev.bluecom.starminer.basics.common;

import dev.bluecom.starminer.basics.SMModContainer;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class CommonNetworkHandler {
	private static SimpleChannel CHANNEL;
	private static int ID = 0;
	
	private static int nextID() {
		return ID++;
	}
	
	public static void registerMessages() {
		CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation(SMModContainer.MODID, "simple_channel"), () -> "1.0", (s) -> true, (s) -> true);
		
		CHANNEL.messageBuilder(PacketGravityCoreGUI.class, nextID())
			.encoder(PacketGravityCoreGUI::toBytes)
			.decoder(PacketGravityCoreGUI::new)
			.consumer(PacketGravityCoreGUI::handle)
			.add();
	}
	
	public static void sendToClient(Object packet, ServerPlayerEntity player) {
		CHANNEL.sendTo(packet, player.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
	}
	
	public static void sendToServer(Object packet) {
		CHANNEL.sendToServer(packet);
	}
}
