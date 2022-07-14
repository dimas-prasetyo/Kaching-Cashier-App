package com.epzigsoftwarehouse.chachingapp

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_home.*
import java.io.File
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class HomeActivity : AppCompatActivity() {
    private val GALLERY_READ_REQUEST_CODE: Int =  104
    private val GALLERY_WRITE_REQUEST_CODE: Int =  105
    lateinit var temp_setting: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        temp_setting = getSharedPreferences("setting_info", Context.MODE_PRIVATE)
        loadStoreDetail()

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
            val intent = Intent(this, SettingActivity::class.java)
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

        /*if (!writeGalleryPermissionCheck()){
            requestWriteGalleryPermission()
        }*/
        if (!galleryPermissionCheck()){
            requestGalleryPermission()
        }

    }

    private fun loadStoreDetail() {
        try {
            val logoPath = temp_setting.getString("store_logo", "").toString()

            val imgFile = File(logoPath)
            if (imgFile.exists()) {
                val myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
                logo_store.setImageBitmap(myBitmap)
            }

            val storeName = temp_setting.getString("store_name", "").toString()
            txt_store_name.text = storeName
        } catch (e: Exception){

        }
    }

    private fun galleryPermissionCheck(): Boolean {
        val readStoragePermission: Boolean = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED

        return readStoragePermission
    }

    private fun requestGalleryPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), GALLERY_READ_REQUEST_CODE)
    }

    private fun writeGalleryPermissionCheck(): Boolean {
        val readStoragePermission: Boolean = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED

        return readStoragePermission
    }

    private fun requestWriteGalleryPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), GALLERY_WRITE_REQUEST_CODE)
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

    override fun onResume() {
        super.onResume()
        loadStoreDetail()
    }

}