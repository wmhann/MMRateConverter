package com.example.mmrateconverter.data.repository

import kotlinx.coroutines.flow.Flow
import com.example.mmrateconverter.domain.entities.ExchangeRateEntity


interface ExchangeRateRepository {
    // UI ကနေ စောင့်ကြည့်မယ့် Flow (Local/Remote Sync လုပ်ပြီး ပြန်လာမည်)
    fun getExchangeRates(): Flow<List<ExchangeRateEntity>>

    // Favorite Status ကို ပြောင်းလဲခြင်း (Local မှာသာ ပြောင်းမည်)
    suspend fun toggleFavorite(rateId: String, isFavorite: Boolean)

    // (Gold Price အတွက်လည်း အလားတူ Methods များ ထပ်ထည့်ရပါမည်)
}

