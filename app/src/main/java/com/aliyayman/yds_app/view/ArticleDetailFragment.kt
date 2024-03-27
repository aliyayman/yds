package com.aliyayman.yds_app.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.aliyayman.yds_app.databinding.FragmentArticleDetailBinding
import com.aliyayman.yds_app.model.Article
import com.aliyayman.yds_app.viewmodel.ArticleViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

@AndroidEntryPoint
class ArticleDetailFragment : Fragment(),CoroutineScope {
    private lateinit var binding: FragmentArticleDetailBinding
    val args: ArticleDetailFragmentArgs by navArgs<ArticleDetailFragmentArgs>()
    private var articleId = 0
    private  lateinit var  viewModel : ArticleViewModel
    private lateinit var article : Article

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentArticleDetailBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(ArticleViewModel::class.java)

        arguments?.let {
            articleId = args.articleId
            print("article:"+articleId)
        }

        viewModel.launch() {
            article = viewModel.getArticleFromId(articleId)
            observeLiveData()
           // article = myDatabase.invoke(requireContext().applicationContext).articleDao().getArticleFromId(articleId)
           // binding.articleTitle.text = article.name
           // binding.articleBody.text = article.text
        }
    }

    private  fun observeLiveData() {
        viewModel.myArticle.observe(viewLifecycleOwner, Observer { articles ->
            articles?.let {
                binding.articleTitle.text = articles.name
                binding.articleBody.text = articles.text
            }
        })

    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

}