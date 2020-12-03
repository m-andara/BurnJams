package com.example.burnjams.utils

class TimeConversion {

    companion object {
        fun milToMin(mil: Int): String {
            val minutes = mil / 1000 / 60
            val seconds = (mil / 1000 % 60)
            val formattedSeconds = seconds.toString().format("%.2f", seconds)
            val finalTime = "$minutes:$formattedSeconds"
            if (formattedSeconds.length == 3) {
                finalTime.plus("0")
            }
            return finalTime
        }
    }
}