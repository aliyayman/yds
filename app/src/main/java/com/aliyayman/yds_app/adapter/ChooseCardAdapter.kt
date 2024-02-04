package com.aliyayman.yds_app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.aliyayman.yds_app.R
import com.aliyayman.yds_app.model.Word

class ChooseCardAdapter(val list: ArrayList<Word>, val isIng: Boolean) :
    RecyclerView.Adapter<ChooseCardAdapter.ChooseCardViewHolder>() {
        private lateinit var clickedListener: onItemClickedListener


    class ChooseCardViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChooseCardViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view =
            inflater.inflate(R.layout.choose_card_item, parent, false)
        return ChooseCardViewHolder(view)


    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ChooseCardViewHolder, position: Int) {
        if (isIng) {
            holder.view.findViewById<TextView>(R.id.textViewWord).setText(list[position].ing)
        } else holder.view.findViewById<TextView>(R.id.textViewWord).setText(list[position].tc)

        holder.view.findViewById<CardView>(R.id.cardView).setOnClickListener {
            it.let {
                clickedListener.cardClicked(it, position, list)

            }
        }
    }

    interface onItemClickedListener : OnItemClickListener {
        fun cardClicked(view: View, position: Int, wordList: List<Word>)
    }

    fun setOnCardClickedListener(onItemClickedListener: onItemClickedListener) {
        clickedListener = onItemClickedListener


    }

}