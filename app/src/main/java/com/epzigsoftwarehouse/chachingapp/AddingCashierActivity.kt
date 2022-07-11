package com.epzigsoftwarehouse.chachingapp

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.epzigsoftwarehouse.chachingapp.cashier.Cashier
import com.epzigsoftwarehouse.chachingapp.database.DatabaseHandler
import kotlinx.android.synthetic.main.activity_adding_cashier.*
import net.alhazmy13.mediapicker.Image.ImagePicker
import java.io.File

class AddingCashierActivity : AppCompatActivity() {
    private lateinit var name_input: String
    private lateinit var position_input: String
    private lateinit var contact_input: String
    private lateinit var photo_path_input: String
    private var statusCashier = "create"
    private var idCashier= 0

    private val GALLERY_READ_REQUEST_CODE: Int =  104
    private var imagePath = ""
    private lateinit var infoDialogBox: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adding_cashier)

        cv_btn_delete.visibility = View.GONE
        try {
            var cashier_id = intent.getStringExtra("cashier_id").toString().toInt()

            if (cashier_id != null){
                loadCashierDetail(cashier_id)
            }
            statusCashier = "edit"
            idCashier = cashier_id
        } catch (e: Exception){

        }

        btn_back.setOnClickListener {
            onBackPressed()
        }

        try {
            var cashier_id = intent.getStringExtra("cashier_id").toString().toInt()

            if (cashier_id != null){
                loadCashierDetail(cashier_id)
            }
            statusCashier = "edit"
            idCashier = cashier_id
        } catch (e: Exception){

        }

        cv_btn_add.setOnClickListener {
            if (!galleryPermissionCheck()){
                requestGalleryPermission()
            } else {
                pickImage()
            }
        }

        btn_done.setOnClickListener {
            if (statusCashier.equals("edit")){
                val databaseHandler: DatabaseHandler = DatabaseHandler(this)
                if (checkForm() == true){
                    try {
                        val status = databaseHandler.updateCashier(Cashier(idCashier, name_input, position_input, photo_path_input,contact_input))
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
                        val status = databaseHandler.addCashier(Cashier(0, name_input, position_input, photo_path_input,contact_input))
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
    }

    private fun loadCashierDetail(cashierId: Int) {
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        val cashier: Cashier = databaseHandler.getCashierDetail(cashierId)

        val imgFile = File(cashier.photo_path)
        if (imgFile.exists()) {
            imagePath = cashier.photo_path
            val myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
            cashier_photo.setImageBitmap(myBitmap)

            btn_add.setImageResource(R.drawable.icon_edit)
            cv_btn_delete.visibility = View.VISIBLE
        }

        input_name.setText(cashier.name)

        if (cashier.position.equals("") || cashier.position ==  null){
        } else{
            input_position.setText(cashier.position)
        }

        if (cashier.contact.equals("") || cashier.contact ==  null){

        } else{
            input_contact.setText(cashier.contact)
        }

    }

    private fun checkForm(): Boolean {
        if (1 == 1){
            if (input_name.text.equals("") || input_name.text == null){
                input_name.setError("Please input email")
                return false
            } else {
                name_input = input_name.getText().toString()

                if (input_position.getText().toString() == "" || input_position.getText() == null){
                    position_input = ""
                } else {
                    position_input = input_position.getText().toString()
                }

                if (input_contact.getText().toString() == "" || input_contact.getText() == null){
                    contact_input = ""
                } else {
                    contact_input = input_contact.getText().toString()
                }

                photo_path_input = imagePath

                return true
            }
        } else {
            return false
        }
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

        if (statusCashier.equals("edit")){
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

        if (statusCashier.equals("edit")){
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

    private fun galleryPermissionCheck(): Boolean {
        val readStoragePermission: Boolean = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED

        return readStoragePermission
    }

    private fun requestGalleryPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), GALLERY_READ_REQUEST_CODE)
    }

    private fun pickImage() {
        ImagePicker.Builder(this)
            .mode(ImagePicker.Mode.GALLERY)
            .allowMultipleImages(true)
            .compressLevel(ImagePicker.ComperesLevel.MEDIUM)
            .directory(Environment.getExternalStorageDirectory().toString() + "/Kaching/Cashier/")
            .extension(ImagePicker.Extension.JPG)
            .allowOnlineImages(true)
            .scale(600, 600)
            .allowMultipleImages(true)
            .enableDebuggingMode(true)
            .build()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode === ImagePicker.IMAGE_PICKER_REQUEST_CODE && resultCode === RESULT_OK) {
            val mPaths: List<String> = data?.getStringArrayListExtra(ImagePicker.EXTRA_IMAGE_PATH)!!
            imagePath = mPaths[0]

            val imgFile = File(imagePath)
            if (imgFile.exists()) {
                val myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
                cashier_photo.setImageBitmap(myBitmap)

                btn_add.setImageResource(R.drawable.icon_edit)
                cv_btn_delete.visibility = View.VISIBLE
            }
        }
    }
}