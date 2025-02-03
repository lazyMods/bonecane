package net.marcosantos.sugarbone;

import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import org.jetbrains.annotations.NotNull;

public class BonemealDispenserBehavior implements DispenseItemBehavior {
    @Override
    public @NotNull ItemStack dispense(BlockSource blockSource, ItemStack itemStack) {
        var direction = blockSource.state().getValue(DispenserBlock.FACING);
        var targetPos = blockSource.pos().offset(direction.getNormal());

        //note(marco): Jetbrains yells about AutoClosable on level
        var at = blockSource.level().getBlockState(targetPos);

        if (at.is(Blocks.SUGAR_CANE)) {
            if (Helper.growSugarcane(targetPos, blockSource.level())) {
                itemStack.shrink(1);
            }
        }

        return itemStack;
    }
}
