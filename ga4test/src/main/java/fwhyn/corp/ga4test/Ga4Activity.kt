package fwhyn.corp.ga4test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase

class Ga4Activity : AppCompatActivity() {

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ga4)

        // Obtain the FirebaseAnalytics instance.
        firebaseAnalytics = Firebase.analytics

        findViewById<TextView>(R.id.text_view).setOnClickListener{
            Log.d("fwhyn_test", "log event")
            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_ITEM) {
                param(FirebaseAnalytics.Param.ITEM_ID, 1)
                param(FirebaseAnalytics.Param.ITEM_NAME, "Test GA4")
                param(FirebaseAnalytics.Param.CONTENT_TYPE, "image")
            }
        }

        findViewById<Button>(R.id.button1).setOnClickListener {
            firebaseAnalytics.logEvent("share_image") {
                param("image_name", "name")
                param("full_text", "text")
            }
        }

        findViewById<Button>(R.id.button2).setOnClickListener {
            val parameters = Bundle().apply {
                this.putString("level_name", "Caverns01")
                this.putInt("level_difficulty", 4)
            }

            firebaseAnalytics.setDefaultEventParameters(parameters)
        }

        findViewById<Button>(R.id.button3).setOnClickListener {
            firebaseAnalytics.setDefaultEventParameters(null)
        }
    }
}