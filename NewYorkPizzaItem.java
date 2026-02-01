package com.example.finalprojectmod;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class NewYorkPizzaItem extends Item {
    public NewYorkPizzaItem(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        ItemStack result = super.finishUsingItem(stack, level, entity);

        if (!level.isClientSide && entity instanceof Player) {
            // Add speed effect when pizza is eaten
            entity.addEffect(new MobEffectInstance(
                    MobEffects.SPEED,  // Speed effect
                    200,                         // 10 seconds (200 ticks)
                    1                            // Speed II (amplifier 1)
            ));
        }

        return result;
    }
}