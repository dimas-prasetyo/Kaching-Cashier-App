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
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.epzigsoftwarehouse.chachingapp.database.DatabaseHandler
import com.epzigsoftwarehouse.chachingapp.products.Product
import kotlinx.android.synthetic.main.activity_adding_menu.*
import net.alhazmy13.mediapicker.Image.ImagePicker
import java.io.File
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*


class AddingMenuActivity : AppCompatActivity() {
    private lateinit var category_input: String
    private lateinit var name_input: String
    private var price_input = 0.0
    private var proportion_input = 0.0
    private var amount_input = 0
    private lateinit var unit_input: String
    private lateinit var photo_path_input: String
    private lateinit var barcode_input: String
    private lateinit var currency: String
    //private var product_id by Delegates.notNull<Int>()
    private var statusProduct = "create"
    private var idProduct = 0

    private val GALLERY_READ_REQUEST_CODE: Int =  104
    private var imagePath = ""

    private lateinit var infoDialogBox: AlertDialog
    lateinit var temp_setting: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adding_menu)

        try {
            var product_id = intent.getStringExtra("product_id").toString().toInt()

            if (product_id != null){
                loadMenuDetail(product_id)
            }
            statusProduct = "edit"
            idProduct = product_id
        } catch (e: Exception){

        }

        temp_setting = getSharedPreferences("setting_info", Context.MODE_PRIVATE)
        currency = temp_setting.getString("currency", "").toString()

        if (currency.equals("dollar")){
            txt_currency.text = "$"
        }

        btn_done.setOnClickListener {
            if (statusProduct.equals("edit")){
                val databaseHandler: DatabaseHandler = DatabaseHandler(this)
                if (checkForm() == true){
                    try {
                        val status = databaseHandler.updateProduct(Product(idProduct, category_input, name_input, price_input, proportion_input, unit_input, amount_input, photo_path_input, barcode_input, 0))
                        if (status > -1) {
                            showSuccessDialog()
                        } else {
                            showFailedDialog()
                        }
                    } catch (e: Exception){

                    }
                }
            } else {
                val databaseHandler: DatabaseHandler = DatabaseHandler(this)
                if (checkForm() == true){
                    try {
                        val status = databaseHandler.addProduct(Product(0, category_input, name_input, price_input, proportion_input, unit_input, amount_input, photo_path_input, barcode_input, 0))
                        if (status > -1) {
                            showSuccessDialog()
                        } else {
                            showFailedDialog()
                        }
                    } catch (e: Exception){

                    }
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

        cv_info_unit.setOnClickListener {
            showInfoUnit()
        }

        btn_back.setOnClickListener {
            onBackPressed()
        }

        cv_button_scan_barcode.setOnClickListener {
            val i = Intent(this, BarcodeScannerActivity::class.java)
            startActivityForResult(i, 100)
        }
/*
        input_price.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                input_price.removeTextChangedListener(this)

                try {
                    var originalString = s.toString()
                    val longval: Long

                    val re = Regex("[^A-Za-z0-9 ]")
                    originalString = re.replace(originalString, "")

                    longval = originalString.toLong()

                    val formatter = NumberFormat.getInstance(Locale.US) as DecimalFormat
                    formatter.applyPattern("#,###,###,###")
                    var formattedString = formatter.format(longval)

                    if (formattedString.contains(",")) {
                        formattedString = formattedString.replace(",".toRegex(), ".")
                    }
                    //setting text after format to EditText
                    input_price.setText(formattedString)
                    input_price.setSelection(input_price.getText().length)
                } catch (nfe: NumberFormatException) {
                    nfe.printStackTrace()
                }

                input_price.addTextChangedListener(this)
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }
        })

        input_proportion.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                input_proportion.removeTextChangedListener(this)

                try {
                    var originalString = s.toString()
                    val longval: Long

                    val re = Regex("[^A-Za-z0-9 ]")
                    originalString = re.replace(originalString, "")

                    longval = originalString.toLong()

                    val formatter = NumberFormat.getInstance(Locale.US) as DecimalFormat
                    formatter.applyPattern("#,###,###,###")
                    var formattedString = formatter.format(longval)

                    if (formattedString.contains(",")) {
                        formattedString = formattedString.replace(",".toRegex(), ".")
                    }
                    //setting text after format to EditText
                    input_proportion.setText(formattedString)
                    input_proportion.setSelection(input_proportion.getText().length)
                } catch (nfe: NumberFormatException) {
                    nfe.printStackTrace()
                }

                input_proportion.addTextChangedListener(this)
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }
        })
        */
    }

    private fun loadMenuDetail(productId: Int) {
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        //calling the viewEmployee method of DatabaseHandler class to read the records
        val product: Product = databaseHandler.getProduct(productId)

        input_category.setText(product.category)
        input_name.setText(product.name)
        input_price.setText(product.price.toString())
        input_proportion.setText(product.proportion.toString())
        input_unit.setText(product.unit)
        input_amount.setText(product.amount.toString())
        input_barcode.setText(product.barcode)

        val imgFile = File(product.photo_path)
        if (imgFile.exists()) {
            imagePath = product.photo_path
            val myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
            product_photo.setImageBitmap(myBitmap)
        }
    }

    private fun showInfoProportion() {
        val layoutInflater = LayoutInflater.from(this)
        val promptView: View = layoutInflater.inflate(R.layout.layout_info_dialog_box, null)
        val layoutShowInfo = AlertDialog.Builder(this)
        layoutShowInfo.setView(promptView)

        val txt_info = promptView.findViewById<TextView>(R.id.txt_main_text) as TextView
        val icon_dialog = promptView.findViewById<ImageView>(R.id.icon_dialog) as ImageView
        val bg_dialog_box = promptView.findViewById<RelativeLayout>(R.id.bg_dialog_box) as RelativeLayout

        bg_dialog_box.setBackgroundColor(ContextCompat.getColor(this, R.color.yellowInfo))
        icon_dialog.setImageResource(R.drawable.icon_info_outline)

        txt_info.text = "Proportion is the size or portion of the item you want to add (optional)."

        // create an alert dialog
        val layoutInput: AlertDialog = layoutShowInfo.create()
        layoutInput.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        //layoutInput.setView(promptView, 0, 0, 0, 0)

        // create an loading dialog
        infoDialogBox = layoutShowInfo.create()
        infoDialogBox.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        infoDialogBox.show()
    }

    private fun showInfoUnit() {
        val layoutInflater = LayoutInflater.from(this)
        val promptView: View = layoutInflater.inflate(R.layout.layout_info_dialog_box, null)
        val layoutShowInfo = AlertDialog.Builder(this)
        layoutShowInfo.setView(promptView)

        val txt_info = promptView.findViewById<TextView>(R.id.txt_main_text) as TextView
        val icon_dialog = promptView.findViewById<ImageView>(R.id.icon_dialog) as ImageView
        val bg_dialog_box = promptView.findViewById<RelativeLayout>(R.id.bg_dialog_box) as RelativeLayout

        bg_dialog_box.setBackgroundColor(ContextCompat.getColor(this, R.color.yellowInfo))
        icon_dialog.setImageResource(R.drawable.icon_info_outline)

        txt_info.text = "Unit is the unit of measure of the proportion value (optional)."

        // create an alert dialog
        val layoutInput: AlertDialog = layoutShowInfo.create()
        layoutInput.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        //layoutInput.setView(promptView, 0, 0, 0, 0)

        // create an loading dialog
        infoDialogBox = layoutShowInfo.create()
        infoDialogBox.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        infoDialogBox.show()
    }

    private fun showSuccessDialog() {
        val layoutInflater = LayoutInflater.from(this)
        val promptView: View = layoutInflater.inflate(R.layout.layout_info_dialog_box, null)
        val layoutShowInfo = AlertDialog.Builder(this)
        layoutShowInfo.setCancelable(false)
        layoutShowInfo.setView(promptView)

        val txt_info = promptView.findViewById<TextView>(R.id.txt_main_text) as TextView
        val icon_dialog = promptView.findViewById<ImageView>(R.id.icon_dialog) as ImageView
        val bg_dialog_box = promptView.findViewById<RelativeLayout>(R.id.bg_dialog_box) as RelativeLayout

        bg_dialog_box.setBackgroundColor(ContextCompat.getColor(this, R.color.greenSuccess))
        icon_dialog.setImageResource(R.drawable.icon_done)

        if (statusProduct.equals("edit")){
            txt_info.text = "Successfully changed data"
        } else {
            txt_info.text = "Successfully added data"
        }

        // create an alert dialog
        val layoutInput: AlertDialog = layoutShowInfo.create()
        layoutInput.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        //layoutInput.setView(promptView, 0, 0, 0, 0)

        // create an loading dialog
        infoDialogBox = layoutShowInfo.create()
        infoDialogBox.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        infoDialogBox.show()

        Handler().postDelayed({
            infoDialogBox.dismiss()
            onBackPressed()
        }, 2000)
    }

    private fun showFailedDialog() {
        val layoutInflater = LayoutInflater.from(this)
        val promptView: View = layoutInflater.inflate(R.layout.layout_info_dialog_box, null)
        val layoutShowInfo = AlertDialog.Builder(this)
        layoutShowInfo.setCancelable(false)
        layoutShowInfo.setView(promptView)

        val txt_info = promptView.findViewById<TextView>(R.id.txt_main_text) as TextView
        val icon_dialog = promptView.findViewById<ImageView>(R.id.icon_dialog) as ImageView
        val bg_dialog_box = promptView.findViewById<RelativeLayout>(R.id.bg_dialog_box) as RelativeLayout

        bg_dialog_box.setBackgroundColor(ContextCompat.getColor(this, R.color.redFailed))
        icon_dialog.setImageResource(R.drawable.icon_failed)

        if (statusProduct.equals("edit")){
            txt_info.text = "Failed to changed data"
        } else {
            txt_info.text = "Failed to added data"
        }

        // create an alert dialog
        val layoutInput: AlertDialog = layoutShowInfo.create()
        layoutInput.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        //layoutInput.setView(promptView, 0, 0, 0, 0)

        // create an loading dialog
        infoDialogBox = layoutShowInfo.create()
        infoDialogBox.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        infoDialogBox.show()

        Handler().postDelayed({
            infoDialogBox.dismiss()
        }, 2000)
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

                if (input_amount.getText().toString() == "" || input_amount.getText() == null || input_amount.getText().toString() == "0"){
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