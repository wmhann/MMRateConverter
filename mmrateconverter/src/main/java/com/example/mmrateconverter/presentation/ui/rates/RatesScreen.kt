package com.example.mmrateconverter.presentation.ui.rates

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
// ... (Compose imports)

@Composable
fun RatesScreen(viewModel: RatesViewModel = viewModel()) {
    // ViewModel ရဲ့ StateFlow ကို စောင့်ကြည့်ခြင်း
    val state by viewModel.state.collectAsState()

    Column {
        // 1. Loading Indicator
        if (state.isLoading && state.rates.isEmpty()) {
            CircularProgressIndicator()
        }

        // 2. Last Updated Text
        Text(text = state.lastUpdatedText)

        // 3. Error Message
        state.error?.let { Text(text = it, color = Color.Red) }

        // 4. Rates List
        LazyColumn {
            items(state.rates) { rate ->
                RateItem(rate = rate, onFavoriteClick = { viewModel.onFavoriteToggle(rate) })
            }
        }
    }
}

// Rate Item တစ်ခုချင်းစီအတွက် Composable
@Composable
fun RateItem(rate: ExchangeRateEntity, onFavoriteClick: () -> Unit) {
    Row {
        Text(text = "${rate.name} (${rate.id})")
        Text(text = "1 ${rate.id} = ${rate.rateToMMK} MMK")

        // Favorite Button
        IconButton(onClick = onFavoriteClick) {
            Icon(
                imageVector = if (rate.isFavorite) Icons.Filled.Star else Icons.Outlined.Star,
                contentDescription = "Favorite"
            )
        }
    }
}