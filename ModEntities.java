package com.example.finalprojectmod;

import com.example.finalprojectmod.CorgiEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.entity.monster.CaveSpider;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(Registries.ENTITY_TYPE, FinalProjectMod.MODID);

    // ADD ENTITIES HERE
    public static final DeferredHolder<EntityType<?>, EntityType<RadioactiveSpiderEntity>> RADIOACTIVE_SPIDER =
            ENTITY_TYPES.register("radioactive_spider", () ->
                    EntityType.Builder.<RadioactiveSpiderEntity>of(RadioactiveSpiderEntity::new,
                                    MobCategory.MONSTER)
                            .sized(0.7F, 0.5F) // Same size as cave spider
                            .clientTrackingRange(8)
                            .build(ResourceKey.create(
                                    Registries.ENTITY_TYPE,
                                    ResourceLocation.fromNamespaceAndPath(FinalProjectMod.MODID,
                                            "radioactive_spider")
                            ))
            );

    public static final Supplier<EntityType<WebProjectile>> WEB_PROJECTILE =
            ENTITY_TYPES.register("web_projectile",
                    () -> EntityType.Builder.<WebProjectile>of(WebProjectile::new, MobCategory.MISC)
                            .sized(0.25F, 0.25F)
                            .clientTrackingRange(4)
                            .updateInterval(10)
                            .build(ResourceKey.create(
                                    Registries.ENTITY_TYPE,
                                    ResourceLocation.fromNamespaceAndPath(FinalProjectMod.MODID, "web_projectile")
                            ))
            );

    public static final DeferredHolder<EntityType<?>, EntityType<CorgiEntity>> CORGI =
            ENTITY_TYPES.register("corgi", () ->
                    EntityType.Builder.<CorgiEntity>of(CorgiEntity::new, MobCategory.CREATURE)
                            .sized(0.6F, 0.85F)
                            .clientTrackingRange(10)
                            .build(ResourceKey.create(
                                    Registries.ENTITY_TYPE,
                                    ResourceLocation.fromNamespaceAndPath(FinalProjectMod.MODID,
                                            "corgi")
                            ))
            );

    public static void register(IEventBus modBus) {
        ENTITY_TYPES.register(modBus);
        modBus.addListener(ModEntities::onAttributes);
    }

    private static void onAttributes(EntityAttributeCreationEvent event) {
        event.put(CORGI.get(), Wolf.createAttributes().build());
        event.put(RADIOACTIVE_SPIDER.get(), CaveSpider.createCaveSpider().build());
    }
}
