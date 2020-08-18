package com.example.spendwithbrain.screens.register

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.spendwithbrain.R
import com.example.spendwithbrain.db.RoomDb
import com.example.spendwithbrain.db.tables.UserDetails
import com.example.spendwithbrain.screens.login.LoginActivity
import com.example.spendwithbrain.utils.Constants
import com.example.spendwithbrain.utils.Validations
import com.google.android.material.textfield.TextInputEditText

class RegisterActivity : AppCompatActivity() {
    private lateinit var registerBtn: ImageView
    private lateinit var nameEditText: TextInputEditText
    private lateinit var emailEditText: TextInputEditText
    private lateinit var passwordEditText: TextInputEditText
    private lateinit var loginBtn: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        initComponents()
    }

    private fun initComponents() {
        registerBtn = findViewById(R.id.btn_register)
        nameEditText = findViewById(R.id.edit_text_name)
        emailEditText = findViewById(R.id.edit_text_email)
        passwordEditText = findViewById(R.id.edit_text_password)
        loginBtn = findViewById(R.id.login_textView_link)
        registerBtn.setOnClickListener(registerOnClickListener)
        loginBtn.setOnClickListener(loginOnClickListener)
    }

    private val loginOnClickListener = View.OnClickListener {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }


    private val registerOnClickListener = View.OnClickListener {
        if (checkInputs()) {
            Thread {
                val userDetails = UserDetails(
                    userEmail = emailEditText.text.toString(),
                    userName = nameEditText.text.toString(),
                    userPassword = passwordEditText.text.toString()
                )
                RoomDb.db.userDetailsDAO().insertOrUpdateUser(userDetails)

//                RoomDb.db.userDetailsDAO().getAllUsers().forEach {
//                    Log.i("@TAG", """"ID is: ${it.userId}"""")
//                    Log.i("@TAG", """"Name is: ${it.userName}"""")
//                    Log.i("@TAG", """"Email is: ${it.userEmail}"""")
//                }
            }.start()

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun checkInputs(): Boolean {
        val errors = StringBuilder("")
        var ok = true
        if (!Validations.nameValidation(nameEditText.text.toString())) {
            ok = false
            errors.append(getString(R.string.wrong_name))
            errors.append("\n")
        }
        if (!Validations.emailValidation(emailEditText.text.toString())) {
            ok = false
            errors.append(getString(R.string.wrong_email))
            errors.append("\n")
        }
        if (!Validations.passwordValidation(passwordEditText.text.toString())) {
            ok = false
            errors.append(getString(R.string.wrong_password))
            errors.append("\n")
        }
        if (!ok) {
            Toast.makeText(this@RegisterActivity, errors, Toast.LENGTH_SHORT).show()
        }
        return ok
    }
}