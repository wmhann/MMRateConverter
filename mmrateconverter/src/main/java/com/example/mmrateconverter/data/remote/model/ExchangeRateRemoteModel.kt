package com.example.mmrateconverter.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class ExchangeRateRemoteModel(
    val id: String,
    val name: String,
    val rateToMMK: Double,
    val lastUpdated: Long   // Firebase Timestamp ကို Long အနေနဲ့ ပြောင်းယူရပါမည်။
)

