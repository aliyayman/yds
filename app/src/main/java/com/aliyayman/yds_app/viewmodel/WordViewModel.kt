package com.aliyayman.yds_app.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.aliyayman.yds_app.model.Word
import com.aliyayman.yds_app.repository.WordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WordViewModel @Inject constructor(
    application: Application,
    private val repository: WordRepository
    ) : BaseViewModel(application) {
    val words = MutableLiveData<List<Word>>()
    val wordError = MutableLiveData<Boolean>()
    val wordLoading = MutableLiveData<Boolean>()

    fun refreshWord(id: Int) {
        getFromRoom(id)
    }

    fun refreshFavoriteWord() {
        launch {
            val roomList=repository.getFavoritesWord()
            showWord(roomList)
        }
    }

    fun addFavorite(word: Word) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateFavorite(true, word.id)
        }
    }

    fun removeFavorite(word: Word) {
        viewModelScope.launch(Dispatchers.Main) {
            repository.updateFavorite(false, word.id)
        }
    }
    fun removeAndGetFavorite(word: Word) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateFavorite(false, word.id)
            words.postValue(repository.getFavoritesWord())
        }
    }

    private fun getFromRoom(id: Int) {
        wordLoading.value = true
        launch {
            val roomList = repository.getWordFromCategory(id)
            showWord(roomList)
        }
    }

    private fun showWord(wordlist: List<Word>) {
        words.postValue(wordlist)
        wordError.value = false
        wordLoading.value = false
    }
}