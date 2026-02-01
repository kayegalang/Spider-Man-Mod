package com.example.finalprojectmod;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.CaveSpider;
import net.minecraft.world.level.Level;

public class RadioactiveSpiderEntity extends CaveSpider {

    public RadioactiveSpiderEntity(EntityType<? extends CaveSpider> type, Level level) {
        super(type, level);
    }
}