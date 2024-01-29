package com.aliyayman.yds_app.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.aliyayman.yds_app.R
import com.aliyayman.yds_app.databinding.ItemWordBinding
import com.aliyayman.yds_app.model.Word

class WordAdapter(val wordList : ArrayList<Word>) :RecyclerView.Adapter<WordAdapter.WordViewHolder>() {

    class WordViewHolder(val view: ItemWordBinding) : RecyclerView.ViewHolder(view.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
       val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<ItemWordBinding>(inflater,R.layout.item_word,parent,false)
        return WordViewHolder(view)
    }

    override fun getItemCount(): Int {
       return wordList.size
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        holder.view.word = wordList[position]
    }

    fun updateWordList(newWordList: List<Word>){
        wordList.clear()
        wordList.addAll(newWordList)
        notifyDataSetChanged()
    }

}