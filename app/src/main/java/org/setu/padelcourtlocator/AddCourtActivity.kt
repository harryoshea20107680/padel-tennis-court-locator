package org.setu.padelcourtlocator

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.setu.padelcourtlocator.models.PadelCourt

class AddCourtActivity : AppCompatActivity() {

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

        findViewById<Button>(R.id.saveCourtButton)
            .setOnClickListener { saveCourt() }

        findViewById<Button>(R.id.cancelCourtButton)
            .setOnClickListener { finish() }
    }

    private fun saveCourt() {
        val nameInput =
            findViewById<EditText>(R.id.courtNameInput)
        val addressInput =
            findViewById<EditText>(R.id.courtAddressInput)
        val websiteInput =
            findViewById<EditText>(R.id.courtWebsiteInput)
        val countInput =
            findViewById<EditText>(R.id.courtCountInput)
        val indoorInput =
            findViewById<CheckBox>(R.id.indoorCheckBox)

        val name = nameInput.text.toString().trim()
        val address = addressInput.text.toString().trim()
        val website = websiteInput.text.toString().trim()
        val courtCount = countInput.text
            .toString().toIntOrNull()

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

        val court = PadelCourt(
            name = name,
            address = address,
            website = website.ifBlank { null },
            isIndoor = indoorInput.isChecked,
            courtCount = courtCount
        )

        val savedCourt = AppData.courts.create(court)

        Toast.makeText(
            this,
            "Court #${savedCourt.id} added",
            Toast.LENGTH_SHORT
        ).show()

        finish()
    }
}
