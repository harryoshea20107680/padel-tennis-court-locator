package org.setu.padelcourtlocator

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker

class MapPickerActivity : AppCompatActivity() {

    private lateinit var map: MapView
    private var selectedPoint: GeoPoint? = null
    private var marker: Marker? = null
    private var selectedTitle: String = "Selected court location"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Configuration.getInstance().userAgentValue = packageName
        setContentView(R.layout.activity_map_picker)

        map = findViewById(R.id.map)
        map.setTileSource(TileSourceFactory.MAPNIK)
        map.setMultiTouchControls(true)

        selectedTitle = intent.getStringExtra("title")
            ?.takeIf { it.isNotBlank() }
            ?: "Selected court location"

        // Start at saved court location or SETU
        val lat = intent.getDoubleExtra("latitude", 52.245954)
        val lon = intent.getDoubleExtra("longitude", -7.139539)

        val start = GeoPoint(lat, lon)

        map.controller.setZoom(18.0)
        map.controller.setCenter(start)

        setSelectedPoint(start)
        showExistingMarkers()

        val receiver = object : MapEventsReceiver {

            override fun singleTapConfirmedHelper(
                point: GeoPoint
            ): Boolean {
                setSelectedPoint(point)
                return true
            }

            override fun longPressHelper(
                point: GeoPoint
            ): Boolean {
                setSelectedPoint(point)
                return true
            }
        }

        map.overlays.add(MapEventsOverlay(receiver))

        findViewById<Button>(
            R.id.confirmLocationButton
        ).setOnClickListener {

            val point = selectedPoint
                ?: return@setOnClickListener

            val result = Intent()
                .putExtra("latitude", point.latitude)
                .putExtra("longitude", point.longitude)

            setResult(RESULT_OK, result)
            finish()
        }
    }

    private fun setSelectedPoint(point: GeoPoint) {
        selectedPoint = point

        if (marker == null) {
            marker = Marker(map).apply {
                setAnchor(
                    Marker.ANCHOR_CENTER,
                    Marker.ANCHOR_BOTTOM
                )

                icon = androidx.core.content.ContextCompat.getDrawable(
                    this@MapPickerActivity,
                    android.R.drawable.ic_menu_mylocation
                )
            }

            map.overlays.add(marker)
        }

        marker?.position = point
        marker?.title = selectedTitle

        marker?.snippet =
            "GPS: ${point.latitude}, ${point.longitude}"

        map.invalidate()
    }

    private fun showExistingMarkers() {
        val courts = AppData.courts.findAll()

        courts.forEach { court ->
            val lat = court.latitude
            val lon = court.longitude

            if (lat != null && lon != null &&
                lat.isFinite() && lon.isFinite() &&
                lat in -90.0..90.0 &&
                lon in -180.0..180.0) {

                val point = GeoPoint(lat, lon)

                val existingMarker = Marker(map).apply {
                    position = point
                    title = court.name
                    snippet = court.address

                    setAnchor(
                        Marker.ANCHOR_CENTER,
                        Marker.ANCHOR_BOTTOM
                    )
                }

                map.overlays.add(existingMarker)
            }
        }

        map.invalidate()
    }

    override fun onResume() {
        super.onResume()
        map.onResume()
    }

    override fun onPause() {
        map.onPause()
        super.onPause()
    }
}