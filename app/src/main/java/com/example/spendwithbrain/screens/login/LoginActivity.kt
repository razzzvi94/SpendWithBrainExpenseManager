package com.example.spendwithbrain.screens.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.spendwithbrain.R
import com.example.spendwithbrain.db.ExpensesManagerDB
import com.example.spendwithbrain.screens.main.MainActivity
import com.example.spendwithbrain.screens.register.RegisterActivity
import com.example.spendwithbrain.utils.Validations
import com.google.android.material.textfield.TextInputEditText


class LoginActivity : AppCompatActivity() {
    lateinit var loginBtn: ImageView
    lateinit var emailEditText: TextInputEditText
    lateinit var passwordEditText: TextInputEditText
    lateinit var registerBtn: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initComponents()
    }

    private fun initComponents() {
        loginBtn = findViewById(R.id.btn_login)
        emailEditText = findViewById(R.id.edit_text_email)
        passwordEditText = findViewById(R.id.edit_text_password)
        registerBtn = findViewById(R.id.register_textView_link)
        loginBtn.setOnClickListener(loginOnClickListener)
        registerBtn.setOnClickListener(registerOnClickListener)
    }

    private val loginOnClickListener =
        View.OnClickListener {
            if (checkInputs()) {
                Thread{
                    val db = Room.databaseBuilder(
                        applicationContext,
                        ExpensesManagerDB::class.java,
                        "ExpenseManager.db"
                    ).build()
                    if(db.userDetailsDAO().getUserByEmailAndPassword(emailEditText.text.toString(), passwordEditText.text.toString()).isNotEmpty()){
                        //if user exists in DB
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    else{
                        //if user DOESN'T exist in DB
                        val intent = Intent(this, RegisterActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }.start()
            }
        }

    private val registerOnClickListener = View.OnClickListener {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
        finish()

    }

    private fun checkInputs(): Boolean {
        val errors = StringBuilder("")
        var ok = true
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
            Toast.makeText(this@LoginActivity, errors, Toast.LENGTH_SHORT).show()
        }
        return ok
    }
}