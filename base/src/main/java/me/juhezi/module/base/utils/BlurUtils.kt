package me.juhezi.module.base.utils

import android.graphics.Bitmap
import java.lang.Exception
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Callable

/**
 * Created by Juhezi[juhezix@163.com] on 2017/9/14.
 */
class BlurUtils {
    companion object {
        private val EXECUTOR_THREADS = Runtime.getRuntime().availableProcessors()
        private val EXECUTOR: ExecutorService by lazy {
            Executors.newFixedThreadPool(EXECUTOR_THREADS)
        }
        private val STACKBLUR_MUL: List<Short> by lazy {
            listOf<Short>(512, 512, 456, 512, 328, 456, 335, 512, 405, 328, 271, 456, 388, 335, 292,
                    512, 454, 405, 364, 328, 298, 271, 496, 456, 420, 388, 360, 335, 312, 292, 273,
                    512, 482, 454, 428, 405, 383, 364, 345, 328, 312, 298, 284, 271, 259, 496, 475,
                    456, 437, 420, 404, 388, 374, 360, 347, 335, 323, 312, 302, 292, 282, 273, 265,
                    512, 497, 482, 468, 454, 441, 428, 417, 405, 394, 383, 373, 364, 354, 345, 337,
                    328, 320, 312, 305, 298, 291, 284, 278, 271, 265, 259, 507, 496, 485, 475, 465,
                    456, 446, 437, 428, 420, 412, 404, 396, 388, 381, 374, 367, 360, 354, 347, 341,
                    335, 329, 323, 318, 312, 307, 302, 297, 292, 287, 282, 278, 273, 269, 265, 261,
                    512, 505, 497, 489, 482, 475, 468, 461, 454, 447, 441, 435, 428, 422, 417, 411,
                    405, 399, 394, 389, 383, 378, 373, 368, 364, 359, 354, 350, 345, 341, 337, 332,
                    328, 324, 320, 316, 312, 309, 305, 301, 298, 294, 291, 287, 284, 281, 278, 274,
                    271, 268, 265, 262, 259, 257, 507, 501, 496, 491, 485, 480, 475, 470, 465, 460,
                    456, 451, 446, 442, 437, 433, 428, 424, 420, 416, 412, 408, 404, 400, 396, 392,
                    388, 385, 381, 377, 374, 370, 367, 363, 360, 357, 354, 350, 347, 344, 341, 338,
                    335, 332, 329, 326, 323, 320, 318, 315, 312, 310, 307, 304, 302, 299, 297, 294,
                    292, 289, 287, 285, 282, 280, 278, 275, 273, 271, 269, 267, 265, 263, 261, 259)
        }
        private val STACKBLUR_SHR: List<Byte> by lazy {
            listOf<Byte>(9, 11, 12, 13, 13, 14, 14, 15, 15, 15, 15, 16, 16, 16, 16, 17, 17, 17, 17,
                    17, 17, 17, 18, 18, 18, 18, 18, 18, 18, 18, 18, 19, 19, 19, 19, 19, 19, 19, 19,
                    19, 19, 19, 19, 19, 19, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20,
                    20, 20, 20, 20, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21,
                    21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 22, 22, 22, 22, 22, 22, 22, 22, 22,
                    22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22,
                    22, 22, 22, 22, 22, 22, 22, 22, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23,
                    23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23,
                    23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23,
                    23, 23, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24,
                    24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24,
                    24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24,
                    24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24)
        }

        @JvmStatic
        fun blur(original: Bitmap, radius: Float): Bitmap? {
            try {
                val width = original.width
                val height = original.height
                val pixels = IntArray(width * height, { 0 })
                original.getPixels(pixels, 0, width, 0, 0, width, height)
                val cores = EXECUTOR_THREADS
                val horizontal = ArrayList<BlurTask>()
                val vertical = ArrayList<BlurTask>()
                for (bitmap in 0 until cores) {
                    horizontal.add(BlurTask(pixels, width, height,
                            radius.toInt(), cores, bitmap, 1))
                    vertical.add(BlurTask(pixels, width, height,
                            radius.toInt(), cores, bitmap, 2))
                }

                try {
                    EXECUTOR.invokeAll(horizontal)
                } catch (ex: InterruptedException) {
                    return null
                }

                try {
                    EXECUTOR.invokeAll(vertical)
                } catch (ex: InterruptedException) {
                    return null
                }

                return Bitmap.createBitmap(pixels, width, height, original.config)
            } catch (ex: OutOfMemoryError) {
                return null
            } catch (ex: Exception) {
                return null
            }
        }


        private fun blurIteration(src: IntArray, w: Int, h: Int, radius: Int, cores: Int, core: Int, step: Int) {
            val wm = w - 1
            val hm = h - 1
            val div = radius * 2 + 1
            val mul_sum: Int = STACKBLUR_MUL[radius].toInt()
            val shr_sum: Int = STACKBLUR_SHR[radius].toInt()
            val stack = IntArray(div)
            var x: Int
            var y: Int
            var i: Int
            var sp: Int
            var stack_start: Int
            var stack_i: Int
            var src_i: Int
            var dst_i: Int
            var sum_r: Long
            var sum_g: Long
            var sum_b: Long
            var sum_in_r: Long
            var sum_in_g: Long
            var sum_in_b: Long
            var sum_out_r: Long
            var sum_out_g: Long
            var sum_out_b: Long
            val minX: Int
            val maxX: Int
            if (step == 1) {
                minX = core * h / cores
                maxX = (core + 1) * h / cores

                y = minX
                while (y < maxX) {
                    sum_out_b = 0L
                    sum_out_g = 0L
                    sum_out_r = 0L
                    sum_in_b = 0L
                    sum_in_g = 0L
                    sum_in_r = 0L
                    sum_b = 0L
                    sum_g = 0L
                    sum_r = 0L
                    src_i = w * y

                    i = 0
                    while (i <= radius) {
                        stack[i] = src[src_i]
                        sum_r += ((src[src_i].ushr(16) and 255) * (i + 1)).toLong()
                        sum_g += ((src[src_i].ushr(8) and 255) * (i + 1)).toLong()
                        sum_b += ((src[src_i] and 255) * (i + 1)).toLong()
                        sum_out_r += (src[src_i].ushr(16) and 255).toLong()
                        sum_out_g += (src[src_i].ushr(8) and 255).toLong()
                        sum_out_b += (src[src_i] and 255).toLong()
                        ++i
                    }

                    i = 1
                    while (i <= radius) {
                        if (i <= wm) {
                            ++src_i
                        }

                        stack_i = i + radius
                        stack[stack_i] = src[src_i]
                        sum_r += ((src[src_i].ushr(16) and 255) * (radius + 1 - i)).toLong()
                        sum_g += ((src[src_i].ushr(8) and 255) * (radius + 1 - i)).toLong()
                        sum_b += ((src[src_i] and 255) * (radius + 1 - i)).toLong()
                        sum_in_r += (src[src_i].ushr(16) and 255).toLong()
                        sum_in_g += (src[src_i].ushr(8) and 255).toLong()
                        sum_in_b += (src[src_i] and 255).toLong()
                        ++i
                    }

                    sp = radius
                    var xp = radius
                    if (radius > wm) {
                        xp = wm
                    }

                    src_i = xp + y * w
                    dst_i = y * w

                    x = 0
                    while (x < w) {
                        src[dst_i] = ((src[dst_i] and -16777216).toLong() or ((sum_r * mul_sum.toLong()).ushr(shr_sum) and 255L shl 16) or ((sum_g * mul_sum.toLong()).ushr(shr_sum) and 255L shl 8) or ((sum_b * mul_sum.toLong()).ushr(shr_sum) and 255L)).toInt()
                        ++dst_i
                        sum_r -= sum_out_r
                        sum_g -= sum_out_g
                        sum_b -= sum_out_b
                        stack_start = sp + div - radius
                        if (stack_start >= div) {
                            stack_start -= div
                        }

                        sum_out_r -= (stack[stack_start].ushr(16) and 255).toLong()
                        sum_out_g -= (stack[stack_start].ushr(8) and 255).toLong()
                        sum_out_b -= (stack[stack_start] and 255).toLong()
                        if (xp < wm) {
                            ++src_i
                            ++xp
                        }

                        stack[stack_start] = src[src_i]
                        sum_in_r += (src[src_i].ushr(16) and 255).toLong()
                        sum_in_g += (src[src_i].ushr(8) and 255).toLong()
                        sum_in_b += (src[src_i] and 255).toLong()
                        sum_r += sum_in_r
                        sum_g += sum_in_g
                        sum_b += sum_in_b
                        ++sp
                        if (sp >= div) {
                            sp = 0
                        }

                        sum_out_r += (stack[sp].ushr(16) and 255).toLong()
                        sum_out_g += (stack[sp].ushr(8) and 255).toLong()
                        sum_out_b += (stack[sp] and 255).toLong()
                        sum_in_r -= (stack[sp].ushr(16) and 255).toLong()
                        sum_in_g -= (stack[sp].ushr(8) and 255).toLong()
                        sum_in_b -= (stack[sp] and 255).toLong()
                        ++x
                    }
                    ++y
                }
            } else if (step == 2) {
                minX = core * w / cores
                maxX = (core + 1) * w / cores

                x = minX
                while (x < maxX) {
                    sum_out_b = 0L
                    sum_out_g = 0L
                    sum_out_r = 0L
                    sum_in_b = 0L
                    sum_in_g = 0L
                    sum_in_r = 0L
                    sum_b = 0L
                    sum_g = 0L
                    sum_r = 0L
                    src_i = x

                    i = 0
                    while (i <= radius) {
                        stack[i] = src[src_i]
                        sum_r += ((src[src_i].ushr(16) and 255) * (i + 1)).toLong()
                        sum_g += ((src[src_i].ushr(8) and 255) * (i + 1)).toLong()
                        sum_b += ((src[src_i] and 255) * (i + 1)).toLong()
                        sum_out_r += (src[src_i].ushr(16) and 255).toLong()
                        sum_out_g += (src[src_i].ushr(8) and 255).toLong()
                        sum_out_b += (src[src_i] and 255).toLong()
                        ++i
                    }

                    i = 1
                    while (i <= radius) {
                        if (i <= hm) {
                            src_i += w
                        }

                        stack_i = i + radius
                        stack[stack_i] = src[src_i]
                        sum_r += ((src[src_i].ushr(16) and 255) * (radius + 1 - i)).toLong()
                        sum_g += ((src[src_i].ushr(8) and 255) * (radius + 1 - i)).toLong()
                        sum_b += ((src[src_i] and 255) * (radius + 1 - i)).toLong()
                        sum_in_r += (src[src_i].ushr(16) and 255).toLong()
                        sum_in_g += (src[src_i].ushr(8) and 255).toLong()
                        sum_in_b += (src[src_i] and 255).toLong()
                        ++i
                    }

                    sp = radius
                    var yp = radius
                    if (radius > hm) {
                        yp = hm
                    }

                    src_i = x + yp * w
                    dst_i = x

                    y = 0
                    while (y < h) {
                        src[dst_i] = ((src[dst_i] and -16777216).toLong() or ((sum_r * mul_sum.toLong()).ushr(shr_sum.toInt()) and 255L shl 16) or ((sum_g * mul_sum.toLong()).ushr(shr_sum.toInt()) and 255L shl 8) or ((sum_b * mul_sum.toLong()).ushr(shr_sum) and 255L)).toInt()
                        dst_i += w
                        sum_r -= sum_out_r
                        sum_g -= sum_out_g
                        sum_b -= sum_out_b
                        stack_start = sp + div - radius
                        if (stack_start >= div) {
                            stack_start -= div
                        }

                        sum_out_r -= (stack[stack_start].ushr(16) and 255).toLong()
                        sum_out_g -= (stack[stack_start].ushr(8) and 255).toLong()
                        sum_out_b -= (stack[stack_start] and 255).toLong()
                        if (yp < hm) {
                            src_i += w
                            ++yp
                        }

                        stack[stack_start] = src[src_i]
                        sum_in_r += (src[src_i].ushr(16) and 255).toLong()
                        sum_in_g += (src[src_i].ushr(8) and 255).toLong()
                        sum_in_b += (src[src_i] and 255).toLong()
                        sum_r += sum_in_r
                        sum_g += sum_in_g
                        sum_b += sum_in_b
                        ++sp
                        if (sp >= div) {
                            sp = 0
                        }

                        sum_out_r += (stack[sp].ushr(16) and 255).toLong()
                        sum_out_g += (stack[sp].ushr(8) and 255).toLong()
                        sum_out_b += (stack[sp] and 255).toLong()
                        sum_in_r -= (stack[sp].ushr(16) and 255).toLong()
                        sum_in_g -= (stack[sp].ushr(8) and 255).toLong()
                        sum_in_b -= (stack[sp] and 255).toLong()
                        ++y
                    }
                    ++x
                }
            }
        }
    }

    private class BlurTask(val src: IntArray,
                           val w: Int,
                           val h: Int,
                           val radius: Int,
                           val totalCores: Int,
                           val coreIndex: Int,
                           val round: Int) : Callable<Any> {
        override fun call(): Any {
            BlurUtils.blurIteration(src, w, h, radius, totalCores, coreIndex, round)
            return 0
        }
    }
}

//Java Code
/*private static void blurIteration(int[] src, int w, int h, int radius, int cores, int core, int step) {
    int wm = w - 1;
    int hm = h - 1;
    int div = radius * 2 + 1;
    short mul_sum = stackblur_mul[radius];
    byte shr_sum = stackblur_shr[radius];
    int[] stack = new int[div];
    int x;
    int y;
    int i;
    int sp;
    int stack_start;
    int stack_i;
    int src_i;
    int dst_i;
    long sum_r;
    long sum_g;
    long sum_b;
    long sum_in_r;
    long sum_in_g;
    long sum_in_b;
    long sum_out_r;
    long sum_out_g;
    long sum_out_b;
    int minX;
    int maxX;
    if(step == 1) {
        minX = core * h / cores;
        maxX = (core + 1) * h / cores;

        for(y = minX; y < maxX; ++y) {
            sum_out_b = 0L;
            sum_out_g = 0L;
            sum_out_r = 0L;
            sum_in_b = 0L;
            sum_in_g = 0L;
            sum_in_r = 0L;
            sum_b = 0L;
            sum_g = 0L;
            sum_r = 0L;
            src_i = w * y;

            for(i = 0; i <= radius; ++i) {
            stack[i] = src[src_i];
            sum_r += (long)((src[src_i] >>> 16 & 255) * (i + 1));
            sum_g += (long)((src[src_i] >>> 8 & 255) * (i + 1));
            sum_b += (long)((src[src_i] & 255) * (i + 1));
            sum_out_r += (long)(src[src_i] >>> 16 & 255);
            sum_out_g += (long)(src[src_i] >>> 8 & 255);
            sum_out_b += (long)(src[src_i] & 255);
        }

            for(i = 1; i <= radius; ++i) {
            if(i <= wm) {
                ++src_i;
            }

            stack_i = i + radius;
            stack[stack_i] = src[src_i];
            sum_r += (long)((src[src_i] >>> 16 & 255) * (radius + 1 - i));
            sum_g += (long)((src[src_i] >>> 8 & 255) * (radius + 1 - i));
            sum_b += (long)((src[src_i] & 255) * (radius + 1 - i));
            sum_in_r += (long)(src[src_i] >>> 16 & 255);
            sum_in_g += (long)(src[src_i] >>> 8 & 255);
            sum_in_b += (long)(src[src_i] & 255);
        }

            sp = radius;
            int xp = radius;
            if(radius > wm) {
                xp = wm;
            }

            src_i = xp + y * w;
            dst_i = y * w;

            for(x = 0; x < w; ++x) {
            src[dst_i] = (int)((long)(src[dst_i] & -16777216) | (sum_r * (long)mul_sum >>> shr_sum & 255L) << 16 | (sum_g * (long)mul_sum >>> shr_sum & 255L) << 8 | sum_b * (long)mul_sum >>> shr_sum & 255L);
            ++dst_i;
            sum_r -= sum_out_r;
            sum_g -= sum_out_g;
            sum_b -= sum_out_b;
            stack_start = sp + div - radius;
            if(stack_start >= div) {
                stack_start -= div;
            }

            sum_out_r -= (long)(stack[stack_start] >>> 16 & 255);
            sum_out_g -= (long)(stack[stack_start] >>> 8 & 255);
            sum_out_b -= (long)(stack[stack_start] & 255);
            if(xp < wm) {
                ++src_i;
                ++xp;
            }

            stack[stack_start] = src[src_i];
            sum_in_r += (long)(src[src_i] >>> 16 & 255);
            sum_in_g += (long)(src[src_i] >>> 8 & 255);
            sum_in_b += (long)(src[src_i] & 255);
            sum_r += sum_in_r;
            sum_g += sum_in_g;
            sum_b += sum_in_b;
            ++sp;
            if(sp >= div) {
                sp = 0;
            }

            sum_out_r += (long)(stack[sp] >>> 16 & 255);
            sum_out_g += (long)(stack[sp] >>> 8 & 255);
            sum_out_b += (long)(stack[sp] & 255);
            sum_in_r -= (long)(stack[sp] >>> 16 & 255);
            sum_in_g -= (long)(stack[sp] >>> 8 & 255);
            sum_in_b -= (long)(stack[sp] & 255);
        }
        }
    } else if(step == 2) {
        minX = core * w / cores;
        maxX = (core + 1) * w / cores;

        for(x = minX; x < maxX; ++x) {
            sum_out_b = 0L;
            sum_out_g = 0L;
            sum_out_r = 0L;
            sum_in_b = 0L;
            sum_in_g = 0L;
            sum_in_r = 0L;
            sum_b = 0L;
            sum_g = 0L;
            sum_r = 0L;
            src_i = x;

            for(i = 0; i <= radius; ++i) {
            stack[i] = src[src_i];
            sum_r += (long)((src[src_i] >>> 16 & 255) * (i + 1));
            sum_g += (long)((src[src_i] >>> 8 & 255) * (i + 1));
            sum_b += (long)((src[src_i] & 255) * (i + 1));
            sum_out_r += (long)(src[src_i] >>> 16 & 255);
            sum_out_g += (long)(src[src_i] >>> 8 & 255);
            sum_out_b += (long)(src[src_i] & 255);
        }

            for(i = 1; i <= radius; ++i) {
            if(i <= hm) {
                src_i += w;
            }

            stack_i = i + radius;
            stack[stack_i] = src[src_i];
            sum_r += (long)((src[src_i] >>> 16 & 255) * (radius + 1 - i));
            sum_g += (long)((src[src_i] >>> 8 & 255) * (radius + 1 - i));
            sum_b += (long)((src[src_i] & 255) * (radius + 1 - i));
            sum_in_r += (long)(src[src_i] >>> 16 & 255);
            sum_in_g += (long)(src[src_i] >>> 8 & 255);
            sum_in_b += (long)(src[src_i] & 255);
        }

            sp = radius;
            int yp = radius;
            if(radius > hm) {
                yp = hm;
            }

            src_i = x + yp * w;
            dst_i = x;

            for(y = 0; y < h; ++y) {
            src[dst_i] = (int)((long)(src[dst_i] & -16777216) | (sum_r * (long)mul_sum >>> shr_sum & 255L) << 16 | (sum_g * (long)mul_sum >>> shr_sum & 255L) << 8 | sum_b * (long)mul_sum >>> shr_sum & 255L);
            dst_i += w;
            sum_r -= sum_out_r;
            sum_g -= sum_out_g;
            sum_b -= sum_out_b;
            stack_start = sp + div - radius;
            if(stack_start >= div) {
                stack_start -= div;
            }

            sum_out_r -= (long)(stack[stack_start] >>> 16 & 255);
            sum_out_g -= (long)(stack[stack_start] >>> 8 & 255);
            sum_out_b -= (long)(stack[stack_start] & 255);
            if(yp < hm) {
                src_i += w;
                ++yp;
            }

            stack[stack_start] = src[src_i];
            sum_in_r += (long)(src[src_i] >>> 16 & 255);
            sum_in_g += (long)(src[src_i] >>> 8 & 255);
            sum_in_b += (long)(src[src_i] & 255);
            sum_r += sum_in_r;
            sum_g += sum_in_g;
            sum_b += sum_in_b;
            ++sp;
            if(sp >= div) {
                sp = 0;
            }

            sum_out_r += (long)(stack[sp] >>> 16 & 255);
            sum_out_g += (long)(stack[sp] >>> 8 & 255);
            sum_out_b += (long)(stack[sp] & 255);
            sum_in_r -= (long)(stack[sp] >>> 16 & 255);
            sum_in_g -= (long)(stack[sp] >>> 8 & 255);
            sum_in_b -= (long)(stack[sp] & 255);
        }
        }
    }

}*/

