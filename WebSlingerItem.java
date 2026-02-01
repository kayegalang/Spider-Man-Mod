package com.example.finalprojectmod;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class WebSlingerItem extends Item {

    public WebSlingerItem(Properties properties) {
        super(properties);
    }

    // RIGHT-CLICK: Grapple/Launch to block
    @Override
    public @NotNull InteractionResult use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            // Try to grapple to a block
            Vec3 lookVec = player.getLookAngle();
            Vec3 start = player.getEyePosition();
            Vec3 end = start.add(lookVec.scale(30)); // 30 block range

            BlockHitResult hitResult = level.clip(
                    new ClipContext(
                            start, end,
                            ClipContext.Block.OUTLINE,
                            ClipContext.Fluid.NONE,
                            player
                    )
            );

            if (hitResult.getType() == HitResult.Type.BLOCK) {
                // Launch player toward the block
                BlockPos attachPoint = hitResult.getBlockPos();
                launchPlayerToBlock(player, attachPoint);

                // Play thwip sound
                level.playSound(null, player.blockPosition(),
                        FinalProjectMod.THWIP.value(),
                        SoundSource.PLAYERS, 1.0F, 1.0F);

                // Create snowflake/web line effect for grappling
                if (level instanceof ServerLevel serverLevel) {
                    Vec3 direction = player.getLookAngle();

                    // Spawn web-like particles in a line
                    for (int i = 0; i < 25; i++) {
                        double distance = i * 0.4;
                        Vec3 particlePos = start.add(direction.scale(distance));

                        serverLevel.sendParticles(
                                ParticleTypes.SNOWFLAKE, // Web-like snowflake particles
                                particlePos.x, particlePos.y, particlePos.z,
                                2, // More particles
                                0.1, 0.1, 0.1, // Bit more spread
                                0.02 // Slight motion
                        );
                    }
                }

                // Damage item
                itemStack.hurtAndBreak(1, player, player.getEquipmentSlotForItem(itemStack));

                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.CONSUME;
    }

    private void launchPlayerToBlock(Player player, BlockPos targetBlock) {
        Vec3 playerPos = player.position();
        Vec3 targetPos = Vec3.atCenterOf(targetBlock);

        // Calculate direction from player to block
        Vec3 direction = targetPos.subtract(playerPos).normalize();

        // Launch with strong velocity
        double launchPower = 2.0;
        Vec3 launchVelocity = direction.scale(launchPower);

        // Add extra upward boost if target is above player
        if (targetPos.y > playerPos.y) {
            launchVelocity = launchVelocity.add(0, 0.3, 0);
        }

        player.setDeltaMovement(launchVelocity);
        player.hurtMarked = true;
        player.hasImpulse = true;
    }

    // LEFT-CLICK: Shoot cobweb projectile
    @Override
    public boolean onEntitySwing(@NotNull ItemStack stack, @NotNull LivingEntity entity, @NotNull InteractionHand hand) {
        if (entity instanceof Player player) {
            Level level = player.level();

            if (!level.isClientSide) {
                shootWeb(level, player, stack);
            }

            return true; // Prevents normal swing animation/sound
        }

        return false;
    }

    private void shootWeb(Level level, Player player, ItemStack stack) {
        // Shoot web projectile
        WebProjectile webProjectile = new WebProjectile(level, player);

        Vec3 lookVec = player.getLookAngle();
        webProjectile.shoot(lookVec.x, lookVec.y, lookVec.z, 1.5F, 1.0F);

        level.addFreshEntity(webProjectile);

        // Play thwip sound
        level.playSound(null, player.blockPosition(),
                FinalProjectMod.THWIP.value(),
                SoundSource.PLAYERS, 1.0F, 1.0F);

        // Create explosive cloud effect for web shooting
        if (level instanceof ServerLevel serverLevel) {
            Vec3 start = player.getEyePosition();
            Vec3 direction = player.getLookAngle();

            // Spawn explosive cloud particles in a burst pattern
            for (int i = 0; i < 20; i++) {
                double distance = i * 0.3;
                Vec3 particlePos = start.add(direction.scale(distance));

                serverLevel.sendParticles(
                        ParticleTypes.CLOUD, // Explosive cloud particles
                        particlePos.x, particlePos.y, particlePos.z,
                        3, // More particles for explosive effect
                        0.2, 0.2, 0.2, // More spread for explosive look
                        0.05 // More speed/motion
                );

                // Add some poof particles for extra effect
                serverLevel.sendParticles(
                        ParticleTypes.POOF,
                        particlePos.x, particlePos.y, particlePos.z,
                        2,
                        0.15, 0.15, 0.15,
                        0.03
                );
            }
        }

        // Damage item
        stack.hurtAndBreak(1, player, player.getEquipmentSlotForItem(stack));
    }

    // Prevent breaking blocks while holding web slinger
    @Override
    public boolean mineBlock(@NotNull ItemStack stack,
                             @NotNull Level level,
                             @NotNull BlockState state,
                             @NotNull BlockPos pos,
                             @NotNull LivingEntity entity) {
        return false;
    }

    @Override
    public float getDestroySpeed(@NotNull ItemStack stack, @NotNull BlockState state) {
        return 0.0F;
    }

    @Override
    public int getUseDuration(@NotNull ItemStack stack, @NotNull LivingEntity entity) {
        return 0;
    }
}