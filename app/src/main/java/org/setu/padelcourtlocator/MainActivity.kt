package org.setu.padelcourtlocator

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.pm.ApplicationInfo
import timber.log.Timber


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialise Timber
        if (
            (applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0 &&
            Timber.forest().isEmpty()
        ) {
            Timber.plant(Timber.DebugTree())
        }

        Timber.d("MainActivity created")

        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val padding = (24 * resources.displayMetrics.density).toInt()

        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.main)
        ) { view, insets ->
            val bars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars()
            )
            view.setPadding(
                bars.left + padding,
                bars.top + padding,
                bars.right + padding,
                bars.bottom + padding
            )
            insets
        }

        findViewById<Button>(R.id.addCourtButton)
            .setOnClickListener {
                startActivity(
                    Intent(this, AddCourtActivity::class.java)
                )
            }

        findViewById<Button>(R.id.viewCourtsButton)
            .setOnClickListener {
                startActivity(
                    Intent(this, ViewCourtsActivity::class.java)
                )
            }

    }

    override fun onResume() {
        super.onResume()

        val count = AppData.courts.findAll().size
        findViewById<TextView>(R.id.courtCountText).text =
            "Courts added this session: $count"
    }
}
