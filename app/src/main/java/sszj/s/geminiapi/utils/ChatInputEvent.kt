package sszj.s.geminiapi.utils
import android.graphics.Bitmap

sealed class ChatInputEvent {
    data class UpdatePrompt(val newPrompt: String) : ChatInputEvent()
    data class SendPrompt(
        val prompt: String,
        val bitmap: Bitmap?
    ) : ChatInputEvent()
}
