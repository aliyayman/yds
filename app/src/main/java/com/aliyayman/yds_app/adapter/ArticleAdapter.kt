package com.aliyayman.yds_app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.aliyayman.yds_app.R
import com.aliyayman.yds_app.model.Article
import com.aliyayman.yds_app.view.ArticleFragmentDirections
import com.aliyayman.yds_app.view.CategoryFragmentDirections

class ArticleAdapter(val articleList: ArrayList<Article>): RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>() {
    class ArticleViewHolder(val view: View): RecyclerView.ViewHolder(view){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_category,parent,false)
        return ArticleViewHolder(view)
    }

    override fun getItemCount(): Int {
        return articleList.size
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.view.findViewById<TextView>(R.id.categoryTextView).text = articleList[position].name
        holder.view.findViewById<CardView>(R.id.category_card).setOnClickListener{
            val action = ArticleFragmentDirections.actionArticleFragmentToArticleDetailFragment(articleList[position].id)
            it.findNavController().navigate(action)
        }
    }

    fun updateArticleList(newArticleList: List<Article>){
        articleList.clear()
        articleList.addAll(newArticleList)
        notifyDataSetChanged()
    }
}