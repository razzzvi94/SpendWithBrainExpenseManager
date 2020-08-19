package com.example.spendwithbrain.utils

import java.text.SimpleDateFormat
import java.util.*

object ConverterFunctions {
    fun gmtToLocalTime(timestamp: Long): String{
        val formatter = SimpleDateFormat("yyyy/MM/dd' at 'HH:mm")
        return formatter.format( Date(timestamp))
    }
}