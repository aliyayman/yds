package com.aliyayman.yds_app.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.aliyayman.yds_app.data.RemoteConfig
import com.aliyayman.yds_app.model.Category
import com.aliyayman.yds_app.service.myDatabase
import com.aliyayman.yds_app.util.CustomSharedPreferences
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
            getDataFromSQLite()
        } else {
            getFromRemoteConfig()
        }
    }

    private fun getDataFromSQLite() {
        launch {
            val categories = myDatabase(getApplication()).categoryDao().getAllCategories()
            showData(categories)
            println("sql:")
            println(categories.toString())
        }
    }

    private fun storeInSQLite(list: List<Category>) {
        launch {
            val dao = myDatabase(getApplication()).categoryDao()
            dao.deleteAllCategory()
            dao.insertAll(*list.toTypedArray())
            val categorList = dao.getAllCategories()
            showData(categoryList)
            println("sqliete")
            println(categorList.toString())
        }

        customSharedPreferences.saveTime(System.nanoTime())


    }

    private fun showData(list: List<Category>) {
        categories.value = list
        categoryError.value = false
        categoryLoading.value = false
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
                    storeInSQLite(categoryList)
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