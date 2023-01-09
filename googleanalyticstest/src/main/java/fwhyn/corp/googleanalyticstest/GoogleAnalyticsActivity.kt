package fwhyn.corp.googleanalyticstest

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.analytics.HitBuilders
import com.google.android.gms.analytics.ecommerce.Product
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase

class GoogleAnalyticsActivity : AppCompatActivity() {

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_analytics)

        // Obtain the FirebaseAnalytics instance.
        firebaseAnalytics = Firebase.analytics

        // Obtain the shared Tracker instance.
        val app = application as AnalyticsApplication
        val mTracker = app.defaultTracker

        val name = "MainActivity"
        Log.d("fwhyn_test", "Setting screen name: $name")
        with(mTracker!!) {
            setScreenName("Screen-$name")
            setAppName("My Application by fwhyn")
            send(HitBuilders.ScreenViewBuilder().build())
        }

        findViewById<Button>(R.id.ua).setOnClickListener {
            Log.d("fwhyn_test", "send ua")

            val product = Product()
            with(product) {
                setBrand("WhyWhy Collection")
                setName("Key Chain")
            }

            val dimensionValue1 = "SOME_DIMENSION_VALUE_1"
            val dimensionValue2 = "SOME_DIMENSION_VALUE_2"

            mTracker.send(
                HitBuilders.EventBuilder()
                    .setCategory("Test-Category")
                    .setAction("Test-Action")
                    .setLabel("Test-Label")
                    .setValue(7294)
                    .addProduct(product)
                    .setCustomDimension(1, dimensionValue1)
                    .setCustomDimension(2, dimensionValue2)
                    .build()
            )
        }

        findViewById<Button>(R.id.ga4).setOnClickListener {
            Log.d("fwhyn_test", "send ga4")

            firebaseAnalytics.logEvent("test_event") {
                param("string_param", "test")
                param("integer_param", 99)
            }
        }
    }
}