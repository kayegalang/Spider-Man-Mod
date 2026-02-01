package com.example.finalprojectmod;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class SpiderSuitLayer extends RenderLayer<PlayerRenderState, PlayerModel> {

    private static final ResourceLocation SUIT_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(FinalProjectMod.MODID,
                    "textures/entity/spider_suit.png");

    public SpiderSuitLayer(RenderLayerParent<PlayerRenderState, PlayerModel> parent) {
        super(parent);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight,
                       PlayerRenderState renderState, float limbSwing, float limbSwingAmount) {

        // Check our tracker instead of PlayerRenderState!
        if (SpiderSuitTracker.isWearingSpiderSuit()) {
            this.getParentModel().renderToBuffer(
                    poseStack,
                    buffer.getBuffer(RenderType.entityTranslucent(SUIT_TEXTURE)),
                    packedLight,
                    OverlayTexture.NO_OVERLAY
            );
        }
    }
}