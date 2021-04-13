package com.example.gamenews.adapters

import android.content.Context
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.gamenews.databinding.NewsItemBinding
import com.example.gamenews.model.News
import java.net.URI
import java.util.*
import kotlin.collections.ArrayList


class FeedNewsAdapter(
    private val context: Context,
    private val newsList: ArrayList<News>?
) :
    RecyclerView.Adapter<FeedNewsAdapter.NewsHolder>(), Filterable {

    var newsFilterList: ArrayList<News>? = newsList


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsHolder {
        val binding = NewsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return NewsHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsHolder, position: Int) {
        with(newsFilterList?.get(position)) {
            holder.binding.newsText.text = this?.title ?: ""
            holder.binding.newsSource.text = HtmlCompat.fromHtml(
                "<a href =\"${this?.click_url}\">${URI(this?.click_url).host}</a>",
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
            holder.binding.timeAgo.text = "- ${this?.time}"
            holder.binding.newsSource.movementMethod = LinkMovementMethod.getInstance();
            if (this?.img?.isNullOrEmpty()==false) {
                Glide.with(context)
                    .load(img)
                    .into(holder.binding.newsImage)
                    .apply { Log.e("Loading error", "${RequestOptions().errorId}") }
            } else {
                holder.binding.newsImage.visibility = View.GONE
            }
        }
    }

    override fun getItemCount() = newsFilterList?.size ?: 0

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val searchText = constraint.toString()
                if (searchText.isEmpty()) {
                    newsFilterList = newsList
                } else {
                    val resultList = ArrayList<News>()
                    if (newsList != null) {
                        for (news in newsList) {
                            if (news.title!!.toLowerCase(Locale.ROOT)
                                    .contains(searchText.toLowerCase(Locale.ROOT))
                            ) {
                                resultList.add(news)
                            }
                        }
                    }
                    newsFilterList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = newsFilterList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                newsFilterList = results?.values as ArrayList<News>
                notifyDataSetChanged()
            }
        }
    }

    inner class NewsHolder(val binding: NewsItemBinding) : RecyclerView.ViewHolder(binding.root)


}