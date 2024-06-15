package dev.ashutoshwahane.matchmate.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


fun String.convertToDate(): String{
    try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val date: Date? = inputFormat.parse(this)
        val formattedDate = outputFormat.format(date)

        return formattedDate
    }catch (e: Exception){
        return "10/01/1998"
    }
}