package com.kevin.cermatitest.services

import com.kevin.cermatitest.MyApplication
import com.kevin.cermatitest.MyApplication.Companion.getContext
import com.kevin.cermatitest.utils.AppConstants
import com.readystatesoftware.chuck.ChuckInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object APIClient {
    val client: Retrofit
        get() {
            val retrofit: Retrofit

            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY

            var client: OkHttpClient? = null


            client = OkHttpClient.Builder()
                .connectTimeout(AppConstants.TIME_OUT_LIMIT.toLong(), TimeUnit.SECONDS)
                .writeTimeout(AppConstants.TIME_OUT_LIMIT.toLong(), TimeUnit.SECONDS)
                .readTimeout(AppConstants.TIME_OUT_LIMIT.toLong(), TimeUnit.SECONDS)
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .hostnameVerifier { hostname, session -> true }
                .addInterceptor(ChuckInterceptor(getContext()))
                .addInterceptor(ConnectivityInterceptor(getContext()!!))
                .build()

            retrofit = Retrofit.Builder()
                .baseUrl(AppConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build()

            return retrofit
        }
}