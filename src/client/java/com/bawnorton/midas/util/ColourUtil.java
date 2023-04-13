package com.bawnorton.midas.util;

import net.minecraft.client.texture.NativeImage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.system.MemoryUtil;

import java.nio.IntBuffer;

public class ColourUtil {
    public static double luminance(int r, int g, int b) {
        if (r == g && r == b) return r;

        return 0.299 * r + 0.587 * g + 0.114 * b;
    }

    public static int toGray(double luminance) {
        return (int) (Math.round(luminance));
    }

    public static NativeImage convertToGreyscale(@NotNull NativeImage nativeImageOrigin) {
        NativeImage imageCopy = new NativeImage(nativeImageOrigin.getWidth(), nativeImageOrigin.getHeight(),false);
        imageCopy.copyFrom(nativeImageOrigin);

        long pointer = imageCopy.pointer;
        final IntBuffer buffer = MemoryUtil.memIntBuffer(pointer, (imageCopy.getWidth() * imageCopy.getHeight()));
        int[] pixelColors = new int[buffer.remaining()];

        buffer.get(pixelColors);
        buffer.clear();

        int lightest = 0;
        for (int currentColor : pixelColors) {
            int a1 = (currentColor >> 24) & 0xff;
            if (a1 != 0) {
                int r1 = (currentColor >> 16) & 0xFF;
                int g1 = (currentColor >> 8) & 0xFF;
                int b1 = currentColor & 0xFF;

                int grayValue = ColourUtil.toGray(ColourUtil.luminance(r1, g1, b1));

                if (grayValue > lightest) {
                    lightest = grayValue;
                }
            }
        }
        if(lightest == 0) return imageCopy;

        for (int i = 0; i < pixelColors.length; i++) {
            int currentColor = pixelColors[i];
            int a1 = (currentColor >> 24) & 0xff;
            if (a1 != 0) {
                int r1 = (currentColor >> 16) & 0xFF;
                int g1 = (currentColor >> 8) & 0xFF;
                int b1 = currentColor & 0xFF;

                int grayValue = ColourUtil.toGray(ColourUtil.luminance(r1, g1, b1)) * 255 / lightest;
                int newGrayValue = Math.min(grayValue, 255);

                int newColor = (a1 << 24) | (newGrayValue << 16) | (newGrayValue << 8) | newGrayValue;
                pixelColors[i] = newColor;
            }
        }

        buffer.put(pixelColors);
        buffer.clear();

        imageCopy.pointer = pointer;
        return imageCopy;
    }
}
