package org.setu.padelcourtlocator.models

data class PadelCourt(
    val id: Long = 0L,
    val name: String,
    val address: String = "",
    val website: String? = "",
    val rating: Double? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val isIndoor: Boolean = false,
    val courtCount: Int = 1,
    val imageUri: String? = null
) {
    fun validate() {
        require(name.isNotBlank()) {
            "Court name must not be empty."
        }

        require(address.isNotBlank()) {
            "Address must not be empty."
        }

        require(latitude == null && longitude == null ||
                latitude != null && longitude != null &&
                latitude.isFinite() &&
                latitude in -90.0..90.0 &&
                longitude.isFinite() &&
                longitude in -180.0..180.0) {
            "Please enter valid latitude and longitude."
        }
    }
}

