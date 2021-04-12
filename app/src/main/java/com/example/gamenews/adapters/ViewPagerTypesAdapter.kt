package com.example.gamenews.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.gamenews.ui.MainActivity
import com.example.gamenews.ui.NewsFeedFragment

class ViewPagerTypesAdapter(mainActivity: MainActivity) : FragmentStateAdapter(mainActivity) {

    private val tabsName: ArrayList<String> = arrayListOf(
        "STORIES",
        "VIDEOS",
        "FAVORITES"
    )

    fun getTabsName(position: Int): String {
        return tabsName[position]
    }

    override fun getItemCount() = tabsName.size

    override fun createFragment(position: Int): Fragment {
        return NewsFeedFragment.newInstance(tabsName[position])
    }
}