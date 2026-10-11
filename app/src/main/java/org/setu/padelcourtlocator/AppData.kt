package org.setu.padelcourtlocator

import org.setu.padelcourtlocator.models.PadelCourtMemStore
import org.setu.padelcourtlocator.models.PadelCourtStore

object AppData {
    val courts: PadelCourtStore = PadelCourtMemStore()
}
