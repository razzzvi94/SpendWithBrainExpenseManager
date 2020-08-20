package com.example.spendwithbrain.db.interfaces

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.spendwithbrain.db.entities.ExpensesDetails
import com.example.spendwithbrain.db.entities.IncomeDetails
import com.example.spendwithbrain.db.entities.UserDetails


@Dao
interface UserDAO {
    @Query("SELECT userId FROM userDetails WHERE userEmail like :user_email AND userPassword like :user_password")
    fun getIdByEmailPassword(user_email: String, user_password: String): Int

    @Query("SELECT userName FROM userDetails WHERE userEmail like :user_email AND userPassword like :user_password")
    fun getUserName(user_email: String, user_password: String): String

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateUser(user: UserDetails)

    @Query("SELECT * FROM userDetails WHERE userEmail like :user_email AND userPassword like :user_password")
    fun getUserByEmailAndPassword(user_email: String, user_password: String): List<UserDetails>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateIncome(income: IncomeDetails)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateExpense(expense: ExpensesDetails)

    @Query("SELECT userCurrentBalance FROM userDetails WHERE userId = :userId")
    fun getUserBalance(userId: Int): Int
}