package com.aliyayman.yds_app.viewmodel

import android.app.Application
import com.aliyayman.yds_app.model.Word
import com.aliyayman.yds_app.service.myDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Collections

class ChooseViewModel(application: Application) : BaseViewModel(application){
      var wordList = ArrayList<Word>()
      private var randomList = ArrayList<Word>()





    suspend fun getWords() : ArrayList<Word>{
        withContext(Dispatchers.IO){
            wordList = myDatabase(getApplication()).wordDao().getWordFive().toList() as ArrayList<Word>
            randomList.clear()
            randomList.addAll(wordList)
            randomList.shuffle()
        }
        return randomList

    }
}