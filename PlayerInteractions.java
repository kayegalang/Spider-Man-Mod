package com.example.finalprojectmod;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

public class PlayerInteractions {

    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        Player player = event.getEntity();                     // The player
        ItemStack item = event.getItemStack();                 // The item used
        Level world = event.getLevel();                        // The world (level)
        InteractionHand hand = event.getHand();                // Main or off-hand

        if (!world.isClientSide) {
            // DO STUFF HERE
        }
    }

    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        Player player = event.getEntity();                     // The player
        ItemStack item = event.getItemStack();                 // The item used
        Level world = event.getLevel();                        // The world
        InteractionHand hand = event.getHand();                // Hand used
        Entity target = event.getTarget();                     // Entity being interacted with

        if (!world.isClientSide) {
            // DO STUFF HERE
        }
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        Level world = event.getLevel();
        InteractionHand hand = event.getHand();
        ItemStack item = player.getItemInHand(hand);
        BlockPos pos = event.getPos();

        if (!world.isClientSide) {
            // DO STUFF HERE
        }
    }
}