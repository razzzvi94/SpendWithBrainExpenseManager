package com.example.spendwithbrain.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.spendwithbrain.db.dao.UserDAO
import com.example.spendwithbrain.db.entities.ExpensesDetails
import com.example.spendwithbrain.db.entities.IncomeDetails
import com.example.spendwithbrain.db.entities.UserDetails
import com.example.spendwithbrain.db.dao.ExpenseDAO
import com.example.spendwithbrain.db.dao.IncomeDAO

@Database(
    entities = [UserDetails::class, IncomeDetails::class, ExpensesDetails::class],
    version = 1
)
abstract class ExpensesManagerDB : RoomDatabase() {
    abstract fun userDetailsDAO(): UserDAO
    abstract fun incomeDetailsDAO(): IncomeDAO
    abstract fun expenseDetailsDAO(): ExpenseDAO
}