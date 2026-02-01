package com.example.finalprojectmod;

import net.minecraft.client.renderer.entity.CaveSpiderRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class RadioactiveSpiderRenderer extends CaveSpiderRenderer {
    private static final ResourceLocation RADIOACTIVE_SPIDER_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(FinalProjectMod.MODID,
                    "textures/entity/radioactive_spider.png");

    public RadioactiveSpiderRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull LivingEntityRenderState state) {
        return RADIOACTIVE_SPIDER_TEXTURE;
    }
}