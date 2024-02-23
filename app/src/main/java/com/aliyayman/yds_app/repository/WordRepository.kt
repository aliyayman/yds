package com.aliyayman.yds_app.repository


import com.aliyayman.yds_app.model.Word
import com.aliyayman.yds_app.service.WordDao
import javax.inject.Inject

class WordRepository @Inject constructor(
    private val wordDao: WordDao
) {
    suspend fun insertAll(vararg words: Word) :List<Long> = wordDao.insertAll(*words)
    suspend fun getFavoritesWord() = wordDao.getFavoritesWord()
    suspend fun updateFavorite(isFavorite: Boolean,id:Int) = wordDao.updateFavorite(isFavorite,id)
    suspend fun getWordFromCategory(categoryid: Int) = wordDao.getWordFromCategory(categoryid)
    suspend fun getWordTen() = wordDao.getWordTen()
    suspend fun getWordFive() = wordDao.getWordFive()
    suspend fun getFaultWord3(wordID: Int) = wordDao.getFaultWord3(wordID)
    suspend fun delete(word: Word) = wordDao.delete(word)
    suspend fun deleteAllWords() = wordDao.deleteAllWords()
    suspend fun getAllWords() = wordDao.getAllWords()
}