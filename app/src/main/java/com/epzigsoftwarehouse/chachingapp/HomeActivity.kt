package com.epzigsoftwarehouse.chachingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_home.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        card_view_menu_1.setOnClickListener {
            val intent = Intent(this, MenuListActivity::class.java)
            startActivity(intent)
        }

        card_view_menu_2.setOnClickListener {
            val intent = Intent(this, MenuEditableActivity::class.java)
            startActivity(intent)
        }

        card_view_menu_3.setOnClickListener {
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
        }

        card_view_menu_4.setOnClickListener {
            val intent = Intent(this, CashierListActivity::class.java)
            startActivity(intent)
        }

        card_view_menu_5.setOnClickListener {
            val intent = Intent(this, AboutActivity::class.java)
            startActivity(intent)
        }

        card_view_menu_6.setOnClickListener {
            val intent = Intent(this, AboutActivity::class.java)
            startActivity(intent)
        }

        val sdfDate = SimpleDateFormat("yyyy-MM-dd")
        val currentDate = sdfDate.format(Date()).toString()
        val dateFormated = formatDate(currentDate, "EEE, dd MMMM yyyy")

        txt_date.text = dateFormated
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
}