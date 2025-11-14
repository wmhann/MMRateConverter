package com.example.mmrateconverter.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gold_prices_table")
data class GoldRoomEntity(
    @PrimaryKey
    val id: String,         // "WORLD", "LOCAL"
    val name: String,
    val price: Double,
    val unit: String,
    val lastUpdated: Long,
    val isFavorite: Boolean = false
)