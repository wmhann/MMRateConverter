package com.example.mmrateconverter.presentation.ui.rates

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import com.example.mmrateconverter.domain.entities.ExchangeRateEntity
import com.example.mmrateconverter.domain.usecase.GetLatestRatesUseCase
import com.example.mmrateconverter.domain.usecase.ToggleFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class RatesViewModel @Inject constructor(
    private val getLatestRatesUseCase: GetLatestRatesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

    // UI State ကို ပြင်ပသို့ ထုတ်ပေးမယ့် LiveData/StateFlow
    private val _state = MutableStateFlow(RatesViewState())
    val state: StateFlow<RatesViewState> = _state.asStateFlow()

    init {
        fetchRates()
    }

    private fun fetchRates() {
        getLatestRatesUseCase()
            .onStart {
                // Repository က စတင် အလုပ်လုပ်ရင် Loading State ကို ပြောင်း
                _state.value = _state.value.copy(isLoading = true, error = null)
            }
            .onEach { rates ->
                // Data အသစ်ရောက်လာတိုင်း UI State ကို Update လုပ်
                _state.value = RatesViewState(
                    rates = sortRates(rates), // Favorite များကို အပေါ်သို့ စီစဉ်ခြင်း
                    isLoading = false,
                    lastUpdatedText = formatLastUpdated(rates.firstOrNull()) // ဥပမာ- Format လုပ်မည်
                )
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

    // Helper Functions (Sorting & Formatting)
    private fun sortRates(rates: List<ExchangeRateEntity>): List<ExchangeRateEntity> {
        // Favorite ကို True ဖြစ်တာကို အပေါ်မှာ၊ ကျန်တာကို အောက်မှာ စီစဉ်သည်။
        return rates.sortedByDescending { it.isFavorite }
    }

    private fun formatLastUpdated(rate: ExchangeRateEntity?): String {
        // Long (Timestamp) ကို Date String အဖြစ် ပြောင်းလဲပေးမယ့် Logic
        // ... (ကိုယ်တိုင် Date/Time Formatting Logic ကို ရေးပါ)
        return "Last Update: ${rate?.lastUpdated ?: "N/A"}"
    }
}