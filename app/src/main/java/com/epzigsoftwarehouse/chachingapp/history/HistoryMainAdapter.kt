package com.epzigsoftwarehouse.chachingapp.history

import android.content.Context
import android.content.SharedPreferences
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
    lateinit var temp_setting: SharedPreferences

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryMainAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_main_history_list, parent, false))
    }

    override fun onBindViewHolder(holder: HistoryMainAdapter.ViewHolder, position: Int) {
        temp_setting = context?.getSharedPreferences("setting_info", Context.MODE_PRIVATE)!!
        val usedCurrency = temp_setting.getString("currency", "").toString()
        val item = items.get(position)

        if (usedCurrency.equals("dollar")){
            holder.txt_symbol_currency.text = "$"
        } else if (usedCurrency.equals("rupiah")){
            holder.txt_symbol_currency.text = "Rp."
        }
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
                        totalPrice= Math.round(totalPrice * 100.0) / 100.0
                        totalProduct = totalProduct + 1
                    }
                }
                holder.txt_product.text = totalProduct.toString() + " product"
                //holder.txt_time.text = timesFormated
                holder.txt_time.text = item.time
                holder.txt_total_price.text = totalPrice.toString()
            }

            /*println("ukuran: " + items.size)
            println("item: " + items)*/
            if (item.date.equals(items[position - 1].date)){
                var tempList: ArrayList<String> = ArrayList()
                for (i in 0..position-1){
                    if (item.date.equals(items[i].date)){
                        tempList.add(items[i].transaction_id)
                    }
                }
                var categoryList = tempList.distinct()
                var textNumber = categoryList.size + 1
                holder.txt_number.text = textNumber.toString()
                /*if (item.time.equals(items[position - 1].time)){

                } else {
                    var tempList: ArrayList<String> = ArrayList()
                    for (i in 0..position-1){
                        if (item.date.equals(items[i].date)){
                            tempList.add(items[i].transaction_id)
                        }
                    }
                    var categoryList = tempList.distinct()
                    var textNumber = categoryList.size + 1
                    holder.txt_number.text = textNumber.toString()
                }*/
            } else {
                holder.txt_number.text = "1"
            }
            /*var textNumber = 2
            for (i in 0..position-1) {
                println("item ini: " + item.time + ", produk: " + item.product_name)
                println("item sebelumnya: " + items[position - 1].time + ", produk: " + items[position - 1].product_name)
                if (item.date.equals(items[position - 1].date)){
                    if (item.time.equals(items[position - 1].time)){

                    } else {
                        holder.txt_number.text = textNumber.toString()
                        println("urutan: " + textNumber)
                        textNumber = textNumber + 1
                    }
                } else {
                    holder.txt_number.text = "1"
                }
            }*/
        } else {
            holder.txt_transaction_date.text = dateFormated

            var totalPrice = 0.00
            var totalProduct = 0
            for (i in 0..items.size-1){
                if (item.transaction_id.equals(items[i].transaction_id)){
                    totalPrice = totalPrice + items[i].price * items[i].amount
                    totalPrice= Math.round(totalPrice * 100.0) / 100.0
                    totalProduct = totalProduct + 1
                }
            }
            holder.txt_product.text = totalProduct.toString() + " product"
            //holder.txt_time.text = timesFormated
            holder.txt_total_price.text = totalPrice.toString()
            holder.txt_time.text = item.time
            holder.txt_number.text = "1"
        }
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
        val txt_symbol_currency = view.txt_symbol_currency
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