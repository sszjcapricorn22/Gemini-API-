package sszj.s.geminiapi.ui

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.speech.RecognizerIntent
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.request.ImageRequest
import coil.transform.RoundedCornersTransformation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import sszj.s.geminiapi.R
import sszj.s.geminiapi.adapter.ChatAdapter
import sszj.s.geminiapi.data.model.Chat
import sszj.s.geminiapi.data.repository.ChatRepository
import sszj.s.geminiapi.utils.ChatInputEvent
import sszj.s.geminiapi.viewmodel.ChatViewModel
import sszj.s.geminiapi.viewmodel.ChatViewModelFactory
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private val uriState = MutableStateFlow("")

    private val imagePicker =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                uriState.value = uri.toString()
            }
        }

    private lateinit var chatViewModel: ChatViewModel
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var chatInput: EditText
    private lateinit var addPhotoIcon: ImageView
    private lateinit var sendIcon: ImageView
    private lateinit var selectedImage: ImageView
    private lateinit var voiceInputButton: ImageButton
    private lateinit var voiceInputLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        chatViewModel = ViewModelProvider(
            this,
            ChatViewModelFactory(ChatRepository())
        ).get(ChatViewModel::class.java)

        recyclerView = findViewById(R.id.recycler_view)
        chatInput = findViewById(R.id.chat_input)
        addPhotoIcon = findViewById(R.id.add_photo_icon)
        sendIcon = findViewById(R.id.send_icon)
        selectedImage = findViewById(R.id.selected_image)
        voiceInputButton = findViewById(R.id.voice_input_button)

        val layoutManager = LinearLayoutManager(this)
        layoutManager.stackFromEnd = true
        layoutManager.reverseLayout = true
        recyclerView.layoutManager = layoutManager

        chatAdapter = ChatAdapter()
        recyclerView.adapter = chatAdapter

        observeViewModel()

        addPhotoIcon.setOnClickListener {
            imagePicker.launch("image/*")
        }

        sendIcon.setOnClickListener {
            val prompt = chatInput.text.toString()
            lifecycleScope.launch {
                val bitmap = getBitmapFromUri(uriState.value)
                chatViewModel.onEvent(ChatInputEvent.SendPrompt(prompt, bitmap))
                uriState.update { "" }
                chatInput.text.clear()
            }
        }


        voiceInputButton.setOnClickListener {
            startVoiceInput()
        }

        // Register activity result launcher for voice input
        voiceInputLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                    val matches =
                        result.data!!.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    if (matches != null && matches.isNotEmpty()) {
                        chatInput.setText(matches[0])
                    }
                }
            }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            chatViewModel.chatState.collect { chatState ->
                chatAdapter.submitList(chatState.chatList)
                recyclerView.scrollToPosition(chatAdapter.itemCount - 1)
            }
        }

        lifecycleScope.launch {
            uriState.collect { uri ->
                if (uri.isNotEmpty()) {
                    val bitmap = getBitmapFromUri(uri)
                    selectedImage.setImageBitmap(bitmap)
                    selectedImage.visibility = View.VISIBLE
                } else {
                    selectedImage.visibility = View.GONE
                }
            }
        }
    }

    private suspend fun getBitmapFromUri(uri: String): Bitmap? {
        if (uri.isEmpty()) return null

        val imageLoader = ImageLoader(this)
        val request = ImageRequest.Builder(this)
            .data(Uri.parse(uri))
            .transformations(RoundedCornersTransformation(12f))
            .build()

        val result = imageLoader.execute(request)
        return result.drawable?.toBitmap()
    }


    private fun startVoiceInput() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        try {
            voiceInputLauncher.launch(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "Your device does not support speech input", Toast.LENGTH_SHORT)
                .show()
        }
    }
}
