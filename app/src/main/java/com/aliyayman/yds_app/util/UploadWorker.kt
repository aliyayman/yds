package com.aliyayman.yds_app.util

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.aliyayman.yds_app.data.RemoteConfig
import com.aliyayman.yds_app.model.Article
import com.aliyayman.yds_app.model.FirebaseWord
import com.aliyayman.yds_app.model.Word
import com.aliyayman.yds_app.service.myDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import java.lang.Exception
import kotlin.coroutines.CoroutineContext


class UploadWorker(val appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams), CoroutineScope {
    private var wordList = ArrayList<Word>()
    private var articleList = ArrayList<Article>()
    private var rc = RemoteConfig()


    override fun doWork(): Result {
        launch {
            getArticleFirebase()
            getDataFirebase()
        }
        println("do work çalıştı")
        return Result.success()
    }

    private fun getDataFirebase() {
        println("firebase work")
        val db = Firebase.firestore
        launch {
            val myList = myDatabase.invoke(appContext).wordDao().getAllWords()
            db.collection("words")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        val firebaseWord = document.toObject(FirebaseWord::class.java)
                        val word =
                            Word(firebaseWord.ing, firebaseWord.tc, firebaseWord.isFavorite, firebaseWord.categoryId)
                        wordList.add(word)
                    }
                    if (myList.isNullOrEmpty()){
                        storeInRoom(wordList)
                    }

                }
                .addOnFailureListener { exception ->
                    Log.w("TAG", "Error getting documents.", exception)
                }
        }
    }

    private fun getArticleFirebase() {
        println("firebase article work")
        val db = Firebase.firestore
        launch {
            db.collection("articles")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        val firebaseArticle = document.toObject(Article::class.java)
                        val article =
                            Article(firebaseArticle.id, firebaseArticle.name, firebaseArticle.text)
                        articleList.add(article)
                    }
                    storeInRoomArticle(articleList)
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
                        addFirebase(word)
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

    private fun getFromRemoteConfigArticle(name: String) {
        var remoteConfig = rc.getInitial()
        remoteConfig.fetchAndActivate().addOnCompleteListener {
            if (it.isSuccessful) {
                val json = remoteConfig.getString("$name")
                try {
                    val jsonArray = JSONArray(json)
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val id= jsonObject.getInt("id")
                        val name = jsonObject.getString("name")
                        val text = jsonObject.getString("text")

                        val article = Article(id,name, text)
                      //  addFirebaseArticle(article)
                        articleList.add(article)
                    }
                    println("remoteconfigden gelen artile:")
                    storeInRoomArticle(articleList)

                } catch (e: Exception) {
                    println(e.message)
                }
            } else {
                println("NO Connected!")
            }
        }
    }

    private fun addFirebase(getword: Word) {
        val db = Firebase.firestore
        val word = hashMapOf(
            "ing" to getword.ing,
            "tc" to getword.tc,
            "isFavorite" to getword.isFavorite,
            "categoryId" to getword.categoryId
        )

        db.collection("words")
            .add(word)
            .addOnSuccessListener { documentReference ->
                Log.d("TAG", "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("TAG", "Error adding document", e)
            }
    }
    private fun addFirebaseArticle(getword: Article) {
        val db = Firebase.firestore
        val word = hashMapOf(
            "id" to getword.id,
            "name" to getword.name,
            "text" to getword.text,


        )

        db.collection("articles")
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
            println("added words in room")
        }
    }

    private fun storeInRoomArticle(list: List<Article>) {
        launch {
            val dao = myDatabase(appContext).articleDao()
            val listLong = dao.insertAll(*list.toTypedArray())
            var i = 0
            while (i < list.size) {
                list[i].id = listLong[i].toInt()
                i += 1
            }
            println("added article in room")
        }
    }
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

}



