package sszj.s.geminiapi.utils

import android.graphics.Bitmap
import sszj.s.geminiapi.data.model.Chat

data class ChatViewState (
    val chatList: MutableList<Chat> = mutableListOf(),
    val prompt: String = "",
    val bitmap: Bitmap? = null
)