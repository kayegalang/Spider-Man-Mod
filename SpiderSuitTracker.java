package com.example.finalprojectmod;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;

public class SpiderSuitTracker {

    // Static variable to track if the local player is wearing the spider suit
    private static boolean isWearingSpiderSuit = false;

    public static boolean isWearingSpiderSuit() {
        return isWearingSpiderSuit;
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;

        if (player != null) {
            // Check what's in the chest armor slot
            ItemStack chestItem = player.getItemBySlot(EquipmentSlot.CHEST);

            // Update the static variable
            isWearingSpiderSuit = chestItem.getItem() == FinalProjectMod.SPIDER_SUIT.get();
        } else {
            isWearingSpiderSuit = false;
        }
    }
}