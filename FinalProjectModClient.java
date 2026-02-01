package com.example.finalprojectmod;

import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.neoforged.neoforge.common.NeoForge;

@EventBusSubscriber(modid = FinalProjectMod.MODID, value = Dist.CLIENT)
public class FinalProjectModClient {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        FinalProjectMod.LOGGER.info("HELLO FROM CLIENT SETUP");
        FinalProjectMod.LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        NeoForge.EVENT_BUS.register(SpiderSuitTracker.class);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.WEB_PROJECTILE.get(), WebProjectileRenderer::new);
        event.registerEntityRenderer(ModEntities.RADIOACTIVE_SPIDER.get(), RadioactiveSpiderRenderer::new);
        event.registerEntityRenderer(ModEntities.CORGI.get(), CorgiRenderer::new);
    }

    @SubscribeEvent
    public static void addPlayerLayers(EntityRenderersEvent.AddLayers event) {
        // Get the default (wide) player renderer
        PlayerRenderer renderer = event.getSkin(net.minecraft.client.resources.PlayerSkin.Model.WIDE);
        if (renderer != null) {
            renderer.addLayer(new SpiderSuitLayer(renderer));
        }

        // Get the slim player renderer
        PlayerRenderer slimRenderer = event.getSkin(net.minecraft.client.resources.PlayerSkin.Model.SLIM);
        if (slimRenderer != null) {
            slimRenderer.addLayer(new SpiderSuitLayer(slimRenderer));
        }
    }
}