package com.aliyayman.yds_app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aliyayman.yds_app.R
import com.aliyayman.yds_app.model.Word

class WordAdapter(val wordList : ArrayList<Word>) :RecyclerView.Adapter<WordAdapter.WordViewHolder>() {

    class WordViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
       val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_word,parent,false)
        return WordViewHolder(view)
    }

    override fun getItemCount(): Int {
       return wordList.size
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
      holder.view.findViewById<TextView>(R.id.textViewTc).text = wordList[position].tc
      holder.view.findViewById<TextView>(R.id.textViewIng).text = wordList[position].ing
    }

    fun updateWordList(newWordList: List<Word>){
        wordList.clear()
        wordList.addAll(newWordList)
        notifyDataSetChanged()
    }

}