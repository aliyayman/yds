package com.aliyayman.yds_app.view

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
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
import com.aliyayman.yds_app.model.Word
import com.aliyayman.yds_app.util.Resourse
import com.aliyayman.yds_app.viewmodel.WordViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.util.Locale
import kotlin.coroutines.CoroutineContext
@AndroidEntryPoint
class WordsFragment : Fragment(), TextToSpeech.OnInitListener, CoroutineScope {
    private lateinit var binding: FragmentWordsBinding
    private var wordAdapter = WordAdapter(isFavorite = false)
    private lateinit var viewModel: WordViewModel
    val args: WordsFragmentArgs by navArgs<WordsFragmentArgs>()
    private var categoryId = 0
    private lateinit var tts: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWordsBinding.inflate(layoutInflater)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            categoryId = args.categoryId
        }
        tts = TextToSpeech(requireContext(), this)
        viewModel = ViewModelProvider(this).get(WordViewModel::class.java)

        binding.recyclerViewWord.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewWord.adapter = wordAdapter

        observeWordLiveData()
        observeRefresh()


        wordAdapter.onItemClicked = { word ->
            speakOut(word)
        }
        wordAdapter.onItemFavClicked = { word ->
            if (word.isFavorite == true) viewModel.removeFavorite(word)
            else viewModel.addFavorite(word)

            viewModel.refreshWord(categoryId)
        }
        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.recyclerViewWord.visibility = View.GONE
            binding.errorWordTextview.visibility = View.GONE
            binding.loadingWordProgressbar.visibility = View.VISIBLE
            binding.swipeRefreshLayout.isRefreshing = false
            viewModel.refreshWord(categoryId)
        }
        observeLiveData()
    }

    private fun observeWordLiveData(){
        viewModel.refreshWord(categoryId)
         observeLiveData()
    }

    private fun observeRefresh(){
        viewModel.isRefresh.observe(viewLifecycleOwner){
            observeWordLiveData()
        }
    }

    private fun speakOut(name: String) {
        tts.speak(name, TextToSpeech.QUEUE_FLUSH, null, "")
    }

    private  fun observeLiveData(){
        viewModel.words.observe(viewLifecycleOwner, Observer {
            when (it){
                is Resourse.Loading -> {
                    binding.recyclerViewWord.visibility = View.GONE
                    binding.errorWordTextview.visibility = View.GONE

                }
                is Resourse.Success -> {
                    binding.loadingWordProgressbar.visibility = View.GONE
                    binding.errorWordTextview.visibility = View.GONE
                    binding.recyclerViewWord.visibility = View.VISIBLE
                    wordAdapter.differ.submitList(it.data as List<Word>)
                }
                is Resourse.Error -> {
                    binding.loadingWordProgressbar.visibility = View.GONE
                    binding.recyclerViewWord.visibility = View.GONE
                    binding.errorWordTextview.visibility = View.VISIBLE
                }
            }
        })
    }
    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts!!.setLanguage(Locale.US)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "The Language not supported!")
            } else {
                Log.e("TTS", "The Language not supported!")
            }
        }
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main
}