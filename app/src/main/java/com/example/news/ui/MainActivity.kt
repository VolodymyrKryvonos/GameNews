package com.example.news.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.example.news.adapters.ViewPagerTypesAdapter
import com.example.news.adapters.TopNewsAdapter
import com.example.news.databinding.ActivityMainBinding
import com.example.news.viewmodel.MainViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private lateinit var viewPagerTypesAdapter: ViewPagerTypesAdapter
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModel()
    private var topNewsPagerAdapter: TopNewsAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpTopNews()
        setUpSearch()

        setUpViewPager()
        binding.update.setOnRefreshListener {
            updateNews()
        }

        lifecycleScope.launchWhenStarted {
            viewModel.newsUpdated.onEach {
                binding.update.isRefreshing = !it
            }.launchIn(this)
        }

        binding.search.setOnClickListener {
            binding.toolBarLayout.isVisible = false
            binding.searchLayout.isVisible = true
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
                lifecycleScope.launchWhenStarted {
                    viewModel.searchText.emit(s.toString())
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        binding.clear.setOnClickListener {
            binding.searchInput.text.clear()
            lifecycleScope.launchWhenStarted {
                viewModel.searchText.emit("")
            }
        }

        binding.back.setOnClickListener {
            binding.searchInput.text.clear()
            lifecycleScope.launchWhenStarted {
                viewModel.searchText.emit("")
            }
            binding.toolBarLayout.visibility = View.VISIBLE
            binding.searchLayout.visibility = View.GONE
        }

    }

    private fun updateNews() {
        viewModel.updateNews()
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
        topNewsPagerAdapter = TopNewsAdapter()
        binding.topNews.adapter = topNewsPagerAdapter
        lifecycleScope.launchWhenStarted {
            viewModel.topNews.collectLatest {
                topNewsPagerAdapter?.submitList(it)
                if (it.isEmpty()) {
                    binding.topNews.visibility = View.GONE
                    binding.dotTabs.visibility = View.GONE
                } else {
                    binding.topNews.visibility = View.VISIBLE
                    binding.dotTabs.visibility = View.VISIBLE
                }

                TabLayoutMediator(binding.dotTabs, binding.topNews) { tab: TabLayout.Tab, pos: Int ->
                    tab.view.isClickable = false
                }.attach()
            }
        }
    }
    private fun setUpViewPager() {
        viewPagerTypesAdapter = ViewPagerTypesAdapter(this)
        binding.viewPager.adapter = viewPagerTypesAdapter
        TabLayoutMediator(binding.navigationTabs, binding.viewPager) { tab, position ->
            tab.text = viewPagerTypesAdapter.getTabsName(position)
        }.attach()
    }


}