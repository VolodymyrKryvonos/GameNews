package com.example.utils

import android.util.Log
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


fun Date.parseDate(pattern: String): String{
    return try {
        val simpleDate = SimpleDateFormat(pattern, Locale.getDefault())
        simpleDate.format(this)
    }catch (e: Exception){
        ""
    }
}

fun String.toDate(pattern: String): Date?{
    return try {
        val simpleDate = SimpleDateFormat(pattern, Locale.getDefault())
        simpleDate.parse(this)
    }catch (e: Exception){
        null
    }
}