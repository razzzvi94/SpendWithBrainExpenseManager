package com.example.spendwithbrain.screens.register

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.spendwithbrain.R
import com.example.spendwithbrain.screens.login.LoginActivity
import com.example.spendwithbrain.utils.Validations
import com.google.android.material.textfield.TextInputEditText

class RegisterActivity : AppCompatActivity() {
    lateinit var registerBtn: ImageView
    lateinit var nameEditText: TextInputEditText
    lateinit var emailEditText: TextInputEditText
    lateinit var passwordEditText: TextInputEditText
    lateinit var loginBtn: TextView

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