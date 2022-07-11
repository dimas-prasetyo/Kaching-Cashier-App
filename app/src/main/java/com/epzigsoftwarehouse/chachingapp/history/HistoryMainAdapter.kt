package com.epzigsoftwarehouse.chachingapp.history

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.epzigsoftwarehouse.chachingapp.R
import kotlinx.android.synthetic.main.layout_main_history_list.view.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HistoryMainAdapter (val context: Context?, val items: List<History>) : RecyclerView.Adapter<HistoryMainAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryMainAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_main_history_list, parent, false))
    }

    override fun onBindViewHolder(holder: HistoryMainAdapter.ViewHolder, position: Int) {
        val item = items.get(position)

        val dateFormated = formatDate(item.date, "EEE, dd MMMM yyyy")
        val timesFormated = formatTime(item.time, "HH:mm:ss")

        if (position > 0){
            if (item.date.equals(items[position-1].date)){
                holder.txt_transaction_date.visibility = View.GONE
            } else {
                holder.txt_transaction_date.text = dateFormated
            }

            if (item.transaction_id.equals(items[position - 1].transaction_id)){
                holder.cv_history.visibility = View.GONE
            } else {
                var totalPrice = 0.00
                var totalProduct = 0
                for (i in 0..items.size-1) {
                    if (item.transaction_id.equals(items[i].transaction_id)) {
                        totalPrice = totalPrice + items[i].price * items[i].amount
                        totalProduct = totalProduct + 1
                    }
                }
                holder.txt_product.text = totalProduct.toString() + " product"
                holder.txt_time.text = timesFormated
                holder.txt_total_price.text = totalPrice.toString()
            }
        } else {
            holder.txt_transaction_date.text = dateFormated

            var totalPrice = 0.00
            var totalProduct = 0
            for (i in 0..items.size-1){
                if (item.transaction_id.equals(items[i].transaction_id)){
                    totalPrice = totalPrice + items[i].price * items[i].amount
                    totalProduct = totalProduct + 1
                }
            }
            holder.txt_product.text = totalProduct.toString() + " product"
            holder.txt_time.text = timesFormated
            holder.txt_total_price.text = totalPrice.toString()
            /*if (item.transaction_id.equals(items[position + 1].transaction_id)){
                println("Ya ada duplicate")
                holder.cv_history.visibility = View.GONE
            } else {
                println("tidak ada duplicate")
                var totalPrice = 0.00
                var totalProduct = 0
                for (i in 0..items.size-1){
                    if (item.transaction_id.equals(items[i].transaction_id)){
                        totalPrice = totalPrice + items[i].price
                        totalProduct = totalProduct + 1
                    }
                }
                holder.txt_product.text = totalProduct.toString() + " product"
                holder.txt_time.text = timesFormated
                holder.txt_total_price.text = totalPrice.toString()
            }*/
        }




        /*if (position < items.size-1){
            if (item.date.equals(items[position + 1].date)){
                holder.txt_transaction_date.visibility = View.GONE
            } else {
                holder.txt_transaction_date.text = dateFormated
            }

            if (item.transaction_id.equals(items[position + 1].transaction_id)){
                holder.cv_history.visibility = View.GONE
            } else {
                *//*var totalPrice = 0.00
                var totalProduct = 0
                for (i in 0..items.size-1){
                    if (item.transaction_id.equals(items[i].transaction_id)){
                        totalPrice = totalPrice + items[i].price
                        totalProduct = totalProduct + 1
                    }
                }*//*
                holder.txt_product.text = item.transaction_id
                holder.txt_time.text = timesFormated
                holder.txt_total_price.text = item.price.toString()
            }
        }*/
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txt_transaction_date = view.txt_transaction_date
        val cv_history = view.cv_history
        val txt_number = view.txt_number
        val txt_time = view.txt_time
        val txt_product = view.txt_product
        val txt_total_price = view.txt_total_price
        //val rv_history_list = view.rv_history_list
    }

    private fun formatTime(date: String, format: String): String {
        var formatedTimes = ""
        val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        sdf.timeZone = TimeZone.getTimeZone("UTC")

        try {
            val parseDate = sdf.parse(date)
            formatedTimes = SimpleDateFormat(format).format(parseDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return formatedTimes
    }

    private fun formatDate(date: String, format: String): String {
        var formatedDate = ""
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        sdf.timeZone = TimeZone.getTimeZone("UTC")

        try {
            val parseDate = sdf.parse(date)
            formatedDate = SimpleDateFormat(format).format(parseDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return formatedDate
    }
}