package com.aliyayman.yds_app.view

import android.os.Bundle
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

class FavoritesFragment : Fragment() {
    private lateinit var binding: FragmentFavoritesBinding
    private var wordAdapter = WordAdapter()
    private lateinit var viewModel: WordViewModel

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

        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.recyclerViewWordFav.visibility = View.GONE
            binding.errorWordTextview.visibility = View.GONE
            binding.loadingWordProgressbar.visibility = View.VISIBLE
            binding.swipeRefreshLayout.isRefreshing = false
            viewModel.refreshFavoriteWord()
        }

        wordAdapter.onItemFavClicked = { word ->
            if (word.isFavorite == true!!) {
                viewModel.removeFavorite(word)
                viewModel.refreshFavoriteWord()
                viewModel.words.observe(viewLifecycleOwner, Observer {
                    wordAdapter.differ.submitList(it)
                    println("***" + wordAdapter.differ.currentList)
                })
            } else
                viewModel.addFavorite(word)
        }

        observeLiveData()
    }

    private fun observeLiveData() {
        viewModel.words.observe(viewLifecycleOwner, Observer { words ->
            words?.let {
                binding.recyclerViewWordFav.visibility = View.VISIBLE
                //  wordAdapter.updateWordList(words)
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

}