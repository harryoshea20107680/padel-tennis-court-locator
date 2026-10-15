package org.setu.padelcourtlocator

import android.app.AlertDialog
import android.content.Intent
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
import org.setu.padelcourtlocator.models.PadelCourt
import timber.log.Timber
import androidx.appcompat.widget.SearchView

class ViewCourtsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PadelCourtAdapter
    private lateinit var emptyText: TextView
    private lateinit var countText: TextView
    private var searchQuery = ""

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

        recyclerView = findViewById(R.id.courtsRecyclerView)
        emptyText = findViewById(R.id.emptyCourtsText)
        countText = findViewById(R.id.courtsCountText)

        // Configure adapter with listener
        adapter = PadelCourtAdapter(
            courts = AppData.courts.findAll(),

            listener = object : OnCourtListener {

                override fun onCourtClick(court: PadelCourt) {
                    editCourt(court)
                }

                override fun onCourtLongClick(
                    court: PadelCourt
                ): Boolean {
                    confirmDelete(court)
                    return true
                }
            }
        )

        recyclerView.layoutManager =
            LinearLayoutManager(this)

        recyclerView.adapter = adapter

        val searchView =
            findViewById<SearchView>(R.id.searchCourtsView)

        searchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {

                override fun onQueryTextSubmit(query: String?): Boolean {
                    searchQuery = query.orEmpty()
                    displayCourts()
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    searchQuery = newText.orEmpty()
                    displayCourts()
                    return true
                }
            }
        )


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

        val allCourts = AppData.courts.findAll()

        val filteredCourts = allCourts.filter {
            it.name.contains(searchQuery.trim(), ignoreCase = true)
        }

        adapter.updateCourts(filteredCourts)

        if (searchQuery.isBlank()) {
            countText.text = "Saved courts: ${allCourts.size}"
        } else {
            countText.text =
                "Courts matching $searchQuery : ${filteredCourts.size} of ${allCourts.size} saved courts"
        }

        if (filteredCourts.isEmpty()) {

            emptyText.text = if (allCourts.isEmpty()) {
                "No courts saved."
            } else {
                "No courts saved matching $searchQuery"
            }

            emptyText.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE

        } else {
            emptyText.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }
    }

private fun editCourt(court: PadelCourt) {
        Timber.d(
            "Opening court editor: id=%d",
            court.id
        )

        val intent = Intent(
            this,
            AddCourtActivity::class.java
        )

        intent.putExtra(
            AddCourtActivity.EXTRA_COURT_ID,
            court.id
        )

        startActivity(intent)
    }

    private fun confirmDelete(court: PadelCourt) {
        Timber.d(
            "Delete confirmation requested: id=%d",
            court.id
        )

        AlertDialog.Builder(this)
            .setTitle("Delete Court")
            .setMessage(
                "Are you sure you want to delete '${court.name}'?"
            )
            .setPositiveButton("Delete") { _, _ ->
                deleteCourt(court)
            }
            .setNegativeButton("Cancel") { _, _ ->
                Timber.d(
                    "Court deletion cancelled: id=%d",
                    court.id
                )
            }
            .show()
    }

    private fun deleteCourt(court: PadelCourt) {
        val deleted = AppData.courts.delete(court.id)

        if (deleted) {
            Timber.i(
                "Court deleted: id=%d",
                court.id
            )
        } else {
            Timber.w(
                "Court deletion failed: id=%d",
                court.id
            )
        }

        displayCourts()
    }
}