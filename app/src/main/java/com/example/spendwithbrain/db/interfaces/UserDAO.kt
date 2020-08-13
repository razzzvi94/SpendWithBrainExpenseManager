package com.example.spendwithbrain.db.interfaces

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
    fun readDB(): List<UserDetails>
}