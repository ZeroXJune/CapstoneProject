package com.tpc.trikride.repositories

import com.tpc.trikride.models.FareConfig
import com.tpc.trikride.models.FareStop
import com.tpc.trikride.services.FirebaseService
import com.tpc.trikride.utils.FareSeed
import kotlinx.coroutines.flow.Flow

class FareRepository(
    private val firebase: FirebaseService = FirebaseService()
) {
    fun fareConfig(): Flow<FareConfig> = firebase.getFareConfigFlow()

    suspend fun fareConfigOnce(): FareConfig = firebase.getFareConfigOnce()

    suspend fun save(config: FareConfig) = firebase.updateFareConfig(config)

    fun fareStops(): Flow<List<FareStop>> = firebase.getFareStopsFlow()

    suspend fun saveStop(stop: FareStop) = firebase.saveFareStop(stop)

    suspend fun deleteStop(stopId: String) = firebase.deleteFareStop(stopId)

    /**
     * Loads the transcribed FeTODAT table into the database and stamps the
     * config so the admin screen can show when it last happened.
     */
    suspend fun importOfficialRates(current: FareConfig) {
        firebase.importFareStops(FareSeed.STOPS)
        firebase.updateFareConfig(
            current.copy(
                source = FareConfig.DEFAULT_SOURCE,
                seededAt = System.currentTimeMillis().toString()
            )
        )
    }
}
