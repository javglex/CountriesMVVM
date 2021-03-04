package com.swiftly.countriesmvvm.di

import com.swiftly.countriesmvvm.model.CountriesService
import com.swiftly.countriesmvvm.viewmodel.ListViewModel
import dagger.Component

@Component(modules = [ApiModule::class])
interface ApiComponent {

    fun inject(service: CountriesService)

    fun inject(viewModel: ListViewModel)

}