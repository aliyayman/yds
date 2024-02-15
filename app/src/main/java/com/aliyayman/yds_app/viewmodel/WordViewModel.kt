package com.aliyayman.yds_app.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.aliyayman.yds_app.model.Word
import com.aliyayman.yds_app.service.myDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class WordViewModel(application: Application) : BaseViewModel(application) {
    val words = MutableLiveData<List<Word>>()
    val wordError = MutableLiveData<Boolean>()
    val wordLoading = MutableLiveData<Boolean>()

    fun refreshWord(id: Int) {
        getFromRoom(id)
    }

    fun refreshFavoriteWord() {
        launch {
            val roomList = myDatabase(getApplication()).wordDao().getFavoritesWord()
            showWord(roomList)

        }
    }

    fun addFavorite(word: Word) {
        viewModelScope.launch(Dispatchers.IO) {
            if (word !== null) {
                myDatabase(getApplication()).wordDao().updateFavorite(true,word.id)
            }
        }
    }

    fun removeFavorite(word: Word) {
        viewModelScope.launch(Dispatchers.IO) {
            if (word.id !== null) {
                myDatabase(getApplication()).wordDao().updateFavorite(false,word.id)
            }
        }
    }

    private fun getFromRoom(id: Int) {
        wordLoading.value = true
        launch {
            val roomList = myDatabase(getApplication()).wordDao().getWordFromCategory(id)
            showWord(roomList)
        }
    }
    private fun showWord(wordlist: List<Word>) {
        words.value = wordlist
        wordError.value = false
        wordLoading.value = false
    }
}