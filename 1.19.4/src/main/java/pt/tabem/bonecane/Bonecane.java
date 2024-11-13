package pt.tabem.bonecane;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod("bonecane")
public class Bonecane {

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class ModEvents {

        @SubscribeEvent
        static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
            boolean validate = event.getLevel().getBlockState(event.getPos()).getBlock() == Blocks.SUGAR_CANE
                    && event.getEntity().getItemInHand(event.getHand()).getItem() == Items.BONE_MEAL
                    && !event.getLevel().isClientSide();
            if (validate) {

                record IsValid(Level levelIn, BlockPos posIn) {
                    public boolean validate() {
                        return IsValid.this.levelIn.isEmptyBlock(IsValid.this.posIn.above()) &&
                                (IsValid.this.levelIn.getBlockState(posIn().below()).is(BlockTags.SAND)
                                        || IsValid.this.levelIn.getBlockState(posIn().below()).is(BlockTags.DIRT));
                    }
                }

                var hasValidSoil = new IsValid(event.getLevel(), event.getPos());
                if (hasValidSoil.validate()) {
                    for (int i = 0; i < 3; ++i)
                        event.getLevel().setBlockAndUpdate(event.getPos().above(i), Blocks.SUGAR_CANE.defaultBlockState());
                    event.getLevel().levelEvent(1505, event.getPos(), 0);
                    if (!event.getEntity().isCreative())
                        event.getEntity().getItemInHand(event.getHand()).shrink(1);
                }
            }
        }
    }
}
