package com.example.mmrateconverter.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class GoldPriceRemoteModel(
    val id: String,
    val name: String,
    val price: Double,
    val lastUpdated: Long,
    val unit: String
)