package com.example.spendwithbrain.app

import android.app.Application
import androidx.room.Room
import com.example.spendwithbrain.db.ExpensesManagerDB
import com.example.spendwithbrain.db.RoomDb

class ExpensesApp: Application() {
    override fun onCreate() {
        super.onCreate()
        RoomDb.initDb(this)
    }
}