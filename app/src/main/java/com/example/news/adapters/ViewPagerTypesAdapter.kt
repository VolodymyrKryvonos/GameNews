package com.example.news.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.news.ui.MainActivity
import com.example.news.ui.NewsFeedFragment

class ViewPagerTypesAdapter(mainActivity: MainActivity) : FragmentStateAdapter(mainActivity) {

    private val tabsName: ArrayList<String> = arrayListOf(
        "NEWS",
        "SAVED"
    )

    fun getTabsName(position: Int): String {
        return tabsName[position]
    }

    override fun getItemCount() = tabsName.size

    override fun createFragment(position: Int): Fragment {
        return NewsFeedFragment.newInstance(tabsName[position])
    }
}