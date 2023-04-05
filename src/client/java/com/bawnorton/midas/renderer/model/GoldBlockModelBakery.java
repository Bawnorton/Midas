package com.bawnorton.midas.renderer.model;

import com.bawnorton.midas.Midas;
import com.bawnorton.midas.util.GoldBlockData;
import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.Baker;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public interface GoldBlockModelBakery<T> {
    BakedModel baseModel();
    T bake(@Nullable Object cacheKey, @Nullable Block original);

    default T bake(@Nullable GoldBlockData data) {
        if(data == null) return bake(null, null);
        return bake(data, data.block());
    }

    class Caching<T> implements GoldBlockModelBakery<T> {
        public Caching(GoldBlockModelBakery<T> uncached) { this.uncached = uncached; }
        private final GoldBlockModelBakery<T> uncached;
        private final Map<Object, T> cache = new HashMap<>();
        private final Object UPDATE_LOCK = new Object();

        @Override
        public T bake(@Nullable Object cacheKey, @Nullable Block originial) {
//            T result = cache.get(cacheKey);
//            if(result != null) return result;

            T result = uncached.bake(cacheKey, originial);
            synchronized(UPDATE_LOCK) { cache.put(cacheKey, result); }
            return result;
        }

        @Override
        public BakedModel baseModel() {
            return uncached.baseModel();
        }
    }

    abstract class Factory<T> {
        private static final SpriteIdentifier PLACEHOLDER = new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, Midas.id("block/placeholder"));

        private final Identifier blockModelId;

        public Factory(Identifier blockModelId) {
            this.blockModelId = blockModelId;
        }

        public Collection<Identifier> getModelDependencies() {
            return ImmutableList.of(blockModelId);
        }

        public void setParents(Function<Identifier, UnbakedModel> unbakedModelGetter) {
            unbakedModelGetter.apply(blockModelId);
        }

        public GoldBlockModelBakery<T> createBakery(Baker baker, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings settings, Identifier modelId) {
            BakedModel baseModel = baker.getOrLoadModel(blockModelId).bake(baker, textureGetter, settings, modelId);
            Sprite placeHolderSprite = textureGetter.apply(PLACEHOLDER);

            return new Caching<>(createBakery(baseModel, placeHolderSprite));
        }

        public abstract GoldBlockModelBakery<T> createBakery(BakedModel baseModel, Sprite placeHolderSprite);
    }
}
