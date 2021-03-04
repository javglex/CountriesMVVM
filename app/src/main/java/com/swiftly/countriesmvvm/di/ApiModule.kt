package com.swiftly.countriesmvvm.di

import com.swiftly.countriesmvvm.model.CountriesApi
import com.swiftly.countriesmvvm.model.CountriesService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
class ApiModule {
    private val BASE_URL = "https://raw.githubusercontent.com"

    @Provides
    fun provideCountriesApi(): CountriesApi {
        return Retrofit.Builder() //create fw for retrofit
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())  //someone can subscribe to and get notifications from
            .build()
            .create(CountriesApi::class.java)
    }

    @Provides
    fun provideCountriesService(): CountriesService {
        return CountriesService()
    }

}