package com.example.playlist_maker.Utils

fun formatMinutes(minutes: Int): String {
    return when {
        minutes % 10 == 1 && minutes % 100 != 11 -> "$minutes минута"
        minutes % 10 in 2..4 && (minutes % 100 !in 12..14) -> "$minutes минуты"
        else -> "$minutes минут"
    }
}