package sszj.s.geminiapi.data.model

import android.graphics.Bitmap

data class Chat(
    val prompt: String,
    val imageBitmap: Bitmap?,
    val isUser: Boolean
)
