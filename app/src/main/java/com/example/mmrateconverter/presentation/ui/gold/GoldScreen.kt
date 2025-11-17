package com.example.mmrateconverter.presentation.ui.gold

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.CenterAlignedTopAppBar
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.mmrateconverter.domain.entities.GoldPriceEntity
import com.example.mmrateconverter.presentation.ui.rates.RatesViewModel


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

            Text(
                text = "${String.format("%,.2f", goldPrice.price)} ${goldPrice.unit}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.DarkGray
            )
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
fun GoldScreen(
    viewModel: RatesViewModel = hiltViewModel(),
    navController: NavController
) {
    val state by viewModel.state.collectAsState()
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Gold Prices") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer)

            )
        }
    ) { paddingValues ->
        Column (
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
        ) {
            Text(
                text = "World Gold Prices",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .padding(horizontal = 16.dp)
            )
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(state.goldPrices) { goldPrice ->
                    GoldPriceItem(
                        goldPrice = goldPrice,
                        onFavoriteClick = { viewModel.onGoldFavoriteToggle(goldPrice) },
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }

        } // <-- Scaffold ရဲ့ 괄호 ပိတ်ခြင်း
    }
}






