package com.jquery.service.android.service

import android.app.IntentService
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.support.v4.app.NotificationCompat
import android.support.v4.content.FileProvider
import com.jquery.service.android.BuildConfig
import com.jquery.service.android.R
import com.jquery.service.android.entity.DownloadEntity
import com.jquery.service.android.listener.DownloadProgressListener
import com.jquery.service.android.utils.StringUtils
import java.io.File

/**
 * 下载Services
 * @author J.query
 * @date 2019/5/30
 * @email j-query@foxmail.com
 */
class DownloadServices : IntentService {

    private val TAG = "DownloadServices"

    private var notificationBuilder: NotificationCompat.Builder? = null
    private var notificationManager: NotificationManager? = null

    internal var downloadCount = 0

    private val apkUrl = "http://download.fir.im/v2/app/install/595c5959959d6901ca0004ac?download_token=1a9dfa8f248b6e45ea46bc5ed96a0a9e&source=update"

    constructor() : super("DownloadServices")

    private var outputFile: File? = null

    override fun onHandleIntent(intent: Intent?) {
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationBuilder = NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.video_icon)
                .setContentTitle("DownloadEntity")
                .setContentText("Downloading File")
                .setAutoCancel(true)

        notificationManager!!.notify(0, notificationBuilder!!.build())

        download()
    }

    private fun download() {
        val listener = object : DownloadProgressListener {
            override fun update(bytesRead: Long, contentLength: Long, done: Boolean) {
                //不频繁发送通知，防止通知栏下拉卡顿
                val progress = (bytesRead * 100 / contentLength).toInt()
                if (downloadCount == 0 || progress > downloadCount) {
                    val download = DownloadEntity()
                    download.setTotalFileSize(contentLength)
                    download.setCurrentFileSize(bytesRead)
                    download.setProgress(progress)

                    sendNotification(download)
                }
            }
        }
        outputFile = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "file.apk")

        if (outputFile!!.exists()) {
            outputFile!!.delete()
        }


        val baseUrl = StringUtils.getHostName(apkUrl)
        //RetrofitUtils?.downloadAPK(apkUrl, outputFile, object : Subscriber()){}
        /* RetrofitUtils(baseUrl, listener).downloadAPK(apkUrl, outputFile, object : Subscriber() {
             fun onCompleted() {
                 downloadCompleted()
             }

             fun onError(e: Throwable) {
                 e.printStackTrace()
                 downloadCompleted()
                 Log.e(TAG, "onError: " + e.message)
             }

             fun onNext(o: Any) {

             }
         })*/
    }

    private fun downloadCompleted() {
        val download = DownloadEntity()
        download.setProgress(100)
        sendIntent(download)

        notificationManager!!.cancel(0)
        notificationBuilder!!.setProgress(0, 0, false)
        notificationBuilder!!.setContentText("File Downloaded")
        notificationManager!!.notify(0, notificationBuilder!!.build())
        installNormal(this, outputFile)
        //安装apk
    }


    /**
     * 提示安装
     * @param context 上下文
     * @param apkPath apk下载完成在手机中的路径
     */
    private fun installNormal(context: Context, apkPath: File?) {
        val intent = Intent(Intent.ACTION_VIEW)
        //版本在7.0以上是不能直接通过uri访问的
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            // File file = (new File(apkPath));
            // 由于没有在Activity环境下启动Activity,设置下面的标签
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            //参数1:上下文, 参数2:Provider主机地址 和配置文件中保持一致,参数3:共享的文件
            val apkUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileprovider", apkPath!!)
            // Uri apkUri = FileProvider.getUriForFile(context, "${applicationId}.fileprovider", apkPath);
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive")
        } else {
            intent.setDataAndType(Uri.fromFile(apkPath),
                    "application/vnd.android.package-archive")
        }
        context.startActivity(intent)
    }

    private fun sendNotification(download: DownloadEntity) {

        sendIntent(download)
        notificationBuilder!!.setProgress(100, download.getProgress(), false)
        notificationBuilder!!.setContentText(
                StringUtils.getDataSize(download.getCurrentFileSize()) + "/" +
                        StringUtils.getDataSize(download.getTotalFileSize()))
        notificationManager!!.notify(0, notificationBuilder!!.build())
    }

    private fun sendIntent(download: DownloadEntity) {

        // val intent = Intent(SplashActivity.MESSAGE_PROGRESS)
        // intent.putExtra("download", download)
        // LocalBroadcastManager.getInstance(this@DownloadServices).sendBroadcast(intent)
    }

    override fun onTaskRemoved(rootIntent: Intent) {
        notificationManager!!.cancel(0)
    }
}