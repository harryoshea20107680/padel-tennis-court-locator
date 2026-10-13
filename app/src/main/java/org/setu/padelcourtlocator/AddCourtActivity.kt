package org.setu.padelcourtlocator

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.setu.padelcourtlocator.models.PadelCourt
import timber.log.Timber

class AddCourtActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_COURT_ID = "court_id"
    }

    private lateinit var nameInput: EditText
    private lateinit var addressInput: EditText
    private lateinit var websiteInput: EditText
    private lateinit var countInput: EditText
    private lateinit var indoorInput: CheckBox

    private var editingCourt: PadelCourt? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_court)

        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.addCourtRoot)
        ) { view, insets ->
            val bars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars()
            )

            view.setPadding(
                bars.left, bars.top,
                bars.right, bars.bottom
            )
            insets
        }

        nameInput = findViewById(R.id.courtNameInput)
        addressInput = findViewById(R.id.courtAddressInput)
        websiteInput = findViewById(R.id.courtWebsiteInput)
        countInput = findViewById(R.id.courtCountInput)
        indoorInput = findViewById(R.id.indoorCheckBox)

        val courtId = intent.getLongExtra(
            EXTRA_COURT_ID, -1L
        )

        if (courtId != -1L) {
            loadCourt(courtId)
        }

        findViewById<Button>(R.id.saveCourtButton)
            .setOnClickListener {
                saveCourt()
            }

        findViewById<Button>(R.id.cancelCourtButton)
            .setOnClickListener {
                finish()
            }
    }

    private fun loadCourt(id: Long) {
        val court = AppData.courts.findOne(id)

        if (court == null) {
            Timber.w("Court not found: id=%d", id)

            Toast.makeText(
                this,
                "Court not found",
                Toast.LENGTH_SHORT
            ).show()

            finish()
            return
        }

        editingCourt = court

        findViewById<TextView>(R.id.courtFormTitle)
            .text = "Edit Court"

        nameInput.setText(court.name)
        addressInput.setText(court.address)
        websiteInput.setText(court.website.orEmpty())
        countInput.setText(court.courtCount.toString())
        indoorInput.isChecked = court.isIndoor

        Timber.d("Loaded court for editing: id=%d", id)
    }

    private fun saveCourt() {
        val name = nameInput.text.toString().trim()
        val address = addressInput.text.toString().trim()
        val website = websiteInput.text.toString().trim()

        val courtCount = countInput.text
            .toString()
            .toIntOrNull()

        // Validate input
        if (name.isBlank()) {
            nameInput.error = "Court name is required"
            return
        }

        if (address.isBlank()) {
            addressInput.error = "Address is required"
            return
        }

        if (courtCount == null || courtCount < 1) {
            countInput.error = "At least 1 court required"
            return
        }

        val existing = editingCourt

        if (existing == null) {
            // Create a new court
            val newCourt = PadelCourt(
                name = name,
                address = address,
                website = website.ifBlank { null },
                courtCount = courtCount,
                isIndoor = indoorInput.isChecked
            )

            val saved = AppData.courts.create(newCourt)

            Timber.i("Court created: id=%d", saved.id)

            Toast.makeText(
                this,
                "Court added successfully",
                Toast.LENGTH_SHORT
            ).show()

        } else {
            // Preserve ID and other existing properties
            val updatedCourt = existing.copy(
                name = name,
                address = address,
                website = website.ifBlank { null },
                courtCount = courtCount,
                isIndoor = indoorInput.isChecked
            )

            val updated = AppData.courts.update(updatedCourt)

            if (!updated) {
                Timber.w(
                    "Court update failed: id=%d",
                    existing.id
                )

                Toast.makeText(
                    this,
                    "Unable to update court",
                    Toast.LENGTH_SHORT
                ).show()
                return
            }

            Timber.i("Court updated: id=%d", existing.id)

            Toast.makeText(
                this,
                "Court updated successfully",
                Toast.LENGTH_SHORT
            ).show()
        }

        finish()
    }
}
