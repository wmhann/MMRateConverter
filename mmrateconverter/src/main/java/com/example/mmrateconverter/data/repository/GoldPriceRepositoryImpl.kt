package com.example.mmrateconverter.data.repository

import android.util.Log
import com.example.mmrateconverter.data.local.LocalDataSource
import com.example.mmrateconverter.data.local.TimestampDataSource
import com.example.mmrateconverter.data.mapper.toDomainEntity
import com.example.mmrateconverter.data.mapper.toRoomEntity
import com.example.mmrateconverter.data.remote.RemoteDataSource
import com.example.mmrateconverter.domain.entities.GoldPriceEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GoldPriceRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val timestampDataSource: TimestampDataSource
) : GoldPriceRepository {
    private val ONE_DAY_IN_MILLIS = 24 * 60 * 60 * 1000L
    private val RATES_KEY = "rates_last_fetch"

    override fun getGoldPrices(): Flow<List<GoldPriceEntity>> = flow {

        // 1. Local Cache → Instant UI
        val cachedPrices = localDataSource.getGoldPricesList()
        if (cachedPrices.isNotEmpty()) {
            emit(cachedPrices.map { it.toDomainEntity() })
            }

        val lastFetchTime = timestampDataSource.getLastFetchTime(RATES_KEY)
        val isCacheOutdated = (System.currentTimeMillis() - lastFetchTime) > ONE_DAY_IN_MILLIS
        val isCacheEmpty = cachedPrices.isEmpty()
        if (isCacheEmpty || isCacheOutdated) {
        // 2. Remote Fetch → Firebase
        try {
            Log.d("REPO_SYNC", "Fetching Gold Prices...")
            val remotePrices = remoteDataSource.fetchGoldPrices()

            // 3. Merge Favorite Status
            val currentLocalPrices = localDataSource.getGoldPricesList()

            val pricesToSave = remotePrices.map { remoteModel ->
                val isFav = currentLocalPrices.find { it.id == remoteModel.id }?.isFavorite ?: false
                remoteModel.toRoomEntity(isFavorite = isFav)
            }

            // 4. Save to Local DB
            localDataSource.saveGoldPrices(pricesToSave)
            timestampDataSource.saveLastFetchTime(RATES_KEY, System.currentTimeMillis())
            emit(localDataSource.getGoldPricesList().map { it.toDomainEntity() })

        } catch (e: Exception) {
            Log.e("REPO_SYNC", "Gold Fetch Failed: ${e.message}")
            if (isCacheEmpty) throw e
        }
        } else {
            Log.d("REPO_SYNC", "Gold Prices cache is fresh. No remote fetch needed.")
        }
    }

    override suspend fun toggleFavorite(priceId: String, isFavorite: Boolean) {
        localDataSource.updateGoldFavoriteStatus(priceId, isFavorite)
    }
}