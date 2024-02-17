package com.aliyayman.yds_app.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.aliyayman.yds_app.data.RemoteConfig
import com.aliyayman.yds_app.model.Category
import com.aliyayman.yds_app.model.CategoryFire
import com.aliyayman.yds_app.service.myDatabase
import com.aliyayman.yds_app.util.CustomSharedPreferences
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import org.json.JSONArray

class CategoryViewModel(application: Application) : BaseViewModel(application) {
    val categories = MutableLiveData<List<Category>>()
    val categoryError = MutableLiveData<Boolean>()
    val categoryLoading = MutableLiveData<Boolean>()
    private var categoryList = ArrayList<Category>()
    private var rc = RemoteConfig()
    private var customSharedPreferences = CustomSharedPreferences(getApplication())
    private var refreshTime = 10 * 60 * 1000 * 1000 * 1000L
    fun resfreshCategory() {
        val updateTime = customSharedPreferences.getTime()
        if (updateTime != null && updateTime != 0L && System.nanoTime() - updateTime < refreshTime) {
            getDataFromRoom()
        } else {
            getFromRemoteConfig()
            // getFireStore()
        }
    }

    private fun getDataFromRoom() {
        launch {
            val categories = myDatabase(getApplication()).categoryDao().getAllCategories()
            showData(categories)
        }
    }

    private fun storeInRoom(list: List<Category>) {
        launch {
            val dao = myDatabase(getApplication()).categoryDao()
            dao.deleteAllCategory()
            dao.insertAll(*list.toTypedArray())
            showData(categoryList)
            println("room:")
            println(categoryList.size)
        }

        customSharedPreferences.saveTime(System.nanoTime())


    }

    private fun showData(list: List<Category>) {
        categories.value = list
        categoryError.value = false
        categoryLoading.value = false
    }

    private fun getFireStore() {
        println("firebase category work")
        val db = Firebase.firestore
        launch {
            db.collection("category")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        println(document.data)
                        val categoryFire = document.toObject(CategoryFire::class.java)
                        if (categoryFire.categoryId != null) {
                            val category = Category(categoryFire.categoryId, categoryFire.name)
                            categoryList.add(category)
                        }
                    }
                    println("firecategory: $categoryList")
                    storeInRoom(categoryList)
                }
                .addOnFailureListener { exception ->
                    Log.w("TAG", "Error getting documents.", exception)
                }
        }
    }

    private fun getFromRemoteConfig() {
        println("remote categori")
        categoryList.clear()
        var remoteConfig = rc.getInitial()
        remoteConfig.fetchAndActivate().addOnCompleteListener {
            if (it.isSuccessful) {
                val json = remoteConfig.getString("categories")
                try {
                    val jsonArray = JSONArray(json)
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val categoryId = jsonObject.getInt("categoryId")
                        val name = jsonObject.getString("name")

                        val category = Category(categoryId, name)
                        categoryList.add(category)
                    }
                    storeInRoom(categoryList)
                } catch (e: Exception) {
                    println(e.message)
                    categoryError.value = true
                    categoryLoading.value = false
                }
            } else {
                println("No data!")
            }
        }
    }
}