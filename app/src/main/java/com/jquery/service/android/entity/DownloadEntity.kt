package com.jquery.service.android.entity

import android.os.Parcel
import android.os.Parcelable

/**
 * @author J.query
 * @date 2019/5/30
 * @email j-query@foxmail.com
 */
class DownloadEntity: Parcelable {

    private var progress: Int = 0
    private var currentFileSize: Long = 0
    private var totalFileSize: Long = 0

    fun getProgress(): Int {
        return progress
    }

    fun setProgress(progress: Int) {
        this.progress = progress
    }

    fun getCurrentFileSize(): Long {
        return currentFileSize
    }

    fun setCurrentFileSize(currentFileSize: Long) {
        this.currentFileSize = currentFileSize
    }

    fun getTotalFileSize(): Long {
        return totalFileSize
    }

    fun setTotalFileSize(totalFileSize: Long) {
        this.totalFileSize = totalFileSize
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(this.progress)
        dest.writeLong(this.currentFileSize)
        dest.writeLong(this.totalFileSize)
    }

    constructor(): super() {}

    constructor(`in`: Parcel):  super()  {
        this.progress = `in`.readInt()
        this.currentFileSize = `in`.readLong()
        this.totalFileSize = `in`.readLong()
    }

    val CREATOR: Parcelable.Creator<DownloadEntity> = object : Parcelable.Creator<DownloadEntity> {
        override fun createFromParcel(source: Parcel): DownloadEntity {
            return DownloadEntity(source)
        }

        override fun newArray(size: Int): Array<DownloadEntity?> {
            return arrayOfNulls(size)
        }
    }
}