package com.aliyayman.yds_app.service

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aliyayman.yds_app.model.Article
import com.aliyayman.yds_app.model.Category
import com.aliyayman.yds_app.model.Word

@Dao
interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg article: Article): List<Long>
    @Query("SELECT * FROM article")
    suspend fun getAllArticles(): List<Article>
    @Query("SELECT * FROM article WHERE id=:id")
    suspend fun getArticleFromId(id: Int): Article

}