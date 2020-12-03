package com.example.burnjams.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Token(
        @PrimaryKey(autoGenerate = true) val tokenId: Int = 0,
        @ColumnInfo(name = "token") val token: String
)

@Entity
data class Code(
        @PrimaryKey(autoGenerate = true) val codeId: Int = 0,
        @ColumnInfo(name = "code") val code: String
)

@Entity
data class Workout(
        @PrimaryKey(autoGenerate = true) val workoutId: Int = 0,
        var name: String,
        var durationInMinutes: String = "0:00 mins",
        var durationInMilliseconds: Int = 0,
        var description: String,
        var playlistId: String
)

@Entity
data class Track(
        @PrimaryKey(autoGenerate = true) val trackId: Int = 0,
        val artists: String,
        val duration: Int,
        val name: String,
        val uri: String
)