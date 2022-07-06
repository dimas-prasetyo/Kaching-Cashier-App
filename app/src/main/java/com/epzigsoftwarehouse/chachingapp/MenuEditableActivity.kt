package com.epzigsoftwarehouse.chachingapp

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.epzigsoftwarehouse.chachingapp.database.DatabaseHandler
import com.epzigsoftwarehouse.chachingapp.products.CategoryListAdapter
import com.epzigsoftwarehouse.chachingapp.products.Product
import com.epzigsoftwarehouse.chachingapp.products.ProductEditableListAdapter
import kotlinx.android.synthetic.main.activity_menu_editable.*

class MenuEditableActivity : AppCompatActivity() {
    private lateinit var productList: ArrayList<Product>
    private var filteredProductList: ArrayList<Product> = ArrayList()
    private lateinit var selectedProduct: List<Int>
    private var tempSelectedProduct: ArrayList<Int> = ArrayList()
    private var categoryActive = "All"
    private lateinit var infoDialogBox: AlertDialog
    private var productListSize = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_editable)

        loadProductList()

        input_search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                if (productListSize > 0){
                    if (p0 != null) {
                        loadProductBy(categoryActive, p0)
                    }
                }
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                if (productListSize > 0){
                    if (p0 != null) {
                        loadProductBy(categoryActive, p0)
                    }
                }
                return false
            }

        })

        cv_add_product.setOnClickListener {
            val intent = Intent(this, AddingMenuActivity::class.java)
            startActivity(intent)
        }

        btn_back.setOnClickListener {
            onBackPressed()
        }

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

            val productItemAdapter = ProductEditableListAdapter(this, filteredProductList)
            rv_menu_list.adapter = productItemAdapter

            productItemAdapter.onDeleteItemClick = { product ->
                showDeleteDialogBox(product.id)
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

            val productItemAdapter = ProductEditableListAdapter(this, filteredProductList)
            rv_menu_list.adapter = productItemAdapter

            productItemAdapter.onDeleteItemClick = { product ->
                showDeleteDialogBox(product.id)
            }

        }

    }

    private fun loadProductList() {
        if (getProductList().size > 0) {
            rv_menu_list.visibility = View.VISIBLE
            txt_no_list.visibility = View.GONE

            productListSize = 1

            productList = getProductList()
            loadCategory()
            loadProduct()

        } else {
            rv_menu_list.visibility = View.GONE
            txt_no_list.visibility = View.VISIBLE
        }
    }

    private fun getProductList(): ArrayList<Product> {
        //creating the instance of DatabaseHandler class
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        //calling the viewEmployee method of DatabaseHandler class to read the records
        val empList: ArrayList<Product> = databaseHandler.viewAllProduct()

        return empList
    }


    private fun loadCategory() {
        rv_kategori_list.hasFixedSize()
        rv_kategori_list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)


        var tempList: ArrayList<String> = ArrayList()
        tempList.add(categoryActive)
        tempList.add("All")

        for (i in 0..productList.size - 1) {
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
        if (getProductList().size > 0) {
            loadCategory()
            loadProductBy(categoryActive, "")
        }
    }

    private fun loadProduct() {
        rv_menu_list.hasFixedSize()
        rv_menu_list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val productItemAdapter = ProductEditableListAdapter(this, productList)
        rv_menu_list.adapter = productItemAdapter

        productItemAdapter.onDeleteItemClick = { product ->
            showDeleteDialogBox(product.id)
        }
    }

    private fun showDeleteDialogBox(productId: Int) {
        val layoutInflater = LayoutInflater.from(this)
        val promptView: View = layoutInflater.inflate(R.layout.layout_confirmation, null)
        val layoutShowInfo = AlertDialog.Builder(this)
        layoutShowInfo.setView(promptView)

        val txt_info = promptView.findViewById<TextView>(R.id.txt_main_text) as TextView
        val icon_dialog = promptView.findViewById<ImageView>(R.id.icon_dialog) as ImageView
        val bg_dialog_box = promptView.findViewById<RelativeLayout>(R.id.bg_dialog_box) as RelativeLayout

        val btn_opt_1 = promptView.findViewById<TextView>(R.id.btn_opt_1) as TextView
        val btn_opt_2 = promptView.findViewById<TextView>(R.id.btn_opt_2) as TextView

        bg_dialog_box.setBackgroundColor(ContextCompat.getColor(this, R.color.redFailed))
        icon_dialog.setImageResource(R.drawable.icon_delete)
        txt_info.text = "Do you want to remove this product?"
        // create an alert dialog
        val layoutInput: AlertDialog = layoutShowInfo.create()
        layoutInput.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        //layoutInput.setView(promptView, 0, 0, 0, 0)

        infoDialogBox = layoutShowInfo.create()
        infoDialogBox.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        infoDialogBox.show()

        btn_opt_1.setOnClickListener {
            val databaseHandler: DatabaseHandler = DatabaseHandler(this)
            val deleteProduct = databaseHandler.deleteProduct(productId)
            if (deleteProduct > -1) {
                infoDialogBox.dismiss()
                showSuccessDelete()
            } else {
                infoDialogBox.dismiss()
                showFailedDelete()
            }
        }

        btn_opt_2.setOnClickListener {
            infoDialogBox.dismiss()
        }

    }

    private fun showSuccessDelete() {
        val layoutInflater = LayoutInflater.from(this)
        val promptView: View = layoutInflater.inflate(R.layout.layout_info_dialog_box, null)
        val layoutShowInfo = AlertDialog.Builder(this)
        layoutShowInfo.setCancelable(false)
        layoutShowInfo.setView(promptView)

        val txt_info = promptView.findViewById<TextView>(R.id.txt_main_text) as TextView
        val icon_dialog = promptView.findViewById<ImageView>(R.id.icon_dialog) as ImageView
        val bg_dialog_box = promptView.findViewById<RelativeLayout>(R.id.bg_dialog_box) as RelativeLayout

        bg_dialog_box.setBackgroundColor(ContextCompat.getColor(this, R.color.greenSuccess))
        icon_dialog.setImageResource(R.drawable.icon_done)

        txt_info.text = "Successfully delete product"

        // create an alert dialog
        val layoutInput: AlertDialog = layoutShowInfo.create()
        layoutInput.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        //layoutInput.setView(promptView, 0, 0, 0, 0)

        // create an loading dialog
        infoDialogBox = layoutShowInfo.create()
        infoDialogBox.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        infoDialogBox.show()

        Handler().postDelayed({
            infoDialogBox.dismiss()
            refreshAll()
        }, 2000)
    }

    private fun showFailedDelete() {
        val layoutInflater = LayoutInflater.from(this)
        val promptView: View = layoutInflater.inflate(R.layout.layout_info_dialog_box, null)
        val layoutShowInfo = AlertDialog.Builder(this)
        layoutShowInfo.setCancelable(false)
        layoutShowInfo.setView(promptView)

        val txt_info = promptView.findViewById<TextView>(R.id.txt_main_text) as TextView
        val icon_dialog = promptView.findViewById<ImageView>(R.id.icon_dialog) as ImageView
        val bg_dialog_box = promptView.findViewById<RelativeLayout>(R.id.bg_dialog_box) as RelativeLayout

        bg_dialog_box.setBackgroundColor(ContextCompat.getColor(this, R.color.redFailed))
        icon_dialog.setImageResource(R.drawable.icon_failed)

        txt_info.text = "Failed to delete product"

        // create an alert dialog
        val layoutInput: AlertDialog = layoutShowInfo.create()
        layoutInput.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        //layoutInput.setView(promptView, 0, 0, 0, 0)

        // create an loading dialog
        infoDialogBox = layoutShowInfo.create()
        infoDialogBox.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        infoDialogBox.show()

        Handler().postDelayed({
            infoDialogBox.dismiss()
        }, 2000)
    }


    override fun onResume() {
        refreshAll()
        super.onResume()
    }
}