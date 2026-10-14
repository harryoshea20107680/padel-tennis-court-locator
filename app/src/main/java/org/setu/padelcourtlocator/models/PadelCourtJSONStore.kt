package org.setu.padelcourtlocator.models

import android.content.Context
import android.net.Uri
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import org.setu.padelcourtlocator.helpers.*
import timber.log.Timber
import java.lang.reflect.Type
import java.util.*

const val JSON_FILE = "padelCourts.json"
val gsonBuilder: Gson = GsonBuilder().setPrettyPrinting()
    .create()

val listType: Type = object : TypeToken<ArrayList<PadelCourt>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}

class PadelCourtJSONStore(private val context: Context) : PadelCourtStore {

    var padelCourts = mutableListOf<PadelCourt>()

    init {
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    override fun findAll(): MutableList<PadelCourt> {
        logAll()
        return padelCourts
    }

    override fun findOne(id: Long): PadelCourt? {
        return padelCourts.find { it.id == id }
    }

    override fun create(court: PadelCourt): PadelCourt {
        val newCourt = court.copy(id = generateRandomId())

        padelCourts.add(newCourt)
        serialize()

        return newCourt
    }


    override fun update(court: PadelCourt): Boolean {
        val index = padelCourts.indexOfFirst {
            it.id == court.id
        }

        if (index == -1) {
            return false
        }

        padelCourts[index] = court
        serialize()

        return true
    }

    override fun delete(id: Long): Boolean {
        val deleted = padelCourts.removeAll {
            it.id == id
        }

        if (deleted) {
            serialize()
        }

        return deleted
    }
    private fun serialize() {
        val jsonString = gsonBuilder.toJson(padelCourts, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        padelCourts = gsonBuilder.fromJson(jsonString, listType)
    }

    private fun logAll() {
        padelCourts.forEach { Timber.i("$it") }
    }
}