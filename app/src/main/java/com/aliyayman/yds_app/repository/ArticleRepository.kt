package com.aliyayman.yds_app.repository

import com.aliyayman.yds_app.service.ArticleDao
import javax.inject.Inject

class ArticleRepository @Inject constructor(
    private val articleDao: ArticleDao
) {
    suspend fun  getAllArticles() = articleDao.getAllArticles()

    suspend fun  getArticleFromId(id: Int)=articleDao.getArticleFromId(id)
}