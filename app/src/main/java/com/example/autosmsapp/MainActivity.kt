package com.example.autosmsapp

import android.Manifest
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.CallLog
import android.util.Log
import android.widget.EditText
import android.widget.Button
import android.widget.Spinner
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

class MainActivity : AppCompatActivity() {
    lateinit var editMessage: EditText
    lateinit var editDays: EditText
    lateinit var simSpinner: Spinner
    lateinit var saveBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val permissions = arrayOf(Manifest.permission.READ_CALL_LOG, Manifest.permission.SEND_SMS)

        // checking call log and send sms permission
        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissions, 101)
        }
        else {
            //if permissions are granted then do magic
            getLog()
            //sendsms()
        }

        editMessage = findViewById(R.id.editMessage)
        editDays = findViewById(R.id.editDays)
        simSpinner = findViewById(R.id.simSpinner)
        saveBtn = findViewById(R.id.btnSave)

        simSpinner.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            listOf("SIM 1", "SIM 2")
        )

        saveBtn.setOnClickListener {
            val message = editMessage.text.toString()
            val sim = simSpinner.selectedItemPosition
            val days = editDays.text.toString().toIntOrNull() ?: 0

            val prefs = getSharedPreferences("AutoSMS", MODE_PRIVATE)
            prefs.edit().apply {
                putString("message", message)
                putInt("sim", sim)
                putInt("days", days)
                apply()
            }
            Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show()
        }
    }

    //this runs after the permissions window
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.d("PERMISSION","Im here")
        if (requestCode==101 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Log.d("PERMISSION","Im here")
        }
            getLog()
    }

    var cols = arrayOf (
        CallLog.Calls._ID,
        CallLog.Calls.NUMBER,
        CallLog.Calls.DURATION
    )

    private fun getLog() {
        Log.d("CALL_LOG", "Calls are being accessed")
        var cursor = contentResolver.query(CallLog.Calls.CONTENT_URI, cols, null,null,"${CallLog.Calls.LAST_MODIFIED} DESC")
        cursor?.use {
            while (it.moveToNext()) {
                val id = it.getString(0)
                val number = it.getString(1)
                val duration = it.getString(2)
                Log.d("Contacts", "Number: $number, Duration: $duration")
            }
        }
    }
}