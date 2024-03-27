package com.aliyayman.yds_app.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.aliyayman.yds_app.model.Article
import com.aliyayman.yds_app.repository.ArticleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ArticleViewModel @Inject constructor(
    application: Application,
    private val repository: ArticleRepository
) : BaseViewModel(application) {
    private var articleList = ArrayList<Article>()
    private var article  = Article(0,"","No data!")

    val myArticle = MutableLiveData<Article>()
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

   suspend  fun getArticleFromId(id:Int) : Article{

       viewModelScope.async {
           //article = myDatabase.invoke(getApplication()).articleDao().getArticleFromId(id)
           article = repository.getArticleFromId(id)
           showArticle(article)
       }
       return article
   }


    private fun showData(list: List<Article>) {
        articles.postValue(list)
        articleError.value = false
        articleLoading.value = false
    }

    private fun showArticle(article: Article) {
        myArticle.postValue(article)
        articleError.value = true
        articleLoading.value = true
    }
}