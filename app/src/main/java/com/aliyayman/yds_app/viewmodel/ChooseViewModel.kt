package com.aliyayman.yds_app.viewmodel

import android.app.Application
import com.aliyayman.yds_app.model.Word
import com.aliyayman.yds_app.service.myDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ChooseViewModel(application: Application) : BaseViewModel(application){
      var wordList = ArrayList<Word>()
      var randomList = HashSet<String>()
      private var answerList = ArrayList<String>()





    suspend fun getWords() : ArrayList<String>{
        withContext(Dispatchers.IO){
            wordList = myDatabase(getApplication()).wordDao().getWordFive().toList() as ArrayList<Word>
            randomList.clear()
            for (i in 0 until  wordList.size){
                randomList.add(wordList[i].tc.toString())
            }

        }
        answerList.clear()
        answerList.addAll(randomList)
        return answerList

    }
}