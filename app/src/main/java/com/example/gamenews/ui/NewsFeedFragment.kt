package com.example.gamenews.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gamenews.adapters.FeedNewsAdapter
import com.example.gamenews.databinding.FragmentNewsFeedBinding
import com.example.gamenews.viewmodel.MainViewModel
import androidx.fragment.app.activityViewModels

private const val TYPE = "type"

class NewsFeedFragment : Fragment() {
    private var type: String? = null

    private var _binding: FragmentNewsFeedBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var feedNewsAdapter: FeedNewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            type = it.getString(TYPE)
        }
        observeList()
        viewModel.searchText.observe(this,{
            feedNewsAdapter.filter.filter(viewModel.searchText.value)
        })
    }

    private fun observeList(){
        when (type){
            "STORIES" -> {
                viewModel.stories.observe(this,{
                    feedNewsAdapter.notifyDataSetChanged()
                })
            }
            "VIDEOS" -> {
                viewModel.videos.observe(this,{
                    feedNewsAdapter.notifyDataSetChanged()
                })
            }
            "FAVORITES" -> {
                viewModel.favorites.observe(this,{
                    feedNewsAdapter.notifyDataSetChanged()
                })
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewsFeedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpNewsFeed()
    }


    private fun setUpNewsFeed() {
        when (type){
            "STORIES" -> {
                feedNewsAdapter = FeedNewsAdapter(
                    requireContext(), viewModel.stories.value
                )
            }
            "VIDEOS" -> {
                feedNewsAdapter = FeedNewsAdapter(
                    requireContext(), viewModel.videos.value
                )
            }
            "FAVORITES" -> {
                feedNewsAdapter = FeedNewsAdapter(
                    requireContext(), viewModel.favorites.value
                )
            }
        }
        binding.newsFeed.layoutManager = LinearLayoutManager(context)
        binding.newsFeed.adapter = feedNewsAdapter
        feedNewsAdapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        @JvmStatic
        fun newInstance(type: String) =
            NewsFeedFragment().apply {
                arguments = Bundle().apply {
                    putString(TYPE, type)
                }
            }
    }
}