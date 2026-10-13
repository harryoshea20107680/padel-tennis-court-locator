package org.setu.padelcourtlocator

import org.setu.padelcourtlocator.models.PadelCourt

interface OnCourtListener {

    fun onCourtClick(court: PadelCourt)

    fun onCourtLongClick(court: PadelCourt): Boolean
}
