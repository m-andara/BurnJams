package com.example.burnjams.repository

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.burnjams.entities.Code
import com.example.burnjams.entities.Token
import com.example.burnjams.entities.Track
import com.example.burnjams.entities.Workout

@Database(entities = [Token::class, Code::class, Track::class, Workout::class], version = 11)
abstract class BurnJamsDatabase : RoomDatabase() {

    abstract fun burnJamsDao(): BurnJamsDao
}