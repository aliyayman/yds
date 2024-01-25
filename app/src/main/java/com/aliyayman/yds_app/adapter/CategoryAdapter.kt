package com.aliyayman.yds_app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.aliyayman.yds_app.R
import com.aliyayman.yds_app.model.Category
import com.aliyayman.yds_app.view.CategoryFragmentDirections

class CategoryAdapter(val categoryList: ArrayList<Category>): RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {
    class CategoryViewHolder(val view: View): RecyclerView.ViewHolder(view){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_category,parent,false)
        return CategoryViewHolder(view)



    }

    override fun getItemCount(): Int {
       return categoryList.size

    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.view.findViewById<TextView>(R.id.categoryTextView).text = categoryList[position].name
        holder.view.findViewById<CardView>(R.id.category_card).setOnClickListener{
            val action = CategoryFragmentDirections.actionCategoryFragmentToWordsFragment(categoryList[position].categoryId)
            println(categoryList[position].categoryId.toString() +"aaaa")
            it.findNavController().navigate(action)
        }

    }

    fun updateCategoryList(newCategoryList: List<Category>){
        categoryList.clear()
        categoryList.addAll(newCategoryList)
        notifyDataSetChanged()

    }
}