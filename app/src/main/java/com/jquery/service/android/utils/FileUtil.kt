package com.jquery.service.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.support.v4.content.FileProvider
import android.util.Log
import com.jquery.service.android.BuildConfig
import java.io.*


/**
 * @author J.query
 * @date 2019/1/21
 * @email j-query@foxmail.com
 */
object FileUtil {

    val TAG = "FileUtil"

    private val SEPARATOR = File.separator//路径分隔符

//    private val filePath = android.os.Environment.getExternalStorageDirectory().toString() + SEPARATOR + "QwKrom"
    /**
     * 文件夹根目录（存放App）
     */
    val filePath = Environment.getExternalStorageDirectory().absolutePath + SEPARATOR + "Jquery"

    /**
     * 不显示根目录的文件路径（用于界面显示）
     */
    val showPath = "Jquery"+ SEPARATOR + "Log"

    val logPath = filePath + SEPARATOR + "Log"

    private var txt = ".txt"
    private var underline = "_"


    /**
     * 获取Asset目录下的升级文件有效数据集合
     */
    fun readAssetTxt(context: Context): ArrayList<String> {
        val fileList = ArrayList<String>()
        val resultList = ArrayList<String>()
        try {
            val isr = InputStreamReader(context.resources.assets.open("APP0004.hex"))
            val br = BufferedReader(isr)
            //数据行
            var lineTxt: String
            var reds: String
            var trim: String
            var count = 0//初始化 key值
            var tempString = ""
            while (true) {
                lineTxt = br.readLine() ?: break
                fileList.add(lineTxt)

                trim = lineTxt.trim()
                if (trim.substring(7, 9) == "00") {
                    reds = trim.substring(9, trim.length - 2)
                    count++
                    Log.e(TAG, "count= $count  reds= $reds")
                    tempString += reds
                    if (count % 12 == 0) {
                        resultList.add(tempString)
                        tempString = ""
                    }
                } else {
                    Log.e(TAG, "  非有效数据 = " + lineTxt)
                }
            }
            if (count % 12 != 0) { //判断count为12倍数的情况下不处理tempString
                resultList.add(tempString)
            }

            isr.close()
            br.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        resultList.forEachIndexed { index, result ->
            Log.e(TAG, "  index = $index  result = $result")
        }
        return resultList
    }


    /**
     * 根据文件大小读取行数代码
     * @return
     */
    fun readFileLineToList(filePath: String): ArrayList<String> {

        //将读出来的一行行数据使用List存储
//        val filePath = filePath + SEPARATOR + "APP0003.hex"
        val fileList = ArrayList<String>()
        val resultList = ArrayList<String>()

        try {
            val file = File(filePath)
            if (file.exists() && file.isFile) { //文件存在
                val isr = InputStreamReader(FileInputStream(file))
                val br = BufferedReader(isr)
                //数据行
                var lineTxt: String
                var reds: String
                var trim: String
                var count = 0//初始化 key值
                var tempString = ""
                while (true) {
                    lineTxt = br.readLine() ?: break
                    fileList.add(lineTxt)

                    trim = lineTxt.trim()
                    if (trim.substring(7, 9) == "00") {
                        reds = trim.substring(9, trim.length - 2)
                        count++
//                    Log.e(TAG, "count= "+count+"  reds= " + reds)
                        tempString += reds
                        if (count % 12 == 0) {
                            resultList.add(tempString)
                            tempString = ""
                        }
                    } else {
//                    Log.e(TAG, "  非有效数据 = " + lineTxt)
                    }
                }
                if (count % 12 != 0) { //判断count为12倍数的情况下不处理tempString
                    resultList.add(tempString)
                }
                isr.close()
                br.close()
            } else {

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return resultList
    }


    fun writeFromAssetsToSDCard(context: Context, assetsFileName: String) {
        val inputStream: InputStream
        try {
            inputStream = context.resources.assets.open(assetsFileName)
            val file = File(FileUtil.filePath)
            if (!file.exists()) {
                file.mkdirs()
            }
            val fileOutputStream = FileOutputStream(filePath + SEPARATOR + assetsFileName)
            val buffer = ByteArray(512)
            var count: Int

            while (true) {
                count = inputStream.read(buffer)
                // 将Buffer中的数据写到outputStream对象中
                if (count <= 0) {
                    break
                }
                fileOutputStream.write(buffer, 0, count)
            }
            fileOutputStream.flush()
            fileOutputStream.close()
            inputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    /**
     * content   内容
     *
     * deviceNumber   设备编号
     *
     */
    fun writeToTXT(content: String,path:String): Boolean {

        val o: FileOutputStream?



        val buff: ByteArray
        try {
            val file = File(path)
            if (!file.exists()) {
                file.createNewFile()
            }
            buff = content.toByteArray()
            o = FileOutputStream(file, true) //false 为覆盖原内容
            o.write(buff)
            o.write("\r\n".toByteArray())//写入换行
            o.flush()
            o.close()
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
        return true
    }

    fun getLogTextPath(deviceNumber: String,name: String): String {

        val filename = name  + underline + deviceNumber + txt
        return logPath + SEPARATOR + filename
    }


    fun openAssignFolder(context: Context,path: String) {
        Log.e(FileUtil.TAG, "path= $path")
        val file = File(path )
        if ( !file.exists()) {
            return
        }

        val intent =Intent(Intent.ACTION_VIEW)
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            val contentUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileProvider"
                    , file)
            //com.qwkrom.service.android.fileProvider
            intent.setDataAndType(contentUri, "text/plain")
        } else {
            intent.setDataAndType(Uri.fromFile(file), "text/plain")
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)

    }


}
