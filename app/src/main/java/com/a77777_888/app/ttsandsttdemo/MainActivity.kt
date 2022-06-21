package com.a77777_888.app.ttsandsttdemo

import android.annotation.SuppressLint
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.a77777_888.app.ttsandsttdemo.databinding.ActivityMainBinding
import java.util.*
import android.content.Intent


class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    @SuppressLint("QueryPermissionsNeeded")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        textToSpeech = TextToSpeech(this, this)

        viewModel.init(textToSpeech, startForResult)

        val sttIsEnabled = this.packageManager
            .queryIntentActivities(Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0)
            .size != 0

        with(binding) {
            sayButton.setOnClickListener { viewModel.speechToText() }
            playButton.setOnClickListener {
                viewModel.textToSpeech(editText.text.toString())
            }
            clearButton.setOnClickListener { editText.text?.clear() }
            quitButton.setOnClickListener { finish() }

            if (sttIsEnabled) {
                playButton.isEnabled = true
            } else editText.append(getString(R.string.stt_fail))
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        textToSpeech.shutdown()
    }

    private val startForResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            binding.editText.text?.clear()
            result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                ?.forEach{
                    binding.editText.append("$it ")
                }

        }
    }

    private lateinit var textToSpeech: TextToSpeech

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            textToSpeech.apply {
                val locale = Locale(Locale.getDefault().language)
                language = if (isLanguageAvailable(locale) == TextToSpeech.LANG_AVAILABLE) {
                    locale
                } else Locale.US
            }
            binding.playButton.isEnabled = true
        } else {
            binding.editText.append(getString(R.string.tts_fail))
        }
    }
}