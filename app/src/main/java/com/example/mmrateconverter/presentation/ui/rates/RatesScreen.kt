package com.example.mmrateconverter.presentation.ui.rates

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn // Import
import androidx.compose.foundation.lazy.items // Import
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold // Import
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier // Import
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp // Import
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mmrateconverter.domain.entities.ExchangeRateEntity
import com.example.mmrateconverter.domain.entities.GoldPriceEntity

// Gold Price Item (Favorite Button ကို Row ထဲမှာ တိုက်ရိုက် ပြင်ဆင်)
@Composable
fun GoldPriceItem(
    goldPrice: GoldPriceEntity,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth().clickable(onClick = onFavoriteClick),
        verticalAlignment = Alignment.CenterVertically // Import ကို Alignment.CenterVertically သုံးပါ
    ) {
        Column(modifier = Modifier.weight(1f)) { // <-- Column ကို weight ပေးပါ
            Text(text = goldPrice.name, style = MaterialTheme.typography.bodyLarge)

            Text(text = "${String.format("%,.2f", goldPrice.price)} ${goldPrice.unit}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.DarkGray)
        }
        // IconButton ကို Column အပြင် Row ထဲမှာ ထားခြင်း
        IconButton(onClick = onFavoriteClick) {
            Icon(
                imageVector = if (goldPrice.isFavorite) Icons.Filled.Star else Icons.Default.StarBorder,
                contentDescription = "Favorite Gold"
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
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
        },
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("MM Exchange Rates & Gold") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            // 1. Loading Indicator
            if (state.isLoading && state.rates.isEmpty()) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .align(Alignment.CenterHorizontally) // <-- Alignment Import ကို သုံးပါ
                )
            }

            // 2. Last Updated Text (Header)
            Row(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
                Text(text = "Latest Rates", modifier = Modifier.weight(1f))
                Text(text = state.lastUpdatedText)
            }

            // 3. Error Message
            state.error?.let { Text(text = it, color = Color.Red, modifier = Modifier.padding(horizontal = 16.dp)) }

            // **4. EXCHANGE RATES LIST**
            Text(
                text = "Exchange Rates",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .padding(horizontal = 16.dp) // <-- Simplified Padding
            )

            // Rates List
            LazyColumn(modifier = Modifier.weight(1f)) { // <-- ဇယားကိုပဲ weight ပေးပါ
                items(state.rates) { rate ->
                    RateItem(
                        rate = rate,
                        onFavoriteClick = { viewModel.onFavoriteToggle(rate) },
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    ) // <-- 괄호 ပိတ်ရပါမယ်
                }
            }

            // **5. GOLD PRICES LIST**
            Text(
                text = "Gold Prices",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .padding(horizontal = 16.dp))

            // Gold List (Rates List အပြီးမှာ ဆက်တိုက် ထည့်သွင်းရန်)
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(state.goldPrices) { goldPrice ->
                    GoldPriceItem(
                        goldPrice = goldPrice,
                        onFavoriteClick = { viewModel.onGoldFavoriteToggle(goldPrice) } ,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
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
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically // <-- Alignment Import ကို သုံးပါ
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
// ... Preview Code ...

@Preview(showBackground = true)
@Composable
fun PreviewRateItem() {
    // Test Data နဲ့ ခေါ်ပါ။ ExchangeRateEntity အတွက် Sample data လိုအပ်ပါမယ်။
    val sampleRate = ExchangeRateEntity(
        id = "USD",
        name = "US Dollar",
        rateToMMK = 3500.0,
        lastUpdated = 1731720000000L,
        isFavorite = false
    )

    RateItem(rate = sampleRate, onFavoriteClick = {})
}