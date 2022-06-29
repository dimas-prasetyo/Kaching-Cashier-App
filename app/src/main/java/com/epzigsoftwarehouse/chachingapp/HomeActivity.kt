package com.epzigsoftwarehouse.chachingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        card_view_menu_1.setOnClickListener {
            val intent = Intent(this, MenuListActivity::class.java)
            startActivity(intent) }

        card_view_menu_2.setOnClickListener {
            val intent = Intent(this, AddingMenuActivity::class.java)
            startActivity(intent) }

        card_view_menu_3.setOnClickListener {
            val intent = Intent(this, MenuEditableActivity::class.java)
            startActivity(intent) }
    }
}