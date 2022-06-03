package com.example.newsapp.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
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
        return binding.root
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
        binding.rvNews.apply {
            addItemDecoration(VerticalSpaceItemDecoration(VERTICAL_ITEM_SPACE));

            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
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
