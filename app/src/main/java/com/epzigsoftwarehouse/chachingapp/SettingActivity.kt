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
            temp_setting.edit().putString("currency", "dollar").apply()
            loadDetailStore()
        }

        layout_language.setOnClickListener {
            temp_setting.edit().putString("currency", "rupiah").apply()
            loadDetailStore()
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

            val tempCurrency = temp_setting.getString("currency", "").toString()
            txt_currency.text = tempCurrency
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

    override fun onResume() {
        super.onResume()
        loadDetailStore()
    }
}