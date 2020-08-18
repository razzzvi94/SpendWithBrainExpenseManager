package com.example.spendwithbrain.db.interfaces

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.spendwithbrain.db.tables.ExpensesDetails
import com.example.spendwithbrain.db.tables.IncomeDetails
import com.example.spendwithbrain.db.tables.UserDetails


@Dao
interface UserDAO {
    @Query("SELECT * FROM userDetails WHERE userId = :id ")
    fun getById(id: String): LiveData<List<UserDetails>>

    @Query("SELECT userId FROM userDetails WHERE userEmail like :user_email AND userPassword like :user_password")
    fun getIdByEmailPassword(user_email: String, user_password: String): Int

    @Query("SELECT userName FROM userDetails WHERE userEmail like :user_email AND userPassword like :user_password")
    fun getUserName(user_email: String, user_password: String): String

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateUser(user: UserDetails)

    @Query("SELECT * FROM userDetails")
    fun getAllUsers(): List<UserDetails>

    @Query("SELECT * FROM userDetails WHERE userEmail like :user_email AND userPassword like :user_password")
    fun getUserByEmailAndPassword(user_email: String, user_password: String): List<UserDetails>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateIncome(income: IncomeDetails)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateExpense(expense: ExpensesDetails)

    @Query("UPDATE userDetails SET userCurrentBalance = userCurrentBalance + :new_income WHERE userId = :id")
    fun updateUserBudget(id: Int, new_income: Long): Int

    @Query("UPDATE userDetails SET userCurrentBalance = userCurrentBalance - :new_expense WHERE userId = :id")
    fun updateUserExpense(id: Int, new_expense: Long): Int
}