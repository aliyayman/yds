package com.aliyayman.yds_app.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.aliyayman.yds_app.R
import com.aliyayman.yds_app.databinding.ItemWordBinding
import com.aliyayman.yds_app.model.Word


class WordAdapter(
    var onItemClicked: ((word: String) -> Unit)? = null,
    var onItemFavClicked: ((word: Word) -> Unit)? = null,
    var isFavorite : Boolean
) : RecyclerView.Adapter<WordAdapter.WordViewHolder>() {

    class WordViewHolder(val view: ItemWordBinding) : RecyclerView.ViewHolder(view.root) {

    }

    private val diffCallback = object : DiffUtil.ItemCallback<Word>() {
        override fun areItemsTheSame(oldItem: Word, newItem: Word): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Word, newItem: Word): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view =
            DataBindingUtil.inflate<ItemWordBinding>(inflater, R.layout.item_word, parent, false)
        return WordViewHolder(view)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        holder.view.word = differ.currentList[position]
        holder.view.imageViewSound.setOnClickListener {
            onItemClicked?.invoke(differ.currentList[position].ing.toString())
        }

        if (!isFavorite){
            if (differ.currentList[position].isFavorite == true) {
                holder.view.imgIsFavorite.setImageResource(R.drawable.ic_fill_favorite)

            } else if (differ.currentList[position].isFavorite == false) {
                holder.view.imgIsFavorite.setImageResource(R.drawable.ic_favorite)
            }

            holder.view.imgIsFavorite.setOnClickListener{
                println(differ.currentList[position] )
                println(differ.currentList[position].id)
                onItemFavClicked?.invoke(differ.currentList[position])
            }
        }else{
            holder.view.imgIsFavorite.visibility= View.GONE
        }
    }
}