package com.epzigsoftwarehouse.chachingapp

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_setting.*
import net.alhazmy13.mediapicker.FileProcessing.getPath
import net.alhazmy13.mediapicker.Image.ImagePicker
import java.io.File

class SettingActivity : AppCompatActivity() {
    private val GALLERY_READ_REQUEST_CODE: Int =  104
    private val GALLERY_WRITE_REQUEST_CODE: Int =  105
    private lateinit var storeName: String
    private lateinit var storeLogoPath: String
    private lateinit var storeCurrency: String
    private var storeTax = 0
    lateinit var temp_setting: SharedPreferences
    private lateinit var infoDialogBox: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        temp_setting = getSharedPreferences("setting_info", Context.MODE_PRIVATE)

        loadDetailStore()
        cv_btn_camera.setOnClickListener {
            if (!galleryPermissionCheck()){
                requestGalleryPermission()
            } else {
                //takeImageFromGallery()
                pickImage()
            }
        }

        btn_back.setOnClickListener {
            onBackPressed()
        }

        layout_currency.setOnClickListener {
            showCurrencyDialogBox()
        }

        layout_language.setOnClickListener {

        }

        layout_tax.setOnClickListener {
            showChangeTaxDialogBox()
        }

        btn_change_name.setOnClickListener {
            showChangeNameDialogBox()
        }
    }

    private fun loadDetailStore() {
        try {
            val logoPath = temp_setting.getString("store_logo", "").toString()

            val imgFile = File(logoPath)
            if (imgFile.exists()) {
                val myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
                logo_store.setImageBitmap(myBitmap)
            }

            storeName = temp_setting.getString("store_name", "").toString()
            txt_store_name.text = storeName

            val tempLanguage = temp_setting.getString("language", "").toString()
            txt_language.text = tempLanguage

            storeCurrency = temp_setting.getString("currency", "").toString()
            txt_currency.text = storeCurrency

            storeTax = temp_setting.getString("tax", "").toString().toInt()
            txt_tax.text = storeTax.toString() + " %"
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

    private fun takeImageFromGallery(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, GALLERY_READ_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        /*if (requestCode == 104 && resultCode == RESULT_OK) {
            val selectedImageUri: Uri = data?.data!!
            val picturePath = getPath(this, selectedImageUri)
            savePhotoPath = picturePath.toString()

            val imgFile = File(savePhotoPath)
            if (imgFile.exists()) {
                val myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
                logo_store.setImageBitmap(myBitmap)
            }
        }*/


        if (requestCode === ImagePicker.IMAGE_PICKER_REQUEST_CODE && resultCode === RESULT_OK) {
            val mPaths: List<String> = data?.getStringArrayListExtra(ImagePicker.EXTRA_IMAGE_PATH)!!
            storeLogoPath = mPaths[0]

            val imgFile = File(storeLogoPath)
            if (imgFile.exists()) {
                val myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
                logo_store.setImageBitmap(myBitmap)

                temp_setting.edit().putString("store_logo", storeLogoPath).apply()
            }
        }
    }

    private fun pickImage() {
        ImagePicker.Builder(this)
            .mode(ImagePicker.Mode.GALLERY)
            .allowMultipleImages(true)
            .compressLevel(ImagePicker.ComperesLevel.MEDIUM)
            .directory(Environment.getExternalStorageDirectory().toString() + "/Kaching/Store/")
            .extension(ImagePicker.Extension.JPG)
            .allowOnlineImages(true)
            .scale(600, 600)
            .allowMultipleImages(true)
            .enableDebuggingMode(true)
            .build()
    }

    private fun showChangeNameDialogBox() {
        val layoutInflater = LayoutInflater.from(this)
        val promptView: View = layoutInflater.inflate(R.layout.layout_input_name, null)
        val layoutShowInfo = AlertDialog.Builder(this)
        layoutShowInfo.setView(promptView)

        val input_store_name = promptView.findViewById<EditText>(R.id.input_store_name) as EditText

        val btn_save = promptView.findViewById<TextView>(R.id.btn_save) as TextView
        val btn_cancel = promptView.findViewById<TextView>(R.id.btn_cancel) as TextView

        input_store_name.setText(storeName)
        // create an alert dialog

        val layoutInput: AlertDialog = layoutShowInfo.create()
        layoutInput.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        //layoutInput.setView(promptView, 0, 0, 0, 0)

        infoDialogBox = layoutShowInfo.create()
        infoDialogBox.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        infoDialogBox.show()

        btn_save.setOnClickListener {
            temp_setting.edit().putString("store_name", input_store_name.getText().toString()).apply()
            infoDialogBox.dismiss()
            loadDetailStore()
        }

        btn_cancel.setOnClickListener {
            infoDialogBox.dismiss()
        }

    }

    private fun showChangeTaxDialogBox() {
        val layoutInflater = LayoutInflater.from(this)
        val promptView: View = layoutInflater.inflate(R.layout.layout_input_tax, null)
        val layoutShowInfo = AlertDialog.Builder(this)
        layoutShowInfo.setView(promptView)

        val input_tax = promptView.findViewById<EditText>(R.id.input_tax) as EditText

        val btn_save = promptView.findViewById<TextView>(R.id.btn_save) as TextView
        val btn_cancel = promptView.findViewById<TextView>(R.id.btn_cancel) as TextView

        input_tax.setText(storeTax.toString())
        // create an alert dialog

        val layoutInput: AlertDialog = layoutShowInfo.create()
        layoutInput.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        //layoutInput.setView(promptView, 0, 0, 0, 0)

        infoDialogBox = layoutShowInfo.create()
        infoDialogBox.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        infoDialogBox.show()

        btn_save.setOnClickListener {
            try {
                val tempInput = input_tax.getText().toString().toInt()
                if (tempInput <= 100 && tempInput >= 0){
                    temp_setting.edit().putString("tax", input_tax.getText().toString()).apply()
                    infoDialogBox.dismiss()
                    loadDetailStore()
                }
            } catch (e: Exception){}

        }

        btn_cancel.setOnClickListener {
            infoDialogBox.dismiss()
        }

    }

    private fun showCurrencyDialogBox() {
        val layoutInflater = LayoutInflater.from(this)
        val promptView: View = layoutInflater.inflate(R.layout.layout_change_currency, null)
        val layoutShowInfo = AlertDialog.Builder(this)
        layoutShowInfo.setView(promptView)

        val icon_done_dollar = promptView.findViewById<ImageView>(R.id.icon_done_dollar) as ImageView
        val icon_done_rupiah = promptView.findViewById<ImageView>(R.id.icon_done_rupiah) as ImageView

        val txt_dollar = promptView.findViewById<TextView>(R.id.txt_dollar) as TextView
        val txt_rupiah = promptView.findViewById<TextView>(R.id.txt_rupiah) as TextView

        // create an alert dialog

        if (storeCurrency.equals("dollar")){
            icon_done_dollar.visibility = View.VISIBLE
            icon_done_rupiah.visibility = View.INVISIBLE

            txt_dollar.setTextColor(ContextCompat.getColor(this, R.color.primary_dark))
            txt_rupiah.setTextColor(ContextCompat.getColor(this, R.color.colorGray))

        } else if (storeCurrency.equals("rupiah")){
            icon_done_dollar.visibility = View.INVISIBLE
            icon_done_rupiah.visibility = View.VISIBLE

            txt_dollar.setTextColor(ContextCompat.getColor(this, R.color.colorGray))
            txt_rupiah.setTextColor(ContextCompat.getColor(this, R.color.primary_dark))
        }
        val layoutInput: AlertDialog = layoutShowInfo.create()
        layoutInput.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        //layoutInput.setView(promptView, 0, 0, 0, 0)

        infoDialogBox = layoutShowInfo.create()
        infoDialogBox.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        infoDialogBox.show()

        txt_dollar.setOnClickListener {
            temp_setting.edit().putString("currency", "dollar").apply()
            infoDialogBox.dismiss()
            loadDetailStore()
        }

        txt_rupiah.setOnClickListener {
            temp_setting.edit().putString("currency", "rupiah").apply()
            infoDialogBox.dismiss()
            loadDetailStore()
        }

    }

    override fun onResume() {
        super.onResume()
        loadDetailStore()
    }
}