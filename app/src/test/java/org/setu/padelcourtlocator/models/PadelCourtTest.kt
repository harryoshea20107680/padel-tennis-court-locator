package org.setu.padelcourtlocator.models

import org.junit.Assert.*
import org.junit.Test

class PadelCourtTest {

    @Test
    fun createPadelCourt() {
        val court = PadelCourt(
            name = "test court",
            address = "1 Waterford Ireland",
            isIndoor = true,
            courtCount = 3
        )

        assertEquals("test court", court.name)
        assertEquals("1 Waterford Ireland", court.address)
        assertTrue(court.isIndoor)
        assertEquals(3, court.courtCount)
        assertNull(court.latitude)
    }

    @Test
    fun updateCourtUsingCopy() {
        val original = PadelCourt(
            id = 1L,
            name = "Original Club"
        )

        val updated = original.copy(
            name = "Updated Club"
        )

        assertEquals("Original Club", original.name)
        assertEquals("Updated Club", updated.name)
        assertEquals(original.id, updated.id)
    }

    @Test
    fun validCourtPassesValidation() {
        val court = PadelCourt(
            name = "Waterford Padel",
            address = "Waterford",
            latitude = 52.245954,
            longitude = -7.139539
        )

        court.validate()
    }

    @Test(expected = IllegalArgumentException::class)
    fun blankCourtNameRejected() {
        val court = PadelCourt(
            name = "   ",
            address = "Waterford"
        )

        court.validate()
    }

    @Test(expected = IllegalArgumentException::class)
    fun invalidLatitudeRejected() {
        val court = PadelCourt(
            name = "Test Court",
            address = "Waterford",
            latitude = 91.0,
            longitude = -7.0
        )

        court.validate()
    }

    @Test(expected = IllegalArgumentException::class)
    fun invalidLongitudeRejected() {
        val court = PadelCourt(
            name = "Test Court",
            address = "Waterford",
            latitude = 53.0,
            longitude = 181.0
        )

        court.validate()
    }

    @Test(expected = IllegalArgumentException::class)
    fun missingLongitudeRejected() {
        val court = PadelCourt(
            name = "Test Court",
            address = "Waterford",
            latitude = 53.0
        )

        court.validate()
    }

}
