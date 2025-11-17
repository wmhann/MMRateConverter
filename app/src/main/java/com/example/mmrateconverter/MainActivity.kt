package com.example.mmrateconverter
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mmrateconverter.presentation.navigation.NavItem
import com.example.mmrateconverter.presentation.ui.calculator.CalculatorScreen
import com.example.mmrateconverter.presentation.ui.gold.GoldScreen
import com.example.mmrateconverter.presentation.ui.rates.RatesScreen
import com.example.mmrateconverter.presentation.ui.settings.SettingsScreen
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
    // Bottom Bar မှာ ပေါ်မယ့် Items ၃ ခုသာ ထည့်ပါ
    val bottomNavItems = listOf(NavItem.Rates, NavItem.Gold, NavItem.Calculator)

    Scaffold(
        bottomBar = {
            NavigationBar { // <-- Bottom Bar
                val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
                bottomNavItems.forEach { item ->
                    val isSelected = currentRoute == item.route

                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) },
                        selected = isSelected,
                        onClick = {
                            if (currentRoute != item.route) {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = NavItem.Rates.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            // Rates Screen (Settings Navigation ကို Controller ပေးပို့ခြင်း)
            composable(NavItem.Rates.route) {
                RatesScreen(
                    onNavigateToCalculator = { navController.navigate(NavItem.Calculator.route) },
                    onNavigateToSettings = { navController.navigate(NavItem.SettingsRoute.route) } // <-- Settings ကို ပို့ရန်
                )
            }
            composable(NavItem.Gold.route) { GoldScreen(navController = navController) } // <-- New Screen လိုအပ်
            composable(NavItem.Calculator.route) { CalculatorScreen(navController = navController) }
            composable(NavItem.SettingsRoute.route) { SettingsScreen(navController = navController) } // <-- New Screen လိုအပ်
        }
    }
}


