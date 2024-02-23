package com.aliyayman.yds_app.repository

import com.aliyayman.yds_app.service.CategoryDao
import javax.inject.Inject

class CategoryRepository @Inject constructor(
    private val categoryDao: CategoryDao
) {
    suspend fun  getAllCategories() = categoryDao.getAllCategories()
}