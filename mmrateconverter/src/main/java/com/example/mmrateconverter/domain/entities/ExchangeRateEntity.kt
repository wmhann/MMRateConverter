package com.example.mmrateconverter.domain.entities

data class ExchangeRateEntity(
    val id: String,
    val name: String,
    val rateToMMK: Double,
    val lastUpdated: Long,
    val isFavorite: Boolean
)