package com.example.spendwithbrain.screens.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.spendwithbrain.R
import com.example.spendwithbrain.db.RoomDb
import com.example.spendwithbrain.db.entities.UserDetails
import com.example.spendwithbrain.screens.main.MainActivity
import com.example.spendwithbrain.screens.register.RegisterActivity
import com.example.spendwithbrain.utils.Constants
import com.example.spendwithbrain.utils.Validations
import com.google.android.material.textfield.TextInputEditText


class LoginActivity : AppCompatActivity() {
    private lateinit var loginBtn: ImageView
    private lateinit var emailEditText: TextInputEditText
    private lateinit var passwordEditText: TextInputEditText
    private lateinit var registerBtn: TextView
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: Editor

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

        sharedPreferences = getSharedPreferences(Constants.MY_SHARED_PREFERENCE, Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
    }

    private val loginOnClickListener =
        View.OnClickListener {
            if (checkInputs()) {
                Thread{
                   var list: List<UserDetails> = RoomDb.db.userDetailsDAO().getUserByEmailAndPassword(emailEditText.text.toString(), passwordEditText.text.toString())
                    if(list.isNotEmpty()){
                        //if user exists in DB
                        editor.putInt(Constants.USER_ID, list[0].userId)
                        editor.putString(Constants.USER_NAME, list[0].userName)
                        editor.commit()
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