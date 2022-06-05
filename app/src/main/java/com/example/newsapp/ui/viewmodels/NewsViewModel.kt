package com.example.newsapp.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.data.model.Article
import com.example.newsapp.data.model.NewsResponse
import com.example.newsapp.repositories.NewsRepository
import com.example.newsapp.utils.Constants
import com.example.newsapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(private val newsRepository: NewsRepository) : ViewModel() {

    private val _breakingNews = MutableLiveData<Resource<NewsResponse>>()
    val breakingNews: LiveData<Resource<NewsResponse>> = _breakingNews
    var breakingPageNumber = 1
    var breakingNewsResponse: NewsResponse? = null

    private val _searchNews = MutableLiveData<Resource<NewsResponse>>()
    val searchNews: LiveData<Resource<NewsResponse>> = _searchNews
    var searchNewsResponse: NewsResponse? = null

    private val _searchQuery = MutableLiveData("")
    val searchQuery: LiveData<String> = _searchQuery
    var searchPageNumber = 1

    //
    //    private val _savedArticles = MutableLiveData<List<Article>>()
    //    val savedArticles: LiveData<List<Article>> = _savedArticles

    private var _searchJob: Job? = null

    init {
        getBreakingNews(countryCode = "id")
    }

    fun getBreakingNews(countryCode: String) = viewModelScope.launch {
        _breakingNews.postValue(Resource.Loading())
        val response = newsRepository.getBreakingNews(countryCode, breakingPageNumber)
        _breakingNews.postValue(handleBreakingNewsResponse(response))
    }

    fun searchNews(query: String) {
        if (query.isNotBlank()) {
            _searchQuery.postValue(query)
            _searchJob?.cancel()
            _searchJob = viewModelScope.launch {
                delay(Constants.DELAY_SEARCH_TIME)
                _searchNews.postValue(Resource.Loading())
                val response = newsRepository.getSearchNews(query, searchPageNumber)
                _searchNews.postValue(handleSearchNewsResponse(response))
            }
        }
    }

    fun deleteArticle(article: Article) =
        viewModelScope.launch { newsRepository.deleteArticle(article) }

    fun saveArticle(article: Article) = viewModelScope.launch {
        newsRepository.upOrInsert(article)
    }

    fun getSavedArticle() = newsRepository.getAllArticle()

    private fun handleBreakingNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                breakingPageNumber++
                if (breakingNewsResponse == null) {

                    breakingNewsResponse = resultResponse
                } else {
                    val oldArticle = breakingNewsResponse?.articles
                    val newArticle = resultResponse.articles
                    oldArticle?.addAll(newArticle)
                }
                return Resource.Success(breakingNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleSearchNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                searchPageNumber++
                if (searchNewsResponse == null) {

                    searchNewsResponse = resultResponse
                } else {
                    val oldArticle = searchNewsResponse?.articles
                    val newArticle = resultResponse.articles
                    oldArticle?.addAll(newArticle)
                }
                return Resource.Success(searchNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
}
