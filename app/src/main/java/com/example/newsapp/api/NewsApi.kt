package com.example.newsapp.api

import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("v2/top-headlines")
    suspend fun getAllBreakingNews(

        //keys in json
        @Query("country")
        countryCode: String = "id",
        @Query("apiKey")
        apiKey: String,
        @Query("page")
        pageNumber: Int = 1,
    )
}
