package com.example.burnjams.repository

import androidx.room.*
import com.example.burnjams.entities.Code
import com.example.burnjams.entities.Token
import com.example.burnjams.entities.Workout

@Dao
interface BurnJamsDao {

    @Insert
    fun add(apiToken: Token)

    @Insert
    fun add(workout: Workout): Long

    @Delete
    fun delete(workout: Workout)

    @Query("SELECT * FROM workout")
    fun getWorkouts(): List<Workout>

    @Update
    fun update(apiToken: Token)

    @Query("SELECT token FROM token WHERE tokenId = 1")
    fun getToken(): String

    @Insert
    fun add(apiCode: Code)

    @Update
    fun update(apiCode: Code)

    @Query("SELECT code FROM code WHERE codeId = 1")
    fun getCode(): String
}