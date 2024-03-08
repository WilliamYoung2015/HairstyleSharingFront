package com.sou.hairstyle.common

import android.webkit.MimeTypeMap
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.File
import java.util.Locale


object FileUtil {
    fun getMediaType(file: File): MediaType? {
        val filePath = file.absolutePath
        val extension = MimeTypeMap.getFileExtensionFromUrl(filePath)
        val mediaType = MimeTypeMap.getSingleton()
            .getMimeTypeFromExtension(extension.lowercase(Locale.getDefault()))
        return mediaType?.toMediaTypeOrNull()
    }
}