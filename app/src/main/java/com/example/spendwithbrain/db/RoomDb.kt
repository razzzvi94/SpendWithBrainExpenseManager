package com.example.spendwithbrain.db

import android.content.Context
import androidx.room.Room

object RoomDb {
    lateinit var db: ExpensesManagerDB
    fun initDb(context: Context) {
        db = Room.databaseBuilder(context, ExpensesManagerDB::class.java, "ExpenseManager.db")
            .allowMainThreadQueries()
            .build()
    }
}