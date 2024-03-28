package com.aliyayman.yds_app.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.aliyayman.yds_app.databinding.FragmentArticleDetailBinding
import com.aliyayman.yds_app.util.Resourse
import com.aliyayman.yds_app.viewmodel.ArticleViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

@AndroidEntryPoint
class ArticleDetailFragment : Fragment(), CoroutineScope {
    private lateinit var binding: FragmentArticleDetailBinding
    val args: ArticleDetailFragmentArgs by navArgs<ArticleDetailFragmentArgs>()
    private var articleId = 0
    private lateinit var viewModel: ArticleViewModel

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
            viewModel.getArticleFromId(articleId)
        }
        observeLiveData()
    }

    private fun observeLiveData() {
        viewModel.myArticle.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resourse.Loading -> {
                    binding.lineerArticle.visibility = View.GONE
                    binding.errorArticle.visibility = View.GONE
                    Log.e("ArticleDetail", "LOADING")
                }

                is Resourse.Success -> {
                    binding.progressArticle.visibility = View.GONE
                    binding.errorArticle.visibility = View.GONE
                    binding.lineerArticle.visibility = View.VISIBLE
                    binding.articleTitle.text = it.data?.name
                    binding.articleBody.text = it.data?.text
                    Log.e("ArticleDetail", "SUCCESS")
                }

                is Resourse.Error -> {
                    binding.errorArticle.visibility = View.VISIBLE
                    binding.progressArticle.visibility = View.GONE
                    binding.lineerArticle.visibility = View.GONE
                    Log.e("ArticleDetail", "ERROR")
                }
            }
        })
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

}