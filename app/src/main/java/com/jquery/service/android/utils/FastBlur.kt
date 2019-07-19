package com.jquery.service.android.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import com.jquery.service.android.R


/**
 * @author J.query
 * @date 2019/4/29
 * @email j-query@foxmail.com
 */
class FastBlur {
    @SuppressLint("NewApi")
    fun fastblur(context: Context, sentBitmap: Bitmap?, radius: Int): Bitmap? {
        if (sentBitmap == null) {
            return null
        }
        if (Build.VERSION.SDK_INT > 16) {
            val bitmap = sentBitmap.copy(sentBitmap.config, true)

            val rs = RenderScript.create(context)
            val input = Allocation.createFromBitmap(rs,
                    sentBitmap, Allocation.MipmapControl.MIPMAP_NONE,
                    Allocation.USAGE_SCRIPT)
            val output = Allocation.createTyped(rs,
                    input.type)
            val script = ScriptIntrinsicBlur.create(rs,
                    Element.U8_4(rs))
            script.setRadius(radius.toFloat() /* e.g. 3.f */)
            script.setInput(input)
            script.forEach(output)
            output.copyTo(bitmap)
            return bitmap
        }
        return stackblur(context, sentBitmap, radius)
    }

    /**
     * 纯Java实现的虚化，适用老版本api，外部只需调上面的fastblur方法，会自动判断
     *
     * @param context
     * @param sentBitmap
     * @param radius
     * @return
     */
    private fun stackblur(context: Context, sentBitmap: Bitmap, radius: Int): Bitmap? {
        try {
            var bitmap: Bitmap? = null
            try {
                bitmap = sentBitmap.copy(sentBitmap.config, true)
            } catch (e: OutOfMemoryError) {
                e.printStackTrace()
                return sentBitmap
            }

            if (radius < 1) {
                return null
            }
            val w = bitmap!!.width
            val h = bitmap.height

            val pix = IntArray(w * h)
            bitmap.getPixels(pix, 0, w, 0, 0, w, h)

            val wm = w - 1
            val hm = h - 1
            val wh = w * h
            val div = radius + radius + 1

            val r = IntArray(wh)
            val g = IntArray(wh)
            val b = IntArray(wh)
            var rsum: Int
            var gsum: Int
            var bsum: Int
            var x: Int
            var y: Int
            var i: Int
            var p: Int
            var yp: Int
            var yi: Int
            var yw: Int
            val vmin = IntArray(Math.max(w, h))

            var divsum = div + 1 shr 1
            divsum *= divsum
            val dv = IntArray(256 * divsum)
            i = 0
            while (i < 256 * divsum) {
                dv[i] = i / divsum
                i++
            }

            yi = 0
            yw = yi

            val stack = Array(div) { IntArray(3) }
            var stackpointer: Int
            var stackstart: Int
            var sir: IntArray
            var rbs: Int
            val r1 = radius + 1
            var routsum: Int
            var goutsum: Int
            var boutsum: Int
            var rinsum: Int
            var ginsum: Int
            var binsum: Int

            y = 0
            while (y < h) {
                bsum = 0
                gsum = bsum
                rsum = gsum
                boutsum = rsum
                goutsum = boutsum
                routsum = goutsum
                binsum = routsum
                ginsum = binsum
                rinsum = ginsum
                i = -radius
                while (i <= radius) {
                    p = pix[yi + Math.min(wm, Math.max(i, 0))]
                    sir = stack[i + radius]
                    sir[0] = p and 0xff0000 shr 16
                    sir[1] = p and 0x00ff00 shr 8
                    sir[2] = p and 0x0000ff
                    rbs = r1 - Math.abs(i)
                    rsum += sir[0] * rbs
                    gsum += sir[1] * rbs
                    bsum += sir[2] * rbs
                    if (i > 0) {
                        rinsum += sir[0]
                        ginsum += sir[1]
                        binsum += sir[2]
                    } else {
                        routsum += sir[0]
                        goutsum += sir[1]
                        boutsum += sir[2]
                    }
                    i++
                }
                stackpointer = radius

                x = 0
                while (x < w) {

                    r[yi] = dv[rsum]
                    g[yi] = dv[gsum]
                    b[yi] = dv[bsum]

                    rsum -= routsum
                    gsum -= goutsum
                    bsum -= boutsum

                    stackstart = stackpointer - radius + div
                    sir = stack[stackstart % div]

                    routsum -= sir[0]
                    goutsum -= sir[1]
                    boutsum -= sir[2]

                    if (y == 0) {
                        vmin[x] = Math.min(x + radius + 1, wm)
                    }
                    p = pix[yw + vmin[x]]

                    sir[0] = p and 0xff0000 shr 16
                    sir[1] = p and 0x00ff00 shr 8
                    sir[2] = p and 0x0000ff

                    rinsum += sir[0]
                    ginsum += sir[1]
                    binsum += sir[2]

                    rsum += rinsum
                    gsum += ginsum
                    bsum += binsum

                    stackpointer = (stackpointer + 1) % div
                    sir = stack[stackpointer % div]

                    routsum += sir[0]
                    goutsum += sir[1]
                    boutsum += sir[2]

                    rinsum -= sir[0]
                    ginsum -= sir[1]
                    binsum -= sir[2]

                    yi++
                    x++
                }
                yw += w
                y++
            }
            x = 0
            while (x < w) {
                bsum = 0
                gsum = bsum
                rsum = gsum
                boutsum = rsum
                goutsum = boutsum
                routsum = goutsum
                binsum = routsum
                ginsum = binsum
                rinsum = ginsum
                yp = -radius * w
                i = -radius
                while (i <= radius) {
                    yi = Math.max(0, yp) + x

                    sir = stack[i + radius]

                    sir[0] = r[yi]
                    sir[1] = g[yi]
                    sir[2] = b[yi]

                    rbs = r1 - Math.abs(i)

                    rsum += r[yi] * rbs
                    gsum += g[yi] * rbs
                    bsum += b[yi] * rbs

                    if (i > 0) {
                        rinsum += sir[0]
                        ginsum += sir[1]
                        binsum += sir[2]
                    } else {
                        routsum += sir[0]
                        goutsum += sir[1]
                        boutsum += sir[2]
                    }

                    if (i < hm) {
                        yp += w
                    }
                    i++
                }
                yi = x
                stackpointer = radius
                y = 0
                while (y < h) {
                    // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                    pix[yi] = (-0x1000000 and pix[yi] or (dv[rsum] shl 16)
                            or (dv[gsum] shl 8) or dv[bsum])

                    rsum -= routsum
                    gsum -= goutsum
                    bsum -= boutsum

                    stackstart = stackpointer - radius + div
                    sir = stack[stackstart % div]

                    routsum -= sir[0]
                    goutsum -= sir[1]
                    boutsum -= sir[2]

                    if (x == 0) {
                        vmin[y] = Math.min(y + r1, hm) * w
                    }
                    p = x + vmin[y]

                    sir[0] = r[p]
                    sir[1] = g[p]
                    sir[2] = b[p]

                    rinsum += sir[0]
                    ginsum += sir[1]
                    binsum += sir[2]

                    rsum += rinsum
                    gsum += ginsum
                    bsum += binsum

                    stackpointer = (stackpointer + 1) % div
                    sir = stack[stackpointer]

                    routsum += sir[0]
                    goutsum += sir[1]
                    boutsum += sir[2]

                    rinsum -= sir[0]
                    ginsum -= sir[1]
                    binsum -= sir[2]

                    yi += w
                    y++
                }
                x++
            }
            bitmap.setPixels(pix, 0, w, 0, 0, w, h)
            return bitmap
        } catch (e: Exception) {
            return BitmapFactory.decodeResource(context.resources, R.drawable.header_icon)
        }

    }

    /**
     * 图片缩放比例
     */
    private val BITMAP_SCALE = 0.4f
    /**
     * 最大模糊度（在0-25之间），值越大，越模糊
     */
    private val BLUR_RADIUS = 23f

    /**
     * 模糊图片的具体算法
     *
     * @param context 上下文对象
     * @param image   需要模糊的图片
     * @return 模糊处理后的图片
     */
    fun blur(context: Context, image: Bitmap): Bitmap {
        try {
            //计算图片缩小后的长度
            val width = Math.round(image.width * BITMAP_SCALE)
            val height = Math.round(image.height * BITMAP_SCALE)
            //将缩小后的图片做为预渲染图片
            val inputBitmap = Bitmap.createScaledBitmap(image, width, height, false)
            //创建一张渲染后的输出图片
            val outputBitmap = Bitmap.createBitmap(inputBitmap)
            //创建RenderScript内核对象
            val rs = RenderScript.create(context)
            //创建一个模糊效果的RenderScript的工具对象
            val blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))
            //由于RenderScript并没有使用VM来分配内存
            //所以需要使用Allocation类来创建和分配内容空间
            //创建Allocation对象的时候其实内存是空的，需要使用copyTo（）将数据填充进去
            val tmpIn = Allocation.createFromBitmap(rs, inputBitmap)
            val tmpOut = Allocation.createFromBitmap(rs, outputBitmap)
            //设置渲染的模糊程度，25f是最大模糊层
            blurScript.setRadius(BLUR_RADIUS)
            //设置blurScript对象的输入内存
            blurScript.setInput(tmpIn)
            //将输出数据保存到输出内存中
            blurScript.forEach(tmpOut)
            //将数据填充到Allocation中
            tmpOut.copyTo(outputBitmap)
            return outputBitmap
        } catch (e: Exception) {
            e.printStackTrace()
            return image
        } catch (error: OutOfMemoryError) {
            return image
        }

    }


}