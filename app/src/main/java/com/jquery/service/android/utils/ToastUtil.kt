package com.jquery.service.android.utils

import android.text.TextUtils
import android.widget.Toast
import com.jquery.service.android.app.App

/**
 * @author J.query
 * @date 2019/3/25
 * @email j-query@foxmail.com
 */
object ToastUtil {
    private val context = App.mContext
    private var toast: Toast? = null

    fun show(resId: Int) {
        show(context.resources.getText(resId), Toast.LENGTH_SHORT)
    }

    fun show(resId: Int, duration: Int) {
        show(context.resources.getText(resId), duration)
    }

    fun show(text: CharSequence) {
        show(text, Toast.LENGTH_SHORT)
    }

    /*public static void showDebug(CharSequence text) {
        if (BuildConfig.DEBUG) {
            show(text, Toast.LENGTH_SHORT);
        }
    }*/

    fun show(text: CharSequence?, duration: Int) {
        var text = text
        text = if (TextUtils.isEmpty(if (text == null) "" else text.toString())) "请检查您的网络！" else text
        if (toast == null) {
            toast = Toast.makeText(context, text, duration)
        } else {
            toast!!.setText(text)
        }
        toast!!.show()
    }

    fun show(resId: Int, vararg args: Any) {
        show(String.format(context.resources.getString(resId), *args),
                Toast.LENGTH_SHORT)
    }

    fun show(format: String, vararg args: Any) {
        show(String.format(format, *args), Toast.LENGTH_SHORT)
    }

    fun show(resId: Int, duration: Int, vararg args: Any) {
        show(String.format(context.resources.getString(resId), *args),
                duration)
    }

    fun show(format: String, duration: Int, vararg args: Any) {
        show(String.format(format, *args), duration)
    }
}