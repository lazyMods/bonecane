package net.marcosantos.sugarbone;


import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.DispenserBlock;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@Mod(Constants.MOD_ID)
public class Sugarbone {

    public Sugarbone(IEventBus bus, ModContainer container) {
        bus.addListener(this::commonSetup);
    }

    public void commonSetup(FMLCommonSetupEvent ev) {
        DispenserBlock.registerBehavior(Items.BONE_MEAL, new BonemealDispenserBehavior());
    }

    @EventBusSubscriber(bus = EventBusSubscriber.Bus.GAME)
    public static class ModEvents {
        @SubscribeEvent
        static void onRightClickBlock(PlayerInteractEvent.RightClickBlock ev) {
            var l = ev.getLevel();
            var bp = ev.getPos();
            var e = ev.getEntity();

            if (e.getItemInHand(ev.getHand()).getItem() != Items.BONE_MEAL) return;

            if (Helper.growSugarcane(bp, l)) {
                if (!e.isCreative()) {
                    e.getItemInHand(ev.getHand()).shrink(1);
                }
            }
        }
    }
}
