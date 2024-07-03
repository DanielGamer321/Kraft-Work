package com.danielgamer321.rotp_kw.client.render.entity.model.projectile;

import com.danielgamer321.rotp_kw.entity.damaging.projectile.KWItemEntity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;

// Made with Blockbench 4.8.3
// Exported for Minecraft version 1.15 - 1.16 with Mojang mappings
// Paste this class into your mod and generate all required imports

public class KWItemModel extends EntityModel<KWItemEntity> {
	

	public KWItemModel() {
		texWidth = 8;
		texHeight = 8;

		
	}

	@Override
	public void setupAnim(KWItemEntity entity, float walkAnimPos, float walkAnimSpeed, float ticks, float yRotationOffset, float xRotation) {
	}

	@Override
	public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
	}
}