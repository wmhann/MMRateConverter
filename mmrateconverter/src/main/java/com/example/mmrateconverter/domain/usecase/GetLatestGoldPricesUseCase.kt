package com.example.mmrateconverter.domain.usecase

import kotlinx.coroutines.flow.Flow
import com.example.mmrateconverter.data.repository.GoldPriceRepository
import com.example.mmrateconverter.domain.entities.GoldPriceEntity

// UI ကို ပြသဖို့ GoldPrice တွေကို ရယူပေးမယ့် Use Case
class GetLatestGoldPricesUseCase(
    private val repository: GoldPriceRepository
) {
    // Flow ကို ပြန်ပေးခြင်းဖြင့် UI ကနေ Data ကို စောင့်ကြည့်နိုင်သည်
    operator fun invoke(): Flow<List<GoldPriceEntity>> {
        return repository.getGoldPrices()
    }
}