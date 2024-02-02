package com.aliyayman.yds_app.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.aliyayman.yds_app.databinding.FragmentChooseBinding
import com.aliyayman.yds_app.viewmodel.ChooseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext


class ChooseFragment : Fragment(),CoroutineScope {
    private lateinit var binding: FragmentChooseBinding
    private lateinit var viewModel: ChooseViewModel
    private  var mList =  ArrayList<String>()


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
           mList = viewModel.getWords()
            println("viewModel:")
            writeWords()
            println(viewModel.wordList)
        }

        binding.textViewIng1.setOnClickListener {
            checkMatch()

        }
        binding.textViewTc1.setOnClickListener {
            checkMatch()
        }
    }


    private fun checkMatch(){



    }

    private fun writeWords(){
        binding.textViewIng1.text = viewModel.wordList[0].ing
        binding.textViewIng2.text = viewModel.wordList[1].ing
        binding.textViewIng3.text = viewModel.wordList[2].ing
        binding.textViewIng4.text = viewModel.wordList[3].ing
        binding.textViewIng5.text = viewModel.wordList[4].ing
        binding.textViewTc1.text = mList[0]
        binding.textViewTc2.text = mList[1]
        binding.textViewTc3.text = mList[2]
        binding.textViewTc4.text = mList[3]
        binding.textViewTc5.text = mList[4]
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

}