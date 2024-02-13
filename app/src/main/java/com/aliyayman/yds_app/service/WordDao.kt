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

    @Query("INSERT INTO word (ing,tc,isFavorite,categoryId) VALUES (:ing,:tc,:isFavorite,:categoryid) ")
    suspend fun insertFavorite(ing:String?,tc:String?,isFavorite:Boolean,categoryid: Int?)

    @Query("UPDATE  word  SET  isFavorite= :isFavorite  Where id =:id ")
    suspend fun updateFavorite(isFavorite: Boolean,id:Int)

    @Query("DELETE  FROM word WHERE categoryId=:categoryid and id=:wordID")
    suspend fun deletewWordFavorites(wordID: Int,categoryid: Int)
    @Query("SELECT * FROM word")
    suspend fun getAllWords(): List<Word>
    @Query("SELECT * FROM word WHERE categoryId=:categoryid")
    suspend fun getWordFromCategory(categoryid: Int): List<Word>

    @Query("SELECT * FROM word  ORDER BY RANDOM() LIMIT 10")
    suspend fun getWordTen(): List<Word>
    @Query("SELECT * FROM word  ORDER BY RANDOM() LIMIT 5")
    suspend fun getWordFive(): List<Word>

    @Query("SELECT * FROM word WHERE id!=:wordID ORDER BY RANDOM() LIMIT 3")
    suspend fun getFaultWord3(wordID: Int): List<Word>

    @Delete
    suspend fun delete(word: Word)

    @Query("DELETE  FROM word")
    suspend fun deleteAllWords()


}