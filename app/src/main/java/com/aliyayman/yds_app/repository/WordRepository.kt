package com.aliyayman.yds_app.repository

import androidx.room.Delete
import androidx.room.Query
import com.aliyayman.yds_app.model.Word
import com.aliyayman.yds_app.service.myDatabase

class WordRepository(
    val db : myDatabase
) {
    suspend fun insertAll(word: Word) = db.wordDao().insertAll(word)
    suspend fun getFavoritesWord() = db.wordDao().getFavoritesWord()
    suspend fun updateFavorite(isFavorite: Boolean,id:Int) = db.wordDao().updateFavorite(isFavorite,id)
    suspend fun getWordFromCategory(categoryid: Int) = db.wordDao().getWordFromCategory(categoryid)
    suspend fun getWordTen() = db.wordDao().getWordTen()
    suspend fun getWordFive() = db.wordDao().getWordFive()
    suspend fun getFaultWord3(wordID: Int) = db.wordDao().getFaultWord3(wordID)
    suspend fun delete(word: Word) = db.wordDao().delete(word)
    suspend fun deleteAllWords() = db.wordDao().deleteAllWords()
    suspend fun getAllWords() = db.wordDao().getAllWords()
}