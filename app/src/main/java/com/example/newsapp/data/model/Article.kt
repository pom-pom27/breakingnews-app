package com.example.newsapp.data.model

import android.os.Parcelable
import com.example.newsapp.data.local.entity.ArticleEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class Article(
    val id: Int?,
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String?,
    val source: Source,
    val title: String?,
    val url: String?,
    val urlToImage: String?
) : Parcelable {
    fun toArticleEntity(): ArticleEntity = ArticleEntity(
        id = id,
        author = author ?: "",
        content = content ?: "",
        description = description ?: "",
        publishedAt = publishedAt ?: "",
        source = source,
        title = title ?: "",
        url = url ?: "",
        urlToImage = urlToImage ?: "",
    )
}
