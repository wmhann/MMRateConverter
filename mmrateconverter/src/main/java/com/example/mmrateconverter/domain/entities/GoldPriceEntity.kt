package com.example.mmrateconverter.domain.entities

data class GoldPriceEntity(
    val id: String,         // "WORLD", "LOCAL"
    val name: String,       // "World Gold Price", "Local Gold Price"
    val price: Double,      // ဈေးနှုန်း (USD or MMK ဖြင့်)
    val unit: String,       // "USD/oz" or "MMK/tical"
    val lastUpdated: Long,  // UNIX timestamp
    val isFavorite: Boolean = false
)