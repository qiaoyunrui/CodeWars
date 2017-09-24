package me.juhezi.module.base.network

import me.juhezi.module.base.extensions.isEmpty
import me.juhezi.module.base.functions.getIMEI
import me.juhezi.module.base.functions.getVersionName
import me.juhezi.module.base.functions.isDebug
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by Juhezi[juhezix@163.com] on 2017/9/24.
 */
class ApiModule private constructor() {
    companion object {
        fun getInstacne() = Holder.sInstacne
    }

    object Holder {
        val sInstacne = ApiModule()
    }

    private var baseUrl: String = ""
        set(value) {
            if (!isEmpty(value)) {
                field = value
            } else {
                throw Exception("The baseUrl can not be empty!")
            }
        }
    private var mApiClient: OkHttpClient? = null
    private var mRetrofit: Retrofit? = null

    fun provideApiClient(client: OkHttpClient,
                         vararg interceptors: Interceptor): OkHttpClient {
        if (mApiClient == null) {
            mApiClient = buildClient(client, *interceptors)
        }
        return mApiClient!!
    }

    private fun buildClient(client: OkHttpClient,
                            vararg interceptors: Interceptor): OkHttpClient {
        val builder = client.newBuilder()
        with(builder) {
            readTimeout(5, TimeUnit.SECONDS)
            writeTimeout(5, TimeUnit.SECONDS)
            addInterceptor {
                var request = it.request()
                var builder = request.url().newBuilder()
                builder.addEncodedQueryParameter("imei", getIMEI())
                        .addEncodedQueryParameter("os_type", "Android")
                        .addEncodedQueryParameter("os_version", android.os.Build.VERSION.RELEASE.trim { it <= ' ' })
                        .addEncodedQueryParameter("version", getVersionName())
                var url = builder.build()
                //Add cookie if need here
                request = request.newBuilder()
                        .addHeader("Referer", "http://api.maxjia.com/")
                        .addHeader("User-Agent", "Mozilla/5.0 AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.118 Safari/537.36 ApiMaxJia/1.0")
                        .url(url)
                        .build()
                it.proceed(request)
            }
            if (isDebug()) {
                addInterceptor(HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BODY))
            }
            for (interceptor in interceptors) {
                addInterceptor(interceptor)
            }
        }
        return builder.build()
    }

    fun provideRetrofit(baseUrl: String, client: OkHttpClient): Retrofit {
        if (mRetrofit == null) {
            mRetrofit = Retrofit.Builder()
                    .client(client)
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
        }
        return mRetrofit!!
    }

    fun provideService(retrofit: Retrofit) =
            retrofit.create(BaseService::class.java)

}
