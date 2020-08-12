package com.example.spendwithbrain.screens.main

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.spendwithbrain.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textView = findViewById<TextView>(R.id.mainScreenTxtView)
        textView.setOnClickListener {
            Toast.makeText(this@MainActivity, "COMPUTER SCIENCE PORTAL", Toast.LENGTH_LONG).show()
            textView.setText("My new message")
        }
    }
}