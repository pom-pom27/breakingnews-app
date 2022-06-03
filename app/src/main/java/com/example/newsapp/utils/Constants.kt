package com.example.newsapp.utils

import com.example.newsapp.BuildConfig

class Constants {
    companion object {
        const val API_KEY = BuildConfig.API_KEY
        const val BASE_URL = "https://newsapi.org/"
        const val VERTICAL_ITEM_SPACE = 24
        const val DELAY_SEARCH_TIME = 500L

    }
}
