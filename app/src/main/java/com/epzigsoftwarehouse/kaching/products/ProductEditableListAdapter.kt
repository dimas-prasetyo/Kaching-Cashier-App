package com.epzigsoftwarehouse.kaching.products

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.epzigsoftwarehouse.kaching.AddingMenuActivity
import com.epzigsoftwarehouse.kaching.R
import kotlinx.android.synthetic.main.layout_menu_editable.view.*
import java.io.File
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class ProductEditableListAdapter (val context: Context?, val items: ArrayList<Product>) : RecyclerView.Adapter<ProductEditableListAdapter.ViewHolder>() {
    lateinit var temp_setting: SharedPreferences
    var onDeleteItemClick: ((Product) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_menu_editable, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        temp_setting = context?.getSharedPreferences("setting_info", Context.MODE_PRIVATE)!!
        val usedCurrency = temp_setting.getString("currency", "").toString()

        val item = items.get(position)

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

        holder.btn_edit.setOnClickListener {
            val productDetailIntent = Intent(context, AddingMenuActivity::class.java)
            productDetailIntent.putExtra("product_id", item.id.toString())

            context?.startActivity(productDetailIntent)
        }

        holder.btn_delete.setOnClickListener {
            onDeleteItemClick?.invoke(item)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val layout_menu_list = view.layout_menu_list
        val cv_layout_menu = view.cv_layout_menu
        val bg_layout_menu = view.bg_layout_menu

        val menu_name = view.menu_name
        val image_menu = view.image_menu
        val menu_proportions = view.menu_proportions
        val menu_price = view.menu_price
        val menu_currency = view.menu_currency

        val cv_btn_edit = view.cv_btn_edit
        val cv_btn_delete = view.cv_btn_delete

        val btn_edit = view.btn_edit
        val btn_delete = view.btn_delete

    }
}