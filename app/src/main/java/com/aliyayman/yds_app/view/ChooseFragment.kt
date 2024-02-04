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
import com.aliyayman.yds_app.viewmodel.ChooseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext


class ChooseFragment : Fragment(),CoroutineScope,ChooseCardAdapter.onItemClickedListener{
    private lateinit var binding: FragmentChooseBinding
    private lateinit var viewModel: ChooseViewModel
    private  var mList =  ArrayList<String>()
    private lateinit var adapterIng : ChooseCardAdapter
    private lateinit var adapterTc : ChooseCardAdapter


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
            val randomTcList = viewModel.getWords()
            val ingList = viewModel.wordList
            adapterIng = ChooseCardAdapter(ingList,true)
            adapterTc = ChooseCardAdapter(randomTcList,false)
            binding.recyclerViewTc.adapter = adapterTc
            binding.recyclerViewIng.adapter = adapterIng
            binding.recyclerViewTc.layoutManager = LinearLayoutManager(context)
            binding.recyclerViewIng.layoutManager = LinearLayoutManager(context)
        }
        }




    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    override fun cardClicked(view: View, position: Int, wordList: List<Word>) {

        println("cardClicked")
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        println("onItemClicked")
    }

    private fun checWord(name : String ){

    }


}