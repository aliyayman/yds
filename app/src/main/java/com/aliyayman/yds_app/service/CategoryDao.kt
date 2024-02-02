package com.aliyayman.yds_app.service

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.aliyayman.yds_app.model.Category
@Dao
interface CategoryDao {
    @Insert
    suspend fun insertAll(vararg category : Category)
    @Query("SELECT * FROM category")
    suspend fun getAllCategories(): List<Category>
    @Query("DELETE  FROM category")
    suspend fun deleteAllCategory()
}