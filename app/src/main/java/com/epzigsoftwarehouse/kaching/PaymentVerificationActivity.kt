package com.epzigsoftwarehouse.kaching

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.epzigsoftwarehouse.kaching.cashier.Cashier
import com.epzigsoftwarehouse.kaching.database.DatabaseHandler
import com.epzigsoftwarehouse.kaching.history.History
import com.epzigsoftwarehouse.kaching.products.Product
import com.epzigsoftwarehouse.kaching.products.ProductSelectedAdapter
import com.google.android.material.card.MaterialCardView
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
    private var cashPayment = 0.00
    private var storeTax = 0
    private var totalTaxPayment = 0.00
    private var tipPayment = 0.00
    private lateinit var infoDialogBox: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_verification)

        temp_setting = getSharedPreferences("setting_info", Context.MODE_PRIVATE)
        selectedProducts = intent.getParcelableArrayListExtra("selectedProducts")!!
        storeName = temp_setting.getString("store_name", "").toString()
        choosenCurrency = temp_setting.getString("currency", "").toString()
        activeCashier = temp_setting.getString("active_cashier", "").toString().toInt()
        storeTax = temp_setting.getString("tax", "").toString().toInt()

        loadSelectedProducts()
        loadSavedSetting()
        hidePaymentDetail()
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
            showPaymentDialogBox()
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
            val status = databaseHandler.addHistory(History(0, transactionId, currentDate, currentTimes, activeCashier, selectedProducts[i].id, selectedProducts[i].name, selectedProducts[i].price, totalTaxPayment, tipPayment, cashPayment, selectedProducts[i].chose_amount))
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

        value_price.text = totalPrice.toString()
        rv_product_list.hasFixedSize()
        rv_product_list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val productSelectedAdapter = ProductSelectedAdapter(this, selectedProducts)
        rv_product_list.adapter = productSelectedAdapter
    }

    private fun loadSavedSetting() {
        txt_store_name.text = storeName

        if (choosenCurrency.equals("dollar")){
            txt_currency.text = "$"
        }

        if (choosenCurrency.equals("rupiah")){
            txt_currency.text = "Rp"
        }

        totalTaxPayment = totalPrice * storeTax / 100
        totalTaxPayment = Math.round(totalTaxPayment * 100.0) / 100.0

        txt_tax.text = "Tax (" + storeTax.toString() + "%)"
        value_tax.text = totalTaxPayment.toString()
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

    private fun hidePaymentDetail() {
        /*txt_tax.visibility = View.GONE
        txt_currency_tax.visibility = View.GONE
        value_tax.visibility = View.GONE*/
        txt_tip.visibility = View.GONE
        txt_currency_tip.visibility = View.GONE
        value_tip.visibility = View.GONE
        line_final_total.visibility = View.GONE
        txt_total_price.visibility = View.GONE
        txt_currency_total_price.visibility = View.GONE
        value_total_price.visibility = View.GONE
        txt_cash.visibility = View.GONE
        txt_currency_cash.visibility = View.GONE
        value_cash.visibility = View.GONE
        txt_change.visibility = View.GONE
        txt_currency_change.visibility = View.GONE
        value_change.visibility = View.GONE
    }

    private fun showPaymentDetail(){
        /*txt_tax.visibility = View.VISIBLE
        txt_currency_tax.visibility = View.VISIBLE
        value_tax.visibility = View.VISIBLE*/
        txt_tip.visibility = View.VISIBLE
        txt_currency_tip.visibility = View.VISIBLE
        value_tip.visibility = View.VISIBLE
        line_final_total.visibility = View.VISIBLE
        txt_total_price.visibility = View.VISIBLE
        txt_currency_total_price.visibility = View.VISIBLE
        value_total_price.visibility = View.VISIBLE
        txt_cash.visibility = View.VISIBLE
        txt_currency_cash.visibility = View.VISIBLE
        value_cash.visibility = View.VISIBLE
        txt_change.visibility = View.VISIBLE
        txt_currency_change.visibility = View.VISIBLE
        value_change.visibility = View.VISIBLE

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

        value_tip.text = tipPayment.toString()
        value_cash.text = cashPayment.toString()
        var tempTotalPayment = totalPrice + tipPayment + totalTaxPayment
        value_total_price.text = tempTotalPayment.toString()
        var tempChange = cashPayment - tempTotalPayment
        tempChange = Math.round(tempChange * 100.0) / 100.0
        value_change.text = tempChange.toString()

        btn_done.visibility = View.GONE

    }

    private fun showPaymentDialogBox() {
        val layoutInflater = LayoutInflater.from(this)
        val promptView: View = layoutInflater.inflate(R.layout.layout_input_payment, null)
        val layoutShowInfo = AlertDialog.Builder(this)
        layoutShowInfo.setView(promptView)

        val input_cash = promptView.findViewById<EditText>(R.id.input_cash) as EditText
        val input_tip = promptView.findViewById<EditText>(R.id.input_tip) as EditText
        val txt_amount_price = promptView.findViewById<TextView>(R.id.txt_amount_price) as TextView

        val txt_currency = promptView.findViewById<TextView>(R.id.txt_currency) as TextView
        val txt_currency_2 = promptView.findViewById<TextView>(R.id.txt_currency_2) as TextView
        val txt_currency_3 = promptView.findViewById<TextView>(R.id.txt_currency_3) as TextView

        val btn_done = promptView.findViewById<MaterialCardView>(R.id.btn_done) as MaterialCardView

        if (choosenCurrency.equals("dollar")){
            txt_currency.text = "$"
            txt_currency_2.text = "$"
            txt_currency_3.text = "$"
        } else if (choosenCurrency.equals("rupiah")){
            txt_currency.text = "Rp."
            txt_currency_2.text = "Rp."
            txt_currency_3.text = "Rp."
        }

        var tempPriceTax = totalPrice + totalTaxPayment
        tempPriceTax = Math.round(tempPriceTax * 100.0) / 100.0
        txt_amount_price.text = tempPriceTax.toString()
        // create an alert dialog

        val layoutInput: AlertDialog = layoutShowInfo.create()
        layoutInput.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        //layoutInput.setView(promptView, 0, 0, 0, 0)

        infoDialogBox = layoutShowInfo.create()
        infoDialogBox.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        infoDialogBox.show()


        btn_done.setOnClickListener {
            cashPayment = input_cash.getText().toString().toDouble()
            tipPayment = input_tip.getText().toString().toDouble()
            var tempTotalPrice = totalPrice + tipPayment + totalTaxPayment
            if (cashPayment >= tempTotalPrice){
                showPaymentDetail()
                addToHistory()
                infoDialogBox.dismiss()
            }
        }

    }

}