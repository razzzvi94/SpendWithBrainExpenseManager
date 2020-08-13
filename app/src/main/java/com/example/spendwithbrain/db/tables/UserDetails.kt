package com.example.spendwithbrain.db.tables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "userDetails")
data class UserDetails(
    @PrimaryKey(autoGenerate = true) var userId: Int = 0,
    @ColumnInfo(name = "userName") var userName: String,
    @ColumnInfo(name = "userEmail") var userEmail: String,
    @ColumnInfo(name = "userPassword") var userPassword: String,
    @ColumnInfo(name = "userCurrentBalance") var userCurrentBalance: Long = 0
)