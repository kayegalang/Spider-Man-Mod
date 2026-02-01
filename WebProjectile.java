package com.example.finalprojectmod;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;

public class WebProjectile extends ThrowableProjectile {

    public WebProjectile(EntityType<? extends WebProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public WebProjectile(Level level, LivingEntity shooter) {
        this(ModEntities.WEB_PROJECTILE.get(), level);
        this.setOwner(shooter);
        this.setPos(shooter.getX(), shooter.getEyeY() - 0.1, shooter.getZ());
    }

    @Override
    protected void onHitEntity(@NotNull EntityHitResult result) {
        super.onHitEntity(result);

        Level level = this.level();
        if (!level.isClientSide) {
            // Damage the entity
            result.getEntity().hurtServer(
                    (net.minecraft.server.level.ServerLevel) this.level(),
                    this.damageSources().thrown(this, this.getOwner()),
                    4.0F
            );

            // Place temporary cobweb at entity's position
            BlockPos pos = result.getEntity().blockPosition();
            placeCobweb(level, pos);

            this.discard();
        }
    }

    @Override
    protected void onHitBlock(@NotNull BlockHitResult result) {
        super.onHitBlock(result);

        Level level = this.level();
        if (!level.isClientSide) {
            // Place temporary cobweb near the hit block
            BlockPos pos = result.getBlockPos().relative(result.getDirection());
            placeCobweb(level, pos);

            this.discard();
        }
    }

    private void placeCobweb(Level level, BlockPos pos) {
        // Try the main position first
        if (level.getBlockState(pos).isAir()) {
            level.setBlock(pos, Blocks.COBWEB.defaultBlockState(), 3);
            return;
        }

        // Try adjacent positions if main is blocked
        for (Direction direction : Direction.values()) {
            BlockPos adjacentPos = pos.relative(direction);
            if (level.getBlockState(adjacentPos).isAir()) {
                level.setBlock(adjacentPos, Blocks.COBWEB.defaultBlockState(), 3);
                return;
            }
        }
    }

    @Override
    public void tick() {
        super.tick();

        // Remove after 5 seconds (100 ticks) if it hasn't hit anything
        if (this.tickCount > 100) {
            this.discard();
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        // Required override - leave empty for basic projectile
    }
}