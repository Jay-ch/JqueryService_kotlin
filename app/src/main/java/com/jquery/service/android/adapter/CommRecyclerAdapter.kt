package com.jquery.service.android.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.jquery.service.android.listener.ListSelectDialogListener

/**
 * @author J.query
 * @date 2019/1/23
 * @email j-query@foxmail.com
 */
abstract class CommRecyclerAdapter<T> : RecyclerView.Adapter<RecyclerView.ViewHolder>, IAdapter<T>, IData<T> {
    abstract var mContext: Context
    private var mLayoutResId: Int = 0
    private var mData: MutableList<T>? = null
    private val VIEW_HEADER: View? = null
    var showList = true
    internal lateinit var itemClickListener: ListSelectDialogListener

    constructor(showList: Boolean, itemClickListener: ListSelectDialogListener) : super() {
        this.showList = showList
        this.itemClickListener = itemClickListener
    }

    fun setType(type: Boolean) {
        this.showList = type
    }

    fun setItemClickListener(itemClickListener: ListSelectDialogListener) {
        this.itemClickListener = itemClickListener
    }

    constructor(context: Context) : this(context, -1, null) {
    }

    constructor(context: Context, layoutResId: Int) : this(context, layoutResId, null) {

    }

    constructor(context: Context, layoutResId: Int, data: List<T>?) : super() {
        this.mData = if (data == null) java.util.ArrayList() else java.util.ArrayList(data)
        this.mContext = context
        this.mLayoutResId = layoutResId
    }


    /* constructor(context: Context) : super() {
         this.mData = null
         this.mContext = context
         this.mLayoutResId = -1
     }


     constructor(context: Context, layoutResId: Int) {
         this.mContext = context
         this.mLayoutResId = layoutResId
     }

     constructor(context: Context, layoutResId: Int, data: List<T>?) {
         this.mData = if (data == null) ArrayList() else ArrayList(data)
         this.mContext = context
         this.mLayoutResId = layoutResId
     }*/

    /*   constructor(context: Context, layoutResId: Int, data: List<T>?): super() {
           this.mData = if (data == null) java.util.ArrayList() else ArrayList(data)
           this.mContext = context
           this.mLayoutResId = layoutResId
       }*/


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val helper = BaseAdapterHelper().get(mContext, null, parent, viewType, -1)
        return RecyclerViewHolder(helper.getView(), helper)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val helper = (holder as CommRecyclerAdapter<*>.RecyclerViewHolder).mAdapterHelper
        helper.setAssociatedObject(getItem(position)!!)
        onUpdate(helper, this!!.getItem(position)!!, position)
    }

   override  fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (null != payloads && payloads.size > 0) {
            val helper = (holder as CommRecyclerAdapter<*>.RecyclerViewHolder).mAdapterHelper
            helper.setAssociatedObject(getItem(position)!!)
            onItemContentChanged(helper, payloads)
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getLayoutResId(this.getItem(position)!!, position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int {

        return if (mData != null) mData!!.size else 0

    }

    override fun getLayoutResId(item: T, position: Int): Int {
        return this.mLayoutResId
    }

    override fun getData(): List<T>? {
        return mData
    }

    override fun add(item: T) {
        mData?.add(item)
        notifyItemInserted(mData?.size!!)
    }

    override fun add(item: T, index: Int) {
        mData?.add(index, item)
        notifyItemInserted(index)
    }

    override fun addAll(list: List<T>) {
        mData?.addAll(list)
        notifyItemRangeInserted(mData?.size!! - list.size, list.size)
    }

    override fun set(oldItem: T, newItem: T) {
        set(mData?.indexOf(oldItem)!!, newItem)
    }

    override fun set(index: Int, item: T) {
        if (index >= 0 && index < itemCount) {
            mData!![index] = item
            notifyItemChanged(index)
        }
    }

    override fun remove(item: T) {
        remove(mData?.indexOf(item)!!)
    }

    override fun remove(index: Int) {
        if (index >= 0 && index < itemCount) {
            mData!!.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    override fun replaceAll(item: List<T>) {
        replaceAll(item, true)
    }

    override fun contains(item: T): Boolean {
        return mData?.contains(item)!!
    }

    override fun clear() {
        mData?.clear()
        notifyDataSetChanged()
    }

    fun replaceAll(elem: List<T>, notifyDataSetChanged: Boolean) {
        mData?.clear()
        mData?.addAll(elem)
        if (notifyDataSetChanged) {
            notifyDataSetChanged()
        }
    }

    /**
     * 更小粒度的更新，比如某个对象的某个属性值改变了，只改变此属性
     * <pre>
     * 此回调执行的前提是：
     * 使用[android.support.v7.util.DiffUtil.Callback]进行数据更新，
     * 并且重写了[android.support.v7.util.DiffUtil.Callback.getChangePayload]方法
     * 使用方法见{https://github.com/qyxxjd/CommonAdapter/blob/master/app/src/main/java/com/classic/adapter/simple/activity/RecyclerViewSimpleActivity.java}
    </pre> *
     *
     * @param helper
     * @param payloads
     */
    fun onItemContentChanged(helper: BaseAdapterHelper,
                             payloads: List<*>?) {

    }

    fun getItem(position: Int): T? {
        return if (position >= mData!!.size) null else mData!![position]
    }

    private inner class RecyclerViewHolder(itemView: View, internal var mAdapterHelper: BaseAdapterHelper) : RecyclerView.ViewHolder(itemView)

    fun isEmpty(): Boolean {
        return mData?.isEmpty() ?: true
    }
}