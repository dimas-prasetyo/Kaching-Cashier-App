package com.epzigsoftwarehouse.chachingapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.epzigsoftwarehouse.chachingapp.cashier.Cashier
import com.epzigsoftwarehouse.chachingapp.cashier.CashierListAdapter
import com.epzigsoftwarehouse.chachingapp.database.DatabaseHandler
import kotlinx.android.synthetic.main.activity_cashier_list.*
import java.io.File

class CashierListActivity : AppCompatActivity() {
    lateinit var temp_setting: SharedPreferences
    private lateinit var cashierList: ArrayList<Cashier>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cashier_list)

        temp_setting = getSharedPreferences("setting_info", Context.MODE_PRIVATE)
        val active_cashier = temp_setting.getString("active_cashier", "").toString()

        loadCashierList()
        chechkActiveCashier(active_cashier)


        cv_add_cashier.setOnClickListener {
            val intent = Intent(this, AddingCashierActivity::class.java)
            startActivity(intent)
        }

        cv_unactive_cashier.setOnClickListener {
            temp_setting.edit().putString("active_cashier", "0").apply()
            loadCashierList()
            chechkActiveCashier(active_cashier)
        }

        btn_back.setOnClickListener {
            onBackPressed()
        }
    }

    private fun chechkActiveCashier(active_cashier: String) {
        if (active_cashier.equals("") || active_cashier == null || active_cashier.equals("0")){
            layout_active_cashier.visibility = View.GONE
            no_cashier.visibility = View.VISIBLE
        } else {
            setActiveCashier(active_cashier)
        }
    }

    private fun setActiveCashier(cashierId: String) {
        layout_active_cashier.visibility = View.VISIBLE
        no_cashier.visibility = View.GONE

        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        val cashier: Cashier = databaseHandler.getCashierDetail(cashierId.toInt())

        cashier_name.text = cashier.name
        cashier_position.text = cashier.position

        val imgFile = File(cashier.photo_path)
        if (imgFile.exists()) {
            val myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
            image_cashier.setImageBitmap(myBitmap)
        }
    }

    private fun loadCashierList() {
        if (getCashierList().size > 0) {
            rv_cashier_list.visibility = View.VISIBLE
            txt_no_list.visibility = View.GONE

            cashierList = getCashierList()
            loadCashiers()
        } else {
            rv_cashier_list.visibility = View.GONE
            txt_no_list.visibility = View.VISIBLE
        }
    }

    private fun getCashierList(): ArrayList<Cashier> {
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        val empList: ArrayList<Cashier> = databaseHandler.viewAllCashier()

        return empList
    }

    private fun loadCashiers() {
        rv_cashier_list.hasFixedSize()
        rv_cashier_list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val cashierItemAdapter = CashierListAdapter(this, cashierList)
        rv_cashier_list.adapter = cashierItemAdapter

        cashierItemAdapter.onActiveCashierClick = { cashierId ->
            temp_setting.edit().putString("active_cashier", cashierId).apply()
            loadCashierList()
            chechkActiveCashier(cashierId)
        }
    }

    override fun onResume() {
        temp_setting = getSharedPreferences("setting_info", Context.MODE_PRIVATE)
        val active_cashier = temp_setting.getString("active_cashier", "").toString()

        loadCashierList()
        chechkActiveCashier(active_cashier)
        super.onResume()
    }
}