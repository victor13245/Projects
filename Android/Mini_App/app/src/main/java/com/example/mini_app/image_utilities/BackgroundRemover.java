package com.example.mini_app.image_utilities;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;

public class BackgroundRemover {

    public static Bitmap removeWhiteBackground(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(bitmap, 0, 0, null);

        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);

        for (int i = 0; i < pixels.length; i++) {
            if (pixels[i] == Color.WHITE) {
                pixels[i] = Color.TRANSPARENT;
            }
        }

        result.setPixels(pixels, 0, width, 0, 0, width, height);
        return result;
    }

    public static Bitmap removeBlackBackground(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(bitmap, 0, 0, null);

        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);

        for (int i = 0; i < pixels.length; i++) {
            if (pixels[i] == Color.BLACK) {
                pixels[i] = Color.TRANSPARENT;
            }
        }

        result.setPixels(pixels, 0, width, 0, 0, width, height);
        return result;
    }

}
