package com.dicoding.asclepius.view.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.remote.model.ArticlesItem
import com.dicoding.asclepius.databinding.ItemNewsBinding
import com.dicoding.asclepius.utils.FormatDate.formatToDateOnly

class NewsAdapter :
    ListAdapter<ArticlesItem, NewsAdapter.MyNewsViewHolder>(NewsDiffCallback()) {

    class MyNewsViewHolder(private val binding: ItemNewsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(news: ArticlesItem) {
            with(binding) {
                // Bind the title, description, author, and date
                textTitle.text = news.title
                textDescription.text = news.description
                textAuthor.text = news.author
                textDate.text = formatToDateOnly(news.publishedAt.toString())

                // Use Glide to load the image if available, or show a placeholder if not
                if (news.urlToImage != null) {
                    Glide.with(itemView.context)
                        .load(news.urlToImage)
                        .into(imageNews)
                } else {
                    Glide.with(itemView.context)
                        .load(R.drawable.baseline_error_24) // Your placeholder image
                        .into(imageNews)
                }

                // On item click, open the article URL in a browser
                itemView.setOnClickListener {
                    val browserIntent = Intent(Intent.ACTION_VIEW, news.url?.toUri())
                    itemView.context.startActivity(browserIntent)
                }
            }
        }
    }

    // DiffCallback to optimize list updates
    private class NewsDiffCallback : DiffUtil.ItemCallback<ArticlesItem>() {
        override fun areItemsTheSame(oldItem: ArticlesItem, newItem: ArticlesItem): Boolean {
            return oldItem.title == newItem.title // You can use a unique identifier if available
        }

        override fun areContentsTheSame(oldItem: ArticlesItem, newItem: ArticlesItem): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyNewsViewHolder {
        val binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyNewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyNewsViewHolder, position: Int) {
        val news = getItem(position)  // Use getItem() to fetch the article
        holder.bind(news)
    }
}
