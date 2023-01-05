package fwhyn.corp.googleanalyticstest

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.analytics.HitBuilders
import com.google.android.gms.analytics.ecommerce.Product

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Obtain the shared Tracker instance.
        val app = application as AnalyticsApplication
        val mTracker = app.defaultTracker

        val name = "MainActivity"
        Log.d("fwhyn_test", "Setting screen name: $name")
        with(mTracker!!) {
            setScreenName("Screen~$name")
            setAppName("My Application by fwhyn")
            send(HitBuilders.ScreenViewBuilder().build())
        }
        findViewById<TextView>(R.id.text_view).setOnClickListener {
            Log.d("fwhyn_test", "send tracker")

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
    }
}