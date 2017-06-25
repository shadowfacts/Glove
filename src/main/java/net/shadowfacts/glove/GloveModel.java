package net.shadowfacts.glove;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.*;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import java.util.List;

/**
 * @author shadowfacts
 */
public class GloveModel implements IBakedModel {

	private IBakedModel original;

	public GloveModel(IBakedModel original) {
		this.original = original;
	}

	@Override
	public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		switch (cameraTransformType) {
			case GUI:
				return Pair.of(original, matrix);
			default:
				return Pair.of(this, matrix);
		}
	}

	@Override
	public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
		return ImmutableList.of();
	}

	@Override
	public boolean isAmbientOcclusion() {
		return false;
	}

	@Override
	public boolean isGui3d() {
		return false;
	}

	@Override
	public boolean isBuiltInRenderer() {
		return false;
	}

	@Override
	public TextureAtlasSprite getParticleTexture() {
		return null;
	}

	@Override
	@Deprecated
	public ItemCameraTransforms getItemCameraTransforms() {
		return ItemCameraTransforms.DEFAULT;
	}

	@Override
	public ItemOverrideList getOverrides() {
		return ItemOverrideList.NONE;
	}

}
