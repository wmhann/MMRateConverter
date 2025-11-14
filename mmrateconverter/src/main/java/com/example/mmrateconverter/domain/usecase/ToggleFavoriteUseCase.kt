package com.example.mmrateconverter.domain.usecase

import com.example.mmrateconverter.data.repository.ExchangeRateRepository

// Favorite Status ကို ပြောင်းလဲပေးမယ့် Use Case
class ToggleFavoriteUseCase(
    private val repository: ExchangeRateRepository
) {
    // Suspend function ဖြစ်ပြီး View ကနေ ခေါ်လိုက်တဲ့အခါ DB Update လုပ်ပေးသည်
    suspend operator fun invoke(rateId: String, isFavorite: Boolean) {
        repository.toggleFavorite(rateId, isFavorite)
    }
}