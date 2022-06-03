package com.example.newsapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.newsapp.R
import com.example.newsapp.databinding.ArticleFragmentBinding

class ArticleFragment : Fragment(R.layout.article_fragment) {

    private val args: ArticleFragmentArgs by navArgs()

    private var _binding: ArticleFragmentBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ArticleFragmentBinding.inflate(layoutInflater, container, false)

        val article = args.article

        binding.webView.apply {
            webViewClient = WebViewClient()

            article.url?.let {
                loadUrl(article.url)
            }

        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
