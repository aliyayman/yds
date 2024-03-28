package com.aliyayman.yds_app.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.aliyayman.yds_app.model.Article
import com.aliyayman.yds_app.repository.ArticleRepository
import com.aliyayman.yds_app.util.Resourse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ArticleViewModel @Inject constructor(
    application: Application,
    private val repository: ArticleRepository
) : BaseViewModel(application) {

    val myArticle = MutableLiveData<Resourse<Article>>()
    val articles = MutableLiveData<Resourse<List<Article>>>()

    fun getAllArticle() {
        viewModelScope.launch {
            articles.postValue(Resourse.Loading())
            try {
                val articleList = repository.getAllArticles() as ArrayList<Article>
                articles.postValue(Resourse.Success(articleList))

            } catch (e: Exception) {
                articles.postValue(Resourse.Error(e.message.toString()))
            }
        }
    }

    fun getArticleFromId(id: Int) {
        viewModelScope.launch {
            myArticle.postValue(Resourse.Loading())
            try {
                val article = repository.getArticleFromId(id)
                myArticle.postValue(Resourse.Success(article))
            } catch (e: Exception) {
                myArticle.postValue(Resourse.Error(e.message.toString()))
            }
        }
    }
}