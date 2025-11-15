package com.example.mmrateconverter.presentation.ui.calculator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import com.example.mmrateconverter.domain.usecase.CalculateExchangeAmountUseCase
import com.example.mmrateconverter.domain.usecase.GetLatestRatesUseCase // Currency စာရင်းရဖို့

class CalculatorViewModel(
    private val getLatestRatesUseCase: GetLatestRatesUseCase,
    private val calculateExchangeAmountUseCase: CalculateExchangeAmountUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(CalculatorViewState())
    val state: StateFlow<CalculatorViewState> = _state.asStateFlow()

    init {
        loadAvailableCurrencies()
    }

    private fun loadAvailableCurrencies() {
        viewModelScope.launch {
            try {
                // Rates List ကို တစ်ခါတည်း ယူသည်
                val rates = getLatestRatesUseCase().first()
                val currencyIds = rates.map { it.id } + listOf("MMK") // MMK ကိုပါ ထည့်ပါ

                _state.value = _state.value.copy(
                    availableCurrencies = currencyIds.distinct(),
                    isLoading = false
                )

                // Currency List ရတာနဲ့ အစောပိုင်း တွက်ချက်မှု တစ်ခု လုပ်ပေးပါ
                calculateRate()
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = "Could not load currency list.")
            }
        }
    }

    // UI Event: Source Amount ပြောင်းလဲခြင်း
    fun updateSourceAmount(amount: String) {
        _state.value = _state.value.copy(sourceAmount = amount)
        calculateRate() // Input ပြောင်းတာနဲ့ ချက်ချင်း တွက်
    }

    // UI Event: Currency ရွေးချယ်ခြင်း
    fun updateCurrencies(isSource: Boolean, currencyId: String) {
        _state.value = if (isSource) {
            _state.value.copy(sourceCurrencyId = currencyId)
        } else {
            _state.value.copy(destinationCurrencyId = currencyId)
        }
        calculateRate() // Currency ပြောင်းတာနဲ့ ချက်ချင်း တွက်
    }

    // UI Event: Source နဲ့ Destination ကို လဲလှယ်ခြင်း
    fun swapCurrencies() {
        _state.value = _state.value.copy(
            sourceCurrencyId = _state.value.destinationCurrencyId,
            destinationCurrencyId = _state.value.sourceCurrencyId
        )
        calculateRate()
    }

    private fun calculateRate() {
        viewModelScope.launch {
            val currentState = _state.value
            val amountDouble = currentState.sourceAmount.toDoubleOrNull()

            if (amountDouble == null || amountDouble <= 0) {
                _state.value = currentState.copy(resultAmount = "")
                return@launch
            }

            try {
                // Use Case ကို ခေါ်ယူ တွက်ချက်ခြင်း
                val result = calculateExchangeAmountUseCase(
                    sourceId = currentState.sourceCurrencyId,
                    destinationId = currentState.destinationCurrencyId,
                    amount = amountDouble
                )

                _state.value = currentState.copy(
                    resultAmount = String.format("%.4f", result), // ၄ နေရာ ကိန်းဂဏန်းအထိ ပြသ
                    error = null
                )
            } catch (e: Exception) {
                _state.value = currentState.copy(resultAmount = "Error", error = "Cannot calculate.")
            }
        }
    }
}