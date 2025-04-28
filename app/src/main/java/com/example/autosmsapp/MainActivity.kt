package com.example.autosmsapp

import android.os.Bundle
import android.widget.EditText
import android.widget.Button
import android.widget.Spinner
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    lateinit var editMessage: EditText
    lateinit var editDays: EditText
    lateinit var simSpinner: Spinner
    lateinit var saveBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
}

