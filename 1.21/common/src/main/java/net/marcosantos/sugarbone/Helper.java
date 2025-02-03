package net.marcosantos.sugarbone;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ParticleUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

import java.util.function.BiFunction;

public class Helper {
    public static boolean growSugarcane(BlockPos bp, Level l) {
        boolean validateItem = l.getBlockState(bp).getBlock() == Blocks.SUGAR_CANE;

        if (!validateItem) return false;

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
            return false;
        }

        if (l.isClientSide()) {
            ParticleUtils.spawnParticleInBlock(l, partPos, 15, ParticleTypes.HAPPY_VILLAGER);
            return false;
        }

        l.levelEvent(1505, bp, 0);
        for (int i = 0; i < amt; ++i) {
            l.setBlockAndUpdate(bp.above(i), Blocks.SUGAR_CANE.defaultBlockState());
        }

        return true;
    }
}
