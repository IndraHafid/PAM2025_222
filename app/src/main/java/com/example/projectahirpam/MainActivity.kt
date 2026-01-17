package com.example.projectahirpam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import com.example.projectahirpam.ui.theme.ProjectAhirPAMTheme
import com.example.projectahirpam.uicontroller.navigation.AppNavGraph
import com.example.projectahirpam.uicontroller.splash.SplashScreen
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ProjectAhirPAMTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFF121212) // dark base seperti WhatsApp
                ) {
                    var showSplash by remember { mutableStateOf(true) }

                    if (showSplash) {
                        SplashScreen(onTimeout = { showSplash = false })
                    } else {
                        AppNavGraph()
                    }
                }
            }
        }
    }
}
