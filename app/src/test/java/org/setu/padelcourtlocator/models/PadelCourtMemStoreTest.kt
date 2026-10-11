package org.setu.padelcourtlocator.models

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class PadelCourtMemStoreTest {

    private lateinit var store: PadelCourtStore

    @Before
    fun setup() {
        store = PadelCourtMemStore()
    }

    @Test
    fun createCourtAssignsUniqueId() {
        val first = store.create(
            PadelCourt(name = "first padel court location")
        )
        val second = store.create(
            PadelCourt(name = "second padel court location")
        )

        assertEquals(1L, first.id)
        assertEquals(2L, second.id)
        assertEquals(2, store.findAll().size)
    }

    @Test
    fun findCourtById() {
        val court = store.create(
            PadelCourt(name = "test court")
        )

        val found = store.findOne(court.id)

        assertEquals(court, found)
    }

    @Test
    fun updateExistingCourt() {
        val court = store.create(
            PadelCourt(name = "current name")
        )

        val updated = court.copy(
            name = "new name",
            courtCount = 4
        )

        assertTrue(store.update(updated))
        assertEquals(
            "new name",
            store.findOne(court.id)?.name
        )
        assertEquals(
            4,
            store.findOne(court.id)?.courtCount
        )
    }

    @Test
    fun deleteExistingCourt() {
        val court = store.create(
            PadelCourt(name = "Galway Padel")
        )

        assertTrue(store.delete(court.id))
        assertNull(store.findOne(court.id))
        assertTrue(store.findAll().isEmpty())
    }

    @Test
    fun missingCourtReturnsNoError() {
        assertNull(store.findOne(999L))
        assertFalse(store.delete(999L))
        assertFalse(
            store.update(
                PadelCourt(
                    id = 999L,
                    name = "Unknown"
                )
            )
        )
    }
}
