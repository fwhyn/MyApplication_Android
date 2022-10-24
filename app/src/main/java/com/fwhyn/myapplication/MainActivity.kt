package com.fwhyn.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.google.android.material.composethemeadapter.MdcTheme
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    private var job: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val greeting = findViewById<TextView>(R.id.test_text)
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