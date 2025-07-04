package com.sultonuzdev.dailydo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.sultonuzdev.dailydo.ui.components.DailyDoBottomBar
import com.sultonuzdev.dailydo.ui.navigation.DailyDoNavGraph
import com.sultonuzdev.dailydo.ui.theme.DailyDoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DailyDoTheme {
                DailyDoApp()
            }
        }
    }
}

@Composable
fun DailyDoApp() {
    val navController = rememberNavController()

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground,
        bottomBar = {
            DailyDoBottomBar(navController = navController)
        }
    ) { internalPadding ->
        // Use the NavGraph directly within the Scaffold content area
        DailyDoNavGraph(
            navController = navController,
            modifier = Modifier
                .fillMaxSize()
                .padding(internalPadding)
        )
    }
}

@Preview
@Composable
fun PreviewDailyDoApp() {
    DailyDoTheme {
        DailyDoApp()
    }
}