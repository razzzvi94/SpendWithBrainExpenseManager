package com.example.spendwithbrain.db.tables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "incomeDetails")
data class IncomeDetails(
    @PrimaryKey(autoGenerate = true) var incomeId: Int = 0,
    @ForeignKey(entity = UserDetails::class, parentColumns = ["userId"], childColumns = ["userId"]) var userId: Int,
    @ColumnInfo(name = "incomeDate") var incomeDate: String,
    @ColumnInfo(name = "incomeAmount") var incomeAmount: Long,
    @ColumnInfo(name = "incomeCategory") var incomeCategory: String,
    @ColumnInfo(name = "incomeDetails") var incomeDetails: String,
    @ColumnInfo(name = "incomeImage") var incomeImage: ByteArray
)