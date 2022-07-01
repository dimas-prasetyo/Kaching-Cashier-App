package com.epzigsoftwarehouse.chachingapp.products

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.epzigsoftwarehouse.chachingapp.R
import kotlinx.android.synthetic.main.layout_menu.view.*
import java.io.File

class ProductListAdapter (val context: Context?, val items: ArrayList<Product>) : RecyclerView.Adapter<ProductListAdapter.ViewHolder>() {
    private var amount_value = 0
    var onItemClick: ((Product) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_menu, parent, false))
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items.get(position)

        item.chose_amount = holder.menu_input_amount.getText().toString().toInt()

        holder.menu_name.text = item.name
        holder.menu_proportions.text = item.proportion.toString() + " " + item.unit + " x " + item.amount.toString()
        holder.menu_price.text = item.price.toString()

        val imgFile = File(item.photo_path)
        if (imgFile.exists()) {
            val myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
            holder.image_menu.setImageBitmap(myBitmap)
        }

        holder.menu_add_item.setOnClickListener {
            item.chose_amount = holder.menu_input_amount.getText().toString().toInt()
            item.chose_amount  = item.chose_amount  + 1
            holder.menu_input_amount.setText(item.chose_amount .toString())

            onItemClick?.invoke(items[position])
        }

        holder.menu_minus_item.setOnClickListener {
            item.chose_amount = holder.menu_input_amount.getText().toString().toInt()
            if (item.chose_amount  > 0){
                item.chose_amount  = item.chose_amount  - 1
                holder.menu_input_amount.setText(item.chose_amount .toString())

                if (item.chose_amount == 0){
                    holder.layout_menu_list.setPadding(0, 0, 0, 0)
                    //holder.cv_layout_menu.backgroundTintList = ColorStateList.valueOf(R.color.primary_light)
                    holder.bg_layout_menu.setBackgroundColor(ContextCompat.getColor(context!!, R.color.primary_light))
                    holder.layout_amount.visibility = View.GONE

                    holder.menu_name.setTextColor(ContextCompat.getColor(context, R.color.primaryTextBlack))
                    holder.menu_proportions.setTextColor(ContextCompat.getColor(context, R.color.primaryTextBlack))
                    holder.menu_price.setTextColor(ContextCompat.getColor(context, R.color.black))

                    onItemClick?.invoke(items[position])
                }
            }
        }

        holder.area_click.setOnClickListener {
            if (holder.menu_input_amount.getText().toString().toInt() < 1){
                item.chose_amount = 1
            } else {
                item.chose_amount = holder.menu_input_amount.getText().toString().toInt()
            }

            holder.layout_menu_list.setPadding(7, 3, 7, 3)
            holder.menu_input_amount.setText(item.chose_amount .toString())
            holder.layout_amount.visibility = View.VISIBLE
            holder.bg_layout_menu.setBackgroundColor(ContextCompat.getColor(context!!, R.color.primary_dark))

            holder.menu_name.setTextColor(ContextCompat.getColor(context, R.color.white))
            holder.menu_proportions.setTextColor(ContextCompat.getColor(context, R.color.white))
            holder.menu_price.setTextColor(ContextCompat.getColor(context, R.color.white))

            onItemClick?.invoke(items[position])

        }

    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val layout_menu_list = view.layout_menu_list
        val cv_layout_menu = view.cv_layout_menu
        val bg_layout_menu = view.bg_layout_menu
        val area_click = view.area_click

        val menu_name = view.menu_name
        val image_menu = view.image_menu
        val menu_proportions = view.menu_proportions
        val menu_price = view.menu_price

        val layout_amount = view.layout_amount

        val menu_minus_item = view.menu_minus_item
        val menu_input_amount = view.menu_input_amount
        val menu_add_item = view.menu_add_item

        /*init {
            area_click.setOnClickListener {
                onItemClick?.invoke(items[adapterPosition])
                //menuGetSelected()
            }
        }*/
    }
}