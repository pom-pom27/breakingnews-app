package com.example.newsapp.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.R
import com.example.newsapp.databinding.SearchNewsFragmentBinding
import com.example.newsapp.ui.adapters.NewsAdapter
import com.example.newsapp.ui.viewmodels.NewsViewModel
import com.example.newsapp.utils.Constants
import com.example.newsapp.utils.Resource
import com.example.newsapp.utils.VerticalSpaceItemDecoration

class SearchNewsFragment : Fragment(R.layout.search_news_fragment) {

    private val TAG = "SearchNewsFragment"

    private val newsViewModel: NewsViewModel by activityViewModels()
    lateinit var newsAdapter: NewsAdapter

    private var _binding: SearchNewsFragmentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SearchNewsFragmentBinding.inflate(layoutInflater, container, false)
        setupRecycleView()
        subscribeToObserver()

        binding.etSearch.addTextChangedListener { editable ->
            newsViewModel.searchNews(editable.toString())
        }

        newsAdapter.setOnItemClickListener { article ->
            val bundle = Bundle().apply {
                putParcelable("article", article)
            }

            findNavController().navigate(
                R.id.action_searchNewsFragment_to_articleFragment, bundle
            )
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun subscribeToObserver() {
        newsViewModel.searchNews.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    showProgressbar()
                }

                is Resource.Error -> {
                    hideProgressbar()
                    Log.d(TAG, "subscribeToObserver: An error occurred: ${response.message}")
                }

                is Resource.Success -> {
                    hideProgressbar()
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles)
                    }
                }
            }

        }
    }

    private fun hideProgressbar() {
        binding.paginationProgressBar.isVisible = false
    }

    private fun showProgressbar() {
        binding.paginationProgressBar.isVisible = true
    }

    private fun setupRecycleView() {
        newsAdapter = NewsAdapter()
        binding.rvSearchNews.apply {
            addItemDecoration(VerticalSpaceItemDecoration(Constants.VERTICAL_ITEM_SPACE));

            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}
