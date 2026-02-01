package com.example.finalprojectmod;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class WebProjectileRenderer extends EntityRenderer<WebProjectile, EntityRenderState> {

    private static final ResourceLocation TEXTURE =
            ResourceLocation.withDefaultNamespace("textures/item/spider_eye.png");

    public WebProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public @NotNull EntityRenderState createRenderState() {
        return new EntityRenderState();
    }

    @Override
    public void extractRenderState(@NotNull WebProjectile entity,
                                   @NotNull EntityRenderState state,
                                   float partialTick) {
        super.extractRenderState(entity, state, partialTick);
    }
}