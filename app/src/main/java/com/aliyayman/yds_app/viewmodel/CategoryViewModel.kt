package com.aliyayman.yds_app.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aliyayman.yds_app.model.Category

class CategoryViewModel : ViewModel() {
    val categories = MutableLiveData<List<Category>>()
    val categoryError = MutableLiveData<Boolean>()
    val categoryLoading = MutableLiveData<Boolean>()

    fun resfreshData(){

        val category1 = Category(1,"Adjectives")
        val category2 = Category(2,"Adverbs")
        val category3 = Category(3,"Nouns")
        val category4 = Category(4,"Verbs")
        val category5 = Category(5,"Phrasal Verbs")
        val category6 = Category(6,"Favorites")

        val categoryList = arrayListOf<Category>(category1,category2,category3,category4,category5,category6)
        categories.value = categoryList
        categoryError.value = false
        categoryLoading.value = false

    }
}