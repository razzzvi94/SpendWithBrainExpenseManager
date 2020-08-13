package com.example.spendwithbrain.screens.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.spendwithbrain.R
import com.example.spendwithbrain.screens.main.MainActivity
import com.example.spendwithbrain.utils.Validations
import com.google.android.material.textfield.TextInputEditText


class LoginActivity : AppCompatActivity() {
    lateinit var loginBtn: ImageView
    lateinit var emailEditText: TextInputEditText
    lateinit var passwordEditText: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initComponents()
    }

    private fun initComponents() {
        loginBtn = findViewById(R.id.btn_login)
        emailEditText = findViewById(R.id.edit_text_email)
        passwordEditText = findViewById(R.id.edit_text_password)
        loginBtn.setOnClickListener(loginOnClickListener)
    }

    private val loginOnClickListener =
        View.OnClickListener {
            if (checkInputs()) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

    private fun checkInputs(): Boolean {
        val errors = StringBuilder("")
        var ok = true
        if (!Validations.emailValidation(emailEditText.text.toString())) {
            ok = false
            errors.append("Email address wrong\n")
        }
        if (!Validations.passwordValidation(passwordEditText.text.toString())) {
            ok = false
            errors.append("Password wrong\n")
        }
        if (!ok) {
            Toast.makeText(this@LoginActivity, errors, Toast.LENGTH_SHORT).show()
        }
        return ok
    }
}