package com.jquery.service.android.adapter

/**
 * @author J.query
 * @date 2019/1/23
 * @email j-query@foxmail.com
 */
interface IData<T> {
    /**
     * 获取当前数据列表
     *
     * @return
     */
    abstract fun getData(): List<T>?

    /**
     * 添加一条数据
     *
     * @param item
     */
    abstract fun add(item: T)

    /**
     * 添加一条数据
     *
     * @param item
     */
    abstract fun add(item: T, index: Int)

    /**
     * 添加多条数据
     *
     * @param list
     */
    abstract fun addAll(list: List<T>)

    /**
     * 更新一条数据
     *
     * @param oldItem
     * @param newItem
     */
    abstract operator fun set(oldItem: T, newItem: T)

    /**
     * 根据下标更新一条数据
     *
     * @param index
     * @param item
     */
    abstract operator fun set(index: Int, item: T)

    /**
     * 删除一条数据
     *
     * @param item
     */
    abstract fun remove(item: T)

    /**
     * 根据下标删除一条数据
     *
     * @param index
     */
    abstract fun remove(index: Int)

    /**
     * 替换所有数据
     *
     * @param item
     */
    abstract fun replaceAll(item: List<T>)

    /**
     * 是否存在某个对象
     *
     * @param item
     * @return
     */
    abstract operator fun contains(item: T): Boolean

    /**
     * 清空数据
     */
    abstract fun clear()
}