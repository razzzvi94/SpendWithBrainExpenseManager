package com.example.spendwithbrain.db.dao

import androidx.room.Dao
import androidx.room.Query

@Dao
interface IncomeDAO {
    @Query("UPDATE userDetails SET userCurrentBalance = userCurrentBalance + :new_income WHERE userId = :id")
    fun updateUserBudget(id: Int, new_income: Long): Int
}