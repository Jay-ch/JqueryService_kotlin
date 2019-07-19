package com.jquery.service.android.utils

import android.util.Log
import java.io.*

/**
 * @author J.query
 * @date 2019/3/11
 * @email j-query@foxmail.com
 */
class SerialPort {
    private val TAG = "SerialPort"

    /*
	 * Do not remove or rename the field mFd: it is used by native method close();
	 */
    private var mFd: FileDescriptor? = null
    private lateinit var mFileInputStream: FileInputStream
    private lateinit var mFileOutputStream: FileOutputStream

    @Throws(SecurityException::class, IOException::class)
    constructor(device: File, baudrate: Int, flags: Int) {

        /* Check access permission */
        if (!device.canRead() || !device.canWrite()) {
            try {
                /* Missing read/write permission, trying to chmod the file */
                val su: Process
                su = Runtime.getRuntime().exec("/system/bin/su")
                val cmd = ("chmod 666 " + device.absolutePath + "\n"
                        + "exit\n")
                su.outputStream.write(cmd.toByteArray())
                if (su.waitFor() != 0 || !device.canRead()
                        || !device.canWrite()) {
                    throw SecurityException()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                throw SecurityException()
            }

        }

        mFd = open(device.absolutePath, baudrate, flags)
        if (mFd == null) {
            Log.e(TAG, "native open returns null")
            throw IOException()
        }
        mFileInputStream = FileInputStream(mFd!!)
        mFileOutputStream = FileOutputStream(mFd!!)
    }

    // Getters and setters
    fun getInputStream(): InputStream {
        return mFileInputStream
    }

    fun getOutputStream(): OutputStream {
        return mFileOutputStream
    }

    // JNI
    private external fun open(path: String, baudrate: Int, flags: Int): FileDescriptor

    external fun close()
  /*  static {
        System.loadLibrary("serial_port");
    }*/
}