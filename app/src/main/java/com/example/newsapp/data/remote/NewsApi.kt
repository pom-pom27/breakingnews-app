package com.example.newsapp.data.remote

import com.example.newsapp.data.model.NewsResponse
import com.example.newsapp.utils.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("v2/top-headlines")
    suspend fun getAllBreakingNews(
        @Query("country") countryCode: String = "id",
        @Query("page") page: Int = 1,
        @Query("apiKey") apiKey: String = Constants.API_KEY
    ): Response<NewsResponse>

    @GET("v2/everything")
    suspend fun searchNews(
        @Query("q") q: String,
        @Query("page") page: Int,
        @Query("apiKey") apiKey: String = Constants.API_KEY
    ): Response<NewsResponse>

}
