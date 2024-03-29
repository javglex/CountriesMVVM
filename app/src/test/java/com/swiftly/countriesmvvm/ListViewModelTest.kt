package com.swiftly.countriesmvvm

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.swiftly.countriesmvvm.model.CountriesService
import com.swiftly.countriesmvvm.model.Country
import com.swiftly.countriesmvvm.viewmodel.ListViewModel
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit

class ListViewModelTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @Mock
    lateinit var countriesService: CountriesService

    @InjectMocks
    var listViewModel = ListViewModel()

    private var testSingle: Single<List<Country>>? = null

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun getCountriesSuccess() {
        val country = Country("countryName", "capital", "url")
        val countriesList = arrayListOf(country)

        testSingle = Single.just(countriesList)

        Mockito.`when`(countriesService.getCountries()).thenReturn(testSingle)

        listViewModel.refresh()

        Assert.assertEquals(1, listViewModel.countries.value?.size)
        Assert.assertEquals(false, listViewModel.countryLoadError.value)
        Assert.assertEquals(false, listViewModel.loading.value)
    }

    @Test
    fun getCountriesError() {

        testSingle = Single.error(Throwable())

        Mockito.`when`(countriesService.getCountries()).thenReturn(testSingle)

        listViewModel.refresh()

        Assert.assertEquals(null, listViewModel.countries.value)
        Assert.assertEquals(true, listViewModel.countryLoadError.value)
        Assert.assertEquals(false, listViewModel.loading.value)
    }

    @Before
    fun setUpRxSchedulers() {
        val immediate = object: Scheduler() { //whenever a observable is called we need to return immediately.
            override fun createWorker(): Worker {
                return ExecutorScheduler.ExecutorWorker(Executor {
                    it.run()
                })
            }

            override fun scheduleDirect(run: Runnable?, delay: Long, unit: TimeUnit?): Disposable {
                return super.scheduleDirect(run, 0, unit)
            }
        }

        //we use the immediate for all schedulers java/android is using
        RxJavaPlugins.setInitIoSchedulerHandler { scheduler -> immediate }
        RxJavaPlugins.setInitComputationSchedulerHandler { scheduler -> immediate}
        RxJavaPlugins.setInitNewThreadSchedulerHandler { scheduler -> immediate }
        RxJavaPlugins.setInitSingleSchedulerHandler { scheduler -> immediate }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { scheduler -> immediate }
    }

}