package com.example.mmrateconverter.domain.usecase
import com.example.mmrateconverter.data.repository.ExchangeRateRepository
import com.example.mmrateconverter.domain.entities.ExchangeRateEntity
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class CalculateExchangeAmountUseCase @Inject constructor(
    private val exchangeRateRepository: ExchangeRateRepository
) {

    suspend operator fun invoke(
        sourceId: String,       // ဥပမာ: "USD"
        destinationId: String,  // ဥပမာ: "EUR"
        amount: Double          // ဥပမာ: 100.0
    ): Double {
// 1. Repository မှ ငွေလဲနှုန်းများ ရယူခြင်း (နောက်ဆုံး Rates များကို Local/Cache မှ ဆွဲယူ)
        val rates = exchangeRateRepository.getExchangeRates().first()

        // 2. Source Rate နှင့် Destination Rate ရှာဖွေခြင်း
        val sourceRateToMMK = findRate(rates, sourceId)
        val destinationRateToMMK = findRate(rates, destinationId)

        // 3. တွက်ချက်မှု Logic

        // MMK ကို Base Currency အဖြစ် သတ်မှတ်ခြင်း (RateToMMK ကို 1.0 သတ်မှတ်)
        val actualSourceRate = if (sourceId == "MMK") 1.0 else sourceRateToMMK
        val actualDestinationRate = if (destinationId == "MMK") 1.0 else destinationRateToMMK

        if (actualSourceRate <= 0 || actualDestinationRate <= 0) {
            // ဈေးနှုန်းမရှိရင် ဒါမှမဟုတ် 0 ဖြစ်ရင် Error ထုတ်ပေးပါ
            throw IllegalArgumentException("Invalid or zero rate for conversion.")
        }

        // Final Calculation: Dest Amount = Source Amount * (Source Rate / Dest Rate)
        val destinationAmount = amount * (actualSourceRate / actualDestinationRate)

        return destinationAmount
    }

    // Helper Function
    private fun findRate(rates: List<ExchangeRateEntity>, id: String): Double {
        // MMK ကို Base အဖြစ်ထားသဖြင့် rateToMMK ကို 1.0 သတ်မှတ်
        if (id == "MMK") return 1.0

        // တခြား Currency ကို ရှာဖွေခြင်း
        return rates.find { it.id == id }?.rateToMMK
            ?: throw NoSuchElementException("Rate for $id not found.")
    }
}