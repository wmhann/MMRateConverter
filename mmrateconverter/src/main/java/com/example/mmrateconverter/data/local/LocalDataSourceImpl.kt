package com.example.mmrateconverter.data.local

import com.example.mmrateconverter.data.local.dao.GoldDao
import kotlinx.coroutines.flow.Flow
import com.example.mmrateconverter.data.local.dao.RateDao
import com.example.mmrateconverter.data.local.entity.ExchangeRateRoomEntity
import com.example.mmrateconverter.data.local.entity.GoldRoomEntity

class LocalDataSourceImpl(
    private val rateDao: RateDao,
    private val goldDao: GoldDao
) : LocalDataSource {

    override fun getRatesFlow(): Flow<List<ExchangeRateRoomEntity>> {
        return rateDao.getAllRates()
    }

    override suspend fun getRatesList(): List<ExchangeRateRoomEntity> {
        return rateDao.getRatesAsList()
    }

    override suspend fun saveRates(rates: List<ExchangeRateRoomEntity>) {
        rateDao.insertAllRates(rates)
    }

    override suspend fun updateFavoriteStatus(rateId: String, isFavorite: Boolean) {
        rateDao.updateFavoriteStatus(rateId, isFavorite)
    }

    override suspend fun saveGoldPrices(prices: List<GoldRoomEntity>) {
        goldDao.insertGoldPrices(prices)
    }

    override fun getGoldPricesFlow(): Flow<List<GoldRoomEntity>> {
        return goldDao.getAllGoldPrices()
    }

    override suspend fun getGoldPricesList(): List<GoldRoomEntity> {
        return goldDao.getGoldPricesAsList()
    }

    override suspend fun updateGoldFavoriteStatus(goldId: String, isFavorite: Boolean) {
        goldDao.updateGoldFavoriteStatus(goldId, isFavorite)
    }

}