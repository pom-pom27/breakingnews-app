package com.example.newsapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.databinding.SavedNewsFragmentBinding
import com.example.newsapp.ui.adapters.NewsAdapter
import com.example.newsapp.ui.viewmodels.NewsViewModel
import com.example.newsapp.utils.Constants
import com.example.newsapp.utils.VerticalSpaceItemDecoration
import com.google.android.material.snackbar.Snackbar

class SavedNewsFragment : Fragment(R.layout.saved_news_fragment) {
    private val TAG = "SavedNewsFragment"

    private var _binding: SavedNewsFragmentBinding? = null

    private val binding get() = _binding!!

    lateinit var newsAdapter: NewsAdapter

    private val newsViewModel: NewsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SavedNewsFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupRecycleView()
        subscribeToObserver()

        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = true

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition

                val article = newsAdapter.differ.currentList[position]
                newsViewModel.deleteArticle(article)

                Snackbar.make(view, "Delete saved article", Snackbar.LENGTH_LONG).apply {
                    setAction("UNDO") {
                        newsViewModel.saveArticle(article)
                    }
                    show()
                }
            }

        }

        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(binding.rvNews)
    }

    private fun subscribeToObserver() {
        newsViewModel.getSavedArticle().observe(viewLifecycleOwner) { articles ->
            newsAdapter.differ.submitList(articles)
        }
    }

    private fun setupRecycleView() {
        newsAdapter = NewsAdapter()
        binding.rvNews.apply {
            addItemDecoration(VerticalSpaceItemDecoration(Constants.VERTICAL_ITEM_SPACE));
            val layoutM = LinearLayoutManager(activity)

            adapter = newsAdapter
            layoutManager = layoutM
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
