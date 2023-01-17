package com.example.flow.di.module

import com.example.flow.network.NetworkResponseAdapterFactory
import com.example.flow.network.api.FlowApi
import dagger.Module
import dagger.Provides
import dagger.Reusable
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named


@Module
class ApiModule {
    @Provides
    @Named("ov1")
    fun provideOkhttpClientV0(): OkHttpClient{
        val client = OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
        return client.build()
    }

    @Provides
    @Reusable
    @Named("v1")
    fun provideRetrofitV1(
        @Named("ov1") okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://openapi.naver.com/v1/search/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(NetworkResponseAdapterFactory())
            .build()
    }

    @Provides
    @Reusable
    fun provideFlowApi(@Named("v1") retrofit: Retrofit): FlowApi{
        return retrofit.create(FlowApi::class.java)
    }

}