package com.example.mmrateconverter.data.mapper

import com.example.mmrateconverter.data.local.entity.ExchangeRateRoomEntity
import com.example.mmrateconverter.data.remote.model.ExchangeRateRemoteModel
import com.example.mmrateconverter.domain.entities.ExchangeRateEntity
import com.example.mmrateconverter.data.local.entity.GoldRoomEntity
import com.example.mmrateconverter.data.remote.model.GoldPriceRemoteModel
import com.example.mmrateconverter.domain.entities.GoldPriceEntity


// Remote Model (Firebase) မှ Local Entity (Room) သို့ ပြောင်းခြင်း
fun ExchangeRateRemoteModel.toRoomEntity(isFavorite: Boolean = false): ExchangeRateRoomEntity {
    return ExchangeRateRoomEntity(
        id = this.id,
        name = this.name,
        rateToMMK = this.rateToMMK,
        lastUpdated = this.lastUpdated,
        isFavorite = isFavorite // Favorite Status ကို Local ကနေပဲ ယူရပါမယ်
    )
}

// Local Entity (Room) မှ Domain Entity သို့ ပြောင်းခြင်း
fun ExchangeRateRoomEntity.toDomainEntity(): ExchangeRateEntity {
    return ExchangeRateEntity(
        id = this.id,
        name = this.name,
        rateToMMK = this.rateToMMK,
        lastUpdated = this.lastUpdated,
        isFavorite = this.isFavorite
    )
}

fun GoldPriceRemoteModel.toRoomEntity(isFavorite: Boolean = false): GoldRoomEntity {
    return GoldRoomEntity(
        id = this.id,
        name = this.name,
        lastUpdated = lastUpdated,
        isFavorite = isFavorite,
        price = this.price,
        unit = this.unit
    )
}

// Local Entity (Room) မှ Domain Entity သို့ ပြောင်းခြင်း
fun GoldRoomEntity.toDomainEntity(): GoldPriceEntity {
    return GoldPriceEntity(
        id = this.id,
        name = this.name,
        lastUpdated = this.lastUpdated,
        isFavorite = this.isFavorite,
        price = this.price,
        unit = this.unit
    )
}