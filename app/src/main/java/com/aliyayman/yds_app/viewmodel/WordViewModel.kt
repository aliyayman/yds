package com.aliyayman.yds_app.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aliyayman.yds_app.data.RemoteConfig
import com.aliyayman.yds_app.model.Word
import org.json.JSONArray
import java.lang.Exception


class WordViewModel : ViewModel() {
    val words = MutableLiveData<List<Word>>()
    val categoryError = MutableLiveData<Boolean>()
    val categoryLoading = MutableLiveData<Boolean>()
    private var wordList = ArrayList<Word>()
    private var rc = RemoteConfig()


    fun fromDataRemoteConfig(id: Int) {
        categoryError.value = false
        categoryLoading.value = false
        val categoryName = findCategoryName(id)
        fetchData(categoryName)
        println("fromDataremoteConfig")
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

    private fun fetchData(name: String) {
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
                    words.value = wordList

                } catch (e: Exception) {
                    println(e.message)
                }
            } else {
                println("veriler gelmedi")

            }
        }
    }
}