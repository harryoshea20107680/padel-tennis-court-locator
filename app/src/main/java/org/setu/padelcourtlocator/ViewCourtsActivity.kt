package org.setu.padelcourtlocator

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ViewCourtsActivity : AppCompatActivity() {

    private lateinit var courtsContainer: LinearLayout
    private lateinit var emptyText: TextView
    private lateinit var countText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_view_courts)

        val root = findViewById<View>(R.id.viewCourtsRoot)
        val left = root.paddingLeft
        val top = root.paddingTop
        val right = root.paddingRight
        val bottom = root.paddingBottom

        ViewCompat.setOnApplyWindowInsetsListener(root) { view, insets ->
            val bars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars()
            )
            view.setPadding(
                left + bars.left,
                top + bars.top,
                right + bars.right,
                bottom + bars.bottom
            )
            insets
        }

        courtsContainer = findViewById(R.id.courtsContainer)
        emptyText = findViewById(R.id.emptyCourtsText)
        countText = findViewById(R.id.courtsCountText)

        findViewById<Button>(R.id.backToMenuButton)
            .setOnClickListener {
                finish()
            }
    }

    override fun onResume() {
        super.onResume()
        displayCourts()
    }

    private fun displayCourts() {
        val courts = AppData.courts.findAll()

        courtsContainer.removeAllViews()
        countText.text = "Saved courts: ${courts.size}"

        emptyText.visibility =
            if (courts.isEmpty()) View.VISIBLE else View.GONE

        for (court in courts) {
            val courtLayout = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                val padding = dp(16)
                setPadding(padding, padding, padding, padding)
            }

            val title = TextView(this).apply {
                text = court.name
                textSize = 20f
                setTypeface(null, Typeface.BOLD)
            }

            val address = TextView(this).apply {
                text = court.address
                textSize = 16f
            }

            val details = TextView(this).apply {
                val type = if (court.isIndoor) "Indoor" else "Outdoor"
                val count = court.courtCount
                val multipleCourts = if (count == 1) "court" else "courts"

                text = "$count $type $multipleCourts"
                textSize = 14f
            }

            courtLayout.addView(title)
            courtLayout.addView(address)
            courtLayout.addView(details)

            val website = court.website
            if (!website.isNullOrBlank()) {
                val websiteText = TextView(this).apply {
                    text = website
                    textSize = 14f
                }
                courtLayout.addView(websiteText)
            }

            courtsContainer.addView(courtLayout)

            val divider = View(this).apply {
                setBackgroundColor(Color.LTGRAY)
            }

            courtsContainer.addView(
                divider,
                LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    dp(1)
                )
            )
        }
    }

    private fun dp(value: Int): Int {
        return (value * resources.displayMetrics.density)
            .toInt()
            .coerceAtLeast(1)
    }
}
