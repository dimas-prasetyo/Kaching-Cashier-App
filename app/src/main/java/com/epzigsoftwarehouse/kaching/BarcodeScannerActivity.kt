package com.epzigsoftwarehouse.kaching

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.*
import kotlinx.android.synthetic.main.activity_barcode_scanner.*


class BarcodeScannerActivity : AppCompatActivity() {
    private var codeScanner: CodeScanner? = null
    private var origin: String? = null
    private val CAMERA_REQUEST_CODE: Int =  101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_barcode_scanner)


        codeScanner = CodeScanner(this, scanner_view)

        // Parameters (default values)
        codeScanner!!.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
        codeScanner!!.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
        // ex. listOf(BarcodeFormat.QR_CODE)
        codeScanner!!.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
        codeScanner!!.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
        codeScanner!!.isAutoFocusEnabled = true // Whether to enable auto focus or not
        codeScanner!!.isFlashEnabled = false // Whether to enable flash or not

        // Callbacks
        codeScanner!!.decodeCallback = DecodeCallback {
            runOnUiThread {
                var tesTemp: String = it.text


                val intent = Intent()
                intent.putExtra("barcodeValue", tesTemp)
                setResult(RESULT_OK, intent)
                finish()

                Toast.makeText(this, "Scan result: ${it.text}", Toast.LENGTH_LONG).show()
            }
        }
        codeScanner!!.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
            runOnUiThread {
                Toast.makeText(this, "Camera initialization error: ${it.message}",
                    Toast.LENGTH_LONG).show()
            }
        }

        scanner_view.setOnClickListener {
            codeScanner!!.startPreview()
        }

        /*codeScanner!!.decodeCallback = DecodeCallback { runOnUiThread {
            println("Hasil Scan: " + result)
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        } }*/
        if (!cameraPermissionCheck()){
            requestCameraPermission()
        } else {

        }

    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)
    }

    private fun cameraPermissionCheck(): Boolean {
        val cameraPermission: Boolean = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED

        return cameraPermission
    }

    override fun onResume() {
        super.onResume()
        codeScanner!!.startPreview()
    }

    override fun onPause() {
        codeScanner!!.releaseResources()
        super.onPause()
    }

}