package com.aliyayman.yds_app.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.aliyayman.yds_app.data.RemoteConfig
import com.aliyayman.yds_app.model.Word
import com.aliyayman.yds_app.service.myDatabase
import com.aliyayman.yds_app.util.CustomSharedPreferences
import kotlinx.coroutines.launch
import org.json.JSONArray
import java.lang.Exception

class WordViewModel(application: Application) : BaseViewModel(application) {
    val words = MutableLiveData<List<Word>>()
    val wordError = MutableLiveData<Boolean>()
    val wordLoading = MutableLiveData<Boolean>()
    private var wordList = ArrayList<Word>()
    private var rc = RemoteConfig()
    private var customSharedPreferences = CustomSharedPreferences(getApplication())
    private var refreshTime = 10 * 60 * 1000 * 1000 * 1000L
    private lateinit var fromRoom : List<Word>

    fun refreshWord(id: Int) {
        fromRoom = getFromRoom(id)
        val updateTime = customSharedPreferences.getTime()
        if (updateTime != null && updateTime != 0L && System.nanoTime() - updateTime < refreshTime) {

            if (fromRoom.isEmpty()) {
                val categoryName = findCategoryName(id)
                getFromRemoteConfig(categoryName)
            } else
                getFromRoom(id)
        } else {
            val categoryName = findCategoryName(id)
            getFromRemoteConfig(categoryName)
        }
    }

    private fun getFromRoom(id: Int): List<Word> {
        wordLoading.value = true
        var mylist = ArrayList<Word>()
        launch {
            val sqlList = myDatabase(getApplication()).wordDao().getWordFromCategory(id)
            showWord(sqlList)
            println("sqlden gelen wordssss:")
            mylist = sqlList as ArrayList<Word>
            println(mylist)
            println("******")

        }
        return mylist

    }

    private fun storeInRoom(list: List<Word>) {
        launch {
            val dao = myDatabase(getApplication()).wordDao()
            val listLong = dao.insertAll(*list.toTypedArray())
            var i = 0
            while (i < list.size) {
                list[i].id = listLong[i].toInt()
                i += 1
            }
            showWord(list)
            println("kelimeler sqlite eklendi")
        }
        customSharedPreferences.saveTime(System.nanoTime())
    }

    private fun showWord(wordlist: List<Word>) {
        words.value = wordlist
        wordError.value = false
        wordLoading.value = false
    }
    private fun findCategoryName(id: Int): String {
        return when (id) {
            1 -> "adjectives"
            2 -> "adverbs"
            3 -> "nouns"
            4 -> "verbs"
            5 -> "pharasel_verbs"
            else -> "nouns"
        }
    }
    private fun getFromRemoteConfig(name: String) {

        var remoteConfig = rc.getInitial()
        remoteConfig.fetchAndActivate().addOnCompleteListener {
            if (it.isSuccessful) {
                val json = remoteConfig.getString("$name")
                try {
                    val jsonArray = JSONArray(json)
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val ing = jsonObject.getString("ing")
                        val tc = jsonObject.getString("tc")
                        val categoryId = jsonObject.getInt("categoryId")
                        val isFavorite = jsonObject.getBoolean("isFavorite")

                        val word = Word(ing, tc, isFavorite, categoryId)
                        wordList.add(word)
                    }
                    println("remoteconfigden gelen word:")
                    println(wordList)
                    storeInRoom(wordList)

                } catch (e: Exception) {
                    println(e.message)
                    wordError.value = true
                    wordLoading.value = false
                }
            } else {
                println("veriler gelmedi")
            }
        }
    }
}