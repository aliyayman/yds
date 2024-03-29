package com.aliyayman.yds_app.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.aliyayman.yds_app.adapter.ChooseCardAdapter
import com.aliyayman.yds_app.databinding.FragmentChooseBinding
import com.aliyayman.yds_app.model.WordRecyler
import com.aliyayman.yds_app.viewmodel.ChooseViewModel
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import com.google.android.gms.ads.AdRequest
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ChooseFragment : Fragment(), CoroutineScope {
    private lateinit var binding: FragmentChooseBinding
    private lateinit var viewModel: ChooseViewModel
    private lateinit var adapterIng: ChooseCardAdapter
    private lateinit var adapterTc: ChooseCardAdapter
    private var selectedIdIng = 0
    private var selectedIdTc = 0
    private lateinit var ingList: ArrayList<WordRecyler>
    private lateinit var randomTcList: ArrayList<WordRecyler>
    private var counter = 5
    private lateinit var mAdView : AdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentChooseBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(ChooseViewModel::class.java)

        launch {
            getAndCheckWords()

        }
        //ca-app-pub-5875015425896746/9884288321
        MobileAds.initialize(requireContext()) {}
        mAdView = binding.adViewChoose
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
    }

    private fun getAndCheckWords() {
        launch {
            randomTcList = viewModel.getWords()
            ingList = viewModel.wordList
            setupRecylerViewAndCheck(ingList,randomTcList)
        }
    }
    private fun setupRecylerViewAndCheck(ingList: ArrayList<WordRecyler> ,randomTcList: ArrayList<WordRecyler>){

        adapterIng = ChooseCardAdapter(true).apply {
            submitList(ingList)

        }
        adapterTc = ChooseCardAdapter(false).apply {
            submitList(randomTcList)
        }
        binding.recyclerViewTc.adapter = adapterTc
        binding.recyclerViewIng.adapter = adapterIng
        binding.recyclerViewTc.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewIng.layoutManager = LinearLayoutManager(context)

        adapterIng.onItemClickedToIng = {
            selectedIdIng = it.id
            check(it)
        }
        adapterTc.onItemClickedToTc = {
            selectedIdTc = it.id
            check(it)
        }
    }

    private fun check(wordRecyler: WordRecyler) {
        if (selectedIdIng == selectedIdTc) {
            wordRecyler.isVisibility = false
            adapterIng.submitList(ingList)
            adapterTc.submitList(randomTcList)
            counter--
        }
        if (counter == 0) {
            getAndCheckWords()
            counter = 5
        }
    }
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main
}

