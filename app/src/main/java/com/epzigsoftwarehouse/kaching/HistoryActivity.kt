package com.epzigsoftwarehouse.kaching

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.epzigsoftwarehouse.kaching.database.DatabaseHandler
import com.epzigsoftwarehouse.kaching.history.History
import com.epzigsoftwarehouse.kaching.history.HistoryMainAdapter
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

        if (empList.size > 0){
            txt_no_transaction.visibility = View.GONE
        } else {
            txt_no_transaction.visibility = View.VISIBLE
        }

        val sortedList = empList.sortedByDescending { it.id }.toCollection(ArrayList())
        rv_main_history_list.hasFixedSize()
        rv_main_history_list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val histroyMainAdapter = HistoryMainAdapter(this, sortedList)
        rv_main_history_list.adapter = histroyMainAdapter
    }
}