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
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.aliyayman.yds_app.adapter.WordAdapter
import com.aliyayman.yds_app.databinding.FragmentFavoritesBinding
import com.aliyayman.yds_app.model.Word
import com.aliyayman.yds_app.util.Resourse
import com.aliyayman.yds_app.viewmodel.WordViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class FavoritesFragment : Fragment(), TextToSpeech.OnInitListener {
    private lateinit var binding: FragmentFavoritesBinding
    private var wordAdapter = WordAdapter(isFavorite = true)
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
          observeLiveData()
        }
        observeLiveData()
        observeRefresh()

        wordAdapter.onItemClicked ={word ->
            speakOut(word)
        }

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return  true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val word = wordAdapter.differ.currentList[position]
                viewModel.removeFavorite(word)
                Snackbar.make(view,"Successfully deleted ${word.ing}", Snackbar.LENGTH_SHORT).apply {
                    setAction("Undo"){
                        viewModel.addFavorite(word)
                    }
                    show()
                }
            }

        }
        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.recyclerViewWordFav)
        }
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
    private  fun observeLiveData(){
        viewModel.getFavoriteWords()
        viewModel.favoriteWords.observe(viewLifecycleOwner, Observer {
            when (it){
                is Resourse.Loading -> {
                    binding.recyclerViewWordFav.visibility = View.GONE
                    binding.errorWordTextview.visibility = View.GONE

                }
                is Resourse.Success -> {
                    binding.loadingWordProgressbar.visibility = View.GONE
                    binding.errorWordTextview.visibility = View.GONE
                    binding.recyclerViewWordFav.visibility = View.VISIBLE
                    wordAdapter.differ.submitList(it.data as List<Word>)
                    if (it.data.isNotEmpty()){
                        binding.emptyTextView.visibility = View.GONE
                    }
                }
                is Resourse.Error -> {
                    binding.loadingWordProgressbar.visibility = View.GONE
                    binding.recyclerViewWordFav.visibility = View.GONE
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
}