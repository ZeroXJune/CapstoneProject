package com.tpc.trikride.repositories

import com.tpc.trikride.models.FareConfig
import com.tpc.trikride.services.FirebaseService
import kotlinx.coroutines.flow.Flow

class FareRepository(
    private val firebase: FirebaseService = FirebaseService()
) {
    fun fareConfig(): Flow<FareConfig> = firebase.getFareConfigFlow()

    suspend fun fareConfigOnce(): FareConfig = firebase.getFareConfigOnce()

    suspend fun save(config: FareConfig) = firebase.updateFareConfig(config)
}
