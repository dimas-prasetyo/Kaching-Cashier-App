package com.epzigsoftwarehouse.kaching.products

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.epzigsoftwarehouse.kaching.R
import kotlinx.android.synthetic.main.layout_list_payment.view.*

class ProductSelectedAdapter (val context: Context?, val items: ArrayList<Product>) : RecyclerView.Adapter<ProductSelectedAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_list_payment, parent, false))
    }

    override fun onBindViewHolder(holder: ProductSelectedAdapter.ViewHolder, position: Int) {
        val item = items.get(position)

        holder.txt_selected_name.text = item.name
        holder.txt_selected_quantity.text = item.chose_amount.toString()
        holder.txt_selected_price.text = item.price.toString()

        var totalPrice = item.chose_amount * item.price
        holder.txt_selected_total.text = totalPrice.toString()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txt_selected_name = view.txt_selected_name
        val txt_selected_quantity = view.txt_selected_quantity
        val txt_selected_price = view.txt_selected_price
        val txt_selected_total = view.txt_selected_total
    }
}