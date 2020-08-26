package com.example.spendwithbrain.screens.main.fragments.expenses.adaptor

import com.example.spendwithbrain.screens.main.fragments.expenses.adaptor.models.TransactionItem

interface TransactionAdapterListener {
    fun onTransactionClick(transaction: TransactionItem)
}