package com.example.newsapp.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    private val _searchNews = MutableLiveData<Resource<NewsResponse>>()
    val searchNews: LiveData<Resource<NewsResponse>> = _searchNews

    private val _searchQuery = MutableLiveData("")
    val searchQuery: LiveData<String> = _searchQuery
    var searchPageNumber = 1

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

    private fun handleBreakingNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleSearchNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
}
