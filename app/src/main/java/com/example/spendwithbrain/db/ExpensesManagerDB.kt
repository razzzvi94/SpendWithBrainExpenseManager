package com.example.spendwithbrain.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.spendwithbrain.db.interfaces.UserDAO
import com.example.spendwithbrain.db.tables.ExpensesDetails
import com.example.spendwithbrain.db.tables.IncomeDetails
import com.example.spendwithbrain.db.tables.UserDetails

@Database(
    entities = [UserDetails::class, IncomeDetails::class, ExpensesDetails::class],
    version = 1
)
abstract class ExpensesManagerDB : RoomDatabase() {
    abstract fun userDetailsDAO(): UserDAO
}