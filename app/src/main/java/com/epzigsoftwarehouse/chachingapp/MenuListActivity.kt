package com.epzigsoftwarehouse.chachingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.epzigsoftwarehouse.chachingapp.database.DatabaseHandler
import com.epzigsoftwarehouse.chachingapp.products.CategoryListAdapter
import com.epzigsoftwarehouse.chachingapp.products.Product
import com.epzigsoftwarehouse.chachingapp.products.ProductListAdapter
import kotlinx.android.synthetic.main.activity_menu_list.*

class MenuListActivity : AppCompatActivity(){
    private lateinit var productList: ArrayList<Product>
    private var filteredProductList: ArrayList<Product> = ArrayList()
    private lateinit var selectedProduct: List<Int>
    private var tempSelectedProduct: ArrayList<Int> = ArrayList()
    private var categoryActive = "All"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_list)

        loadProductList()

        input_search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                /*if (p0.equals("") || p0 == null){
                } else {
                    loadProductBy(categoryActive, p0)
                }*/

                if (p0 != null) {
                    loadProductBy(categoryActive, p0)
                }
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                /*if (p0.equals("") || p0 == null){
                } else {
                    loadProductBy(categoryActive, p0)
                }*/
                if (p0 != null) {
                    loadProductBy(categoryActive, p0)
                }
                return false
            }

        })
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
            /*val categoryItemAdapter = CategoryListAdapter(this, categoryList, category)
            rv_kategori_list.adapter = categoryItemAdapter*/

            //filterByCategory(category, categoryList)
            refreshAll()
        }
    }

    private fun refreshAll() {
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
                if (product.chose_amount == 1){
                    tempSelectedProduct.add(product.id)
                    //selectedProduct = selectedProduct.distinct() as ArrayList<Int>
                }

                if (product.chose_amount < 1){
                    //tempSelectedProduct.filterTo(arraytwo, { it != product.name})
                    tempSelectedProduct.remove(product.id)
                }
                selectedProduct = tempSelectedProduct.distinct()

                showSelectedButton(selectedProduct)
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
                if (product.chose_amount == 1){
                    tempSelectedProduct.add(product.id)
                    //selectedProduct = selectedProduct.distinct() as ArrayList<Int>
                }

                if (product.chose_amount < 1){
                    //tempSelectedProduct.filterTo(arraytwo, { it != product.name})
                    tempSelectedProduct.remove(product.id)
                }
                selectedProduct = tempSelectedProduct.distinct()

                showSelectedButton(selectedProduct)
            }
        }
    }

    private fun loadProduct() {
        rv_menu_list.hasFixedSize()
        rv_menu_list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val productItemAdapter = ProductListAdapter(this, productList)
        rv_menu_list.adapter = productItemAdapter

        productItemAdapter.onItemClick = { product ->
            if (product.chose_amount == 1){
                tempSelectedProduct.add(product.id)
                //selectedProduct = selectedProduct.distinct() as ArrayList<Int>
            }

            if (product.chose_amount < 1){
                //tempSelectedProduct.filterTo(arraytwo, { it != product.name})
                tempSelectedProduct.remove(product.id)
            }
            selectedProduct = tempSelectedProduct.distinct()

            showSelectedButton(selectedProduct)
        }
    }

    private fun showSelectedButton(selectedProduct: List<Int>) {
        if (selectedProduct.size > 0){
            layout_btn_selected.visibility = View.VISIBLE
            txt_selected.text = selectedProduct.size.toString() + " Item Selected"
        } else {
            layout_btn_selected.visibility = View.GONE
        }
    }


    private fun getProductList(): ArrayList<Product> {
        //creating the instance of DatabaseHandler class
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        //calling the viewEmployee method of DatabaseHandler class to read the records
        val empList: ArrayList<Product> = databaseHandler.viewAllProduct()

        return empList
    }
}