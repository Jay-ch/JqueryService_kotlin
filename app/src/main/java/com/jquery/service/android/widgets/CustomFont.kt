package com.jquery.service.android.widgets

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.TextView
import com.jquery.service.android.R
import java.util.*

/**
 * @author j.query
 * @date 2018/10/16
 * @email j-query@foxmail.com
 */
class CustomFont : TextView {
    /*
     * Caches typefaces based on their file path and name, so that they don't have to be created
     * every time when they are referenced.
     */
    private lateinit var mTypefaces: MutableMap<String, Typeface>

    constructor(context: Context) : super(context, null)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        if (mTypefaces == null) {
            mTypefaces = HashMap()
        }

        val array = context.obtainStyledAttributes(attrs, R.styleable.CustomFont)
        if (array != null) {
            val typefaceAssetPath = array.getString(
                    R.styleable.CustomFont_customFont)

            if (typefaceAssetPath != null) {
                var typeface: Typeface? = null

                if (mTypefaces!!.containsKey(typefaceAssetPath)) {
                    typeface = mTypefaces!![typefaceAssetPath]
                } else {
                    val assets = context.assets
                    typeface = Typeface.createFromAsset(assets, typefaceAssetPath)
                    mTypefaces!![typefaceAssetPath] = typeface
                }

                setTypeface(typeface)
            }
            array.recycle()
        }
    }
}