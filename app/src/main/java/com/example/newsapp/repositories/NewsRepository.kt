package com.example.newsapp.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.newsapp.data.local.ArticleDao
import com.example.newsapp.data.model.Article
import com.example.newsapp.data.remote.NewsApi

class NewsRepository(private val newsApi: NewsApi, private val dao: ArticleDao) {

    //countryCode default is id
    suspend fun getBreakingNews(countryCode: String, page: Int) =
        newsApi.getAllBreakingNews(countryCode, page)

    suspend fun getSearchNews(query: String, page: Int) =
        newsApi.searchNews(query, page)

    suspend fun upOrInsert(article: Article) = dao.upOrInsert(article.toArticleEntity())

    suspend fun deleteArticle(article: Article) = dao.deleteArticle(article.toArticleEntity())

    fun getAllArticle(): LiveData<List<Article>> = Transformations.map(dao.getAllArticle()) {
        it.map { articleEntity -> articleEntity.toArticle() }
    }
}
