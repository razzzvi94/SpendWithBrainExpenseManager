package com.example.spendwithbrain.db.interfaces

import android.content.ClipData.Item
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.spendwithbrain.db.tables.UserDetails


@Dao
interface UserDAO {
    @Query("SELECT * FROM userDetails WHERE userId = :id ")
    fun getById(id: String): LiveData<List<UserDetails>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateUser(user: UserDetails)

    @Query("SELECT * FROM userDetails")
    fun getAllUsers(): List<UserDetails>

    @Query("SELECT * FROM userDetails WHERE userEmail like :user_email AND userPassword like :user_password")
    fun getUserByEmailAndPassword(user_email: String, user_password: String): List<UserDetails>
}