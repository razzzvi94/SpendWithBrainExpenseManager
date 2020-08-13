package com.example.spendwithbrain.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.spendwithbrain.db.interfaces.UserDAO
import com.example.spendwithbrain.db.tables.UserDetails

@Database(
    entities = arrayOf(UserDetails::class),//[UserDetails::class /*, add all tables from DB*/],
    version = 1
)
abstract class ExpensesManagerDB : RoomDatabase() {
    abstract fun userDetailsDAO(): UserDAO
}