package com.example.spendwithbrain.db

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

object RoomDb{
    lateinit var db :ExpensesManagerDB
    fun initDb(context:Context){
        db = Room.databaseBuilder(context,ExpensesManagerDB::class.java,"ExpenseManager.db").build()
    }
}