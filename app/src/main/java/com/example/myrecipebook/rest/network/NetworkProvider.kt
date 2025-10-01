package com.example.myrecipebook.rest.network

import com.example.myrecipebook.BuildConfig
import com.example.myrecipebook.rest.api.RecipesRequest
import com.example.myrecipebook.rest.datasource.RecipesDataSource
import com.example.myrecipebook.rest.datasource.RecipesDataSourceImp
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit


object NetworkProvider {

    private val loggingInterceptor: HttpLoggingInterceptor by lazy {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
    }

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .build()
    }

    private val json: Json by lazy {
        Json {
            ignoreUnknownKeys = true
        }
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .addCallAdapterFactory(
                RxJava3CallAdapterFactory.createWithScheduler(Schedulers.io())
            )
            .build()
    }

    val recipesRequest: RecipesRequest by lazy {
        retrofit.create(RecipesRequest::class.java)
    }

    val recipesDataSource: RecipesDataSource by lazy {
        RecipesDataSourceImp(recipesRequest)
    }
}
