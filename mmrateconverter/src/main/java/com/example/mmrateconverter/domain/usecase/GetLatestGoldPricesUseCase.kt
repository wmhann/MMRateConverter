package com.example.mmrateconverter.domain.usecase

import com.example.mmrateconverter.data.repository.GoldPriceRepository
import com.example.mmrateconverter.domain.entities.GoldPriceEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

// UI ကို ပြသဖို့ GoldPrice တွေကို ရယူပေးမယ့် Use Case
class GetLatestGoldPricesUseCase @Inject constructor(
    private val repository: GoldPriceRepository
) {
    // Flow ကို ပြန်ပေးခြင်းဖြင့် UI ကနေ Data ကို စောင့်ကြည့်နိုင်သည်
    operator fun invoke(): Flow<List<GoldPriceEntity>> {
        return repository.getGoldPrices()
    }
}