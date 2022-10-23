package com.example.news.adapters

import android.graphics.Color
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.domain.model.News
import com.example.news.databinding.TopNewsFragmentBinding
import com.example.utils.parseDate
import com.example.utils.toDate
import java.net.URI
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class TopNewsAdapter : ListAdapter<News, TopNewsAdapter.ViewHolder>(COMPARATOR) {

    class ViewHolder(private val binding: TopNewsFragmentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(news: News) {
            binding.newsText.text = news.title
            binding.newsSource.text = HtmlCompat.fromHtml(
                "<a href =\"${news.url}\">${URI(news.url).host}</a>",
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
            binding.newsSource.movementMethod = LinkMovementMethod.getInstance()
            if (news.publishedAt != 0L) {
                binding.timeAgo.text = Date(news.publishedAt).parseDate("dd.MM HH:mm")
            }
            if (!news.urlToImage.isNullOrEmpty()) {
                binding.newsText.setTextColor(Color.WHITE)
                binding.timeAgo.setTextColor(Color.LTGRAY)
                Glide.with(binding.guideline.context)
                    .load(news.urlToImage)
                    .into(binding.newsImage)
            } else {
                binding.newsText.setTextColor(Color.WHITE)
                binding.newsImage.visibility = View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            TopNewsFragmentBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<News>() {
            override fun areItemsTheSame(old: News, new: News) =
                old.title == new.title

            override fun areContentsTheSame(old: News, new: News): Boolean {
                return old == new
            }
        }
    }
}