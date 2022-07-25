package com.epzigsoftwarehouse.kaching.products

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.epzigsoftwarehouse.kaching.R
import kotlinx.android.synthetic.main.layout_menu.view.*
import java.io.File
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class ProductListAdapter (val context: Context?, val items: ArrayList<Product>) : RecyclerView.Adapter<ProductListAdapter.ViewHolder>() {
    lateinit var temp_setting: SharedPreferences
    var onItemClick: ((Product) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_menu, parent, false))
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        temp_setting = context?.getSharedPreferences("setting_info", Context.MODE_PRIVATE)!!
        val usedCurrency = temp_setting.getString("currency", "").toString()

        val item = items.get(position)

        if (item.chose_amount > 0){
            holder.layout_menu_list.setPadding(7, 3, 7, 3)
            holder.menu_input_amount.setText(item.chose_amount .toString())
            holder.layout_amount.visibility = View.VISIBLE
            holder.bg_layout_menu.setBackgroundColor(ContextCompat.getColor(context!!, R.color.primary_dark))

            holder.menu_name.setTextColor(ContextCompat.getColor(context, R.color.white))
            holder.menu_proportions.setTextColor(ContextCompat.getColor(context, R.color.white))
            holder.menu_price.setTextColor(ContextCompat.getColor(context, R.color.white))
            holder.menu_currency.setTextColor(ContextCompat.getColor(context, R.color.white))
        }

        holder.menu_name.text = item.name

        if (item.unit.equals("") || item.unit == null){
            holder.menu_proportions.visibility = View.GONE
        } else {
            holder.menu_proportions.text = item.proportion.toString() + " " + item.unit + " x " + item.amount.toString()
        }

        if (usedCurrency.equals("dollar")){
            holder.menu_currency.text = "$"
            holder.menu_price.text = item.price.toString()
        } else if (usedCurrency.equals("rupiah")){
            holder.menu_currency.text = "Rp."
            val tempPrice = item.price.toInt()

            var originalString = tempPrice.toString()
            val longval: Long

            val re = Regex("[^A-Za-z0-9 ]")
            originalString = re.replace(originalString, "")

            longval = originalString.toLong()

            val formatter = NumberFormat.getInstance(Locale.US) as DecimalFormat
            formatter.applyPattern("#,###,###,###")
            var formattedString = formatter.format(longval)

            if (formattedString.contains(",")) {
                formattedString = formattedString.replace(",".toRegex(), ".")
            }

            holder.menu_price.text = formattedString
        }

        val imgFile = File(item.photo_path)
        if (imgFile.exists()) {
            val myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
            holder.image_menu.setImageBitmap(myBitmap)
        }

        holder.menu_add_item.setOnClickListener {
            item.chose_amount = holder.menu_input_amount.getText().toString().toInt()
            item.chose_amount  = item.chose_amount  + 1
            holder.menu_input_amount.setText(item.chose_amount .toString())

            onItemClick?.invoke(item)
        }

        holder.menu_minus_item.setOnClickListener {
            item.chose_amount = holder.menu_input_amount.getText().toString().toInt()
            if (item.chose_amount  > 0){
                item.chose_amount  = item.chose_amount  - 1
                holder.menu_input_amount.setText(item.chose_amount .toString())

                if (item.chose_amount == 0){
                    holder.layout_menu_list.setPadding(0, 0, 0, 0)
                    holder.bg_layout_menu.setBackgroundColor(ContextCompat.getColor(context!!, R.color.primary_light))
                    holder.layout_amount.visibility = View.GONE

                    holder.menu_name.setTextColor(ContextCompat.getColor(context, R.color.primaryTextBlack))
                    holder.menu_proportions.setTextColor(ContextCompat.getColor(context, R.color.primaryTextBlack))
                    holder.menu_price.setTextColor(ContextCompat.getColor(context, R.color.black))
                    holder.menu_currency.setTextColor(ContextCompat.getColor(context, R.color.black))

                }
                onItemClick?.invoke(item)
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
            holder.menu_currency.setTextColor(ContextCompat.getColor(context, R.color.white))

            onItemClick?.invoke(item)

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
        val menu_currency = view.menu_currency

        val layout_amount = view.layout_amount

        val menu_minus_item = view.menu_minus_item
        val menu_input_amount = view.menu_input_amount
        val menu_add_item = view.menu_add_item

    }
}