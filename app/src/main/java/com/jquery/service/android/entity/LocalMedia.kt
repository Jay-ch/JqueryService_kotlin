package com.jquery.service.android.entity

import android.os.Parcel
import android.os.Parcelable
import android.text.TextUtils

/**
 * @author J.query
 * @date 2019/4/15
 * @email j-query@foxmail.com
 */
class LocalMedia: Parcelable {
    private var path: String? = null
    private var compressPath: String? = null
    private var cutPath: String? = null
    private var duration: Long = 0
    private var isChecked: Boolean = false
    private var isCut: Boolean = false
    private  var position: Int = 0
    private var num: Int = 0
    private var mimeType: Int = 0
    private var pictureType: String? = null
    private var compressed: Boolean = false
    private var width: Int = 0
    private var height: Int = 0
    val CREATOR: Parcelable.Creator<LocalMedia> = object : Parcelable.Creator<LocalMedia> {
        override fun createFromParcel(source: Parcel?): LocalMedia {
            return LocalMedia(source!!)
        }

        override fun newArray(size: Int): Array<LocalMedia?> {
            return arrayOfNulls(size)
        }
    }

    constructor(): super() {}

    constructor(path: String, duration: Long, mimeType: Int, pictureType: String):super() {
        this.path = path
        this.duration = duration
        this.mimeType = mimeType
        this.pictureType = pictureType
    }

    constructor(path: String, duration: Long, mimeType: Int, pictureType: String, width: Int, height: Int): super() {
        this.path = path
        this.duration = duration
        this.mimeType = mimeType
        this.pictureType = pictureType
        this.width = width
        this.height = height
    }

    constructor(path: String, duration: Long, isChecked: Boolean, position: Int, num: Int, mimeType: Int): super() {
        this.path = path
        this.duration = duration
        this.isChecked = isChecked
        this.position = position
        this.num = num
        this.mimeType = mimeType
    }

    fun getPictureType(): String {
        if (TextUtils.isEmpty(this.pictureType)) {
            this.pictureType = "image/jpeg"
        }

        return this.pictureType!!
    }

    fun setPictureType(pictureType: String) {
        this.pictureType = pictureType
    }

    fun getPath(): String? {
        return this.path
    }

    fun setPath(path: String) {
        this.path = path
    }

    fun getCompressPath(): String? {
        return this.compressPath
    }

    fun setCompressPath(compressPath: String) {
        this.compressPath = compressPath
    }

    fun getCutPath(): String? {
        return this.cutPath
    }

    fun setCutPath(cutPath: String) {
        this.cutPath = cutPath
    }

    fun getDuration(): Long {
        return this.duration
    }

    fun setDuration(duration: Long) {
        this.duration = duration
    }

    fun isChecked(): Boolean {
        return this.isChecked
    }

    fun setChecked(checked: Boolean) {
        this.isChecked = checked
    }

    fun isCut(): Boolean {
        return this.isCut
    }

    fun setCut(cut: Boolean) {
        this.isCut = cut
    }

    fun getPosition(): Int {
        return this.position
    }

    fun setPosition(position: Int) {
        this.position = position
    }

    fun getNum(): Int {
        return this.num
    }

    fun setNum(num: Int) {
        this.num = num
    }

    fun getMimeType(): Int {
        return this.mimeType
    }

    fun setMimeType(mimeType: Int) {
        this.mimeType = mimeType
    }

    fun isCompressed(): Boolean {
        return this.compressed
    }

    fun setCompressed(compressed: Boolean) {
        this.compressed = compressed
    }

    fun getWidth(): Int {
        return this.width
    }

    fun setWidth(width: Int) {
        this.width = width
    }

    fun getHeight(): Int {
        return this.height
    }

    fun setHeight(height: Int) {
        this.height = height
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(this.path)
        dest.writeString(this.compressPath)
        dest.writeString(this.cutPath)
        dest.writeLong(this.duration)
        dest.writeByte((if (this.isChecked) 1 else 0).toByte())
        dest.writeByte((if (this.isCut) 1 else 0).toByte())
        dest.writeInt(this.position)
        dest.writeInt(this.num)
        dest.writeInt(this.mimeType)
        dest.writeString(this.pictureType)
        dest.writeByte((if (this.compressed) 1 else 0).toByte())
        dest.writeInt(this.width)
        dest.writeInt(this.height)
    }

    constructor(`in`: Parcel):super() {
        this.path = `in`.readString()
        this.compressPath = `in`.readString()
        this.cutPath = `in`.readString()
        this.duration = `in`.readLong()
        this.isChecked = `in`.readByte().toInt() != 0
        this.isCut = `in`.readByte().toInt() != 0
        this.position = `in`.readInt()
        this.num = `in`.readInt()
        this.mimeType = `in`.readInt()
        this.pictureType = `in`.readString()
        this.compressed = `in`.readByte().toInt() != 0
        this.width = `in`.readInt()
        this.height = `in`.readInt()
    }
}