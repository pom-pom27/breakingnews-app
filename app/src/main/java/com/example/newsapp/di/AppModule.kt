package com.example.newsapp.di

import android.app.Application
import androidx.room.Room
import com.example.newsapp.BuildConfig
import com.example.newsapp.data.local.Converters
import com.example.newsapp.data.local.NewsDatabase
import com.example.newsapp.data.remote.NewsApi
import com.example.newsapp.repositories.NewsRepository
import com.example.newsapp.utils.Constants.Companion.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNewsApi(): NewsApi {
        val httpClient: OkHttpClient.Builder = OkHttpClient.Builder()

        // ? disable logging in production
        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.apply {
                setLevel(BODY)
                redactHeader("Authorization")
                redactHeader("Cookie")
            }
            httpClient
                .addInterceptor(logging)
        }

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())
            .build()
            .create(NewsApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRoomDb(app: Application): NewsDatabase = Room.databaseBuilder(
        app, NewsDatabase::class.java, "news_db"
    ).addTypeConverter(Converters())
        .build()

    @Provides
    @Singleton
    fun provideNewsRepository(api: NewsApi, db: NewsDatabase): NewsRepository =
        NewsRepository(api, db.articleDao)
}
