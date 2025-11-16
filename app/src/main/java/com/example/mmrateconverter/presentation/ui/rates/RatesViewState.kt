package com.example.mmrateconverter.presentation.ui.rates

import com.example.mmrateconverter.domain.entities.ExchangeRateEntity
import com.example.mmrateconverter.domain.entities.GoldPriceEntity

// UI မှာ ပြသရမယ့် အခြေအနေ
data class RatesViewState(
    val rates: List<ExchangeRateEntity> = emptyList(), // ပြသရမယ့် ငွေလဲနှုန်း စာရင်း
    val goldPrices: List<GoldPriceEntity> = emptyList(),
    val isLoading: Boolean = false, // Loading ဖြစ်နေခြင်းရှိမရှိ
    val error: String? = null, // Error Message (ရှိရင်)
    val lastUpdatedText: String = "" // နောက်ဆုံး Update အချိန် စာသား
)

