package com.jquery.service.android.app

import android.app.Activity
import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
import cat.ereza.customactivityoncrash.activity.DefaultErrorActivity
import com.facebook.cache.disk.DiskCacheConfig
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory
import com.pgyersdk.Pgyer
import com.pgyersdk.PgyerActivityManager
import com.pgyersdk.crash.PgyCrashManager
import com.pgyersdk.crash.PgyerCrashObservable
import com.pgyersdk.crash.PgyerObserver
import com.jquery.service.android.BuildConfig
import com.jquery.service.android.constant.Constants
import com.jquery.service.android.constant.UrlConstant
import com.jquery.service.android.dbutils.PrefStore
import com.jquery.service.android.retrofit.HeaderInterceptor
import com.jquery.service.android.retrofit.RetrofitHelper
import com.umeng.analytics.MobclickAgent
import com.umeng.commonsdk.UMConfigure
import me.jessyan.autosize.AutoSize
import me.jessyan.autosize.AutoSizeConfig
import me.jessyan.autosize.external.ExternalAdaptInfo
import me.jessyan.autosize.onAdaptListener
import me.jessyan.autosize.utils.LogUtils
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.*
import java.util.concurrent.TimeUnit
import javax.net.ssl.*


/**
 * @author J.query
 * @date 2018/9/10
 * @email j-query@foxmail.com
 */
class App : Application() {

    companion object {
        // 单例不会是null   所以使用notNull委托
        lateinit var mContext: Context
    }

    override fun onCreate() {
        super.onCreate()
        mContext = applicationContext
        //instance=this
        initNetWork()
       // initDownloadNetWork()
        initFresco()
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, "")
        MobclickAgent.enableEncrypt(true)//确保安全性 加密
        reloadNetWork()
        //Constants.UMENG_CHANNEL;
        AutoSize.initCompatMultiProcess(this)
        AutoSizeConfig.getInstance()
                .setCustomFragment(true).onAdaptListener = object : onAdaptListener {
            override fun onAdaptBefore(target: Any, activity: Activity) {
                LogUtils.d(String.format(Locale.ENGLISH, "%s onAdaptBefore!", target.javaClass.name))
            }

            override fun onAdaptAfter(target: Any, activity: Activity) {
                LogUtils.d(String.format(Locale.ENGLISH, "%s onAdaptAfter!", target.javaClass.name))
            }
        }
        customAdaptForExternal()
       // initPgy()
    }

    private fun initPgy() {
        PgyCrashManager.register()
        PgyerCrashObservable.get().attach(object : PgyerObserver {
           override fun receivedCrash(thread: Thread, throwable: Throwable) {

            }
        })
        PgyerActivityManager.set(this)
    }


    private fun customAdaptForExternal() {

        /**
         * {@link ExternalAdaptManager} 是一个管理外部三方库的适配信息和状态的管理类, 详细介绍请看 {@link ExternalAdaptManager} 的类注释
         */
        AutoSizeConfig.getInstance().externalAdaptManager
                .addExternalAdaptInfoOfActivity(DefaultErrorActivity::class.java!!, ExternalAdaptInfo(true, 400f))
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
        Pgyer.setAppId(Constants().PGY_APP_KEY)
    }

   /* private fun initDownloadNetWork() {
        if (getSSLFactory() == null)
            return
        val builder = OkHttpClient().newBuilder()
        if (BuildConfig.DEBUG) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            builder.addInterceptor(interceptor)
        }
        RetrofitUtils.initializeDoanload(UrlConstant().HOST)
    }*/


    /**
     * 初始化网络
     */
    private fun initNetWork() {
        if (getSSLFactory() == null)
            return
        val builder = OkHttpClient().newBuilder()
        if (BuildConfig.DEBUG) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            builder.addInterceptor(interceptor)
        }
        builder.connectTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(HeaderInterceptor(this))
                .sslSocketFactory(getSSLFactory()!!, xtm)
                .hostnameVerifier(DO_NOT_VERIFY)

        val client = builder.build()
        // RetrofitHelper().initialize(client, if (PrefStore().getInstance()?.getCurApiHost().equals("official")) UrlConstant().HOST else UrlConstant().DEV_HOST)
        RetrofitHelper.initialize(client, if (PrefStore().getInstance()?.getCurApiHost().equals("official")) UrlConstant().HOST else UrlConstant().DEV_HOST)
    }

    fun reloadNetWork() {
        if (getSSLFactory() == null)
            return
        val builder = OkHttpClient().newBuilder()
        if (BuildConfig.DEBUG) {
            val interceptor = HttpLoggingInterceptor()
            //val interceptor = HttpLoggingInterceptor( LogInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            builder.addInterceptor(interceptor)
        }
        builder.connectTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(HeaderInterceptor(this))
                .sslSocketFactory(getSSLFactory()!!, xtm)
                .hostnameVerifier(DO_NOT_VERIFY)

        val client = builder.build()
        RetrofitHelper.reload(client, if (PrefStore().getInstance()?.getCurApiHost().equals("official")) UrlConstant().HOST else UrlConstant().DEV_HOST)
    }

    /**
     * init image loader
     */
    private fun initFresco() {
        val localDiskCacheConfig = DiskCacheConfig
                .newBuilder(this)
                .setIndexPopulateAtStartupEnabled(true)
                .build()
        var client: OkHttpClient? = null
        if (getSSLFactory() != null) {
            client = OkHttpClient.Builder()
                    .sslSocketFactory(getSSLFactory()!!, xtm)
                    .hostnameVerifier(DO_NOT_VERIFY)
                    .build()
        }
        if (client != null) {
            val config = OkHttpImagePipelineConfigFactory
                    .newBuilder(this, client)
                    .setDownsampleEnabled(true)
                    .setMainDiskCacheConfig(localDiskCacheConfig)
                    .build()
            Fresco.initialize(this, config)
        } else {
            Fresco.initialize(this)
        }
    }

    private fun getSSLFactory(): SSLSocketFactory? {
        try {
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, arrayOf<TrustManager>(xtm), SecureRandom())
            return sslContext.socketFactory
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    private val DO_NOT_VERIFY = HostnameVerifier { hostname, session -> true }

    private val xtm = object : X509TrustManager {
        override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}

        override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}

        override fun getAcceptedIssuers(): Array<X509Certificate> {
            return arrayOf()
        }
    }
}