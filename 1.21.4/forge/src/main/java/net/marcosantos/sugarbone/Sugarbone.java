package net.marcosantos.sugarbone;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ParticleUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.function.BiFunction;

@Mod(Constants.MOD_ID)
public class Sugarbone {

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class ModEvents {

        @SubscribeEvent
        static void onRightClickBlock(PlayerInteractEvent.RightClickBlock ev) {
            var l = ev.getLevel();
            var bp = ev.getPos();
            var e = ev.getEntity();

            boolean validateItem = l.getBlockState(bp).getBlock() == Blocks.SUGAR_CANE
                    && e.getItemInHand(ev.getHand()).getItem() == Items.BONE_MEAL;

            if (!validateItem) return;

            BiFunction<Level, BlockPos, Boolean> isValidSoil = (level, pos) -> l.getBlockState(pos).is(BlockTags.SAND) || l.getBlockState(pos).is(BlockTags.DIRT);

            int amt = 0;
            BlockPos partPos = bp;
            if (isValidSoil.apply(l, bp.below()) && l.isEmptyBlock(bp.above())) {
                // At root, one block tall
                amt = 3;
            } else if (isValidSoil.apply(l, bp.below(2)) && l.isEmptyBlock(bp.above())) {
                // At middle, two block tall
                amt = 2;
                partPos = bp.below();
            } else if (isValidSoil.apply(l, bp.below()) && l.getBlockState(bp.above()).is(Blocks.SUGAR_CANE) && l.isEmptyBlock(bp.above(2))) {
                // At root, two block tall
                amt = 3;
            } else {
                return;
            }

            if (l.isClientSide()) {
                ParticleUtils.spawnParticleInBlock(l, partPos, 15, ParticleTypes.HAPPY_VILLAGER);
                return;
            }

            l.levelEvent(1505, bp, 0);
            for (int i = 0; i < amt; ++i) {
                l.setBlockAndUpdate(bp.above(i), Blocks.SUGAR_CANE.defaultBlockState());
            }

            if (!e.isCreative()) {
                e.getItemInHand(ev.getHand()).shrink(1);
            }
        }
    }
}
