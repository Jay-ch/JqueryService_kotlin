package com.jquery.service.android.adapter

import android.content.Context
import android.widget.ImageView
import com.jquery.service.android.widgets.FrescoImageView

/**
 * @author J.query
 * @date 2019/1/23
 * @email j-query@foxmail.com
 */
 abstract class NineGridImageViewAdapter<T> {
    abstract fun onDisplayImage(context: Context, imageView: FrescoImageView, t: T)

    fun onItemImageClick(context: Context, imageView: ImageView, index: Int, list: List<T>) {}

    fun generateImageView(context: Context): ImageView {
        val imageView = FrescoImageView(context)
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP)
        imageView.setShowFilter(true)
        return imageView
    }
}