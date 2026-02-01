package com.example.finalprojectmod;

import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.BuiltInRegistries;

import java.util.List;

@Mod(FinalProjectMod.MODID)
public class FinalProjectMod {
    public static final String MODID = "finalprojectmod";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);


    public static final ToolMaterial SPIDER_MATERIAL = new ToolMaterial(
            BlockTags.INCORRECT_FOR_STONE_TOOL,  // Can mine what stone can mine
            350,    // Durability (stone = 131, iron = 250, diamond = 1561)
            6.0F,   // Speed (stone = 4.0, iron = 6.0, diamond = 8.0)
            2.0F,   // Attack bonus (stone = 1.0, iron = 2.0)
            14,     // Enchantability (stone = 5, iron = 14, diamond = 10)
            ItemTags.PLANKS  // Repairable with planks (or use cobweb tag)
    );

    public static final DeferredItem<Item> SPIDER_TOOL =
            ITEMS.register("spider_tool", registryName ->
                    new Item(new Item.Properties()
                            .setId(ResourceKey.create(Registries.ITEM, registryName))
                            .durability(350)
                            .pickaxe(
                                    SPIDER_MATERIAL,
                                    4.0F,   // Attack damage (stone pickaxe = 3.0)
                                    -2.8F   // Attack speed (pickaxe standard)
                            )
                    )
            );


    public static final DeferredItem<Item> RADIOACTIVE_WEB =
            ITEMS.register("radioactive_web", registryName ->
                    new Item(new Item.Properties()
                            .setId(ResourceKey.create(Registries.ITEM, registryName))
                            .rarity(Rarity.RARE)
                    )
            );

    public static final DeferredItem<Item> NEW_YORK_PIZZA =
            ITEMS.register("new_york_pizza", registryName ->
                    new NewYorkPizzaItem(new Item.Properties()
                            .setId(ResourceKey.create(Registries.ITEM, registryName))
                            .food(new FoodProperties.Builder()
                                    .nutrition(8)
                                    .saturationModifier(0.6f)
                                    .alwaysEdible()
                                    .build()
                            )
                    )
            );


    public static final DeferredBlock<Block> NEW_YORK_PIZZA_BLOCK =
            BLOCKS.registerSimpleBlock("new_york_pizza_block",
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_RED)
                            .strength(0.5f, 1.0f)  // Easy to break
                            .sound(net.minecraft.world.level.block.SoundType.WOOL)
            );

    public static final DeferredItem<BlockItem> NEW_YORK_PIZZA_BLOCK_ITEM =
            ITEMS.registerSimpleBlockItem("new_york_pizza_block",
                    NEW_YORK_PIZZA_BLOCK);


    public static final DeferredItem<Item> RADIOACTIVE_SPIDER_SPAWN_EGG =
            ITEMS.register("radioactive_spider_spawn_egg", (registryName) ->
                    new SpawnEggItem(
                            ModEntities.RADIOACTIVE_SPIDER.get(),
                            new Item.Properties().setId(
                                    ResourceKey.create(Registries.ITEM, registryName)))
            );

    public static final DeferredHolder<Item, Item> WEB_SLINGER =
            ITEMS.register("web_slinger", registryName ->
                    new WebSlingerItem(
                            new Item.Properties()
                                    .setId(ResourceKey.create(Registries.ITEM, registryName))
                                    .stacksTo(1)
                    )
            );

    public static final DeferredItem<Item> SPIDER_SUIT =
            ITEMS.register("spider_suit", registryName ->
                    new SpiderSuitItem(
                            new Item.Properties()
                                    .setId(ResourceKey.create(Registries.ITEM, registryName))
                                    .durability(2031)
                                    .component(
                                            DataComponents.EQUIPPABLE,
                                            net.minecraft.world.item.equipment.Equippable.builder(EquipmentSlot.CHEST)
                                                    .setEquipSound(SoundEvents.ARMOR_EQUIP_NETHERITE)
                                                    .build()
                                    )
                                    .attributes(
                                            ItemAttributeModifiers.builder()
                                                    .add(
                                                            Attributes.ARMOR,
                                                            new AttributeModifier(
                                                                    ResourceLocation.fromNamespaceAndPath(MODID, "armor"),
                                                                    20.0,
                                                                    AttributeModifier.Operation.ADD_VALUE
                                                            ),
                                                            EquipmentSlotGroup.CHEST
                                                    )
                                                    .add(
                                                            Attributes.ARMOR_TOUGHNESS,
                                                            new AttributeModifier(
                                                                    ResourceLocation.fromNamespaceAndPath(MODID, "toughness"),
                                                                    12.0, // Same as netherite
                                                                    AttributeModifier.Operation.ADD_VALUE
                                                            ),
                                                            EquipmentSlotGroup.CHEST
                                                    )
                                                    .add(
                                                            Attributes.KNOCKBACK_RESISTANCE,
                                                            new AttributeModifier(
                                                                    ResourceLocation.fromNamespaceAndPath(MODID, "knockback_resistance"),
                                                                    0.4,
                                                                    AttributeModifier.Operation.ADD_VALUE
                                                            ),
                                                            EquipmentSlotGroup.CHEST
                                                    )
                                                    .build()
                                    )
                    )
            );



    public static final DeferredRegister<SoundEvent> SOUNDS =
            DeferredRegister.create(Registries.SOUND_EVENT, "finalprojectmod");

    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(Registries.MOB_EFFECT, MODID);

    public static final DeferredHolder<MobEffect, MobEffect> SPIDER_POWERS =
            MOB_EFFECTS.register("spider_powers", SpiderPowersEffect::new);

    public static final DeferredHolder<SoundEvent, SoundEvent> THWIP =
            SOUNDS.register("thwip", () ->
                    SoundEvent.createVariableRangeEvent(
                            ResourceLocation.fromNamespaceAndPath("finalprojectmod", "thwip")));

    public static final DeferredHolder<SoundEvent, SoundEvent> CORGI_BARK =
            SOUNDS.register("corgi_bark", () ->
                    SoundEvent.createVariableRangeEvent(
                            ResourceLocation.fromNamespaceAndPath("finalprojectmod", "corgi_bark")));

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register("example_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.finalprojectmod"))
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> WEB_SLINGER.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(WEB_SLINGER.get());
                output.accept(RADIOACTIVE_SPIDER_SPAWN_EGG.get());
                output.accept(RADIOACTIVE_WEB.get());
                output.accept(SPIDER_SUIT.get());
                output.accept(NEW_YORK_PIZZA.get());
                output.accept(NEW_YORK_PIZZA_BLOCK_ITEM.get());
                output.accept(SPIDER_TOOL.get());
            }).build());

    public FinalProjectMod(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);

        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
        SOUNDS.register(modEventBus);

        ModEntities.register(modEventBus);

        NeoForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::addCreative);
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        MOB_EFFECTS.register(modEventBus);

        NeoForge.EVENT_BUS.register(PlayerInteractions.class);
        NeoForge.EVENT_BUS.register(SpiderPowersEventHandler.class);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("HELLO FROM COMMON SETUP");

        if (Config.LOG_DIRT_BLOCK.getAsBoolean()) {
            LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));
        }

        LOGGER.info("{}{}", Config.MAGIC_NUMBER_INTRODUCTION.get(), Config.MAGIC_NUMBER.getAsInt());
        Config.ITEM_STRINGS.get().forEach((item) -> LOGGER.info("ITEM >> {}", item));
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            event.accept(RADIOACTIVE_SPIDER_SPAWN_EGG);
            event.accept(RADIOACTIVE_WEB);
            event.accept(SPIDER_SUIT);
            event.accept(NEW_YORK_PIZZA);
            event.accept(NEW_YORK_PIZZA_BLOCK_ITEM);
            event.accept(SPIDER_TOOL);
        }
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("HELLO from server starting");
    }
}