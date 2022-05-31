package com.example.newsapp.data.remote

import com.example.newsapp.data.model.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface NewsApi {

    @GET("v2/top-headlines?country={countryCode}&page={page}&apiKey={apiKey}")
    suspend fun getAllBreakingNews(
        @Path("country") countryCode: String = "id",
        @Path("page") page: Int = 1,
        @Path("apiKey") apiKey: String
    ): NewsResponse

    @GET("v2/everything?q={q}&page={page}&language={language}&apiKey={apiKey}")
    suspend fun searchNews(
        @Path("q") q: String,
        @Path("country") language: String = "id",
        @Path("page") page: Int,
        @Path("apiKey") apiKey: String
    ): NewsResponse

}
