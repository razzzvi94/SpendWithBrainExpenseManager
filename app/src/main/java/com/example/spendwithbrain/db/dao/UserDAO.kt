package com.example.spendwithbrain.db.dao

import androidx.room.*
import com.example.spendwithbrain.db.entities.ExpensesDetails
import com.example.spendwithbrain.db.entities.IncomeDetails
import com.example.spendwithbrain.db.entities.UserDetails
import com.example.spendwithbrain.db.models.UserWithExpenses
import io.reactivex.Flowable
import io.reactivex.Single


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

    @Transaction
    @Query("SELECT * FROM userDetails WHERE userId=:userId")
    fun getUserExpenses(userId: Int): UserWithExpenses
}