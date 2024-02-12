package com.aliyayman.yds_app.util

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.aliyayman.yds_app.data.RemoteConfig
import com.aliyayman.yds_app.model.Word
import com.aliyayman.yds_app.service.myDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONArray
import java.lang.Exception
import kotlin.coroutines.CoroutineContext


class UploadWorker(val appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams), CoroutineScope {
    private var wordList = ArrayList<Word>()
    private var rc = RemoteConfig()
    private val DATABASE_NAME = "allWords"
    private var mList = ArrayList<Word>()


    override fun doWork(): Result {
        getFromRemoteConfig(DATABASE_NAME)
        getDataFirebase()
        println("do work çalıştı")
        return Result.success()
    }

    private fun getDataFirebase(){
        println("firebase work")
        val db = Firebase.firestore
        launch {
           db.collection("words_db")
                .get()
                .addOnSuccessListener {result->
                    for (document in result){
                    println(document.data)

                    }
                }
                .addOnFailureListener { exception ->
                    Log.w("TAG", "Error getting documents.", exception)
                }
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
                      //  addFirebase(word)
                        wordList.add(word)
                    }
                    println("remoteconfigden gelen word:")
                    println(wordList.size)
                    storeInRoom(wordList)

                } catch (e: Exception) {
                    println(e.message)
                }
            } else {
                println("NO Connected!")
            }
        }
    }
    private fun addFirebase(getword: Word){
        val db = Firebase.firestore
        val word = hashMapOf(
            "ing" to getword.ing,
            "tc" to getword.tc,
            "isFavorite" to getword.isFavorite,
            "categoryId" to getword.categoryId
        )

        db.collection("words_db")
            .add(word)
            .addOnSuccessListener { documentReference ->
                Log.d("TAG", "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("TAG", "Error adding document", e)
            }
    }
    private fun storeInRoom(list: List<Word>) {
        launch {
            val dao = myDatabase(appContext).wordDao()
            val listLong = dao.insertAll(*list.toTypedArray())
            var i = 0
            while (i < list.size) {
                list[i].id = listLong[i].toInt()
                i += 1
            }

            println("kelimeler sqlite eklendi")
        }
    }
    override val coroutineContext: CoroutineContext
        get() =  Dispatchers.IO

}



