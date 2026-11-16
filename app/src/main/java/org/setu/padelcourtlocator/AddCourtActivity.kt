package org.setu.padelcourtlocator

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.setu.padelcourtlocator.models.PadelCourt
import timber.log.Timber
import android.net.Uri
import android.content.Intent
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts

class AddCourtActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_COURT_ID = "court_id"
    }

    private lateinit var nameInput: EditText
    private lateinit var addressInput: EditText
    private lateinit var websiteInput: EditText
    private lateinit var courtCountPicker: NumberPicker
    private lateinit var indoorInput: CheckBox

    private var editingCourt: PadelCourt? = null

    private lateinit var imagePreview: ImageView
    private var selectedImageUri: Uri? = null

    private lateinit var latitudeInput: EditText
    private lateinit var longitudeInput: EditText

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
        courtCountPicker = findViewById(R.id.courtCountPicker)
        courtCountPicker.minValue = 1
        courtCountPicker.maxValue = 10
        courtCountPicker.value = 1
        indoorInput = findViewById(R.id.indoorCheckBox)
        imagePreview = findViewById(R.id.courtImagePreview)
        latitudeInput = findViewById(R.id.courtLatitudeInput)
        longitudeInput = findViewById(R.id.courtLongitudeInput)

        val imagePicker =
            registerForActivityResult(
                ActivityResultContracts.OpenDocument()
            ) { uri: Uri? ->

                if (uri != null) {

                    try {
                        contentResolver.takePersistableUriPermission(
                            uri,
                            Intent.FLAG_GRANT_READ_URI_PERMISSION
                        )
                    } catch (_: SecurityException) { }

                    selectedImageUri = uri
                    imagePreview.setImageURI(uri)
                }
            }

        findViewById<Button>(R.id.imagePickButton)
            .setOnClickListener {
                imagePicker.launch(arrayOf("image/*"))
            }

        val mapPicker =
            registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) { result ->

                if (result.resultCode == RESULT_OK) {
                    val data = result.data
                        ?: return@registerForActivityResult

                    if (data.hasExtra("latitude") &&
                        data.hasExtra("longitude")) {

                        val latitude =
                            data.getDoubleExtra("latitude", 0.0)

                        val longitude =
                            data.getDoubleExtra("longitude", 0.0)

                        latitudeInput.setText(latitude.toString())
                        longitudeInput.setText(longitude.toString())
                    }
                }
            }


        findViewById<Button>(R.id.chooseLocationButton)
            .setOnClickListener {

                val intent = Intent(
                    this,
                    MapPickerActivity::class.java
                )

                val latitude =
                    latitudeInput.text.toString().toDoubleOrNull()

                val longitude =
                    longitudeInput.text.toString().toDoubleOrNull()

                if (latitude != null && longitude != null &&
                    latitude.isFinite() && longitude.isFinite() &&
                    latitude in -90.0..90.0 &&
                    longitude in -180.0..180.0) {

                    intent.putExtra("latitude", latitude)
                    intent.putExtra("longitude", longitude)
                }

                intent.putExtra(
                    "title",
                    nameInput.text.toString().trim()
                )

                mapPicker.launch(intent)
            }

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
        courtCountPicker.value = court.courtCount
        indoorInput.isChecked = court.isIndoor

        latitudeInput.setText(court.latitude?.toString().orEmpty())

        longitudeInput.setText(court.longitude?.toString().orEmpty())

        selectedImageUri = court.imageUri?.let {
            Uri.parse(it)
        }

        if (selectedImageUri != null) {
            imagePreview.setImageURI(selectedImageUri)
        } else {
            imagePreview.setImageResource(
                android.R.drawable.ic_menu_gallery
            )
        }

        Timber.d("Loaded court for editing: id=%d", id)
    }

    private fun saveCourt() {
        val name = nameInput.text.toString().trim()
        val address = addressInput.text.toString().trim()
        val website = websiteInput.text.toString().trim()

        val courtCount = courtCountPicker.value

        val latitudeText = latitudeInput.text.toString().trim()

        val longitudeText = longitudeInput.text.toString().trim()

        val latitude = latitudeText.toDoubleOrNull()
        val longitude = longitudeText.toDoubleOrNull()

        if (latitudeText.isNotEmpty() &&
            (latitude == null || !latitude.isFinite() ||
                    latitude !in -90.0..90.0)) {

            latitudeInput.error = "Enter a valid latitude"
            return
        }

        if (longitudeText.isNotEmpty() &&
            (longitude == null || !longitude.isFinite() ||
                    longitude !in -180.0..180.0)) {

            longitudeInput.error = "Enter a valid longitude"
            return
        }

        if (latitude == null && longitude != null) {
            latitudeInput.error = "Latitude is required"
            return
        }

        if (longitude == null && latitude != null) {
            longitudeInput.error = "Longitude is required"
            return
        }

        if (name.isBlank()) {
            nameInput.error = "Court name is required"
            return
        }

        if (address.isBlank()) {
            addressInput.error = "Address is required"
            return
        }

        val existing = editingCourt

        try {
            if (existing == null) {
                val newCourt = PadelCourt(
                    name = name,
                    address = address,
                    website = website.ifBlank { null },
                    courtCount = courtCount,
                    isIndoor = indoorInput.isChecked,
                    imageUri = selectedImageUri?.toString(),
                    latitude = latitude,
                    longitude = longitude
                )

                val saved = AppData.courts.create(newCourt)

                Timber.i("Court created: id=%d", saved.id)

                Toast.makeText(
                    this,
                    "Court added successfully",
                    Toast.LENGTH_SHORT
                ).show()

            } else {
                val updatedCourt = existing.copy(
                    name = name,
                    address = address,
                    website = website.ifBlank { null },
                    courtCount = courtCount,
                    isIndoor = indoorInput.isChecked,
                    imageUri = selectedImageUri?.toString(),
                    latitude = latitude,
                    longitude = longitude
                )

                val updated = AppData.courts.update(updatedCourt)

                if (!updated) {
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

        } catch (e: IllegalArgumentException) {
            Toast.makeText(
                this,
                e.message ?: "Please check the entered values.",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}
