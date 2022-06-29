package com.epzigsoftwarehouse.chachingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.epzigsoftwarehouse.chachingapp.database.DatabaseHandler
import com.epzigsoftwarehouse.chachingapp.products.CategoryListAdapter
import com.epzigsoftwarehouse.chachingapp.products.Product
import com.epzigsoftwarehouse.chachingapp.products.ProductListAdapter
import kotlinx.android.synthetic.main.activity_menu_list.*

class MenuListActivity : AppCompatActivity(), ProductRVClickListener {
    private lateinit var productList: ArrayList<Product>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_list)

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

    private fun loadCategory() {
        rv_kategori_list.hasFixedSize()
        rv_kategori_list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)


        var tempList: ArrayList<String> = ArrayList()
        tempList.add("All")

        for (i in 0..productList.size - 1){
            tempList.add(productList[i].category)

        }
        var categoryList = tempList.distinct()

        val categoryItemAdapter = CategoryListAdapter(this, categoryList, "All")
        rv_kategori_list.adapter = categoryItemAdapter
    }

    private fun loadProduct() {
        rv_menu_list.hasFixedSize()
        rv_menu_list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val productItemAdapter = ProductListAdapter(this, productList)
        rv_menu_list.adapter = productItemAdapter

        productItemAdapter.onItemClick = { product ->
            Toast.makeText(this, "Product:  ${product.name} berhasil di klik",
                Toast.LENGTH_LONG
            ).show()

            println("Yang diklik: " + product.name)
        }
    }


    private fun getProductList(): ArrayList<Product> {
        //creating the instance of DatabaseHandler class
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        //calling the viewEmployee method of DatabaseHandler class to read the records
        val empList: ArrayList<Product> = databaseHandler.viewProduct()

        return empList
    }

    override fun onItemClicked(view: View, product: Product) {
        Toast.makeText(this, "Product:  ${product.name} berhasil di klik",
            Toast.LENGTH_LONG
        ).show()

        println("Yang diklik: " + product.name)
    }

}