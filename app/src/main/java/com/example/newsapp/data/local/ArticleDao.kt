package com.example.newsapp.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.newsapp.data.local.entity.ArticleEntity

@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upOrInsert(articles: ArticleEntity): Long

    @Delete
    suspend fun deleteArticle(articleEntity: ArticleEntity)

    @Query("SELECT * FROM articles ORDER BY id DESC")
    fun getAllArticle(): LiveData<List<ArticleEntity>>
}

//TODO: add caching breakingNews and searchNews articles
