package com.epzigsoftwarehouse.kaching

import android.app.AlertDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.epzigsoftwarehouse.kaching.cashier.Cashier
import com.epzigsoftwarehouse.kaching.database.DatabaseHandler
import kotlinx.android.synthetic.main.activity_cashier_detail.*
import java.io.File

class CashierDetailActivity : AppCompatActivity() {
    private lateinit var cashier_id: String
    private lateinit var infoDialogBox: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cashier_detail)

        cashier_id = intent.getStringExtra("cashier_id").toString()

        if (cashier_id != null){
            loadCashierDetail(cashier_id.toInt())
        }

        btn_back.setOnClickListener {
            onBackPressed()
        }

        cv_btn_edit.setOnClickListener {
            val intent = Intent(this, AddingCashierActivity::class.java)
            intent.putExtra("cashier_id", cashier_id)
            startActivity(intent)
        }

        cv_btn_delete.setOnClickListener {
            val cashierId = cashier_id.toInt()
            showDeleteDialogBox(cashierId)
        }
    }

    private fun loadCashierDetail(cashierId: Int) {
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        val cashier: Cashier = databaseHandler.getCashierDetail(cashierId)

        val imgFile = File(cashier.photo_path)
        if (imgFile.exists()) {
            val myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
            cashier_photo.setImageBitmap(myBitmap)
        }

        txt_cashier_name.text = cashier.name

        if (cashier.position.equals("") || cashier.position ==  null){
            txt_cashier_position.text = "-"
        } else{
            txt_cashier_position.text = cashier.position
        }

        if (cashier.contact.equals("") || cashier.contact ==  null){
            txt_cashier_contact.text = "-"
        } else{
            txt_cashier_contact.text = cashier.contact
        }
    }


    private fun showDeleteDialogBox(cashierId: Int) {
        val layoutInflater = LayoutInflater.from(this)
        val promptView: View = layoutInflater.inflate(R.layout.layout_confirmation, null)
        val layoutShowInfo = AlertDialog.Builder(this)
        layoutShowInfo.setView(promptView)

        val txt_info = promptView.findViewById<TextView>(R.id.txt_main_text) as TextView
        val icon_dialog = promptView.findViewById<ImageView>(R.id.icon_dialog) as ImageView
        val bg_dialog_box = promptView.findViewById<RelativeLayout>(R.id.bg_dialog_box) as RelativeLayout

        val btn_opt_1 = promptView.findViewById<TextView>(R.id.btn_opt_1) as TextView
        val btn_opt_2 = promptView.findViewById<TextView>(R.id.btn_opt_2) as TextView

        bg_dialog_box.setBackgroundColor(ContextCompat.getColor(this, R.color.redFailed))
        icon_dialog.setImageResource(R.drawable.icon_delete)
        txt_info.text = "Do you want to remove this product?"
        // create an alert dialog
        val layoutInput: AlertDialog = layoutShowInfo.create()
        layoutInput.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        //layoutInput.setView(promptView, 0, 0, 0, 0)

        infoDialogBox = layoutShowInfo.create()
        infoDialogBox.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        infoDialogBox.show()

        btn_opt_1.setOnClickListener {
            val cashierId = cashier_id.toInt()
            val databaseHandler: DatabaseHandler = DatabaseHandler(this)
            val deleteCashier = databaseHandler.deleteCashier(cashierId)
            if (deleteCashier > -1) {
                infoDialogBox.dismiss()
                showSuccessDelete()
            } else {
                infoDialogBox.dismiss()
                showFailedDelete()
            }
        }

        btn_opt_2.setOnClickListener {
            infoDialogBox.dismiss()
        }

    }

    private fun showSuccessDelete() {
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

        txt_info.text = "Successfully delete product"

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

    private fun showFailedDelete() {
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

        txt_info.text = "Failed to delete product"

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

    override fun onResume() {
        loadCashierDetail(cashier_id.toInt())
        super.onResume()
    }
}