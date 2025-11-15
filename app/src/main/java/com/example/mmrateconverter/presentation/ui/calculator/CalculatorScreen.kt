package com.example.mmrateconverter.presentation.ui.calculator

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.unit.sp 
import androidx.compose.material3.LocalTextStyle

@Composable
fun CalculatorScreen(viewModel: CalculatorViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {

        // 1. Source Currency Input
        CurrencyInputRow(
            currencyId = state.sourceCurrencyId,
            amount = state.sourceAmount,
            availableCurrencies = state.availableCurrencies,
            onCurrencyChange = { viewModel.updateCurrencies(isSource = true, it) },
            onAmountChange = viewModel::updateSourceAmount
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 2. Swap Button
        IconButton(
            onClick = viewModel::swapCurrencies,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Icon(Icons.Filled.SwapVert, contentDescription = "Swap")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 3. Destination Currency Output
        CurrencyResultRow(
            currencyId = state.destinationCurrencyId,
            resultAmount = state.resultAmount,
            availableCurrencies = state.availableCurrencies,
            onCurrencyChange = { viewModel.updateCurrencies(isSource = false, it) }
        )

        // 4. Error Message
        state.error?.let {
            Text(
                text = it,
                color = Color.Red,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun CurrencyInputRow(
    currencyId: String,
    amount: String,
    availableCurrencies: List<String>,
    onCurrencyChange: (String) -> Unit,
    onAmountChange: (String) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        OutlinedTextField(
            value = amount,
            onValueChange = { onAmountChange(it) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.weight(0.6f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        CurrencyDropdown(
            selectedId = currencyId,
            currencyIds = availableCurrencies,
            onSelect = onCurrencyChange,
            modifier = Modifier.weight(0.4f)
        )
    }
}

@Composable
fun CurrencyResultRow(
    currencyId: String,
    resultAmount: String,
    availableCurrencies: List<String>,
    onCurrencyChange: (String) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        OutlinedTextField(
            value = resultAmount,
            onValueChange = {},
            readOnly = true,
            modifier = Modifier.weight(0.6f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        CurrencyDropdown(
            selectedId = currencyId,
            currencyIds = availableCurrencies,
            onSelect = onCurrencyChange,
            modifier = Modifier.weight(0.4f)
        )
    }
}

@Composable
fun CurrencyDropdown(
    selectedId: String,
    currencyIds: List<String>,
    onSelect: (String) -> Unit,
    modifier: Modifier
) {
    // Placeholder UI — replace with ExposedDropdownMenuBox later
    Text(
        text = selectedId,
        modifier = modifier.clickable { /* Show dropdown */ }
    )
}