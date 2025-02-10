package com.example.scheduleapp.retrofit

import com.example.scheduleapp.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import java.util.concurrent.TimeUnit.MILLISECONDS

@Suppress("DEPRECATION")
class ScheduleServiceInstance {
    companion object {
        fun <T> createService(serviceClass: Class<T>, timeOutPeriod: Long = 10000L): T {
            val serverLink = BuildConfig.API_LINK

            val retrofit: Retrofit =
                Retrofit.Builder()
                    .baseUrl(serverLink)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addConverterFactory(SimpleXmlConverterFactory.create())
                    .client(
                        OkHttpClient().newBuilder().addInterceptor(
                            HttpLoggingInterceptor().setLevel(
                                HttpLoggingInterceptor.Level.BASIC
                            )
                        )
                            .connectTimeout(timeOutPeriod, MILLISECONDS)
                            .readTimeout(timeOutPeriod, MILLISECONDS)
                            .writeTimeout(timeOutPeriod, MILLISECONDS).build()
                    )
                    .build()

            return retrofit.create(serviceClass)
        }
    }
}