package com.example.spendwithbrain.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "expensesDetails")
data class ExpensesDetails(
    @PrimaryKey(autoGenerate = true) var expensesId: Int = 0,
    @ForeignKey(entity = UserDetails::class, parentColumns = ["userId"], childColumns = ["userId"]) var userId: Int,
    @ColumnInfo(name = "expensesDate") var expensesDate: Long,
    @ColumnInfo(name = "expensesAmount") var expensesAmount: Long,
    @ColumnInfo(name = "expensesCategory") var expensesCategory: String,
    @ColumnInfo(name = "expensesDetails") var expensesDetails: String,
    @ColumnInfo(name = "expensesImage") var expensesImage: ByteArray
)