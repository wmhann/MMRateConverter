package com.example.mmrateconverter.data.local

import com.example.mmrateconverter.data.local.entity.ExchangeRateRoomEntity
import com.example.mmrateconverter.data.local.entity.GoldRoomEntity
import kotlinx.coroutines.flow.Flow


interface LocalDataSource {

    // Exchange Rate Methods
    suspend fun saveRates(rates: List<ExchangeRateRoomEntity>)

    fun getRatesFlow(): Flow<List<ExchangeRateRoomEntity>>

    suspend fun getRatesList(): List<ExchangeRateRoomEntity>

    suspend fun updateFavoriteStatus(rateId: String, isFavorite: Boolean)

    // Gold Price Methods
    suspend fun saveGoldPrices(prices: List<GoldRoomEntity>)

    fun getGoldPricesFlow(): Flow<List<GoldRoomEntity>>

    suspend fun getGoldPricesList(): List<GoldRoomEntity>

    suspend fun updateGoldFavoriteStatus(goldId: String, isFavorite: Boolean)
}
