package com.example.harrypotter.di

import com.example.harrypotter.data.source.remote.HarryPotterApiService
import com.example.harrypotter.util.Constants
import com.example.harrypotter.util.DefaultCoroutineDispatchers
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    @Provides
    @Singleton
    fun provideDefaultCoroutineDispatchers() = DefaultCoroutineDispatchers()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


    @Provides
    @Singleton
    fun provideCoinApi(retrofit: Retrofit): HarryPotterApiService {
        return retrofit.create(HarryPotterApiService::class.java)
    }
}