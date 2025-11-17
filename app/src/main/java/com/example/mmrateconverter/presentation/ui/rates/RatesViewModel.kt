package com.example.mmrateconverter.presentation.ui.rates
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mmrateconverter.domain.entities.ExchangeRateEntity
import com.example.mmrateconverter.domain.entities.GoldPriceEntity
import com.example.mmrateconverter.domain.usecase.GetLatestGoldPricesUseCase
import com.example.mmrateconverter.domain.usecase.GetLatestRatesUseCase
import com.example.mmrateconverter.domain.usecase.ToggleFavoriteUseCase
import com.example.mmrateconverter.domain.usecase.ToggleGoldFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RatesViewModel @Inject constructor(
    private val getLatestRatesUseCase: GetLatestRatesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val getLatestGoldPricesUseCase: GetLatestGoldPricesUseCase,
    private val toggleGoldFavoriteUseCase: ToggleGoldFavoriteUseCase
) : ViewModel() {

    // UI State ကို ပြင်ပသို့ ထုတ်ပေးမယ့် LiveData/StateFlow
    private val _state = MutableStateFlow(RatesViewState())
    val state: StateFlow<RatesViewState> = _state.asStateFlow()

    init {
        fetchRates()
    }

    private fun fetchRates() {
        combine(
            getLatestRatesUseCase(),
            getLatestGoldPricesUseCase()
        ) { rates, goldPrices -> // <-- Transformation Block (Flow တွေဆီက ရလာတဲ့ Data)

            // Data နှစ်ခုလုံး ရရင် UI State အသစ်ကို ပြန်ပေးခြင်း
            RatesViewState(
                rates = sortRates(rates),
                goldPrices = goldPrices,
                isLoading = false,
                error = null,
                lastUpdatedText = formatLastUpdated(rates.firstOrNull())
            )
        }

            .onStart {
                // Repository က စတင် အလုပ်လုပ်ရင် Loading State ကို ပြောင်း
                _state.value = _state.value.copy(isLoading = true, error = null)
                Log.d("VIEWMODEL_INIT", "Fetching started")
            }
            .onEach { newState->
                // Data အသစ်ရောက်လာတိုင်း UI State ကို Update လုပ်
                _state.value = newState
            }
            .catch { e ->
                // Error ဖြစ်ရင် Error State ကို ပြောင်း
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "Data loading failed: ${e.message}"
                )
            }
            .launchIn(viewModelScope) // ViewModel ရဲ့ Scope အတိုင်း Flow ကို Run ခိုင်းခြင်း
    }

    // UI Event: Favorite ကို နှိပ်ရင် ခေါ်မယ့် Function
    fun onFavoriteToggle(rate: ExchangeRateEntity) {
        viewModelScope.launch {
            // Favorite Status ကို ပြောင်းပြီး DB ကို Update လုပ်
            toggleFavoriteUseCase(rate.id, !rate.isFavorite)
        }
    }
    // **အသစ် ထပ်ထည့်ရမည့် Gold Price Favorite Toggle Function**
    fun onGoldFavoriteToggle(goldPrice: GoldPriceEntity) {
        viewModelScope.launch {
            // Use Case ကို ခေါ်ယူပြီး Favorite Status ကို ပြောင်းခြင်း
            toggleGoldFavoriteUseCase(goldPrice.id, !goldPrice.isFavorite)
        }
    }
    // Helper Functions (Sorting & Formatting)
    private fun sortRates(rates: List<ExchangeRateEntity>): List<ExchangeRateEntity> {
        // Favorite ကို True ဖြစ်တာကို အပေါ်မှာ၊ ကျန်တာကို အောက်မှာ စီစဉ်သည်။
        return rates.sortedByDescending { it.isFavorite }
    }

    private fun formatLastUpdated(rate: ExchangeRateEntity?): String {
        // Long (Timestamp) ကို Date String အဖြစ် ပြောင်းလဲပေးမယ့် Logic
        val timestamp = rate?.lastUpdated ?: return "Last Update: N/A"

        val date = java.util.Date(timestamp)
        // 2. SimpleDateFormat (User ရဲ့ Default TimeZone နဲ့ Locale ကို သုံးသည်)
        val formatter = java.text.SimpleDateFormat(
            "dd MMM, yyyy - hh:mm a", // <-- "z" ကို ထပ်ထည့်ခြင်းဖြင့် TimeZone ကိုပါ ဖော်ပြနိုင်သည်
            java.util.Locale.getDefault()
        )

        // **TimeZone ကို သတ်မှတ်ခြင်းမရှိတဲ့အတွက် System Default TimeZone ကို သုံးမှာဖြစ်ပါတယ်**

        return "Last Update: ${formatter.format(date)}"
    }
}