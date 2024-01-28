package com.aliyayman.yds_app.service

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.aliyayman.yds_app.model.Word

@Dao
interface WordDao {
    @Insert
    suspend fun insertAll(vararg words: Word): List<Long>

    @Query("SELECT * FROM word")
    suspend fun getAllWords(): List<Word>

    @Query("SELECT * FROM word WHERE categoryId=:categoryid")
    suspend fun getWordFromCategory(categoryid: Int): List<Word>

    @Query("SELECT * FROM word WHERE categoryId=:categoryid ORDER BY RANDOM() LIMIT 10")
    suspend fun getWordTen(categoryid: Int): List<Word>

    @Delete
    suspend fun delete(word: Word)

    @Query("DELETE  FROM word")
    suspend fun deleteAllWords()


}