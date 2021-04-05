package com.televantou.everylifetasks.api

import androidx.databinding.library.BuildConfig
import com.televantou.everylifetasks.data.tasks.Task
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

/**
 * Created by Eirini Televantou on 03/04/2021 for EveryLife.
 */

//Retrofit Interface class for the call definitions
interface ELApi {

    @GET("tasks.json")
    suspend fun getTasks(): List<Task>


    //For larger/more complex projects we would put this code block in its own class
    companion object {
        private const val BASE_URL = "https://adam-deleteme.s3.amazonaws.com/"

        //log everything if we are running from Android studio or using a debug version of the app
        private val logger = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        }

        fun create(): ELApi {

            val client = OkHttpClient.Builder()
                    .addInterceptor(logger)
                    .build()

            return Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(ELApi::class.java)
        }
    }
}