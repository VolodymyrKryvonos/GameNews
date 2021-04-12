package com.example.gamenews.ui

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.gamenews.R
import com.example.gamenews.adapters.TopNewsAdapter
import com.example.gamenews.adapters.ViewPagerTypesAdapter
import com.example.gamenews.databinding.ActivityMainBinding
import com.example.gamenews.model.News
import com.example.gamenews.repository.Repository
import com.example.gamenews.viewmodel.MainViewModel
import com.example.gamenews.viewmodel.MainViewModelFactory
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import okhttp3.internal.notify
import okhttp3.internal.wait

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewPagerTypesAdapter: ViewPagerTypesAdapter
    private lateinit var viewModel: MainViewModel
    private lateinit var topNewsPagerAdapter: TopNewsAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val repository = Repository()
        val mainViewModelFactory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(this, mainViewModelFactory).get(MainViewModel::class.java)
        if (!viewModel.isResponseReceived) {
            updateNews()
        }
        setUpViewPager()
        setUpTopNews()
        setUpSearch()

        binding.update.setOnRefreshListener {
            updateNews()
            binding.update.isRefreshing = false
        }

        binding.search.setOnClickListener() {
            binding.toolBarLayout.visibility = View.GONE
            binding.searchLayout.visibility = View.VISIBLE
        }
    }

    private fun setUpSearch() {

        binding.searchInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.isNotEmpty() == true) {
                    binding.clear.visibility = View.VISIBLE
                } else {
                    binding.clear.visibility = View.INVISIBLE
                }
                viewModel.searchText.value = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        binding.clear.setOnClickListener() {
            binding.searchInput.text.clear()
            viewModel.searchText.value = ""
        }

        binding.back.setOnClickListener() {
            binding.searchInput.text.clear()
            viewModel.searchText.value = ""
            binding.toolBarLayout.visibility = View.VISIBLE
            binding.searchLayout.visibility = View.GONE
        }

    }

    private fun updateNews() {
        viewModel.makeRequest()
    }

    private fun setUpTopNews() {
        binding.dotTabs.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {

                binding.topNews.currentItem = tab?.position ?: 0

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })
        topNewsPagerAdapter = TopNewsAdapter(
            this, viewModel.getTopNews().value!!
        )
        binding.topNews.adapter = topNewsPagerAdapter
        viewModel.getTopNews().observe(this, {
            topNewsPagerAdapter.notifyDataSetChanged()
        })

        viewModel.getTopNews().observe(this, {
            topNewsPagerAdapter.notifyDataSetChanged()
            if (topNewsPagerAdapter.itemCount == 0) {
                binding.topNews.visibility = View.GONE
                binding.dotTabs.visibility = View.GONE
            } else {
                binding.topNews.visibility = View.VISIBLE
                binding.dotTabs.visibility = View.VISIBLE
            }
            binding.dotTabs.removeAllTabs()
            repeat(viewModel.getTopNews().value!!.size) {
                binding.dotTabs.addTab(binding.dotTabs.newTab())
            }

            TabLayoutMediator(binding.dotTabs, binding.topNews) { _, _ -> }.attach()
        })


    }

    private fun setUpViewPager() {
        viewPagerTypesAdapter = ViewPagerTypesAdapter(this)
        binding.viewPager.adapter = viewPagerTypesAdapter
        TabLayoutMediator(binding.navigationTabs, binding.viewPager) { tab, position ->
            tab.text = viewPagerTypesAdapter.getTabsName(position)
        }.attach()
    }

}