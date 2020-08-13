package com.example.spendwithbrain.utils

import android.util.Patterns
import java.util.regex.Pattern

object Validations {
        fun emailValidation(email: String): Boolean {
            return !email.isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }
        fun passwordValidation(password: String): Boolean {
            return !password.isNullOrEmpty() && Pattern.matches(
                "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!.*()_~])(?=\\S+$).{6,}$",
                password
            )
        }
}