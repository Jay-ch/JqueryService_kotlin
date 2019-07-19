package com.jquery.service.android.adapter

import android.content.Context
import android.net.Uri
import android.view.View
import com.jquery.service.android.R
import com.jquery.service.android.entity.UriViewEntity

/**
 * @author J.query
 * @date 2019/4/15
 * @email j-query@foxmail.com
 */
class PhotoUriAdapter : CommRecyclerAdapter<UriViewEntity> {
    private var context: Context
    override var mContext: Context
        get() = context
        set(value) {}

    private var mUris: MutableList<Uri>? = null
    private var mPaths: MutableList<String>? = null
    val TYPE_CAMERA = 1
    val TYPE_PICTURE = 2


    constructor(context: Context) : super(context) {
        this.context = context
    }

    override fun getLayoutResId(item: UriViewEntity, position: Int): Int {
        return if (showList) {
            R.layout.photo_filter_image
        } else {
            R.layout.photo_filter_image
        }
    }

  /*  override fun getItemViewType(position: Int): Int {
        return if (isShowAddItem(position)) {
            TYPE_CAMERA
        } else {
            TYPE_PICTURE
        }
    }

    private fun isShowAddItem(position: Int): Boolean {
        val size = if (mPaths?.size == 0) 0 else mPaths?.size
        return position == size
    }*/

    internal fun setData(uris: MutableList<Uri>?, paths: MutableList<String>?) {
        mUris = uris
        mPaths = paths
        notifyDataSetChanged()
    }

    override fun onUpdate(helper: BaseAdapterHelper, item: UriViewEntity, position: Int) {
        //mImg = view.findViewById<View>(R.id.fiv) as ImageView
        // ll_del = view.findViewById<View>(R.id.ll_del) as LinearLayout
        //tv_duration = view.findViewById<View>(R.id.tv_duration) as TextView
        //少于8张，显示继续添加的图标
        if (mUris?.size!! >=0) {
            //helper.setImageResource(R.id.fiv, R.drawable.addimg_1x)
            helper.setVisible(R.id.ll_del, true)
            helper.setOnClickListener(R.id.ll_add, View.OnClickListener {
                if (itemClickListener != null)
                    itemClickListener.onItemClick(position)
                //itemClickListener.selectType(item.token)
                //layoutPosition = position
                notifyDataSetChanged()
            })
        } else {
            helper.setVisible(R.id.ll_add, false)
          /*  helper.setImageResource(R.id.fiv, R.drawable.addimg_1x)
            helper.setVisible(R.id.fiv, true)*/
            helper.setVisible(R.id.ll_del, true)
            helper.setOnClickListener(R.id.ll_del, View.OnClickListener {
                //val index = helper.adapterPosition
                // 这里有时会返回-1造成数据下标越界,具体可参考getAdapterPosition()源码，
                // 通过源码分析应该是bindViewHolder()暂未绘制完成导致，知道原因的也可联系我~感谢
               /* if (index != RecyclerView.NO_POSITION) {
                    mPaths?.removeAt(index)
                    notifyItemRemoved(index)
                    notifyItemRangeChanged(index, mPaths?.size!!)
                }*/
            })
        }


        // holder.mUri.setText(mUris?.get(position).toString())
        // holder.mPath.setText(mPaths?.get(position))

        //holder.mUri.setAlpha(if (position % 2 == 0) 1.0f else 0.54f)
        //holder.mPath.setAlpha(if (position % 2 == 0) 1.0f else 0.54f)


        /*       helper.setOnClickListener(R.id.ll_tv_sign_record, View.OnClickListener {
                   val bundle = Bundle()
                   bundle.putString("good_id", item.date)
                   val intent = Intent(context, FaultDetailsActivity::class.java)
                   intent.putExtras(bundle)
                   context.startActivity(intent)
               })*/
    }

}