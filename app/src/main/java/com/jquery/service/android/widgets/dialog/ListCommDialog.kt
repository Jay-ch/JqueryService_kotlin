package com.jquery.service.android.widgets.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import com.jquery.service.android.R
import com.jquery.service.android.adapter.ListCommAdapter
import com.jquery.service.android.entity.ListCommEntity
import com.jquery.service.android.listener.ListSelectDialogListener
import com.jquery.service.android.listener.OnItemClickListener
import jquery.ptr.recyclerview.RecyclerAdapterWithHF
import kotlinx.android.synthetic.main.layout_list_comm_dialog_select.*

/**
 * 自定义列表Dialog
 * @author J.query
 * @date 2019/3/27
 * @email j-query@foxmail.com
 */
class ListCommDialog : Dialog, OnItemClickListener, ListSelectDialogListener {

    internal var context: Context
    lateinit var listener: ListSelectDialogListener
    private var mListCommAdapter: ListCommAdapter? = null

    private var mAdapter: RecyclerAdapterWithHF? = null

    constructor(context: Context, b: Boolean) : super(context, R.style.MyListDialog) {
        this.context = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_list_comm_dialog_select)
        setCancelable(true)
        setCanceledOnTouchOutside(true)
        initViews(true)
    }


    /**
     * 初始化
     */
    private fun initViews(b: Boolean) {
        left.setOnClickListener() {
            dismiss()
        }
        right.setOnClickListener {
            dismiss()
        }
        close.setOnClickListener {
            dismiss()
        }
        list_comm_recycler_view.setOnClickListener {

        }

        mListCommAdapter = ListCommAdapter(context)
        mListCommAdapter?.setItemClickListener(this)
        mAdapter = RecyclerAdapterWithHF(mListCommAdapter)
        list_comm_recycler_view.setLayoutManager(LinearLayoutManager(context))
        list_comm_recycler_view.setAdapter(mAdapter)
        val window = window
        window!!.setGravity(Gravity.BOTTOM)
    }

    private val currentPage: Int = 0

    fun addData(products: List<ListCommEntity>) {
        if (products.isNotEmpty()) {
            if (currentPage > 1) {
                mListCommAdapter?.addAll(products)
            } else {
                mListCommAdapter?.replaceAll(products)
            }
        }
    }

    override fun onItemClick(position: Int) {
        if (listener != null)
            listener.onItemClick(position)
        //dismiss()
    }

    override fun selectType(s: String) {
        if (listener != null)
            listener.selectType(s)
    }

    /**
     * 设置提示内容
     */
    fun setListTitle(s: String) {
        //title.setText(s)
    }

    /**
     * 设置提示内容
     */
    fun setHint(s: String) {
        close.setText(s)
    }


    /**
     * 设置按键值
     */
    fun setLeftAndRightText(lefts: String, rights: String) {
        line.setVisibility(View.VISIBLE)
        left.setText(lefts)
        right.setVisibility(View.VISIBLE)
        right.setText(rights)

    }

    fun showDialog() {
        if (!isShowing) {
            show()
        }
    }

    override fun show() {
        super.show()
        /**
         * 设置宽度全屏，要设置在show的后面
         */
        val layoutParams = window!!.attributes
        layoutParams.gravity = Gravity.BOTTOM
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT

        window!!.decorView.setPadding(0, 0, 0, 0)

        window!!.attributes = layoutParams

    }

    fun setListener(listener: ListSelectDialogListener): ListCommDialog {
        this.listener = listener
        return this
    }
}