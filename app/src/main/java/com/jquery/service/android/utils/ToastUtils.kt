package com.jquery.service.android.utils

import android.content.Context
import android.widget.Toast

/**
 * @author J.query
 * @date 2019/4/8
 * @email j-query@foxmail.com
 */
class ToastUtils {
    private var isShow = true
    private var mToast: Toast? = null


    constructor(): super() {
        throw UnsupportedOperationException("不能被实例化")
    }


    //全局控制是否显示Toast
    fun controlShow(ishowToat: Boolean) {
        isShow = ishowToat
    }

    //取消Toast显示
    fun cancelToast() {

        if (isShow && mToast != null) {
            mToast!!.cancel()
        }
    }


    //短时间显示Toast
    fun showShort(context: Context, message: CharSequence) {

        if (isShow) {
            if (mToast == null) {
                mToast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
                mToast!!.show()
            } else {
                mToast!!.setText(message)
            }


        }
    }

    //短时间显示Toast,传入资源ID
    fun showShort(context: Context, resId: Int) {

        if (isShow) {

            if (mToast == null) {
                mToast = Toast.makeText(context, resId, Toast.LENGTH_SHORT)
                mToast!!.show()

            } else {
                mToast!!.setText(resId)
            }


        }

    }

    //长时显示Toast
    fun showLong(context: Context, message: CharSequence) {
        if (isShow) {
            if (mToast == null) {
                mToast = Toast.makeText(context, message, Toast.LENGTH_LONG)
                mToast!!.show()

            } else {
                mToast!!.setText(message)
            }

        }

    }

    //长时间显示Toast
    fun showLong(context: Context, resId: Int) {

        if (isShow) {

            if (mToast == null) {
                mToast = Toast.makeText(context, resId, Toast.LENGTH_LONG)
                mToast!!.show()
            } else {

                mToast!!.setText(resId)
            }


        }

    }


    //自定义显示的Toast时间
    fun show(context: Context, message: CharSequence, duration: Int) {
        if (isShow) {

            if (mToast == null) {
                mToast = Toast.makeText(context, message, duration)
                mToast!!.show()

            } else {
                mToast!!.setText(message)
            }


        }

    }

    //自定义显示时间
    fun show(context: Context, resId: Int, duration: Int) {

        if (isShow) {
            if (mToast == null) {
                mToast = Toast.makeText(context, resId, duration)
                mToast!!.show()
            } else {
                mToast!!.setText(resId)
            }

        }

    }
}