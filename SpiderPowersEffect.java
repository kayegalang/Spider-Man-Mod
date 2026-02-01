package com.example.finalprojectmod;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class SpiderPowersEffect extends MobEffect {

    public SpiderPowersEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xFF0000); // Red color (Spider-Man!)
    }
}