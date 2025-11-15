package com.example.mmrateconverter.presentation.ui.rates

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mmrateconverter.domain.entities.ExchangeRateEntity


@Composable
fun RatesScreen(viewModel: RatesViewModel = hiltViewModel()) {
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
                imageVector = if (rate.isFavorite) Icons.Filled.Star else Icons.Default.StarBorder,
                contentDescription = "Favorite"
            )
        }
    }
}