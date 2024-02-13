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

    fun addFavorite(word: Word) {
        viewModelScope.launch(Dispatchers.IO) {
            if (word !== null) {
                myDatabase(getApplication()).wordDao().updateFavorite(true,word.id)
                myDatabase(getApplication()).wordDao().insertFavorite(word.ing, word.tc, true, 6)
            }
        }
    }

    fun removeFavorite(word: Word) {
        viewModelScope.launch(Dispatchers.IO) {
            if (word.id !== null) {
                myDatabase(getApplication()).wordDao().updateFavorite(false,word.id)
                myDatabase(getApplication()).wordDao().deletewWordFavorites(word.id, 6)
            }
        }
    }

    private fun getFromRoom(id: Int) {
        wordLoading.value = true
        var mylist = ArrayList<Word>()
        launch {
            val sqlList = myDatabase(getApplication()).wordDao().getWordFromCategory(id)
            showWord(sqlList)
            mylist = sqlList as ArrayList<Word>

        }
    }
    private fun showWord(wordlist: List<Word>) {
        words.value = wordlist
        wordError.value = false
        wordLoading.value = false
    }
}