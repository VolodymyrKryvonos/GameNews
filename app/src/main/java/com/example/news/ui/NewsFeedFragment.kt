package com.example.news.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.news.adapters.FeedNewsAdapter
import com.example.news.databinding.FragmentNewsFeedBinding
import com.example.news.viewmodel.MainViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

private const val TYPE = "type"

class NewsFeedFragment : Fragment() {
    private var type: String? = null

    private var _binding: FragmentNewsFeedBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by sharedViewModel()
    private var feedNewsAdapter: FeedNewsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            type = it.getString(TYPE)
        }
        observeList()
        lifecycleScope.launchWhenStarted {
            viewModel.searchText.collectLatest {
                feedNewsAdapter?.filter?.filter(it)
            }
        }
    }

    private fun observeList() {
        lifecycleScope.launchWhenStarted {
            if (type == "NEWS") {
                viewModel.getNews().collectLatest {
                    feedNewsAdapter?.submitData(ArrayList(it))
                }
            } else {
                viewModel.getFavorites()
                    .collectLatest { feedNewsAdapter?.submitData(it) }
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
        feedNewsAdapter = FeedNewsAdapter {
            if(it.isFavorite){
                viewModel.removeFavoriteNews(it)
            }else{
                viewModel.addFavoriteNews(it)
            }
        }
        binding.newsFeed.layoutManager = LinearLayoutManager(context)
        binding.newsFeed.adapter = feedNewsAdapter
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