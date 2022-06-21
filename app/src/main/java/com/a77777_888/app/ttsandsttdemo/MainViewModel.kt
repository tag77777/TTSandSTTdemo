package com.a77777_888.app.ttsandsttdemo

import android.content.Intent
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.util.*

class MainViewModel : ViewModel() {

    private lateinit var tts: TextToSpeech
    private lateinit var startForResult: ActivityResultLauncher<Intent>


    fun init(ttsParam: TextToSpeech, launcher: ActivityResultLauncher<Intent>) =
        viewModelScope.launch {
        tts = ttsParam
        startForResult = launcher
    }

    fun speechToText() {
        startForResult.launch(Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale(Locale.getDefault().language))
        })
    }

    fun textToSpeech(text: String) = viewModelScope.launch{
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }
}