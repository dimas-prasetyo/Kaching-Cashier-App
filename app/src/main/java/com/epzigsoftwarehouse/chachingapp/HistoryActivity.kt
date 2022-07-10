package com.epzigsoftwarehouse.chachingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.epzigsoftwarehouse.chachingapp.database.DatabaseHandler
import com.epzigsoftwarehouse.chachingapp.history.History
import com.epzigsoftwarehouse.chachingapp.history.HistoryMainAdapter
import kotlinx.android.synthetic.main.activity_history.*

class HistoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        loadSelectedProducts()
        //Toast.makeText(this, "Panjang: " + empList.size, Toast.LENGTH_LONG).show()

        btn_back.setOnClickListener {
            onBackPressed()
        }

    }

    private fun loadSelectedProducts() {
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        val empList: ArrayList<History> = databaseHandler.viewAllHistory()

        val sortedList = empList.sortedByDescending { it.id }.toCollection(ArrayList())
        rv_main_history_list.hasFixedSize()
        rv_main_history_list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val histroyMainAdapter = HistoryMainAdapter(this, sortedList)
        rv_main_history_list.adapter = histroyMainAdapter
    }
}