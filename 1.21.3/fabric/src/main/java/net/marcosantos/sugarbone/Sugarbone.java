package net.marcosantos.sugarbone;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

public class Sugarbone implements ModInitializer {

    @Override
    public void onInitialize() {
        UseBlockCallback.EVENT.register((e, l, h, hr) -> {
            var bp = hr.getBlockPos();

            boolean validateItem = l.getBlockState(bp).getBlock() == Blocks.SUGAR_CANE
                    && e.getItemInHand(h).getItem() == Items.BONE_MEAL
                    && !l.isClientSide();

            // Check its right-clicking the ground sugarcane to prevent infinite growth
            boolean validateSoil = l.isEmptyBlock(bp.above()) &&
                    (l.getBlockState(bp.below()).is(BlockTags.SAND)
                    || l.getBlockState(bp.below()).is(BlockTags.DIRT));

            if (!(validateItem && validateSoil)) return InteractionResult.PASS;

            for (int i = 0; i < 3; ++i) {
                l.setBlockAndUpdate(bp.above(i), Blocks.SUGAR_CANE.defaultBlockState());
            }

            l.levelEvent(1505, bp, 0);

            if (!e.isCreative()) {
                e.getItemInHand(h).shrink(1);
            }

            return InteractionResult.SUCCESS;
        });
    }
}
