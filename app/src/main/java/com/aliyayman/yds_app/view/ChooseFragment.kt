package com.aliyayman.yds_app.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.aliyayman.yds_app.adapter.ChooseCardAdapter
import com.aliyayman.yds_app.databinding.FragmentChooseBinding
import com.aliyayman.yds_app.model.Word
import com.aliyayman.yds_app.model.WordRecyler
import com.aliyayman.yds_app.viewmodel.ChooseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext


class ChooseFragment : Fragment(), CoroutineScope {
    private lateinit var binding: FragmentChooseBinding
    private lateinit var viewModel: ChooseViewModel
    private var mList = ArrayList<String>()
    private lateinit var adapterIng: ChooseCardAdapter
    private lateinit var adapterTc: ChooseCardAdapter
    private var selectedIdIng = 0
    private var selectedIdTc = 0
    private lateinit var ingList:ArrayList<WordRecyler>
    private lateinit var randomTcList:ArrayList<WordRecyler>

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
             randomTcList = viewModel.getWords()
             ingList = viewModel.wordList
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

            binding.recyclerViewTc.isActivated=false

            adapterIng.onItemClickedToIng = {
                selectedIdIng = it.id
                binding.recyclerViewTc.isActivated=true
                println(it.id)
            }
            adapterTc.onItemClickedToTc = {
                selectedIdTc = it.id
                binding.recyclerViewIng.isActivated=false
                check(it)
                println(it.id)
            }
        }
    }

    private fun check(wordRecyler: WordRecyler){
        if (selectedIdIng==selectedIdTc){
            wordRecyler.isVisibility=false
            adapterIng.submitList(ingList)
            adapterTc.submitList(randomTcList)
        }
        binding.recyclerViewTc.isActivated=true
        binding.recyclerViewTc.isActivated=false
    }


    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main


    private fun checWord(name: String) {

    }


}