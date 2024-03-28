package com.aliyayman.yds_app.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.aliyayman.yds_app.adapter.ArticleAdapter
import com.aliyayman.yds_app.databinding.FragmentArticleBinding
import com.aliyayman.yds_app.model.Article
import com.aliyayman.yds_app.util.Resourse
import com.aliyayman.yds_app.viewmodel.ArticleViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

@AndroidEntryPoint
class ArticleFragment : Fragment(),CoroutineScope {
    private lateinit var binding: FragmentArticleBinding
    private   var articleAdapter = ArticleAdapter(arrayListOf())
    private  lateinit var  viewModel : ArticleViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentArticleBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(ArticleViewModel::class.java)

        binding.recyclerViewArticle.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewArticle.adapter = articleAdapter

        viewModel.getAllArticle()
        observeLiveData()

    }

    private  fun observeLiveData(){
        viewModel.articles.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resourse.Loading -> {
                    binding.recyclerViewArticle.visibility = View.GONE
                    binding.errorTextview.visibility = View.GONE
                    Log.e("Article", "LOADING")
                }
                is Resourse.Success -> {
                    binding.recyclerViewArticle.visibility = View.VISIBLE
                    binding.errorTextview.visibility = View.GONE
                    binding.loadingProgressbar.visibility = View.GONE
                    articleAdapter.updateArticleList(it.data as List<Article>)
                    Log.e("Article", "Succes")


                }
                is Resourse.Error -> {
                    binding.errorTextview.visibility = View.VISIBLE
                    binding.recyclerViewArticle.visibility = View.GONE
                    binding.loadingProgressbar.visibility = View.GONE
                    Log.e("Article", "Error")

                }
            }
        })
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

}