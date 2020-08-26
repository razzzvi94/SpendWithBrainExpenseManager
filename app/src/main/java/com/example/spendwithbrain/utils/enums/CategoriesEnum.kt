package com.example.spendwithbrain.utils.enums

import android.content.Context
import com.example.spendwithbrain.R

enum class CategoriesEnum {
    NONE {
        override fun getName(context: Context): String {
            return NONE.getName(context)
        }

        override fun getIcon(context: Context): Int {
            return R.drawable.ic_category_income
        }
    };

    abstract fun getName(context: Context): String
    abstract fun getIcon(context: Context): Int
}