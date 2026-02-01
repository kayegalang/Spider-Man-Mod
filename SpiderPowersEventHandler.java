package com.example.finalprojectmod;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingFallEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = FinalProjectMod.MODID)
public class SpiderPowersEventHandler {

    @SubscribeEvent
    public static void onPlayerDamaged(LivingDamageEvent.Pre event) {
        // Check if a radioactive spider damaged a player
        if (event.getEntity() instanceof Player player &&
                event.getSource().getEntity() instanceof RadioactiveSpiderEntity) {

            if (!player.level().isClientSide) {
                // Give Spider Powers (infinite duration)
                player.addEffect(new MobEffectInstance(
                        FinalProjectMod.SPIDER_POWERS,
                        -1, // Infinite duration
                        0   // Amplifier 0
                ));

                // Give Strength effect for stronger punches
                player.addEffect(new MobEffectInstance(
                        MobEffects.STRENGTH,
                        -1, // Infinite duration
                        1   // Level II
                ));

                // Grant achievement
                if (player instanceof ServerPlayer serverPlayer) {
                    var server = serverPlayer.getServer();
                    if (server != null) {
                        var advancement = server.getAdvancements()
                                .get(ResourceLocation.fromNamespaceAndPath(
                                        FinalProjectMod.MODID, "spider_bite"));
                        if (advancement != null) {
                            for (String criterion : advancement.value().criteria().keySet()) {
                                serverPlayer.getAdvancements().award(advancement, criterion);
                            }
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();

        // Check if player has spider powers
        if (!player.hasEffect(FinalProjectMod.SPIDER_POWERS)) {
            return;
        }

        // COBWEB IMMUNITY - Speed boost when in cobwebs
        if (isInCobweb(player)) {
            // Speed effect (helps on ground)
            player.addEffect(new MobEffectInstance(
                    MobEffects.SPEED,
                    2,
                    29,     // Speed 30
                    true,
                    false
            ));

            // Velocity boost (helps in air when jumping through)
            player.setDeltaMovement(
                    player.getDeltaMovement().x * 5.0,
                    player.getDeltaMovement().y * 2.0,
                    player.getDeltaMovement().z * 5.0
            );
        }

        // WALL CLIMBING
        if (!player.onGround() && isAgainstWall(player)) {
            if (player.isJumping()) {
                // Climbing up (space bar)
                player.setDeltaMovement(player.getDeltaMovement().x, 0.2, player.getDeltaMovement().z);
            } else if (player.isShiftKeyDown()) {
                // Climbing down (shift key)
                player.setDeltaMovement(player.getDeltaMovement().x, -0.1, player.getDeltaMovement().z);
            } else {
                // Stick to wall - completely stop falling!
                player.setDeltaMovement(player.getDeltaMovement().x, 0, player.getDeltaMovement().z);
            }
            player.fallDistance = 0; // Reset fall distance while on wall
        }
    }

    private static boolean isInCobweb(Player player) {
        BlockPos pos = player.blockPosition();
        BlockPos posAbove = pos.above(); // Check block above (head/chest level)

        // Check if player is in cobweb at feet level OR head level
        return player.level().getBlockState(pos).is(Blocks.COBWEB) ||
                player.level().getBlockState(posAbove).is(Blocks.COBWEB);
    }

    private static boolean isAgainstWall(Player player) {
        BlockPos pos = player.blockPosition();

        // Check all horizontal directions for solid blocks
        for (Direction direction : new Direction[]{Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST}) {
            BlockPos checkPos = pos.relative(direction);
            BlockState state = player.level().getBlockState(checkPos);

            if (!state.isAir() && state.blocksMotion()) {
                return true;
            }
        }

        return false;
    }

    @SubscribeEvent
    public static void onPlayerFall(LivingFallEvent event) {
        if (event.getEntity() instanceof Player player) {
            // Negate fall damage if player has spider powers
            if (player.hasEffect(FinalProjectMod.SPIDER_POWERS)) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onBreakSpeed(net.neoforged.neoforge.event.entity.player.PlayerEvent.BreakSpeed event) {
        Player player = event.getEntity();
        BlockState state = event.getState();

        // If player has spider powers and is breaking a cobweb, make it instant!
        if (player.hasEffect(FinalProjectMod.SPIDER_POWERS) && state.is(Blocks.COBWEB)) {
            // Set break speed to something very high = instant break
            event.setNewSpeed(100.0F); // Instant break like creative mode!
        }
    }
}