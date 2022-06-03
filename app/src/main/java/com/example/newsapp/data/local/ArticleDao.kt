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

    @Query("SELECT * FROM articles")
    fun getAllArticle(): LiveData<List<ArticleEntity>>
}
