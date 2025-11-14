package com.example.mmrateconverter.domain.usecase

import com.example.mmrateconverter.data.repository.GoldPriceRepository


// Favorite Status ကို ပြောင်းလဲပေးမယ့် Use Case
class ToggleGoldFavoriteUseCase(
    private val repository: GoldPriceRepository
) {
    // Suspend function ဖြစ်ပြီး View ကနေ ခေါ်လိုက်တဲ့အခါ DB Update လုပ်ပေးသည်
    suspend operator fun invoke(rateId: String, isFavorite: Boolean) {
        repository.toggleGoldFavorite(goldId, isFavorite)
    }
}