package com.jquery.service.android.adapter


/**
 * @author J.query
 * @date 2019/1/23
 * @email j-query@foxmail.com
 */
interface IAdapter<T> {
    /**
     * 数据更新回调
     *
     * @param helper
     * @param item
     * @param position
     */
    abstract fun onUpdate(helper: BaseAdapterHelper, item: T, position: Int)

    /**
     * 当前Item的布局文件
     *
     * @param item
     * @param position
     * @return
     */
    abstract fun getLayoutResId(item: T, position: Int): Int
}