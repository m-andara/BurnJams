package com.example.burnjams.utils

class TimeConversion {

    companion object {
        fun milToMin(mil: Long): String {
            val minutes = mil / 1000 / 60
            val seconds = (mil / 1000 % 60)
            var formattedSeconds = ""
            formattedSeconds = if (seconds < 10) "0".plus(seconds.toString().format("%.2f", seconds))
            else seconds.toString().format("%.2f", seconds)
            return "$minutes:$formattedSeconds"
        }
    }
}