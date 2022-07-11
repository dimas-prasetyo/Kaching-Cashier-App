package com.epzigsoftwarehouse.chachingapp.cashier

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.epzigsoftwarehouse.chachingapp.CashierDetailActivity
import com.epzigsoftwarehouse.chachingapp.R
import kotlinx.android.synthetic.main.layout_cashier.view.*
import java.io.File

class CashierListAdapter (val context: Context?, val items: ArrayList<Cashier>) : RecyclerView.Adapter<CashierListAdapter.ViewHolder>() {
    lateinit var temp_setting: SharedPreferences
    var onActiveCashierClick: ((String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CashierListAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_cashier, parent, false))
    }

    override fun onBindViewHolder(holder: CashierListAdapter.ViewHolder, position: Int) {
        val item = items.get(position)

        temp_setting = context!!.getSharedPreferences("setting_info", Context.MODE_PRIVATE)

        holder.cashier_name.text = item.name

        if (item.position.equals("") && item.position == null){
            holder.cashier_position.text = "-"
        } else {
            holder.cashier_position.text = item.position
        }

        if (item.contact.equals("") && item.contact == null){
            holder.cashier_contact.text = "-"
        } else {
            holder.cashier_contact.text = item.contact
        }

        val imgFile = File(item.photo_path)
        if (imgFile.exists()) {
            val myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
            holder.image_cashier.setImageBitmap(myBitmap)
        }

        holder.txt_active_cashier.setOnClickListener {
            temp_setting.edit().putString("active_cashier", item.id.toString()).apply()
            onActiveCashierClick?.invoke(item.id.toString())
        }

        holder.area_click.setOnClickListener {
            val productDetailIntent = Intent(context, CashierDetailActivity::class.java)
            productDetailIntent.putExtra("cashier_id", item.id.toString())

            context?.startActivity(productDetailIntent)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val cv_layout_cashier = view.cv_layout_cashier
        val bg_layout_cashier = view.bg_layout_cashier
        val cv_image_cashier = view.cv_image_cashier

        val image_cashier = view.image_cashier
        val cashier_name = view.cashier_name
        val cashier_position = view.cashier_position
        val cashier_contact = view.cashier_contact

        val btn_choose_cashier = view.btn_choose_cashier
        val txt_active_cashier = view.txt_active_cashier
        val area_click = view.area_click

    }

}