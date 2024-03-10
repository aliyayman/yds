package com.aliyayman.yds_app.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.aliyayman.yds_app.model.Article
import com.aliyayman.yds_app.repository.ArticleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ArticleViewModel @Inject constructor(
    application: Application,
    private val repository: ArticleRepository
) : BaseViewModel(application) {
    private var articleList = ArrayList<Article>()
    private  var article = Article(1,"","")
    val articles = MutableLiveData<List<Article>>()
    val articleError = MutableLiveData<Boolean>()
    val articleLoading = MutableLiveData<Boolean>()


    suspend fun getAllArticle() : List<Article> {
       launch {
            articleList = repository.getAllArticles() as ArrayList<Article>
           showData(articleList)

        }

        return articleList
    }

     fun getArticleFromId(id:Int) : Article{
       viewModelScope.launch {
            article = repository.getArticleFromId(id)
        }

        return article
    }

    private fun showData(list: List<Article>) {
        articles.postValue(list)
        articleError.value = false
        articleLoading.value = false
    }
}