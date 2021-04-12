package com.example.gamenews.ui

import android.graphics.Color
import android.os.Bundle
import android.text.method.LinkMovementMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import com.example.gamenews.databinding.TopNewsFragmentBinding
import com.example.gamenews.model.News
import java.net.URI

private const val NEWS = "news"

class TopNewsFragment : Fragment() {
    private var _binding: TopNewsFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var news: News

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            news = it.getSerializable(NEWS) as News
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         _binding = TopNewsFragmentBinding.inflate(inflater,container,false)
        setContent()
        return binding.root
    }


    private fun setContent(){
        binding.newsText.text = news.title
        binding.newsSource.text = HtmlCompat.fromHtml(
            "<a href =\"${news.click_url}\">${URI(news.click_url).host}</a>",
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
        binding.newsSource.movementMethod = LinkMovementMethod.getInstance();
        binding.timeAgo.text = "- ${news.time}"
        if (!news.img.isNullOrEmpty()) {
            binding.newsText.setTextColor(Color.WHITE)
            binding.timeAgo.setTextColor(Color.LTGRAY)
            Glide.with(requireContext())
                .load(news.img)
                .into(binding.newsImage)
        }else{
            binding.newsText.setTextColor(Color.BLACK)
            binding.newsImage.visibility = View.GONE
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(news : News) =
            TopNewsFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(NEWS, news)
                }
            }
    }
}