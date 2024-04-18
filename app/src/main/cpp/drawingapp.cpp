// Write C++ code here.
//
// Do not forget to dynamically load the C++ library into your application.
//
// For instance,
//
// In MainActivity.java:
//    static {
//       System.loadLibrary("drawingapp");
//    }
//
// Or, in MainActivity.kt:
//    companion object {
//      init {
//         System.loadLibrary("drawingapp")
//      }
//    }

#include <stdint.h>
#include <jni.h>
#include <android/bitmap.h>


static int rgb_clamp(int i);

static void invertColors(AndroidBitmapInfo* info, void* pixels) {
    int xx, yy, red, green, blue;
    uint32_t* line;

    for(yy = 0; yy < info->height; yy++) {
        line = (uint32_t*)pixels;
        for(xx = 0; xx < info->width; xx++) {
            // Extract the RGB values from the pixel
            red = (int)((line[xx] & 0x00FF0000) >> 16);
            green = (int)((line[xx] & 0x0000FF00) >> 8);
            blue = (int)(line[xx] & 0x000000FF);

            // Invert each color value
            red = 255 - red;
            green = 255 - green;
            blue = 255 - blue;

            // Set the new pixel back in
            line[xx] =
                    ((red << 16) & 0x00FF0000) |
                    ((green << 8) & 0x0000FF00) |
                    (blue & 0x000000FF);
        }

        pixels = (char*)pixels + info->stride;
    }
}

static void blur(AndroidBitmapInfo* info, void* pixels) {
    int xx, yy, red, green, blue;
    uint32_t* line;
    uint32_t* nextLine;
    uint32_t* prevLine;
    int prevPixelIndex, nextPixelIndex;
    int blurredRed, blurredGreen, blurredBlue;
    const int kernelSize = 3; // Change this value for different blur strengths
    const int kernelHalf = kernelSize / 2;
    int kernelSum = kernelSize * kernelSize;

    for(yy = 0; yy < info->height; yy++) {
        line = (uint32_t*)pixels;

        // Handle edge case for first and last lines
        prevLine = (yy == 0) ? line : (uint32_t*)((char*)line - info->stride);
        nextLine = (yy == info->height - 1) ? line : (uint32_t*)((char*)line + info->stride);

        for(xx = 0; xx < info->width; xx++) {
            // Extract the RGB values from the pixel
            red = (int)((line[xx] & 0x00FF0000) >> 16);
            green = (int)((line[xx] & 0x0000FF00) >> 8);
            blue = (int)(line[xx] & 0x000000FF);

            blurredRed = blurredGreen = blurredBlue = 0;

            for (int i = -kernelHalf; i <= kernelHalf; i++) {
                prevPixelIndex = rgb_clamp(xx + i);
                nextPixelIndex = rgb_clamp(xx + i);

                // Extract the RGB values from the neighboring pixels
                red = (int)((prevLine[prevPixelIndex] & 0x00FF0000) >> 16);
                green = (int)((prevLine[prevPixelIndex] & 0x0000FF00) >> 8);
                blue = (int)(prevLine[prevPixelIndex] & 0x000000FF);

                blurredRed += red;
                blurredGreen += green;
                blurredBlue += blue;

                // Extract the RGB values from the neighboring pixels
                red = (int)((nextLine[nextPixelIndex] & 0x00FF0000) >> 16);
                green = (int)((nextLine[nextPixelIndex] & 0x0000FF00) >> 8);
                blue = (int)(nextLine[nextPixelIndex] & 0x000000FF);

                blurredRed += red;
                blurredGreen += green;
                blurredBlue += blue;
            }

            // Calculate the average value of the neighboring pixels
            blurredRed /= kernelSum;
            blurredGreen /= kernelSum;
            blurredBlue /= kernelSum;

            // Set the new pixel back in
            line[xx] =
                    ((blurredRed << 16) & 0x00FF0000) |
                    ((blurredGreen << 8) & 0x0000FF00) |
                    (blurredBlue & 0x000000FF);
        }

        pixels = (char*)pixels + info->stride;
    }

}



static int rgb_clamp(int value) {
    if (value > 255) {
        return 255;
    }
    if (value < 0) {
        return 0;
    }
    return value;
}

