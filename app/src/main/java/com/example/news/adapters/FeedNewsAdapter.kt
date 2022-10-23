package com.example.news.adapters

import android.content.res.ColorStateList
import android.graphics.Color
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.domain.model.News
import com.example.news.R
import com.example.news.databinding.NewsItemBinding
import com.example.utils.parseDate
import com.example.utils.toDate
import java.net.URI
import java.util.*


class FeedNewsAdapter(private val listener: (News) -> Unit) :
    RecyclerView.Adapter<FeedNewsAdapter.NewsHolder>(), Filterable {

    private var newsList: MutableList<News> = mutableListOf()

    private val newsFilterList: MutableList<News>
        get() = mDiffer.currentList

    private val mDiffer: AsyncListDiffer<News> =
        AsyncListDiffer(this, NewsDiffCallback())

    fun submitData(list: List<News>) {
        newsList = list.sortedByDescending { it.publishedAt }.toMutableList()
        mDiffer.submitList(newsList)
    }

    fun submitFiltering(list: List<News>) {
        mDiffer.submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsHolder {
        val binding = NewsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return NewsHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsHolder, position: Int) {
        with(newsFilterList[position]) {
            holder.binding.newsText.text = title
            holder.binding.newsSource.text = HtmlCompat.fromHtml(
                "<a href =\"${url}\">${URI(url).host}</a>",
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
            holder.binding.favorite.isActivated = isFavorite
            holder.binding.favorite.backgroundTintList = ColorStateList.valueOf(
                holder.binding.root.resources.getColor(
                    if (isFavorite) R.color.favorite else R.color.white,
                    null
                )
            )
            holder.binding.favorite.setOnClickListener {
                listener(this)
                holder.binding.favorite.isActivated = isFavorite
                holder.binding.favorite.backgroundTintList = ColorStateList.valueOf(
                    holder.binding.root.resources.getColor(
                        if (isFavorite) R.color.favorite else R.color.white,
                        null
                    )
                )
            }

            holder.binding.newsSource.movementMethod = LinkMovementMethod.getInstance()
            if (publishedAt != 0L) {
                holder.binding.timeAgo.text = Date(publishedAt).parseDate("dd.MM HH:mm")
            }
            if (!urlToImage.isNullOrEmpty()) {
                Glide.with(holder.binding.root.context)
                    .load(urlToImage)
                    .into(holder.binding.newsImage)
            } else {
                holder.binding.newsText.setTextColor(Color.BLACK)
                holder.binding.newsImage.visibility = View.GONE
            }
        }
    }

    override fun getItemCount() = newsFilterList.size

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()

                val filterResults = FilterResults()
                filterResults.values = if (charSearch.isEmpty()) {
                    newsList
                } else {
                    val resultList = mutableListOf<News>()
                    for (row in newsList) {
                        if (row.title.lowercase()
                                .contains(constraint.toString().lowercase())
                        ) {
                            resultList.add(row)
                        }
                    }
                    resultList
                }
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                @Suppress("UNCHECKED_CAST")
                submitFiltering(results?.values as? MutableList<News> ?: emptyList())
            }
        }
    }

    class NewsDiffCallback : DiffUtil.ItemCallback<News>() {

        override fun areItemsTheSame(old: News, new: News) =
            old.title == new.title

        override fun areContentsTheSame(old: News, new: News): Boolean {
            return old == new
        }

    }

    inner class NewsHolder(val binding: NewsItemBinding) : RecyclerView.ViewHolder(binding.root)
}