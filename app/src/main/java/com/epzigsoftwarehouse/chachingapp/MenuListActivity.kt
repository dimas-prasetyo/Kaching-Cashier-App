package com.epzigsoftwarehouse.chachingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.epzigsoftwarehouse.chachingapp.database.DatabaseHandler
import com.epzigsoftwarehouse.chachingapp.products.CategoryListAdapter
import com.epzigsoftwarehouse.chachingapp.products.Product
import com.epzigsoftwarehouse.chachingapp.products.ProductListAdapter
import kotlinx.android.synthetic.main.activity_menu_list.*

class MenuListActivity : AppCompatActivity(){
    private lateinit var productList: ArrayList<Product>
    private var filteredProductList: ArrayList<Product> = ArrayList()
    private var selectedProducts: ArrayList<Product> = ArrayList()
    private var categoryActive = "All"
    private var productListSize = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_list)

        loadProductList()

        input_search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                if (productListSize > 0) {
                    if (p0 != null) {
                        loadProductBy(categoryActive, p0)
                    }
                }
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                if (productListSize > 0) {
                    if (p0 != null) {
                        loadProductBy(categoryActive, p0)
                    }
                }
                return false
            }

        })

        add_product.setOnClickListener {
            val intent = Intent(this, AddingMenuActivity::class.java)
            startActivity(intent)
        }

        btn_selected.setOnClickListener {
            val intent = Intent(this, PaymentVerificationActivity::class.java)
            intent.putParcelableArrayListExtra("selectedProducts", selectedProducts)
            startActivity(intent)
        }

        btn_back.setOnClickListener {
            onBackPressed()
        }

        cv_btn_clear.setOnClickListener {
            val tempList = arrayListOf<Product>()
            selectedProducts = tempList
            loadProductList()
            layout_btn_selected.visibility = View.GONE
        }
    }

    private fun loadProductList() {
        if (getProductList().size > 0) {
            rv_menu_list.visibility = View.VISIBLE
            layout_no_product.visibility = View.GONE

            productListSize = 1

            productList = getProductList()
            loadCategory()
            loadProductBy(categoryActive, "")
        } else {
            rv_menu_list.visibility = View.GONE
            layout_no_product.visibility = View.VISIBLE

        }
    }

    private fun loadCategory() {
        rv_kategori_list.hasFixedSize()
        rv_kategori_list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        var tempList: ArrayList<String> = ArrayList()
        tempList.add(categoryActive)
        tempList.add("All")

        for (i in 0..productList.size - 1){
            tempList.add(productList[i].category)

        }
        var categoryList = tempList.distinct()

        val categoryItemAdapter = CategoryListAdapter(this, categoryList, categoryActive)
        rv_kategori_list.adapter = categoryItemAdapter

        categoryItemAdapter.onItemClick = { category ->
            categoryActive = category
            refreshAll()
        }
    }

    private fun refreshAll() {
        //loadProductList()
        loadCategory()
        loadProductBy(categoryActive, "")
    }

    private fun loadProductBy(categoryActive: String, inputText: String) {
        if (categoryActive.equals("All")){
            rv_menu_list.hasFixedSize()
            rv_menu_list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

            val tempList = arrayListOf<Product>()
            filteredProductList = tempList

            if (inputText == null || inputText.equals("")){
                for (i in 0..productList.size-1){
                    filteredProductList.add(productList[i])
                }
            } else {
                for (i in 0..productList.size-1){
                    if (productList[i].name.contains(inputText, ignoreCase = true)){
                        filteredProductList.add(productList[i])
                    }
                }
            }


            val productItemAdapter = ProductListAdapter(this, filteredProductList)
            rv_menu_list.adapter = productItemAdapter

            productItemAdapter.onItemClick = { product ->
                for (i in 0..productList.size-1){
                    if (productList[i].id == product.id){
                        productList[i].chose_amount = product.chose_amount
                    }
                }

                showSelectedButton()
            }

        } else {
            rv_menu_list.hasFixedSize()
            rv_menu_list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)


            val tempList = arrayListOf<Product>()
            filteredProductList = tempList

            if (inputText == null || inputText.equals("")){
                for (i in 0..productList.size-1){
                    if (productList[i].category.equals(categoryActive)){
                        filteredProductList.add(productList[i])
                    }
                }
            } else {
                for (i in 0..productList.size-1){
                    if (productList[i].category.equals(categoryActive) && productList[i].name.contains(inputText, ignoreCase = true)){
                        filteredProductList.add(productList[i])
                    }
                }
            }

            val productItemAdapter = ProductListAdapter(this, filteredProductList)
            rv_menu_list.adapter = productItemAdapter

            productItemAdapter.onItemClick = { product ->
                for (i in 0..productList.size-1){
                    if (productList[i].id == product.id){
                        productList[i].chose_amount = product.chose_amount
                    }
                }
                showSelectedButton()
            }
        }

    }

    private fun showSelectedButton() {
        val tempList = arrayListOf<Product>()
        selectedProducts = tempList
        for (i in 0..productList.size-1){
            if (productList[i].chose_amount > 0){
                selectedProducts.add(productList[i])
            }
        }

        if (selectedProducts.size > 0){
            layout_btn_selected.visibility = View.VISIBLE
            txt_selected.text = selectedProducts.size.toString() + " Item Selected"
        } else {
            layout_btn_selected.visibility = View.GONE
        }
    }


    private fun getProductList(): ArrayList<Product> {
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        val empList: ArrayList<Product> = databaseHandler.viewAllProduct()

        return empList
    }

    override fun onResume() {
        //refreshAll()
        super.onResume()
        if (productListSize > 0) {
            loadProductBy(categoryActive, "")
        }
    }
}