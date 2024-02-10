package com.aliyayman.yds_app.viewmodel

import android.app.Application
import com.aliyayman.yds_app.model.Word
import com.aliyayman.yds_app.model.WordRecyler
import com.aliyayman.yds_app.service.myDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Collections

class ChooseViewModel(application: Application) : BaseViewModel(application){
      var wordList = ArrayList<WordRecyler>()
      private var randomList = ArrayList<WordRecyler>()





    suspend fun getWords() : ArrayList<WordRecyler>{
        withContext(Dispatchers.IO){
            wordList = myDatabase(getApplication()).wordDao().getWordFive().map {
                WordRecyler(it.id,it.ing,it.tc,it.isFavorite,it.categoryId,true)
            }.toList() as ArrayList<WordRecyler>
            randomList.clear()
            randomList.addAll(wordList)
            randomList.shuffle()
        }
        return randomList

    }
}