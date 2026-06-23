package com.dwialfa0010.foodgallery.util

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

fun uriToFile(
    uri: Uri,
    context: Context
): File {

    val inputStream =
        context.contentResolver.openInputStream(uri)

    val tempFile = File.createTempFile(
        "food_",
        ".jpg",
        context.cacheDir
    )

    val outputStream =
        FileOutputStream(tempFile)

    inputStream?.copyTo(outputStream)

    inputStream?.close()
    outputStream.close()

    return tempFile
}