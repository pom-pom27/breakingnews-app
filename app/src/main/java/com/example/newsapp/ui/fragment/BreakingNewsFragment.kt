package com.example.newsapp.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.activity.addCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.databinding.BreakingNewsFragmentBinding
import com.example.newsapp.ui.adapters.NewsAdapter
import com.example.newsapp.ui.viewmodels.NewsViewModel
import com.example.newsapp.utils.Constants.Companion.VERTICAL_ITEM_SPACE
import com.example.newsapp.utils.Resource
import com.example.newsapp.utils.VerticalSpaceItemDecoration
import kotlin.system.exitProcess

class BreakingNewsFragment : Fragment(R.layout.breaking_news_fragment) {

    private val TAG = "BreakingNewsFragment"

    private val newsViewModel: NewsViewModel by activityViewModels()
    lateinit var newsAdapter: NewsAdapter

    private var _binding: BreakingNewsFragmentBinding? = null

    private var backPressedTime: Long = 0

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BreakingNewsFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupRecycleView()
        subscribeToObserver()

        newsAdapter.setOnItemClickListener { article ->
            val bundle = Bundle().apply {
                putParcelable("article", article)
            }
            Log.d(TAG, "onCreateView: $article")

            findNavController().navigate(
                R.id.action_breakingNewsFragment_to_articleFragment, bundle
            )
        }

        addDoubleBackToExit()
    }

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }

        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLastPage && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= 20
            val shouldPaginate =
                isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScrolling

            if (shouldPaginate) {
                newsViewModel.getBreakingNews("id")
                isScrolling = false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun subscribeToObserver() {
        newsViewModel.breakingNews.observe(viewLifecycleOwner) { response ->
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
                        newsAdapter.differ.submitList(newsResponse.articles.toList())
                        val totalPages = newsResponse.totalResults / 20 + 2
                        isLastPage = newsViewModel.breakingPageNumber == totalPages
                        if (isLastPage) {
                            binding.rvNews.setPadding(0, 0, 0, 0)
                        }
                    }
                }
            }

        }
    }

    private fun hideProgressbar() {
        binding.paginationProgressBar.isVisible = false
        isLoading = false
    }

    private fun showProgressbar() {
        binding.paginationProgressBar.isVisible = true
        isLoading = true
    }

    private fun setupRecycleView() {
        newsAdapter = NewsAdapter()
        binding.rvNews.apply {
            addItemDecoration(VerticalSpaceItemDecoration(VERTICAL_ITEM_SPACE));

            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@BreakingNewsFragment.scrollListener)
        }
    }

    private fun addDoubleBackToExit() {

        // This callback will only be called when MyFragment is at least Started.
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            if (backPressedTime + 3000 > System.currentTimeMillis()) {
                Log.d("TAG", "onCreate: press one more to exit")

                exitProcess(1)
            } else {
                Log.d("TAG", "onCreate: press 1 more to exit")
            }
            backPressedTime = System.currentTimeMillis()
        }
    }
}
