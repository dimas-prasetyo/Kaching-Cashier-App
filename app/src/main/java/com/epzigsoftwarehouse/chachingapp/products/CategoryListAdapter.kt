package com.epzigsoftwarehouse.chachingapp.products

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.epzigsoftwarehouse.chachingapp.R
import kotlinx.android.synthetic.main.layout_category.view.*

class CategoryListAdapter (val context: Context?, val items: List<String>, val activeTab: String) : RecyclerView.Adapter<CategoryListAdapter.ViewHolder>() {
    var onItemClick: ((String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryListAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_category, parent, false))
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: CategoryListAdapter.ViewHolder, position: Int) {
        val item = items.get(position)

        holder.text_category.text = item
        if (item.equals(activeTab)){
            //holder.cv_kategory.backgroundTintList = ColorStateList.valueOf(R.color.primary_dark)
            holder.bg_category.setBackgroundColor(ContextCompat.getColor(context!!, R.color.primary_dark))
            holder.text_category.setTextColor(ContextCompat.getColor(context, R.color.white))
        }
        holder.text_category.setOnClickListener {
            onItemClick?.invoke(items[position])
        }

    }

    override fun getItemCount(): Int {
        return items.size
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cv_kategory = view.cv_kategory
        val bg_category = view.bg_category
        val text_category = view.text_category
    }
}