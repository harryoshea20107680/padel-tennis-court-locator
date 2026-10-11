package org.setu.padelcourtlocator.models

interface PadelCourtStore {

    fun findAll(): List<PadelCourt>

    fun findOne(id: Long): PadelCourt?

    fun create(court: PadelCourt): PadelCourt

    fun update(court: PadelCourt): Boolean

    fun delete(id: Long): Boolean
}
