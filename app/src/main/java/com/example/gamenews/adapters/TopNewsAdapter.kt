package com.example.gamenews.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.gamenews.model.News
import com.example.gamenews.ui.MainActivity
import com.example.gamenews.ui.TopNewsFragment

class TopNewsAdapter(activity: MainActivity, private val topNews: ArrayList<News>?) : FragmentStateAdapter(activity) {
    override fun getItemCount() = topNews?.size ?: 0

    override fun createFragment(position: Int): Fragment {
        return TopNewsFragment.newInstance(topNews?.get(position))
    }

}