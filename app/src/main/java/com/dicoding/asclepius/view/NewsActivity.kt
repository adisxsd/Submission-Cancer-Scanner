package com.dicoding.asclepius.view.news

import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.NewsBinding
import com.dicoding.asclepius.view.adapter.NewsAdapter

class NewsActivity : AppCompatActivity() {

    private lateinit var binding: NewsBinding
    private val viewModel: NewsVM by viewModels() // Use the existing NewsVM
    private lateinit var newsAdapter: NewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = NewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupRecyclerView()

        observeViewModel()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.navigationIcon?.setTint(Color.WHITE)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish() // Close the activity
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter() // Assuming NewsAdapter is already set up
        binding.rvNews.layoutManager = LinearLayoutManager(this)
        binding.rvNews.adapter = newsAdapter
    }

    private fun observeViewModel() {
        // Observe loading state to show or hide the progress bar
        viewModel.loading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            if (isLoading) {
                binding.tvNoNews.visibility = View.GONE
            }
        }

        // Observe news articles
        viewModel.newsArticles.observe(this) { articles ->
            if (articles.isEmpty()) {
                binding.tvNoNews.visibility = View.VISIBLE
            } else {
                newsAdapter.submitList(articles)
                binding.tvNoNews.visibility = View.GONE
            }
        }


        // Handle errors or empty data
        viewModel.newsArticles.observe(this) { articles ->
            if (articles.isEmpty()) {
                binding.tvNoNews.visibility = View.VISIBLE
                Toast.makeText(this, "No articles available", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
