package com.aliyayman.yds_app.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aliyayman.yds_app.data.RemoteConfig
import com.aliyayman.yds_app.model.Category
import org.json.JSONArray

class CategoryViewModel : ViewModel() {
    val categories = MutableLiveData<List<Category>>()
    val categoryError = MutableLiveData<Boolean>()
    val categoryLoading = MutableLiveData<Boolean>()
    private var categoryList = ArrayList<Category>()
    private var rc = RemoteConfig()

    fun resfreshData(){
        categoryError.value = false
        categoryLoading.value = false
        fetchData()

    }
   private fun fetchData(){
       categoryList.clear()
        var remoteConfig = rc.getInitial()
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener {
                if (it.isSuccessful){
                    val json = remoteConfig.getString("categories")
                    try {
                        val jsonArray = JSONArray(json)
                        for (i in 0 until jsonArray.length()) {
                            val jsonObject = jsonArray.getJSONObject(i)
                            val categoryId = jsonObject.getInt("categoryId")
                            val name = jsonObject.getString("name")

                            val category = Category(categoryId ,name)
                            categoryList.add(category)
                        }
                        categories.value = categoryList
                    }catch (e:Exception){
                        println(e.message)
                    }
                }else{
                    println("No data!")
                }
            }
    }

}