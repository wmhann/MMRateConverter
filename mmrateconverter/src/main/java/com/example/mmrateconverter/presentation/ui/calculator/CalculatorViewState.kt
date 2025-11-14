package com.example.mmrateconverter.presentation.ui.calculator

data class CalculatorViewState(
    // ရွေးချယ်နိုင်သော ငွေကြေးများအားလုံး (Dropdown အတွက်)
    val availableCurrencies: List<String> = emptyList(),

    // Input Value
    val sourceCurrencyId: String = "USD",
    val destinationCurrencyId: String = "MMK",
    val sourceAmount: String = "1.0", // UI Text Field နဲ့ ကိုက်ညီအောင် String သုံးသည်

    // Output Value
    val resultAmount: String = "",

    val isLoading: Boolean = false,
    val error: String? = null
)