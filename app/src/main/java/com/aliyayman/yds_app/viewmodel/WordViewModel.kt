package com.aliyayman.yds_app.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.aliyayman.yds_app.model.Word
import com.aliyayman.yds_app.repository.WordRepository
import com.aliyayman.yds_app.util.Resourse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WordViewModel @Inject constructor(
    application: Application,
    private val repository: WordRepository
) : BaseViewModel(application) {
    val words = MutableLiveData<Resourse<List<Word>>>()
    val favoriteWords = MutableLiveData<Resourse<List<Word>>>()
    val isRefresh = MutableLiveData<Boolean>()

    fun refreshWord(id: Int) {
        getFromRoom(id)
    }

    fun refreshFavoriteWord() {
        viewModelScope.launch {
            favoriteWords.postValue(Resourse.Loading())
            try {
                val roomList=repository.getFavoritesWord()
                favoriteWords.postValue(Resourse.Success(roomList))
            } catch (e: Exception) {
                favoriteWords.postValue(Resourse.Error(e.message.toString()))
            }

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
            favoriteWords.postValue(Resourse.Loading())
            try {
                val roomList=repository.getFavoritesWord()
                favoriteWords.postValue(Resourse.Success(roomList))
            } catch (e: Exception) {
                favoriteWords.postValue(Resourse.Error(e.message.toString()))
            }

        }
    }

    private fun getFromRoom(id: Int) {
        viewModelScope.launch {
            words.postValue(Resourse.Loading())
            try {
                val roomList = repository.getWordFromCategory(id)
                words.postValue(Resourse.Success(roomList))
            } catch (e: Exception) {
                words.postValue(Resourse.Error(e.message.toString()))
            }
        }
    }

}