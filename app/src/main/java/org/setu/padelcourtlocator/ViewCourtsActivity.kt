package org.setu.padelcourtlocator

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ViewCourtsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PadelCourtAdapter
    private lateinit var emptyText: TextView
    private lateinit var countText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_view_courts)

        // Handle system bars
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

        // Connect XML views
        recyclerView = findViewById(R.id.courtsRecyclerView)
        emptyText = findViewById(R.id.emptyCourtsText)
        countText = findViewById(R.id.courtsCountText)

        // Configure RecyclerView
        adapter = PadelCourtAdapter(
            AppData.courts.findAll()
        )

        recyclerView.layoutManager =
            LinearLayoutManager(this)

        recyclerView.adapter = adapter

        // Back navigation
        findViewById<Button>(R.id.backToMenuButton)
            .setOnClickListener {
                finish()
            }
    }

    override fun onResume() {
        super.onResume()

        if (::adapter.isInitialized) {
            displayCourts()
        }
    }

    private fun displayCourts() {
        val courts = AppData.courts.findAll()

        // Refresh list
        adapter.updateCourts(courts)

        // Update count
        countText.text = "Saved courts: ${courts.size}"

        // Show empty message if necessary
        if (courts.isEmpty()) {
            emptyText.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        } else {
            emptyText.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }
    }
}
