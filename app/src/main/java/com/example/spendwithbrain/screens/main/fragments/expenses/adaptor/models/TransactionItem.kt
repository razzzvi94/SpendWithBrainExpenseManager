package com.example.spendwithbrain.screens.main.fragments.expenses.adaptor.models

data class TransactionItem(
    val transactionDate: Long,
    val transactionCategory: String,
    val transactionName: String,
    val transactionAmount: Long,
    val balance: Long
)