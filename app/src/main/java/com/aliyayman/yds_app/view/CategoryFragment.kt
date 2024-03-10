package com.aliyayman.yds_app.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.aliyayman.yds_app.adapter.CategoryAdapter
import com.aliyayman.yds_app.databinding.FragmentCategoryBinding
import com.aliyayman.yds_app.viewmodel.CategoryViewModel
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CategoryFragment : Fragment() {
    private lateinit var binding: FragmentCategoryBinding
    private   var categoryAdapter = CategoryAdapter(arrayListOf())
    private  lateinit var  viewModel : CategoryViewModel
    private lateinit var mAdView : AdView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         binding = FragmentCategoryBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(CategoryViewModel::class.java)
        viewModel.resfreshCategory()
        binding.recyclerViewCategory.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewCategory.adapter = categoryAdapter

        binding.testButton.setOnClickListener {
            val action = CategoryFragmentDirections.actionCategoryFragmentToTestragment(1)
            it.findNavController().navigate(action)
        }
        binding.matchButton.setOnClickListener {
            val action = CategoryFragmentDirections.actionCategoryFragmentToChooseFragment()
            it.findNavController().navigate(action)
        }

        binding.favoritesButton.setOnClickListener {
            val action = CategoryFragmentDirections.actionCategoryFragmentToFavoritesFragment()
            it.findNavController().navigate(action)
        }

        binding.articleButton.setOnClickListener {
            val action = CategoryFragmentDirections.actionCategoryFragmentToArticleFragment()
            it.findNavController().navigate(action)
        }


        observeLiveData()

        MobileAds.initialize(requireContext()) {}
        mAdView = binding.adViewCategory
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

    }

  private  fun observeLiveData(){
        viewModel.categories.observe(viewLifecycleOwner, Observer {categories->
            categories?.let {
                binding.recyclerViewCategory.visibility = View.VISIBLE
                categoryAdapter.updateCategoryList(categories)

            }
        })
        viewModel.categoryError.observe(viewLifecycleOwner, Observer {error->
            error?.let {
                if (it){
                    binding.errorTextview.visibility = View.VISIBLE
                    binding.recyclerViewCategory.visibility = View.GONE
                }else {
                    binding.errorTextview.visibility = View.GONE
                }
            }

        })
        viewModel.categoryLoading.observe(viewLifecycleOwner, Observer {looding->
            looding?.let {
                if(it){
                    binding.loadingProgressbar.visibility = View.VISIBLE
                    binding.recyclerViewCategory.visibility = View.GONE
                    binding.errorTextview.visibility = View.GONE
                }else{
                    binding.loadingProgressbar.visibility = View.GONE
                }
            }
        })
    }
}