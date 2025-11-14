package com.example.mmrateconverter.data.remote

import com.example.mmrateconverter.data.remote.model.ExchangeRateRemoteModel
import com.example.mmrateconverter.data.remote.model.GoldPriceRemoteModel

interface RemoteDataSource {
    suspend fun fetchExchangeRates(): List<ExchangeRateRemoteModel>

    suspend fun fetchGoldPrices(): List<GoldPriceRemoteModel>
}