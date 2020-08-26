package com.example.spendwithbrain.screens.main.fragments.expenses.adapter

import com.example.spendwithbrain.screens.main.fragments.expenses.adapter.models.Transaction

interface TransactionAdapterListener {
    fun onTransactionClick(transaction: Transaction)
}