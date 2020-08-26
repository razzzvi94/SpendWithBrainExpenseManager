package com.example.spendwithbrain.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.spendwithbrain.db.entities.ExpensesDetails

@Dao
interface ExpenseDAO {
    @Query("UPDATE userDetails SET userCurrentBalance = userCurrentBalance - :new_expense WHERE userId = :id")
    fun updateUserExpense(id: Int, new_expense: Long): Int

    @Query("SELECT SUM(expensesAmount) FROM expensesDetails WHERE expensesDate BETWEEN :startDate AND :endDate AND userId=:id GROUP BY :id")
    fun getUserExpenseByDate(startDate: Long, endDate: Long, id: Int): Long

    @Query("SELECT expensesId, userId, expensesDate, SUM(expensesAmount)  AS expensesAmount, expensesCategory, expensesDetails, expensesImage FROM expensesDetails WHERE userId = :id GROUP BY expensesCategory")
    fun getExpenseByCategory(id: Int): List<ExpensesDetails>

    @Query("SELECT SUM(expensesAmount)  AS expensesAmount FROM expensesDetails WHERE userId = :id AND expensesDate BETWEEN :dateStart AND :dateEnd")
    fun getExpenseInInterval(id: Int, dateStart: Long, dateEnd: Long): String

    @Query("SELECT expensesId, userId, expensesDate, SUM(expensesAmount)  AS expensesAmount, expensesCategory, expensesDetails, expensesImage FROM expensesDetails WHERE userId = :id AND expensesDate BETWEEN :dateStart AND :dateEnd GROUP BY expensesCategory")
    fun getExpensesByCategoryInInterval(id: Int, dateStart: Long, dateEnd: Long): MutableList<ExpensesDetails>
}