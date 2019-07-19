package com.jquery.service.android.retrofit

import com.jquery.service.android.listener.DownloadListener
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.*

/**
 * @author J.query
 * @date 2019/5/29
 * @email j-query@foxmail.com
 */
class RetrofitUtils {
    private val TAG = "RetrofitUtils"
    private val DEFAULT_TIMEOUT = 15
    private val listener: DownloadListener?=null
    private val baseUrl: String?=null
    private val downloadUrl: String? = null
    private val retrofitHelper: RetrofitHelper? = null
   /* constructor(baseUrl: String, listener: DownloadListener): super() {
        this.baseUrl = baseUrl
        this.listener = listener
        val mInterceptor = DownloadInterceptor(listener)
        val httpClient = OkHttpClient.Builder()
                .addInterceptor(mInterceptor)
                .retryOnConnectionFailure(true)
                .connectTimeout(DEFAULT_TIMEOUT.toLong(), TimeUnit.SECONDS)
                .build()

        retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(httpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }*/

    @Volatile
    private var retrofit: Retrofit? = null

    fun <T> createApi(paramClass: Class<T>): T {
        return retrofit?.create(paramClass)!!
    }

    private fun initRetrofit(httpClient: OkHttpClient?, url: String) {
        if (retrofit == null)
            retrofit = Retrofit.Builder()
                    .baseUrl(url)
                    .client(httpClient!!)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
    }

    fun initialize(url: String) {
        initRetrofit(null, url)
    }

    fun initialize(client: OkHttpClient, s: String) {
        initRetrofit(client, s)
    }

    fun reload(client: OkHttpClient, s: String) {
        retrofit = null
        initialize(client, s)
    }

    /**
     * 将输入流写入文件
     * @param inputString
     * @param file
     */
     fun writeFile(inputString: InputStream, file: File) {
        if (file.exists()) {
            file.delete()
        }

        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(file)

            val b = ByteArray(1024)

            var len: Int
          /*  while ((len = inputString.read(b)) != -1L) {
                fos.write(b, 0, len)
            }*/

         /*   do {
              var  currentLine = inputString.read(b)
                if ((len = inputString.read(b)) != -1L) {
                    fos.write(b, 0, len)
                }
            } while (true)*/

            do {

            }while (true)

            inputString.close()
            fos.close()

        } catch (e: FileNotFoundException) {
            listener?.onFailed("FileNotFoundException")
        } catch (e: IOException) {
            listener?.onFailed("IOException")
        }

    }
}