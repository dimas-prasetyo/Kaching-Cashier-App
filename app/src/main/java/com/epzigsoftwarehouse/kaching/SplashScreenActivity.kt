package com.epzigsoftwarehouse.kaching

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate

class SplashScreenActivity : AppCompatActivity() {
    lateinit var temp_setting: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        temp_setting = getSharedPreferences("setting_info", Context.MODE_PRIVATE)

        try {
            val first_run = temp_setting.getString("first_run", "").toString()
            if (first_run.equals("no")){

            } else {
                temp_setting.edit().putString("first_run", "no").apply()
                temp_setting.edit().putString("store_logo", "").apply()
                temp_setting.edit().putString("store_name", "Store Name").apply()
                temp_setting.edit().putString("language", "english").apply()
                temp_setting.edit().putString("currency", "dollar").apply()
                temp_setting.edit().putString("active_cashier", "0").apply()
                temp_setting.edit().putString("tax", "0").apply()
                Toast.makeText(this, "Welcome to Kaching", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception){

        }

        Handler().postDelayed({
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000)
    }

    /*private fun createAppFolder() {
        val cashierPhotoFolder = File(Environment.getExternalStorageDirectory().path + "/Kaching/Cashier")
        cashierPhotoFolder.mkdirs()

        val productPhotoFolder = File(Environment.getExternalStorageDirectory().path + "/Kaching/Cashier")
        productPhotoFolder.mkdirs()

        val storePhotoFolder = File(Environment.getExternalStorageDirectory().path + "/Kaching/Store")
        storePhotoFolder.mkdirs()
    }*/
}