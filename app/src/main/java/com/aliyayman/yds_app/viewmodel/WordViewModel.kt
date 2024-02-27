package com.aliyayman.yds_app.viewmodel

import SingleLiveEvent
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
    val words = SingleLiveEvent<List<Word>>()
    val favoriteWords = MutableLiveData<List<Word>>()
    val wordError = MutableLiveData<Boolean>()
    val wordLoading = MutableLiveData<Boolean>()
    val isRefresh = MutableLiveData<Boolean>()

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
            isRefresh.postValue(true)
        }
    }

    fun removeFavorite(word: Word) {
        viewModelScope.launch(Dispatchers.Main) {
            repository.updateFavorite(false, word.id)
            isRefresh.postValue(true)
        }
    }

    fun getFavoriteWords() {
        viewModelScope.launch(Dispatchers.IO) {
            favoriteWords.postValue(repository.getFavoritesWord())
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