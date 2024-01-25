package com.aliyayman.yds_app.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.aliyayman.yds_app.adapter.WordAdapter
import com.aliyayman.yds_app.databinding.FragmentWordsBinding
import com.aliyayman.yds_app.viewmodel.WordViewModel

class WordsFragment : Fragment() {
    private lateinit var binding: FragmentWordsBinding
    private   var wordAdapter = WordAdapter(arrayListOf())
    private lateinit var viewModel: WordViewModel
    val args: WordsFragmentArgs by navArgs<WordsFragmentArgs>()
    private  var categoryId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWordsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(WordViewModel::class.java)
        viewModel.refreshData()
        binding.recyclerViewWord.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewWord.adapter = wordAdapter
        observeLiveData()

        arguments?.let {
           categoryId = args.categoryId
        }


    }

  private  fun observeLiveData(){
      viewModel.words.observe(viewLifecycleOwner, Observer {words->
          words?.let {
              binding.recyclerViewWord.visibility = View.VISIBLE
              wordAdapter.updateWordList(words)
          }

      })
  }


}