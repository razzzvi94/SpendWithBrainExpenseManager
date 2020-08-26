package com.example.spendwithbrain.screens.main.fragments.expenses.adapter

import com.example.spendwithbrain.screens.main.fragments.expenses.adapter.models.TransactionItem

interface TransactionAdapterListener {
    fun onTransactionClick(transaction: TransactionItem)
}