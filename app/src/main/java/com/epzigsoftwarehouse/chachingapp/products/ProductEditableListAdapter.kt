package com.epzigsoftwarehouse.chachingapp.products

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.epzigsoftwarehouse.chachingapp.AddingMenuActivity
import com.epzigsoftwarehouse.chachingapp.R
import com.epzigsoftwarehouse.chachingapp.database.DatabaseHandler
import kotlinx.android.synthetic.main.layout_menu_editable.view.*
import java.io.File

class ProductEditableListAdapter (val context: Context?, val items: ArrayList<Product>) : RecyclerView.Adapter<ProductEditableListAdapter.ViewHolder>() {
    var onDeleteItemClick: ((Product) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_menu_editable, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items.get(position)

        holder.menu_name.text = item.name
        holder.menu_proportions.text = item.proportion.toString() + " " + item.unit + " x " + item.amount.toString()
        holder.menu_price.text = item.price.toString()

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
            /*val databaseHandler: DatabaseHandler = DatabaseHandler(context)
            val deleteProduct = databaseHandler.deleteProduct(item.id)
            if (deleteProduct > -1) {
                println("Berhasil menghapus")
            }*/
            println("Delete diklik (Adapter): " + item.name)
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

        val cv_btn_edit = view.cv_btn_edit
        val cv_btn_delete = view.cv_btn_delete

        val btn_edit = view.btn_edit
        val btn_delete = view.btn_delete

    }
}