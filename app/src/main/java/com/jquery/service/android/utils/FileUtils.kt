package com.jquery.service.android.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.PixelFormat
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.NinePatchDrawable
import android.view.View
import java.io.*

/**
 * @author J.query
 * @date 2019/5/30
 * @email j-query@foxmail.com
 */
object FileUtils {
    /**
     * InputStrem 转byte[]
     *
     * @param in
     * @return
     * @throws Exception
     */
    @Throws(Exception::class)
    fun readStreamToBytes(ins: InputStream): ByteArray {
        val out = ByteArrayOutputStream()
        val buffer = ByteArray(1024 * 8)
        var length = -1
     /*   while (true) {
            length = ins.read(buffer)
            // 将Buffer中的数据写到outputStream对象中
            if (length <= 0) {
                break
            }
            out.write(buffer, 0, length)

        }*/
        while ((ins.read().apply { length = this }) != -1) {
            out.write(buffer, 0, length)
        }



        out.flush()
        val result = out.toByteArray()
        ins.close()
        out.close()
        return result
    }

    /**
     * 写入文件
     *
     * @param in
     * @param file
     */
    @Throws(IOException::class)
    fun writeFile(ins: InputStream, file: File?) {
        if (!file!!.parentFile.exists())
            file.parentFile.mkdirs()

        if (file != null && file.exists())
            file.delete()

        val out = FileOutputStream(file)
        val buffer = ByteArray(1024 * 128)
        var len = -1
       /* while (true) {
            len = ins.read(buffer)
            // 将Buffer中的数据写到outputStream对象中
            if (len <= 0) {
                break
            }
            out.write(buffer, 0, len)

        }*/


        while ((ins.read().apply { len = this }) != -1) {
            out.write(buffer, 0, len)
        }

        out.flush()
        out.close()
        ins.close()

    }

    /**
     * 得到Bitmap的byte
     *
     * @return
     * @author YOLANDA
     */
    fun bmpToByteArray(bmp: Bitmap?): ByteArray? {
        if (bmp == null)
            return null
        val output = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.PNG, 80, output)

        val result = output.toByteArray()
        try {
            output.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return result
    }

    fun drawable2Bitmap(drawable: Drawable): Bitmap? {
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        } else if (drawable is NinePatchDrawable) {
            val bitmap = Bitmap
                    .createBitmap(
                            drawable.getIntrinsicWidth(),
                            drawable.getIntrinsicHeight(),
                            if (drawable.getOpacity() != PixelFormat.OPAQUE)
                                Bitmap.Config.ARGB_8888
                            else
                                Bitmap.Config.RGB_565)
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight())
            drawable.draw(canvas)
            return bitmap
        } else {
            return null
        }
    }

    /*
    * 根据view来生成bitmap图片，可用于截图功能
    */
    fun getViewBitmap(v: View): Bitmap? {

        v.clearFocus() //

        v.isPressed = false //
        // 能画缓存就返回false

        val willNotCache = v.willNotCacheDrawing()
        v.setWillNotCacheDrawing(false)

        val color = v.drawingCacheBackgroundColor
        v.drawingCacheBackgroundColor = 0

        if (color != 0) {
            v.destroyDrawingCache()
        }

        v.buildDrawingCache()

        val cacheBitmap = v.drawingCache ?: return null

        val bitmap = Bitmap.createBitmap(cacheBitmap)
        // Restore the view

        v.destroyDrawingCache()
        v.setWillNotCacheDrawing(willNotCache)
        v.drawingCacheBackgroundColor = color

        return bitmap

    }


}