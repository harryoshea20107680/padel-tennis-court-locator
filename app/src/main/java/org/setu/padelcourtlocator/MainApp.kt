package org.setu.padelcourtlocator

import android.app.Application
import org.setu.padelcourtlocator.models.PadelCourt
import org.setu.padelcourtlocator.models.PadelCourtMemStore
import org.setu.padelcourtlocator.models.PadelCourtStore
import org.setu.padelcourtlocator.models.PadelCourtJSONStore
import timber.log.Timber

class MainApp : Application() {

    lateinit var padelCourts: PadelCourtStore

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        AppData.courts = PadelCourtJSONStore(applicationContext)
        Timber.i("Padel Court Locator started")
    }
}