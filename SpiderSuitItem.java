package com.example.finalprojectmod;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class SpiderSuitItem extends Item {

    public SpiderSuitItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public net.minecraft.world.entity.EquipmentSlot getEquipmentSlot(ItemStack stack) {
        return EquipmentSlot.CHEST;
    }
}