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
    val courtCount: Int = 1
)
