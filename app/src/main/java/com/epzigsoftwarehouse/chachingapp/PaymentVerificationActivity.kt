package com.epzigsoftwarehouse.chachingapp

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.epzigsoftwarehouse.chachingapp.cashier.Cashier
import com.epzigsoftwarehouse.chachingapp.database.DatabaseHandler
import com.epzigsoftwarehouse.chachingapp.history.History
import com.epzigsoftwarehouse.chachingapp.products.Product
import com.epzigsoftwarehouse.chachingapp.products.ProductSelectedAdapter
import kotlinx.android.synthetic.main.activity_payment_verification.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class PaymentVerificationActivity : AppCompatActivity() {
    lateinit var temp_setting: SharedPreferences
    private var selectedProducts: ArrayList<Product> = ArrayList()
    private var statusSaveHistyory: ArrayList<Long> = ArrayList()
    private lateinit var storeName: String
    private lateinit var choosenCurrency: String
    private lateinit var currentDate: String
    private lateinit var currentTimes: String
    private var activeCashier = 0
    private var totalPrice = 0.00

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_verification)

        temp_setting = getSharedPreferences("setting_info", Context.MODE_PRIVATE)
        selectedProducts = intent.getParcelableArrayListExtra("selectedProducts")!!
        storeName = temp_setting.getString("store_name", "").toString()
        choosenCurrency = temp_setting.getString("currency", "").toString()
        activeCashier = temp_setting.getString("active_cashier", "").toString().toInt()

        loadSelectedProducts()
        loadSavedSetting()
        if (activeCashier > 0){
            loadActiveCashier()
        }
        val sdfDate = SimpleDateFormat("yyyy-MM-dd")
        currentDate = sdfDate.format(Date()).toString()
        val dateFormated = formatDate(currentDate, "EEE, dd MMMM yyyy")

        val sdfTimes = SimpleDateFormat("HH:mm:ss")
        currentTimes = sdfTimes.format(Date()).toString()
        val timesFormated = formatTime(currentTimes, "HH:mm:ss")

        txt_time.text = timesFormated + " - " + dateFormated

        btn_done.setOnClickListener {
            addToHistory()
        }

        btn_back.setOnClickListener {
            onBackPressed()
        }
    }

    private fun addToHistory() {
        /*println("Waktu: " + currentTimes)
        Toast.makeText(this, "Waktu: " + currentTimes, Toast.LENGTH_LONG).show()*/
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        val transactionId = currentDate + "/" + currentTimes + "/" + totalPrice

        val tempList = arrayListOf<Long>()
        statusSaveHistyory =  tempList
        var allStatus: Boolean = false
        for (i in 0..selectedProducts.size-1){
            val status = databaseHandler.addHistory(History(0, transactionId, currentDate, currentTimes, selectedProducts[i].id, selectedProducts[i].name, selectedProducts[i].price, selectedProducts[i].chose_amount))
            if (status > -1) {
                //showSuccessDialog()
                statusSaveHistyory.add(status)
            } else {
                //showFailedDialog()
            }
        }

        if (statusSaveHistyory.size == selectedProducts.size){
            println("Hasil: " + statusSaveHistyory.size.toString() + " | " + selectedProducts.size.toString() + " - " + statusSaveHistyory)
        }

    }

    private fun loadSelectedProducts() {
        for (i in 0..selectedProducts.size-1){
            var tempTotal = selectedProducts[i].chose_amount * selectedProducts[i].price
            totalPrice = totalPrice + tempTotal
            totalPrice = Math.round(totalPrice * 100.0) / 100.0
        }

        total_price.text = totalPrice.toString()
        rv_product_list.hasFixedSize()
        rv_product_list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val productSelectedAdapter = ProductSelectedAdapter(this, selectedProducts)
        rv_product_list.adapter = productSelectedAdapter
    }

    private fun loadSavedSetting() {
        txt_store_name.text = storeName

        if (choosenCurrency.equals("dollar"))
            txt_currency.text = "$"

        if (choosenCurrency.equals("rupiah"))
            txt_currency.text = "Rp"
    }

    private fun loadActiveCashier() {
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        val cashier: Cashier = databaseHandler.getCashierDetail(activeCashier)
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