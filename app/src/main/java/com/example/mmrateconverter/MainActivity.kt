package com.example.mmrateconverter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mmrateconverter.presentation.ui.calculator.CalculatorScreen
import com.example.mmrateconverter.presentation.ui.rates.RatesScreen
import com.example.mmrateconverter.presentation.ui.theme.MMRateConverterTheme
import dagger.hilt.android.AndroidEntryPoint

// Hilt ကို အသုံးပြုမယ့် Activity ဖြစ်ကြောင်း ကြေညာခြင်း
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MMRateConverterTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation() // App ရဲ့ Navigation ကို စတင်ခြင်း
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "rates_list") {

        // 1. Rates List Screen (Home)
        composable("rates_list") {
            // ... RatesViewModel Injection Logic ...
            RatesScreen(
                onNavigateToCalculator = {
                    navController.navigate("calculator")
                }
            )
        }

        // 2. Calculator Screen
        composable("calculator") {
            CalculatorScreen() // <--- Calculator Screen ကို ပြသပါမည်။
        }
    }
}


