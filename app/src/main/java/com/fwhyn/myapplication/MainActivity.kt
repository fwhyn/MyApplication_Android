package com.fwhyn.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.fwhyn.myapplication.databindingsample.UserActivity
import kotlinx.coroutines.Job

class MainActivity : AppCompatActivity() {
    private var job: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val greeting = findViewById<TextView>(R.id.test_text)
        greeting.setOnClickListener {
            startActivity(Intent(this, UserActivity::class.java))
        }

//        greeting.setOnClickListener {
//            startActivity(Intent(this, ResultActivity::class.java))
//        }
//        val greeting = findViewById<ComposeView>(R.id.test_text)
//        greeting.setContent {
//            MdcTheme { // or AppCompatTheme
//                Greeting("test")
//            }
//        }
    }

    @Composable
    private fun Greeting(txt: String) {
        Text(
            text = txt,
            style = MaterialTheme.typography.h5,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(R.dimen.margin_small))
                .wrapContentWidth(Alignment.CenterHorizontally)
        )
    }
}