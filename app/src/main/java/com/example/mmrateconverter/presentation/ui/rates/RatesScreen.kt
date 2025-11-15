package com.example.mmrateconverter.presentation.ui.rates

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize // Import
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding // Import
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold // Import
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier // Import
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp // Import
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mmrateconverter.domain.entities.ExchangeRateEntity


@Composable
fun RatesScreen(
    viewModel: RatesViewModel = hiltViewModel(),
    onNavigateToCalculator: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToCalculator) {
                Icon(Icons.Filled.Calculate, contentDescription = "Calculate")
            }
        }
    ) { paddingValues -> // <-- paddingValues ကို လက်ခံပါ

        Column(
            modifier = Modifier
                .padding(paddingValues) // <-- padding ကို သုံးပါ
                .fillMaxSize()
        ) {
            // 1. Loading Indicator
            if (state.isLoading && state.rates.isEmpty()) {
                CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp).align(androidx.compose.ui.Alignment.CenterHorizontally)) // Alignment ထည့်ပါ
            }

            // 2. Last Updated Text (Header ထဲမှာ ထည့်ဖို့ ပြင်ဆင်ပါ)
            Row(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
                Text(text = "Latest Rates", modifier = Modifier.weight(1f))
                Text(text = state.lastUpdatedText)
            }


            // 3. Error Message
            state.error?.let { Text(text = it, color = Color.Red, modifier = Modifier.padding(horizontal = 16.dp)) }

            // 4. Rates List
            LazyColumn { // <-- ဒီနေရာက ရှင်းသွားပါလိမ့်မယ်
                items(state.rates) { rate ->
                    // RateItem ကို ဒီမှာ ခေါ်ရပါမယ်
                    RateItem(
                        rate = rate,
                        onFavoriteClick = { viewModel.onFavoriteToggle(rate) },
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp) // Item ကို padding ပေးပါ
                    )
                }
            }
        }
    } // <-- Scaffold ရဲ့ 괄호 ပိတ်ခြင်း
}


// Rate Item ကို Modifier ပါဝင်အောင် ပြင်ဆင်ပါ
@Composable
fun RateItem(
    rate: ExchangeRateEntity,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier // <-- Modifier ကို လက်ခံပါ
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically // Import လိုပါမည်
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = "${rate.name} (${rate.id})")
            Text(text = "1 ${rate.id} = ${rate.rateToMMK} MMK")
        }

        // Favorite Button
        IconButton(onClick = onFavoriteClick) {
            Icon(
                imageVector = if (rate.isFavorite) Icons.Filled.Star else Icons.Default.StarBorder,
                contentDescription = "Favorite"
            )
        }
    }
}