package com.epzigsoftwarehouse.chachingapp

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.epzigsoftwarehouse.chachingapp.database.DatabaseHandler
import com.epzigsoftwarehouse.chachingapp.products.Product
import kotlinx.android.synthetic.main.activity_adding_menu.*
import net.alhazmy13.mediapicker.Image.ImagePicker
import java.io.File


class AddingMenuActivity : AppCompatActivity() {
    private lateinit var category_input: String
    private lateinit var name_input: String
    private var price_input = 0.0
    private var proportion_input = 0.0
    private var amount_input = 0
    private lateinit var unit_input: String
    private lateinit var photo_path_input: String
    private lateinit var barcode_input: String

    private val GALLERY_READ_REQUEST_CODE: Int =  104
    private var imagePath = ""

    private lateinit var infoDialogBox: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adding_menu)

        btn_done.setOnClickListener {
            val databaseHandler: DatabaseHandler = DatabaseHandler(this)
            if (checkForm() == true){
                try {
                    val status = databaseHandler.addProduct(Product(0, category_input, name_input, price_input, proportion_input, unit_input, amount_input, photo_path_input, barcode_input, 0))
                    if (status > -1) {
                        // Sukses
                        println("Sukses Menambahkan data")
                    }
                } catch (e: Exception){

                }
            }
        }

        cv_button_1.setOnClickListener {
            if (!galleryPermissionCheck()){
                requestGalleryPermission()
            } else {
                //takeImageFromGallery()
                pickImage()
            }
        }

        cv_button_2.setOnClickListener {
            deleteProductImage()
        }
        cv_button_2.visibility = View.GONE

        cv_info_proportion.setOnClickListener {
            showInfoProportion()
        }

        cv_button_scan_barcode.setOnClickListener {
            /*val intent = Intent(this, BarcodeScannerActivity::class.java)
            startActivity(intent)*/

            val i = Intent(this, BarcodeScannerActivity::class.java)
            startActivityForResult(i, 100)
        }
    }

    private fun showInfoProportion() {
        val layoutInflater = LayoutInflater.from(this)
        val promptView: View = layoutInflater.inflate(R.layout.layout_info_dialog_box, null)
        val layoutShowInfo = AlertDialog.Builder(this)
        layoutShowInfo.setView(promptView)

        val txt_info = promptView.findViewById<TextView>(R.id.txt_info) as TextView
        txt_info.text = "Show Info Tentang Proportion dan Unit"

        // create an alert dialog
        val layoutInput: AlertDialog = layoutShowInfo.create()
        layoutInput.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        //layoutInput.setView(promptView, 0, 0, 0, 0)

        // create an loading dialog
        infoDialogBox = layoutShowInfo.create()
        infoDialogBox.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        infoDialogBox.show()
    }

    private fun deleteProductImage() {
        val fdelete: File = File(imagePath)
        if (fdelete.exists()) {
            if (fdelete.delete()) {
                System.out.println("file Deleted :$imagePath")
            } else {
                System.out.println("file not Deleted :$imagePath")
            }
        }

        imagePath = ""
        product_photo.setImageResource(R.drawable.icon_delete)
        button_1.setImageResource(R.drawable.icon_add)
        cv_button_2.visibility = View.GONE
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

    private fun pickImage() {
        ImagePicker.Builder(this)
            .mode(ImagePicker.Mode.GALLERY)
            .allowMultipleImages(true)
            .compressLevel(ImagePicker.ComperesLevel.MEDIUM)
            .directory(Environment.getExternalStorageDirectory().toString() + "/Chaching/Images/")
            .extension(ImagePicker.Extension.JPG)
            .allowOnlineImages(true)
            .scale(600, 600)
            .allowMultipleImages(true)
            .enableDebuggingMode(true)
            .build()
    }

    private fun checkForm(): Boolean {
        if (1 == 1){
            if (input_category.text.equals("") || input_category.text == null){
                input_category.setError("Please input email")
                return false
            } else if (input_name.text.equals("") || input_name.text == null){
                input_name.setError("Please input correct email")
                return false
            } else if (input_price.text.equals("") || input_price.text == null){
                input_price.setError("Please input Password")
                return false
            }  else {
                category_input = input_category.getText().toString()
                name_input = input_name.getText().toString()
                price_input = input_price.getText().toString().toDouble()

                if (input_proportion.getText().toString() == "" || input_proportion.getText() == null){
                    proportion_input = 0.0
                } else {
                    proportion_input = input_proportion.getText().toString().toDouble()
                }

                if (input_unit.getText().toString() == "" || input_unit.getText() == null){
                    unit_input = ""
                } else {
                    unit_input = input_unit.getText().toString()
                }

                if (input_amount.getText().toString() == "" || input_amount.getText() == null){
                    amount_input = 1
                } else {
                    amount_input = input_amount.getText().toString().toInt()
                }

                photo_path_input = imagePath
                barcode_input = input_barcode.getText().toString()

                return true
            }
        } else {
            return false
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                val myStr = data?.getStringExtra("barcodeValue")
                input_barcode.setText(myStr)
            }
        }

        if (requestCode === ImagePicker.IMAGE_PICKER_REQUEST_CODE && resultCode === RESULT_OK) {
            val mPaths: List<String> = data?.getStringArrayListExtra(ImagePicker.EXTRA_IMAGE_PATH)!!
            imagePath = mPaths[0]

            val imgFile = File(imagePath)
            if (imgFile.exists()) {
                val myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
                product_photo.setImageBitmap(myBitmap)


                button_1.setImageResource(R.drawable.icon_edit)
                cv_button_2.visibility = View.VISIBLE
            }
        }
    }
}