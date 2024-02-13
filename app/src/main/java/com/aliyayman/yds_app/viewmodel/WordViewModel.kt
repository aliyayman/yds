package com.aliyayman.yds_app.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.aliyayman.yds_app.model.Word
import com.aliyayman.yds_app.service.myDatabase
import kotlinx.coroutines.launch


class WordViewModel(application: Application) : BaseViewModel(application) {
    val words = MutableLiveData<List<Word>>()
    val wordError = MutableLiveData<Boolean>()
    val wordLoading = MutableLiveData<Boolean>()

    fun refreshWord(id: Int) {
        getFromRoom(id)
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