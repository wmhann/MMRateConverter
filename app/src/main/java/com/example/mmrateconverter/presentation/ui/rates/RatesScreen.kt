package com.example.mmrateconverter.presentation.ui.rates

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mmrateconverter.data.local.entity.ExchangeRateRoomEntity
import com.example.mmrateconverter.domain.entities.ExchangeRateEntity



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

fun viewModel(): RatesViewModel {
    TODO("Not yet implemented")
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