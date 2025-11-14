package com.example.mmrateconverter.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exchange_rates_table")
data class ExchangeRateRoomEntity(
    @PrimaryKey val id: String,
    val name: String,
    val rateToMMK: Double,
    val lastUpdated: Long,
    val isFavorite: Boolean
)
