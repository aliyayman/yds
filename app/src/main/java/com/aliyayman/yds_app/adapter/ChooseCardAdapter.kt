package com.aliyayman.yds_app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aliyayman.yds_app.databinding.ChooseCardItemBinding
import com.aliyayman.yds_app.model.WordRecyler

class ChooseCardAdapter(
    val isIng: Boolean,
    var onItemClickedToTc: ((word: WordRecyler) -> Unit)? = null,
    var onItemClickedToIng: ((word: WordRecyler) -> Unit)? = null,
) :
    RecyclerView.Adapter<ChooseCardAdapter.ChooseCardViewHolder>() {
    private var wordList=ArrayList<WordRecyler>()

    inner class ChooseCardViewHolder(val chooseCardItemBinding: ChooseCardItemBinding) :
        RecyclerView.ViewHolder(chooseCardItemBinding.root)

    fun submitList(list: ArrayList<WordRecyler>) {
        wordList = list
        notifyDataSetChanged()
    }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChooseCardViewHolder {
        return ChooseCardViewHolder(
            ChooseCardItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }
    override fun getItemCount(): Int {
        return wordList.size
    }

    override fun onBindViewHolder(holder: ChooseCardViewHolder, position: Int) {

        with(holder) {
            with(chooseCardItemBinding) {
                with(wordList[position]) {
                    if (isIng) textViewWord.text=this.ing
                    else textViewWord.text=this.tc

                    cardView.setOnClickListener{
                        if (isIng) onItemClickedToIng?.invoke(this)
                        else onItemClickedToTc?.invoke(this)
                    }

                    if (this.isVisibility) cardView.visibility=View.VISIBLE
                    else cardView.visibility=View.INVISIBLE
                }
            }
        }
    }
}

