package com.example.newsapp.repositories

import com.example.newsapp.data.local.ArticleDao
import com.example.newsapp.data.remote.NewsApi

class NewsRepository(private val newsApi: NewsApi, val dao: ArticleDao) {

    //countryCode default is id
    suspend fun getBreakingNews(countryCode: String, page: Int) =
        newsApi.getAllBreakingNews(countryCode, page)

    suspend fun getSearchNews(query: String, page: Int) =
        newsApi.searchNews(query, page)
}
