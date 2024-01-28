package com.aliyayman.yds_app.service

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.aliyayman.yds_app.model.Category
@Dao
interface CategoryDao {
    @Insert
    suspend fun insertAll(vararg category : Category): List<Long>
    @Query("SELECT * FROM category")
    suspend fun getAllWords(): List<Category>
    @Query("DELETE  FROM category")
    suspend fun deleteAllCategory()
}