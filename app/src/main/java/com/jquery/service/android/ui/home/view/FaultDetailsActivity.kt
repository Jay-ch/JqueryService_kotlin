package com.jquery.service.android.ui.home.view

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import com.baidu.mapapi.SDKInitializer
import com.jquery.service.android.Base.BaseActivity
import com.jquery.service.android.R
import com.jquery.service.android.ui.MainActivity
import com.jquery.service.android.utils.CommonsStatusBarUtil
import kotlinx.android.synthetic.main.activity_fault_detils_layout.*
import kotlinx.android.synthetic.main.include_title_bar.*


/**
 * 故障详情
 * @author J.query
 * @date 2019/3/21
 * @email j-query@foxmail.com
 */
class FaultDetailsActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        SDKInitializer.initialize(getApplicationContext())
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        super.onCreate(savedInstanceState)
    }

    override fun createLayout(): Int {
        return R.layout.activity_fault_detils_layout
    }

    override fun setStatusBar() {
        CommonsStatusBarUtil.setStatusBar(this)
        top_title.setTitleBackground(resources.getColor(R.color.c_ff))
        top_title.setTitleTextColor(ContextCompat.getColor(this, R.color.c_33))
        CommonsStatusBarUtil.setStatusViewAttr(this, view_status_bar, ContextCompat.getColor(this, R.color.c_ff))
    }

    override fun initViews() {
        super.initViews()
        setStatusBar()
        /*  ed_editor_detail.addTextChangedListener(object :TextWatcher){


          }*/
        /*  editor_detail, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
          public void editTextDetailChange(Editable editable) {
              var detailLength:Int = editable.length();
              id_editor_detail_font_count.setText(""+detailLength + "/500")
              if (detailLength == 499) {
                  islMaxCount = true
              }
              // 不知道为什么执行俩次，所以增加一个标识符去标识
              if (detailLength == 500 && islMaxCount) {
                  UIHelper.getShortToast(self, (String) StringUtils.getResourceContent(self, Convention.RESOURCE_TYPE_STRING, R.string.string_editor_detail_input_limit));
                  islMaxCount = false
              }
          }*/


        ed_editor_detail.addTextChangedListener(object : TextWatcher {

            private var temp: CharSequence? = null
            private var selectionStart: Int = 0
            private var selectionEnd: Int = 0
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                temp = s
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(editable: Editable) {
                val detailLength = editable.length
                id_editor_detail_font_count.setText(detailLength.toString() + "/500")

                if (temp!!.length > 500) {
                    editable.delete(selectionStart - 1, selectionEnd)
                    val tempSelection = selectionStart
                    showToast("只能输入500个字")
                }
            }
        })
        sc_parent_scroll.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                sc_editor_scroll.parent.requestDisallowInterceptTouchEvent(false)
                return false
            }
        })
        sc_editor_scroll.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                p0?.parent?.requestDisallowInterceptTouchEvent(true)
                return false
            }
        })

      /*  img_right.setOnClickListener {
            *//*   val intent = Intent(this@FaultActivity, SearchActivity::class.java)
               intent.putExtra("id", 1)
               startActivity(intent)*//*
            var bundle = Bundle()
            bundle.putString("time_type", "in_fault_year")
            startActivity(SelectionTimeActivity::class.java, bundle)
        }
        bt_new_fault.setOnClickListener {
            val intent = Intent(this@FaultActivity, NewFaultActivity::class.java)
            intent.putExtra("id", 1)
            startActivity(intent)
        }*/
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            /* var bundle = Bundle()
             bundle.putSerializable("id", 1)
             startActivity(MainActivity::class.java, bundle)
             finish()*/
            var bundle = Bundle()
            bundle.putString("id", "1")
            startActivity(MainActivity::class.java, bundle)
            finish()
            return false
        }
        return super.onKeyDown(keyCode, event)
    }

}