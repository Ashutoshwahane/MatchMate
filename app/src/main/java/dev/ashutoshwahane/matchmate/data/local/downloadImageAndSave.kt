package dev.ashutoshwahane.matchmate.data.local

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.URL

fun downloadImageAndSave(context: Context, imageUrl: String, userId: String): String? {
    return try {
        val url = URL(imageUrl)
        val connection = url.openConnection()
        connection.connect()

        val inputStream = connection.getInputStream()
        val bitmap = BitmapFactory.decodeStream(inputStream)

        val file = File(context.filesDir, "user_$userId.jpg")
        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)

        outputStream.close()
        inputStream.close()
        file.absolutePath
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}