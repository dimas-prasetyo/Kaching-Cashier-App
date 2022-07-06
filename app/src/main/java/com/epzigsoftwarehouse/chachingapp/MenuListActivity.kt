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
    private lateinit var idSelectedProduct: List<Int>
    private var selectedProducts: ArrayList<Product> = ArrayList()

    // tempSelectedProduct bisa dihapus
    private var tempSelectedProduct: ArrayList<Int> = ArrayList()

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
            /*val categoryItemAdapter = CategoryListAdapter(this, categoryList, category)
            rv_kategori_list.adapter = categoryItemAdapter*/

            //filterByCategory(category, categoryList)
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
                if (product.chose_amount == 1){
                    tempSelectedProduct.add(product.id)
                    //selectedProduct = selectedProduct.distinct() as ArrayList<Int>
                }

                if (product.chose_amount < 1){
                    //tempSelectedProduct.filterTo(arraytwo, { it != product.name})
                    tempSelectedProduct.remove(product.id)
                }
                idSelectedProduct = tempSelectedProduct.distinct()

                /*for (i in 0..selectedProduct.size-1){
                    if (product.id == selectedProduct[i] && product.chose_amount > 0){
                        tesSelectedProduct.add(product)
                    }
                }*/

                for (i in 0..productList.size-1){
                    if (productList[i].id == product.id){
                        productList[i].chose_amount = product.chose_amount
                    }
                }
                //
                /*if (product.chose_amount > 0){
                    if (selectedProduct.size < 1){
                        selectedProduct.add(product)
                    } else {
                        for (i in 0..idSelectedProduct.size-1){
                            var checkKetersedian: Boolean = false
                            for (j in 0..selectedProduct.size - 1 ){
                                if (selectedProduct[j].id == product.id){
                                    Toast.makeText(this, "Dipilih: " + selectedProduct[j].name, Toast.LENGTH_LONG).show()
                                    selectedProduct[j].chose_amount = product.chose_amount
                                    checkKetersedian = true
                                }
                            }
                            if (!checkKetersedian){
                                selectedProduct.add(product)
                            }
                        }
                    }
                }*/


                showSelectedButton(idSelectedProduct)
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
                idSelectedProduct = tempSelectedProduct.distinct()

                for (i in 0..productList.size-1){
                    if (productList[i].id == product.id){
                        productList[i].chose_amount = product.chose_amount
                    }
                }
                //
                /*if (product.chose_amount > 0){
                    if (selectedProduct.size < 1){
                        selectedProduct.add(product)
                    } else {
                        for (i in 0..idSelectedProduct.size-1){
                            var checkKetersedian: Boolean = false
                            for (j in 0..selectedProduct.size - 1 ){
                                if (selectedProduct[j].id == product.id){
                                    Toast.makeText(this, "Dipilih: " + selectedProduct[j].name, Toast.LENGTH_LONG).show()
                                    selectedProduct[j].chose_amount = product.chose_amount
                                    checkKetersedian = true
                                }
                            }
                            if (!checkKetersedian){
                                selectedProduct.add(product)
                            }
                        }
                    }
                }*/
                showSelectedButton(idSelectedProduct)
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
            idSelectedProduct = tempSelectedProduct.distinct()

            showSelectedButton(idSelectedProduct)
        }
    }

    private fun showSelectedButton(selectedProduct: List<Int>) {
        //println("Daftar terpilih: " + this.selectedProduct)
        //Toast.makeText(this, "Jumlah Dipili: " + tesSelectedProduct.size, Toast.LENGTH_LONG).show()

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
        //creating the instance of DatabaseHandler class
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        //calling the viewEmployee method of DatabaseHandler class to read the records
        val empList: ArrayList<Product> = databaseHandler.viewAllProduct()

        return empList
    }

    override fun onResume() {
        //refreshAll()
        super.onResume()
    }
}