package com.example.mmrateconverter.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.example.mmrateconverter.data.local.LocalDataSource
import com.example.mmrateconverter.data.mapper.toDomainEntity
import com.example.mmrateconverter.data.mapper.toRoomEntity
import com.example.mmrateconverter.data.remote.RemoteDataSource
import com.example.mmrateconverter.domain.entities.GoldPriceEntity
import java.io.IOException

class GoldPriceRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : GoldPriceRepository {

    override fun getGoldPrices(): Flow<List<GoldPriceEntity>> = flow {

        // 1. Local Cache → Instant UI
        localDataSource.getGoldPricesFlow().collect { localPrices ->
            if (localPrices.isNotEmpty()) {
                emit(localPrices.map { it.toDomainEntity() })
            }
        }

        // 2. Remote Fetch → Firebase
        try {
            val remotePrices = remoteDataSource.fetchGoldPrices()

            // 3. Merge Favorite Status
            val currentLocalPrices = localDataSource.getGoldPricesList()

            val pricesToSave = remotePrices.map { remoteModel ->
                val isFav = currentLocalPrices.find { it.id == remoteModel.id }?.isFavorite ?: false
                remoteModel.toRoomEntity(isFavorite = isFav)
            }

            // 4. Save to Local DB
            localDataSource.saveGoldPrices(pricesToSave)

        } catch (e: IOException) {
            // Network error fallback
        } catch (e: Exception) {
            // Other errors
        }
    }

    override suspend fun toggleFavorite(priceId: String, isFavorite: Boolean) {
        localDataSource.updateGoldFavoriteStatus(priceId, isFavorite)
    }
}