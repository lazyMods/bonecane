package net.marcosantos.sugarbone;


import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@Mod(Constants.MOD_ID)
public class Sugarbone {

    @EventBusSubscriber(bus = EventBusSubscriber.Bus.GAME)
    public static class ModEvents {
        @SubscribeEvent
        static void onRightClickBlock(PlayerInteractEvent.RightClickBlock ev) {
            var l = ev.getLevel();
            var bp = ev.getPos();
            var e = ev.getEntity();

            boolean validateItem = l.getBlockState(bp).getBlock() == Blocks.SUGAR_CANE
                    && e.getItemInHand(ev.getHand()).getItem() == Items.BONE_MEAL
                    && !l.isClientSide();

            boolean validateSoil = l.isEmptyBlock(bp.above()) &&
                    (l.getBlockState(bp.below()).is(BlockTags.SAND)
                    || l.getBlockState(bp.below()).is(BlockTags.DIRT));

            if (!validateItem || !validateSoil) return;

            for (int i = 0; i < 3; ++i) {
                l.setBlockAndUpdate(bp.above(i), Blocks.SUGAR_CANE.defaultBlockState());
            }

            l.levelEvent(1505, bp, 0);

            if (!e.isCreative()) {
                e.getItemInHand(ev.getHand()).shrink(1);
            }
        }
    }
}
