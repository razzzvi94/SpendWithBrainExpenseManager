package com.example.spendwithbrain.screens.spalsh

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.spendwithbrain.R
import com.example.spendwithbrain.screens.login.LoginActivity
import com.example.spendwithbrain.screens.main.MainActivity
import com.example.spendwithbrain.utils.Constants
import com.example.spendwithbrain.utils.SharedPrefUtils

class SplashActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        initComponents()
        splashDelay()
    }

    private fun initComponents() {
        SharedPrefUtils.init(applicationContext)
    }

    private fun splashDelay() {
        //if user logged in go to MainActivity
        if (SharedPrefUtils.hasKey(Constants.USER_ID)) {
            Handler().postDelayed({
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }, Constants.SPLASH_TIME_OUT)
        } else {            // else go to Login page
            Handler().postDelayed({
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }, Constants.SPLASH_TIME_OUT)
        }
    }
}