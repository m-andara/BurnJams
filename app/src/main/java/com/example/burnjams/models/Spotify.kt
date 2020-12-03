package com.example.burnjams.models

data class Track(
        val artists: List<Artist>,
        val duration: Int,
        val name: String,
        val uri: String
)

data class Artist(
        val name: String
)

data class Workout(
        val name: String = "",
        val duration: Int = 0,
        val description: String = ""
)