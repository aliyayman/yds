package com.aliyayman.yds_app.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aliyayman.yds_app.R
import com.aliyayman.yds_app.model.Word
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import org.json.JSONArray
import java.lang.Exception


class WordViewModel : ViewModel() {
   private  var  wordList =  ArrayList<Word>()
    val words = MutableLiveData<List<Word>>()
    private lateinit var remoteConfig: FirebaseRemoteConfig


    fun refreshData() {
        remoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(R.xml.firebase_remote_config)
        fetchData("verbs")


    }



    private fun fetchData(name : String) {
        remoteConfig.fetchAndActivate().addOnCompleteListener {
            if (it.isSuccessful) {
                val verbs_json = remoteConfig.getString("verbs")
                println(verbs_json)
                try {
                    val jsonArray = JSONArray(verbs_json)
                    for (i in 0 until jsonArray.length()){
                        val jsonObject = jsonArray.getJSONObject(i)
                        val ing = jsonObject.getString("ing")
                        val tc = jsonObject.getString("tc")
                        val uniteId = jsonObject.getInt("unite_id")
                        val isFavorite = jsonObject.getBoolean("isFavorite")

                        val word = Word(ing,tc,isFavorite,uniteId)
                        println(jsonArray.length().toString())
                        wordList.add(word)
                        println("******")
                        println(wordList.size.toString())
                    }
                    words.value = wordList

                }catch (e: Exception){
                    println(e.message)
                }
            } else {
                println("veriler gelmedi")

            }
        }
    }
}