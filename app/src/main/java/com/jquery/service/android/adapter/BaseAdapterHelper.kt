package com.jquery.service.android.adapter


import android.annotation.TargetApi
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.graphics.drawable.Icon
import android.net.Uri
import android.os.Build
import android.support.annotation.*
import android.text.util.Linkify
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.*
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.backends.pipeline.PipelineDraweeController
import com.facebook.imagepipeline.common.ResizeOptions
import com.facebook.imagepipeline.request.ImageRequestBuilder
import com.jquery.service.android.widgets.FrescoImageView

/**
 * Created by j.query on 2017/3/31.
 * email j-query@foxmail.com
 */

class BaseAdapterHelper {

    /**
     * Views indexed with their IDs
     */
    /**
     * Views indexed with their IDs
     */
    private lateinit var mViews: SparseArray<View>
    private lateinit var mContext: Context

    private lateinit var mConvertView: View
    private var mPosition: Int = 0
    private var mLayoutId: Int = 0
    /**
     * Package private field to retain the associated user object and detect a change
     */
    internal lateinit var mAssociatedObject: Any

    constructor(context: Context, parent: ViewGroup, layoutId: Int, position: Int) {
        this.mContext = context
        this.mPosition = position
        this.mLayoutId = layoutId
        this.mViews = SparseArray()
        mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false)
        mConvertView?.tag = this
    }

    constructor(mViews: SparseArray<View>, mContext: Context, mConvertView: View, mPosition: Int, mLayoutId: Int, mAssociatedObject: Any) {
        this.mViews = mViews
        this.mContext = mContext
        this.mConvertView = mConvertView
        this.mPosition = mPosition
        this.mLayoutId = mLayoutId
        this.mAssociatedObject = mAssociatedObject
    }

    /**
     * This method is the only entry point to get a BaseAdapterHelper.
     *
     * @param context     The current context.
     * @param convertView The convertView arg passed to the getView() method.
     * @param parent      The parent arg passed to the getView() method.
     * @return A BaseAdapterHelper instance.
     */
    operator fun get(context: Context, convertView: View, parent: ViewGroup,
                     layoutId: Int): BaseAdapterHelper {
        return get(context, convertView, parent, layoutId, -1)
    }

    internal operator fun get(context: Context, convertView: View?, parent: ViewGroup, layoutId: Int,
                              position: Int): BaseAdapterHelper {
        if (convertView == null) {
            return BaseAdapterHelper(context, parent, layoutId, position)
        }

        // Retrieve the existing helper and update its position
        val existingHelper = convertView.tag as BaseAdapterHelper

        if (existingHelper.mLayoutId != layoutId) {
            return BaseAdapterHelper(context, parent, layoutId, position)
        }

        existingHelper.mPosition = position
        return existingHelper
    }

    /**
     * This method allows you to retrieve a view and perform custom
     * operations on it, not covered by the BaseAdapterHelper.
     * If you think it's a common use case, please consider creating
     * a new issue at https://github.com/JoanZapata/base-adapter-helper/issues.
     *
     * @param viewId The guid of the view you want to retrieve.
     */
    fun <T : View> getView(@IdRes viewId: Int): T? {
        return retrieveView(viewId)
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    fun setBackground(@IdRes viewId: Int, drawable: Drawable): BaseAdapterHelper {
        val view = retrieveView<View>(viewId)
        view?.background = drawable
        return this
    }

    /**
     * Will set background color of a view.
     *
     * @param viewId The view guid.
     * @param color  A color, not a resource guid.
     * @return The BaseAdapterHelper for chaining.
     */
    fun setBackgroundColor(@IdRes viewId: Int, @ColorInt color: Int): BaseAdapterHelper {
        val view = retrieveView<View>(viewId)
        view?.setBackgroundColor(color)
        return this
    }

    /**
     * Will set background of a view.
     *
     * @param viewId        The view guid.
     * @param backgroundRes A resource to use as a background.
     * @return The BaseAdapterHelper for chaining.
     */
    fun setBackgroundRes(@IdRes viewId: Int, @DrawableRes backgroundRes: Int): BaseAdapterHelper {
        val view = retrieveView<View>(viewId)
        view?.setBackgroundResource(backgroundRes)
        return this
    }

    /**
     * Will set the text of a TextView.
     *
     * @param viewId The view guid.
     * @param value  The text to put in the text view.
     * @return The BaseAdapterHelper for chaining.
     */
    fun setText(@IdRes viewId: Int, value: CharSequence?): BaseAdapterHelper {
        val view = retrieveView<TextView>(viewId)
        if (value != null && view != null)
            view.text = value
        return this
    }

    fun setTextRes(@IdRes viewId: Int, @StringRes resId: Int): BaseAdapterHelper {
        val view = retrieveView<TextView>(viewId)
        view?.setText(resId)
        return this
    }

    /**
     * Will set text color of a TextView.
     *
     * @param viewId    The view guid.
     * @param textColor The text color (not a resource guid).
     * @return The BaseAdapterHelper for chaining.
     */
    fun setTextColor(@IdRes viewId: Int, @ColorInt textColor: Int): BaseAdapterHelper {
        val view = retrieveView<TextView>(viewId)
        view?.setTextColor(textColor)
        return this
    }

    /**
     * Will set text color of a TextView.
     *
     * @param viewId       The view guid.
     * @param textColorRes The text color resource guid.
     * @return The BaseAdapterHelper for chaining.
     */
    fun setTextColorRes(@IdRes viewId: Int, @ColorRes textColorRes: Int): BaseAdapterHelper {
        val view = retrieveView<TextView>(viewId)
        view?.setTextColor(mContext?.getResources()?.getColor(textColorRes)!!)
        return this
    }

    /**
     * Will set text color of a TextView.
     *
     * @param viewId       The view guid.
     * @param textColorRes The text color resource guid.
     * @param theme        theme The theme used to style the color attributes, may be
     * `null`.
     * @return The BaseAdapterHelper for chaining.
     */
    @TargetApi(Build.VERSION_CODES.M)
    fun setTextColorRes(
            @IdRes viewId: Int, @ColorRes textColorRes: Int, theme: Resources.Theme): BaseAdapterHelper {
        val view = retrieveView<TextView>(viewId)
        view?.setTextColor(mContext?.getResources()?.getColor(textColorRes, theme)!!)
        return this
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    fun setImageIcon(@IdRes viewId: Int, icon: Icon): BaseAdapterHelper {
        val view = retrieveView<ImageView>(viewId)
        view?.setImageIcon(icon)
        return this
    }

    /**
     * Will set the image of an ImageView from a resource guid.
     *
     * @param viewId     The view guid.
     * @param imageResId The image resource guid.
     * @return The BaseAdapterHelper for chaining.
     */
    fun setImageResource(@IdRes viewId: Int, @DrawableRes imageResId: Int): BaseAdapterHelper {
        val view = retrieveView<ImageView>(viewId)
        view?.setImageResource(imageResId)
        return this
    }

    /**
     * Will set the image of an ImageView from a drawable.
     *
     * @param viewId   The view guid.
     * @param drawable The image drawable.
     * @return The BaseAdapterHelper for chaining.
     */
    fun setImageDrawable(@IdRes viewId: Int, drawable: Drawable): BaseAdapterHelper {
        val view = retrieveView<ImageView>(viewId)
        view?.setImageDrawable(drawable)
        return this
    }


    /**
     * Add an action to set the image of an image view. Can be called multiple times.
     */
    fun setImageBitmap(@IdRes viewId: Int, bitmap: Bitmap): BaseAdapterHelper {
        val view = retrieveView<ImageView>(viewId)
        view?.setImageBitmap(bitmap)
        return this
    }

    /**
     * Add an action to set the alpha of a view. Can be called multiple times.
     * Alpha between 0-1.
     */
    fun setAlpha(
            @IdRes viewId: Int, @FloatRange(from = 0.0, to = 1.0) value: Float): BaseAdapterHelper {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            retrieveView<View>(viewId)?.alpha = value
        } else {
            // Pre-honeycomb hack to set Alpha value
            val alpha = AlphaAnimation(value, value)
            alpha.duration = 0
            alpha.fillAfter = true
            retrieveView<View>(viewId)?.startAnimation(alpha)
        }
        return this
    }

    /**
     * Set a view visibility to VISIBLE (true) or GONE (false).
     *
     * @param viewId  The view guid.
     * @param visible True for VISIBLE, false for GONE.
     * @return The BaseAdapterHelper for chaining.
     */
    fun setVisible(@IdRes viewId: Int, visible: Boolean): BaseAdapterHelper {
        val view = retrieveView<View>(viewId)
        view?.visibility = if (visible) View.VISIBLE else View.GONE
        return this
    }

    fun setVisible(@IdRes viewId: Int, visibility: Int): BaseAdapterHelper {
        val view = retrieveView<View>(viewId)
        view?.visibility = visibility
        return this
    }

    fun setEnabled(@IdRes viewId: Int, enabled: Boolean): BaseAdapterHelper {
        val view = retrieveView<View>(viewId)
        view?.isEnabled = enabled
        return this
    }

    fun setFocusable(@IdRes viewId: Int, focusable: Boolean): BaseAdapterHelper {
        val view = retrieveView<View>(viewId)
        view?.isFocusable = focusable
        return this
    }

    fun setFocusableInTouchMode(
            @IdRes viewId: Int, focusableInTouchMode: Boolean): BaseAdapterHelper {
        val view = retrieveView<View>(viewId)
        view?.isFocusableInTouchMode = focusableInTouchMode
        return this
    }

    /**
     * Add links into a TextView.
     *
     * @param viewId The guid of the TextView to linkify.
     * @return The BaseAdapterHelper for chaining.
     */
    fun linkify(@IdRes viewId: Int): BaseAdapterHelper {
        val view = retrieveView<TextView>(viewId)
        Linkify.addLinks(view!!, Linkify.ALL)
        return this
    }

    /**
     * Add links into a TextView.
     *
     * @param viewId The guid of the TextView to linkify.
     * @return The BaseAdapterHelper for chaining.
     * @see Linkify.addLinks
     */
    fun addLinks(@IdRes viewId: Int, mask: Int): BaseAdapterHelper {
        val view = retrieveView<TextView>(viewId)
        Linkify.addLinks(view!!, mask)
        return this
    }

    /**
     * Apply the typeface to the given viewId, and enable subpixel rendering.
     */
    fun setTypeface(@IdRes viewId: Int, typeface: Typeface): BaseAdapterHelper {
        val view = retrieveView<TextView>(viewId)
        view?.typeface = typeface
        view.paintFlags = view.paintFlags or Paint.SUBPIXEL_TEXT_FLAG
        return this
    }

    /**
     * Apply the typeface to all the given viewIds, and enable subpixel rendering.
     */
    fun setTypeface(typeface: Typeface, @IdRes vararg viewIds: Int): BaseAdapterHelper {
        for (viewId in viewIds) {
            val view = retrieveView<TextView>(viewId)
            view?.typeface = typeface
            view.paintFlags = view.paintFlags or Paint.SUBPIXEL_TEXT_FLAG
        }
        return this
    }

    /**
     * Sets the progress of a ProgressBar.
     *
     * @param viewId   The view guid.
     * @param progress The progress.
     * @return The BaseAdapterHelper for chaining.
     */
    fun setProgress(@IdRes viewId: Int, progress: Int): BaseAdapterHelper {
        val view = retrieveView<ProgressBar>(viewId)
        view?.progress = progress
        return this
    }

    /**
     * Sets the progress and max of a ProgressBar.
     *
     * @param viewId   The view guid.
     * @param progress The progress.
     * @param max      The max value of a ProgressBar.
     * @return The BaseAdapterHelper for chaining.
     */
    fun setProgress(@IdRes viewId: Int, progress: Int, max: Int): BaseAdapterHelper {
        val view = retrieveView<ProgressBar>(viewId)
        view?.max = max
        view.progress = progress
        return this
    }

    /**
     * Sets the range of a ProgressBar to 0...max.
     *
     * @param viewId The view guid.
     * @param max    The max value of a ProgressBar.
     * @return The BaseAdapterHelper for chaining.
     */
    fun setMax(@IdRes viewId: Int, max: Int): BaseAdapterHelper {
        val view = retrieveView<ProgressBar>(viewId)
        view?.max = max
        return this
    }

    /**
     * Sets the rating (the number of stars filled) of a RatingBar.
     *
     * @param viewId The view guid.
     * @param rating The rating.
     * @return The BaseAdapterHelper for chaining.
     */
    fun setRating(@IdRes viewId: Int, rating: Float): BaseAdapterHelper {
        val view = retrieveView<RatingBar>(viewId)
        view?.rating = rating
        return this
    }

    /**
     * Sets the rating (the number of stars filled) and max of a RatingBar.
     *
     * @param viewId The view guid.
     * @param rating The rating.
     * @param max    The range of the RatingBar to 0...max.
     * @return The BaseAdapterHelper for chaining.
     */
    fun setRating(@IdRes viewId: Int, rating: Float, max: Int): BaseAdapterHelper {
        val view = retrieveView<RatingBar>(viewId)
        view?.max = max
        view.rating = rating
        return this
    }

    /**
     * Sets the on click listener of the view.
     *
     * @param viewId   The view guid.
     * @param listener The on click listener;
     * @return The BaseAdapterHelper for chaining.
     */
    fun setOnClickListener(
            @IdRes viewId: Int, listener: View.OnClickListener): BaseAdapterHelper {
        val view = retrieveView<View>(viewId)
        view?.setOnClickListener(listener)
        return this
    }

    /**
     * Sets the on touch listener of the view.
     *
     * @param viewId   The view guid.
     * @param listener The on touch listener;
     * @return The BaseAdapterHelper for chaining.
     */
    fun setOnTouchListener(
            @IdRes viewId: Int, listener: View.OnTouchListener): BaseAdapterHelper {
        val view = retrieveView<View>(viewId)
        view?.setOnTouchListener(listener)
        return this
    }

    /**
     * Sets the on long click listener of the view.
     *
     * @param viewId   The view guid.
     * @param listener The on long click listener;
     * @return The BaseAdapterHelper for chaining.
     */
    fun setOnLongClickListener(
            @IdRes viewId: Int, listener: View.OnLongClickListener): BaseAdapterHelper {
        val view = retrieveView<View>(viewId)
        view?.setOnLongClickListener(listener)
        return this
    }

    /**
     * Sets the listview or gridview's item click listener of the view
     *
     * @param viewId   The view guid.
     * @param listener The item on click listener;
     * @return The BaseAdapterHelper for chaining.
     */
    fun setOnItemClickListener(
            @IdRes viewId: Int, listener: AdapterView.OnItemClickListener): BaseAdapterHelper {
        val view = retrieveView<AdapterView<*>>(viewId)
        view?.onItemClickListener = listener
        return this
    }

    /**
     * Sets the listview or gridview's item long click listener of the view
     *
     * @param viewId   The view guid.
     * @param listener The item long click listener;
     * @return The BaseAdapterHelper for chaining.
     */
    fun setOnItemLongClickListener(
            @IdRes viewId: Int, listener: AdapterView.OnItemLongClickListener): BaseAdapterHelper {
        val view = retrieveView<AdapterView<*>>(viewId)
        view?.onItemLongClickListener = listener
        return this
    }

    /**
     * Sets the listview or gridview's item selected click listener of the view
     *
     * @param viewId   The view guid.
     * @param listener The item selected click listener;
     * @return The BaseAdapterHelper for chaining.
     */
    fun setOnItemSelectedClickListener(
            @IdRes viewId: Int, listener: AdapterView.OnItemSelectedListener): BaseAdapterHelper {
        val view = retrieveView<AdapterView<*>>(viewId)
        view?.onItemSelectedListener = listener
        return this
    }

    /**
     * Sets the tag of the view.
     *
     * @param viewId The view guid.
     * @param tag    The tag;
     * @return The BaseAdapterHelper for chaining.
     */
    fun setTag(@IdRes viewId: Int, tag: Any): BaseAdapterHelper {
        val view = retrieveView<View>(viewId)
        view?.tag = tag
        return this
    }

    /**
     * Sets the tag of the view.
     *
     * @param viewId The view guid.
     * @param key    The key of tag;
     * @param tag    The tag;
     * @return The BaseAdapterHelper for chaining.
     */
    fun setTag(@IdRes viewId: Int, key: Int, tag: Any): BaseAdapterHelper {
        val view = retrieveView<View>(viewId)
        view?.setTag(key, tag)
        return this
    }

    /**
     * Sets the checked status of a checkable.
     *
     * @param viewId  The view guid.
     * @param checked The checked status;
     * @return The BaseAdapterHelper for chaining.
     */
    fun setChecked(@IdRes viewId: Int, checked: Boolean): BaseAdapterHelper {
        val view = retrieveView<View>(viewId)
        if (view is CompoundButton) {
            view.isChecked = checked
        } else if (view is CheckedTextView) {
            view.isChecked = checked
        }
        return this
    }

    /**
     * setAdapter未实现
     * Sets the adapter of a adapter view.
     *
     * @param viewId  The view guid.
     * @param mAdapter The adapter;
     * @return The BaseAdapterHelper for chaining.
     */
    fun setAdapter(@IdRes viewId: Int, @NonNull adapter: Adapter): BaseAdapterHelper {
        val view = retrieveView<AdapterView<Adapter>>(viewId)
        view.setAdapter(adapter)
        return this
    }

    /**
     * Retrieve the mConvertView
     */
    fun getView(): View {
        return this.mConvertView!!
    }

    /**
     * Retrieve the overall mPosition of the mData in the list.
     *
     * @throws IllegalArgumentException If the mPosition hasn't been set at the construction of the
     * this helper.
     */
    fun getPosition(): Int {
        if (mPosition == -1) {
            throw IllegalStateException("Use BaseAdapterHelper constructor " + "with mPosition if you need to retrieve the mPosition.")
        }
        return mPosition
    }

    /**
     *
    @SuppressWarnings("unchecked")
    protected <T extends View> T retrieveView(@IdRes int viewId) {
    View view = mViews.get(viewId);
    if (view == null) {
    view = mConvertView.findViewById(viewId);
    mViews.put(viewId, view);
    }
    return (T) view;
    }
     */
    protected fun <T : View> retrieveView(@IdRes viewId: Int): T {
        var view: View? = mViews.get(viewId)
        if (view == null) {
            view = mConvertView.findViewById(viewId)
            mViews.put(viewId, view)
        }
        return (view as T?)!!
    }

    /**
     * Retrieves the last converted object on this view.
     */
    fun getAssociatedObject(): Any {
        return mAssociatedObject
    }

    /**
     * Should be called during convert
     */
    fun setAssociatedObject(associatedObject: Any) {
        this.mAssociatedObject = associatedObject
    }

    /**
     * set fresco image by url
     *
     * @param i
     * @param s
     * @return
     */
    fun setImageUri(i: Int, s: String): BaseAdapterHelper {
        val view: FrescoImageView = retrieveView<View>(i) as FrescoImageView
        view?.setImageURI(s)
        return this
    }

    fun setImageUri(i: Int, uri: Uri, size: Int): BaseAdapterHelper {
        val view: FrescoImageView = retrieveView<View>(i) as FrescoImageView
        val request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setResizeOptions(ResizeOptions(size, size))
                .build()
        val controller = Fresco.newDraweeControllerBuilder()
                .setOldController(view?.getController())
                .setImageRequest(request)
                .build() as PipelineDraweeController
        view.setController(controller)
        return this
    }
    constructor()
}
