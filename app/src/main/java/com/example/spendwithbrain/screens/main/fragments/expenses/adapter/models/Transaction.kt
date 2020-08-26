package com.example.spendwithbrain.screens.main.fragments.expenses.adapter.models

data class Transaction(val date: Long, val amount: Double, val category: String, val name: String, var balance: Double)