package com.epzigsoftwarehouse.chachingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.epzigsoftwarehouse.chachingapp.database.DatabaseHandler
import com.epzigsoftwarehouse.chachingapp.products.CategoryListAdapter
import com.epzigsoftwarehouse.chachingapp.products.Product
import com.epzigsoftwarehouse.chachingapp.products.ProductEditableListAdapter
import kotlinx.android.synthetic.main.activity_menu_editable.*

class MenuEditableActivity : AppCompatActivity() {
    private lateinit var productList: ArrayList<Product>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_editable)

        loadProductList()
    }

    private fun loadProductList() {
        if (getProductList().size > 0) {
            productList = getProductList()
            loadCategory()
            loadProduct()

        } else {
        }
    }

    private fun getProductList(): ArrayList<Product> {
        //creating the instance of DatabaseHandler class
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        //calling the viewEmployee method of DatabaseHandler class to read the records
        val empList: ArrayList<Product> = databaseHandler.viewProduct()

        return empList
    }


    private fun loadCategory() {
        rv_kategori_list.hasFixedSize()
        rv_kategori_list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)


        var tempList: ArrayList<String> = ArrayList()
        tempList.add("All")

        for (i in 0..productList.size - 1){
            println("Kategori " + i + ": "+ productList[i].category)
            tempList.add(productList[i].category)

        }
        var categoryList = tempList.distinct()

        val categoryItemAdapter = CategoryListAdapter(this, categoryList, "All")
        rv_kategori_list.adapter = categoryItemAdapter
    }

    private fun loadProduct() {
        rv_menu_list.hasFixedSize()
        rv_menu_list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val productItemAdapter = ProductEditableListAdapter(this, productList)
        rv_menu_list.adapter = productItemAdapter
    }
}