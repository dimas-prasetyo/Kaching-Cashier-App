package com.epzigsoftwarehouse.chachingapp

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.epzigsoftwarehouse.chachingapp.cashier.Cashier
import com.epzigsoftwarehouse.chachingapp.database.DatabaseHandler
import com.epzigsoftwarehouse.chachingapp.history.History
import com.epzigsoftwarehouse.chachingapp.products.Product
import com.epzigsoftwarehouse.chachingapp.products.ProductSelectedAdapter
import kotlinx.android.synthetic.main.activity_history_detail.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HistoryDetailActivity : AppCompatActivity() {
    lateinit var temp_setting: SharedPreferences
    private lateinit var storeName: String
    private lateinit var choosenCurrency: String
    private lateinit var transactinId: String
    private var selectedHistory: ArrayList<History> = ArrayList()
    private var selectedProducts: ArrayList<Product> = ArrayList()
    private var totalTaxPayment = 0.00
    private var activeCashier = 0
    private var totalPrice = 0.00
    private var cashPayment = 0.00

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_detail)

        temp_setting = getSharedPreferences("setting_info", Context.MODE_PRIVATE)
        storeName = temp_setting.getString("store_name", "").toString()
        choosenCurrency = temp_setting.getString("currency", "").toString()

        transactinId = intent.getStringExtra("product_id").toString()

        loadDetailTransaction()
        btn_back.setOnClickListener {
            onBackPressed()
        }
    }

    private fun loadDetailTransaction() {
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        val history: ArrayList<History> = databaseHandler.getHistoryDetailWhere(transactinId)

        val dateFormated = formatDate(history[0].date, "EEE, dd MMMM yyyy")

        val tempList = arrayListOf<Product>()
        selectedProducts = tempList

        for (i in 0..history.size-1){
            // Count Total Price
            var tempTotal = history[i].amount * history[i].price
            totalPrice = totalPrice + tempTotal
            totalPrice = Math.round(totalPrice * 100.0) / 100.0

            // Create Selected Product
            val tempProduct = Product(i,"", history[i].product_name, history[i].price, 0.00,"", 0, "", "", history[i].amount)
            selectedProducts.add(tempProduct)
        }

        txt_time.text = history[0].time + " - " + dateFormated

        txt_store_name.text = storeName
        value_tax.text = history[0].tax.toString()
        value_tip.text = history[0].tip.toString()
        value_cash.text = history[0].cash.toString()
        value_price.text = totalPrice.toString()

        var tempTotalPrice = totalPrice + history[0].tip + history[0].tax
        value_total_price.text = tempTotalPrice.toString()

        var tempChange = history[0].cash - tempTotalPrice
        tempChange = Math.round(tempChange * 100.0) / 100.0
        value_change.text = tempChange.toString()

        if (choosenCurrency.equals("dollar")){
            txt_currency_tax.text = "$"
            txt_currency_tip.text = "$"
            txt_currency_total_price.text = "$"
            txt_currency_cash.text = "$"
            txt_currency_change.text = "$"
        } else if (choosenCurrency.equals("rupiah")){
            txt_currency_tax.text = "Rp."
            txt_currency_tip.text = "Rp."
            txt_currency_total_price.text = "Rp."
            txt_currency_cash.text = "$"
            txt_currency_change.text = "$"
        }

        loadCashierInfo(history[0].cashier_id)
        loadProductList()
    }

    private fun loadProductList() {
        rv_product_list.hasFixedSize()
        rv_product_list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val productSelectedAdapter = ProductSelectedAdapter(this, selectedProducts)
        rv_product_list.adapter = productSelectedAdapter
    }

    private fun loadCashierInfo(cashierId: Int) {
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        val cashier: Cashier = databaseHandler.getCashierDetail(cashierId)
        cashier_active.text = cashier.name
    }

    private fun formatDate(date: String, format: String): String {
        var formatedDate = ""
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        sdf.timeZone = TimeZone.getTimeZone("UTC")

        try {
            val parseDate = sdf.parse(date)
            formatedDate = SimpleDateFormat(format).format(parseDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return formatedDate
    }

    private fun formatTime(date: String, format: String): String {
        var formatedTimes = ""
        val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        sdf.timeZone = TimeZone.getTimeZone("UTC")

        try {
            val parseDate = sdf.parse(date)
            formatedTimes = SimpleDateFormat(format).format(parseDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return formatedTimes
    }
}