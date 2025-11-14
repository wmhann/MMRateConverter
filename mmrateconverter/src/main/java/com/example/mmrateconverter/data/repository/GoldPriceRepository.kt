package com.example.mmrateconverter.data.repository

import kotlinx.coroutines.flow.Flow
import com.example.mmrateconverter.domain.entities.GoldPriceEntity

interface GoldPriceRepository {
    fun getGoldPrices(): Flow<List<GoldPriceEntity>>
    suspend fun toggleFavorite(rateId: String, isFavorite: Boolean)
    fun toggleGoldFavorite(goldId: Any, favorite: Boolean)
}