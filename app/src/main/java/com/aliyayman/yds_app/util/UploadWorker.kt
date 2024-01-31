package com.aliyayman.yds_app.util

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.aliyayman.yds_app.data.RemoteConfig
import com.aliyayman.yds_app.model.Word
import com.aliyayman.yds_app.service.myDatabase
import com.aliyayman.yds_app.viewmodel.WordViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.json.JSONArray
import java.lang.Exception
import kotlin.coroutines.CoroutineContext


class UploadWorker(val appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams), CoroutineScope {
    private var wordList = ArrayList<Word>()
    private var rc = RemoteConfig()
    private val job = Job()
    private val DATABASE_NAME = "allWords"


    override fun doWork(): Result {
        getFromRemoteConfig(DATABASE_NAME)
        return Result.success()
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
                }
            } else {
                println("NO Connected!")
            }
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
        get() = job + Dispatchers.Main

}



