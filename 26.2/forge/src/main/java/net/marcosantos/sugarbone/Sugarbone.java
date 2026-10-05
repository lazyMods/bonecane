package net.marcosantos.sugarbone;

import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Constants.MOD_ID)
public class Sugarbone {

	public Sugarbone(FMLJavaModLoadingContext context) {
		var modBus = context.getModBusGroup();
		FMLCommonSetupEvent.getBus(modBus).addListener(this::onCommonSetup);
	}

	public void onCommonSetup(FMLCommonSetupEvent ev) {
		DispenserBlock.registerBehavior(Items.BONE_MEAL, new BonemealDispenserBehavior());
	}

	@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
	public static class ModEvents {

		@SubscribeEvent
		static void onRightClickBlock(PlayerInteractEvent.RightClickBlock ev) {

			var l = ev.getLevel();
			var bp = ev.getPos();
			var e = ev.getEntity();

			if (e.getItemInHand(ev.getHand()).getItem() != Items.BONE_MEAL)
				return;

			if (Helper.growSugarcane(bp, l)) {
				if (!e.isCreative()) {
					e.getItemInHand(ev.getHand()).shrink(1);
				}
			}
		}
	}
}
