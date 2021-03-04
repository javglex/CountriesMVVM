package com.swiftly.countriesmvvm.model

import io.reactivex.Single
import retrofit2.http.GET

interface CountriesApi {

    @GET("DevTides/countries/master/countriesV2.json")
    fun getCountries(): Single<List<Country>> //single emits one variable and then closes
}