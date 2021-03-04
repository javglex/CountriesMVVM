package com.swiftly.countriesmvvm.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.swiftly.countriesmvvm.R
import com.swiftly.countriesmvvm.viewmodel.ListViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: ListViewModel
    private val countriesAdapter = CountryListAdapter(arrayListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this).get(ListViewModel::class.java)
        viewModel.refresh()

        rv_countries.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = countriesAdapter
        }

        swipe_view.setOnRefreshListener {
            swipe_view.isRefreshing = false
            viewModel.refresh()
        }

        observeViewModel()
    }

    private fun observeViewModel(){
        viewModel.countries.observe(this, Observer { countries ->

            countriesAdapter.updateCountries(countries)
        })

        viewModel.countryLoadError.observe(this, Observer { isError ->
            isError?.let{ b ->
                tv_error.visibility = if (b) View.VISIBLE else View.GONE
            }
        })

        viewModel.loading.observe(this, Observer{ isLoading ->
            isLoading?.let {
                progress_list_loading.visibility = if(it) View.VISIBLE else View.GONE
                if (it) {
                    tv_error.visibility = View.GONE
                }
            }
        })
    }
}