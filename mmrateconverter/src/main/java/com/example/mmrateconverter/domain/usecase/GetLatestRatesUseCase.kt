package com.example.mmrateconverter.domain.usecase

import com.example.mmrateconverter.data.repository.ExchangeRateRepository
import com.example.mmrateconverter.domain.entities.ExchangeRateEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

// UI ကို ပြသဖို့ Rates တွေကို ရယူပေးမယ့် Use Case
class GetLatestRatesUseCase @Inject constructor(
    private val repository: ExchangeRateRepository
) {
    // Flow ကို ပြန်ပေးခြင်းဖြင့် UI ကနေ Data ကို စောင့်ကြည့်နိုင်သည်
    operator fun invoke(): Flow<List<ExchangeRateEntity>> {
        return repository.getExchangeRates()
    }
}