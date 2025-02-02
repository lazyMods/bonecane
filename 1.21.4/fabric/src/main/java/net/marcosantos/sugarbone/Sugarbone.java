package net.marcosantos.sugarbone;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.DispenserBlock;

public class Sugarbone implements ModInitializer {

    @Override
    public void onInitialize() {

        DispenserBlock.registerBehavior(Items.BONE_MEAL, new BonemealDispenserBehavior());

        UseBlockCallback.EVENT.register((e, l, h, hr) -> {
            if (e.getItemInHand(h).getItem() != Items.BONE_MEAL) return InteractionResult.PASS;

            if (Helper.growSugarcane(hr.getBlockPos(), l)) {
                if (!e.isCreative()) {
                    e.getItemInHand(h).shrink(1);
                }
                return InteractionResult.SUCCESS;
            }

            return InteractionResult.PASS;
        });
    }
}
