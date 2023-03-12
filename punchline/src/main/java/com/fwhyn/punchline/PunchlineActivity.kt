package com.fwhyn.punchline

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fwhyn.punchline.databinding.ActivityPunchlineBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class PunchlineActivity : AppCompatActivity() {
    private val viewModel: PunchlineViewModel by viewModel()
    private lateinit var binding: ActivityPunchlineBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPunchlineBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.state.observe(this, { uiModel ->
            render(uiModel)
        })
        viewModel.getJoke()
    }

    private fun render(uiModel: UiModel) {
        when (uiModel) {
            is UiModel.Success -> showJoke(uiModel.joke)
            is UiModel.Error -> showError(uiModel.error)
        }
    }

    private fun showJoke(joke: Joke) {
    }

    private fun showError(error: String) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show()
    }
}