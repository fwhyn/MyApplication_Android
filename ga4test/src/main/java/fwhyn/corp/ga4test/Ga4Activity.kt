package fwhyn.corp.ga4test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
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

        findViewById<Button>(R.id.printing_button).setOnClickListener {
            firebaseAnalytics.logEvent("printing") {
                param("copies", 3)
                param("paper_size", "A0")
                param("metric_test", 30)
                param("unregistered_param", "unregistered pram test")
            }
        }

        findViewById<Button>(R.id.default_button).setOnClickListener {
            val parameters = Bundle().apply {
                this.putInt("copies", 4)
                this.putString("paper_size", "A4")
            }

            firebaseAnalytics.setDefaultEventParameters(parameters)
        }

        findViewById<Button>(R.id.clear_button).setOnClickListener {
            firebaseAnalytics.setDefaultEventParameters(null)
        }

        findViewById<Button>(R.id.properties_button).setOnClickListener {
            firebaseAnalytics.setUserProperty("user_in", "user logged in")
            firebaseAnalytics.setUserProperty("event_properties", "properties recorded")
        }

        findViewById<Button>(R.id.scanning_button).setOnClickListener {
            firebaseAnalytics.logEvent("scanning") {
                param("copies", 99)
                param("paper_size", "A3")
            }

            firebaseAnalytics.logEvent("copying") {
                param("copies", 2)
                param("paper_size", "B1")
            }
        }
    }
}