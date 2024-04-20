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

#include <time.h>
#include <android/log.h>
#include <stdio.h>
#include <stdlib.h>
#include <math.h>

#define  LOG_TAG    "libimageprocessing"
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)


static int rgb_clamp(int value) {
    if(value > 255) {
        return 255;
    }
    if(value < 0) {
        return 0;
    }
    return value;
}

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
                    (blue & 0x000000FF) | 0xFF000000;
        }

        pixels = (char*)pixels + info->stride;
    }
}
extern "C" JNIEXPORT void JNICALL Java_com_example_drawingapp_Wrappers_invertColors(JNIEnv* env, jobject obj, jobject bitmap) {
    AndroidBitmapInfo info;
    void* pixels;

    // Get bitmap information and pixels
    AndroidBitmap_getInfo(env, bitmap, &info);
    AndroidBitmap_lockPixels(env, bitmap, &pixels);

    // Call the native function
    invertColors(&info, pixels);

    // Release bitmap pixels
    AndroidBitmap_unlockPixels(env, bitmap);
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

            blurredRed = red;
            blurredGreen =  green;
            blurredBlue =  blue;

//            for (int i = -kernelHalf; i <= kernelHalf; i++) {
//                prevPixelIndex = rgb_clamp(xx + i);
//                nextPixelIndex = rgb_clamp(xx + i);
//
//                // Extract the RGB values from the neighboring pixels
//                red = (int)((prevLine[prevPixelIndex] & 0x00FF0000) >> 16);
//                green = (int)((prevLine[prevPixelIndex] & 0x0000FF00) >> 8);
//                blue = (int)(prevLine[prevPixelIndex] & 0x000000FF);
//
//                blurredRed += red;
//                blurredGreen += green;
//                blurredBlue += blue;
//
//                // Extract the RGB values from the neighboring pixels
//                red = (int)((nextLine[nextPixelIndex] & 0x00FF0000) >> 16);
//                green = (int)((nextLine[nextPixelIndex] & 0x0000FF00) >> 8);
//                blue = (int)(nextLine[nextPixelIndex] & 0x000000FF);
//
//                blurredRed += red;
//                blurredGreen += green;
//                blurredBlue += blue;
//            }

            // Calculate the average value of the neighboring pixels
            blurredRed /= 1.5;
            blurredGreen /= 1.5;
            blurredBlue /= 1.5;

            // Set the new pixel back in
            line[xx] =
                    ((blurredRed << 16) & 0x00FF0000) |
                    ((blurredGreen << 8) & 0x0000FF00) |
                    (blurredBlue & 0x000000FF) | 0xFF000000;
        }

        pixels = (char*)pixels + info->stride;
    }


}


extern "C"
JNIEXPORT void JNICALL
Java_com_example_drawingapp_Wrappers_blur(JNIEnv *env, jobject thiz, jobject bitmap) {
    // TODO: implement blur()
    AndroidBitmapInfo info;
    void* pixels;
    AndroidBitmap_getInfo(env, bitmap, &info);
    AndroidBitmap_lockPixels(env, bitmap, &pixels);
    blur(&info, pixels);
    AndroidBitmap_unlockPixels(env, bitmap);
}


static void brightness(AndroidBitmapInfo* info, void* pixels, float brightnessValue){
    int xx, yy, red, green, blue;
    uint32_t* line;

    for(yy = 0; yy < info->height; yy++){
        line = (uint32_t*)pixels;
        for(xx =0; xx < info->width; xx++){

            //extract the RGB values from the pixel
            red = (int) ((line[xx] & 0x00FF0000) >> 16);
            green = (int)((line[xx] & 0x0000FF00) >> 8);
            blue = (int) (line[xx] & 0x00000FF );

            //manipulate each value
            red = rgb_clamp((int)(red * brightnessValue));
            green = rgb_clamp((int)(green * brightnessValue));
            blue = rgb_clamp((int)(blue * brightnessValue));

            // set the new pixel back in
            line[xx] =
                    ((red << 16) & 0x00FF0000) |
                    ((green << 8) & 0x0000FF00) |
                    (blue & 0x000000FF);
        }

        pixels = (char*)pixels + info->stride;
    }
}


extern "C" {
JNIEXPORT void JNICALL Java_com_vm_imageprocessingndkcpp_ImageActivity_brightness(JNIEnv * env, jobject  obj, jobject bitmap, jfloat brightnessValue)
{

    AndroidBitmapInfo  info;
    int ret;
    void* pixels;

    if ((ret = AndroidBitmap_getInfo(env, bitmap, &info)) < 0) {
        LOGE("AndroidBitmap_getInfo() failed ! error=%d", ret);
        return;
    }
    if (info.format != ANDROID_BITMAP_FORMAT_RGBA_8888) {
        LOGE("Bitmap format is not RGBA_8888 !");
        return;
    }

    if ((ret = AndroidBitmap_lockPixels(env, bitmap, &pixels)) < 0) {
        LOGE("AndroidBitmap_lockPixels() failed ! error=%d", ret);
    }

    brightness(&info,pixels, brightnessValue);

    AndroidBitmap_unlockPixels(env, bitmap);
}
}