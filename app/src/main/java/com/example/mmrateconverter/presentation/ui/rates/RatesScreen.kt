package com.example.mmrateconverter.presentation.ui.rates

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mmrateconverter.domain.entities.ExchangeRateEntity

// Gold Price Item (Favorite Button ကို Row ထဲမှာ တိုက်ရိုက် ပြင်ဆင်)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RatesScreen(
    viewModel: RatesViewModel = hiltViewModel(),
    onNavigateToCalculator: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    var menuExpanded by remember { mutableStateOf(false) } // Menu ရဲ့ State

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("MM Exchange Rates & Gold") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer),
                actions = { // <-- Action Buttons နေရာ
                    IconButton(onClick = { menuExpanded = true }) { // <-- Three Dots Button
                        Icon(Icons.Filled.MoreVert, contentDescription = "More Options")
                    }
                    // Dropdown Menu (Three Dots နှိပ်ရင် ပေါ်လာမယ့် Menu)
                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Settings") },
                            onClick = {
                                menuExpanded = false
                                onNavigateToSettings() // <-- Settings Screen ကို သွားခြင်း
                            }
                        )
                        // ... (အခြား Policy/Terms တွေ ဒီမှာ ထပ်ထည့်နိုင်သည်)
                    }
                }
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