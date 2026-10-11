package org.setu.padelcourtlocator.models

class PadelCourtMemStore : PadelCourtStore {

    private val courts = mutableListOf<PadelCourt>()
    private var nextId = 1L

    override fun findAll(): List<PadelCourt> {
        return courts.toList()
    }

    override fun findOne(id: Long): PadelCourt? {
        return courts.find { it.id == id }
    }

    override fun create(court: PadelCourt): PadelCourt {
        val newCourt = court.copy(id = nextId++)
        courts.add(newCourt)
        return newCourt
    }

    override fun update(court: PadelCourt): Boolean {
        val index = courts.indexOfFirst {
            it.id == court.id
        }

        if (index == -1) {
            return false
        }

        courts[index] = court
        return true
    }

    override fun delete(id: Long): Boolean {
        return courts.removeAll { it.id == id }
    }
}
