package com.aliyayman.yds_app.view

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.lifecycle.Observer
import com.aliyayman.yds_app.adapter.WordAdapter
import com.aliyayman.yds_app.databinding.FragmentFavoritesBinding
import com.aliyayman.yds_app.viewmodel.WordViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class FavoritesFragment : Fragment(), TextToSpeech.OnInitListener {
    private lateinit var binding: FragmentFavoritesBinding
    private var wordAdapter = WordAdapter()
    private lateinit var viewModel: WordViewModel
    private lateinit var tts: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoritesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(WordViewModel::class.java)
        viewModel.refreshFavoriteWord()
        binding.recyclerViewWordFav.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewWordFav.adapter = wordAdapter
        tts = TextToSpeech(requireContext(), this)

        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.recyclerViewWordFav.visibility = View.GONE
            binding.errorWordTextview.visibility = View.GONE
            binding.loadingWordProgressbar.visibility = View.VISIBLE
            binding.swipeRefreshLayout.isRefreshing = false
            observeRefresh()
        }
        observeLiveData()

        wordAdapter.onItemFavClicked = { word ->
            if (word.isFavorite == true) {
                viewModel.removeFavorite(word)
            }
        }
        wordAdapter.onItemClicked ={word ->
            speakOut(word)
        }
        observeRefresh()
    }


    private fun observeWordLiveData(){
        viewModel.refreshFavoriteWord()
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
    private fun observeLiveData() {
        viewModel.getFavoriteWords()
        viewModel.favoriteWords.observe(viewLifecycleOwner, Observer { words ->
            words?.let {
                binding.recyclerViewWordFav.visibility = View.VISIBLE
                wordAdapter.differ.submitList(words)
            }
        })
        viewModel.wordError.observe(viewLifecycleOwner, Observer { error ->
            error?.let {
                if (it) {
                    binding.errorWordTextview.visibility = View.VISIBLE
                    binding.loadingWordProgressbar.visibility = View.GONE
                } else {
                    binding.errorWordTextview.visibility = View.GONE
                }
            }
        })
        viewModel.wordLoading.observe(viewLifecycleOwner, Observer { looding ->
            looding?.let {
                if (it) {
                    binding.loadingWordProgressbar.visibility = View.VISIBLE
                    binding.errorWordTextview.visibility = View.GONE
                    binding.recyclerViewWordFav.visibility = View.GONE
                } else {
                    binding.loadingWordProgressbar.visibility = View.GONE
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
}