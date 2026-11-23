package com.example.nextroute

import android.graphics.Color as AndroidColor
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.nextroute.navigation.NextRouteNavHost
import com.example.nextroute.ui.theme.NextRouteTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // Usa o tema de abertura do Android antes de criar a interface Compose.
        installSplashScreen()
        super.onCreate(savedInstanceState)

        window.statusBarColor = AndroidColor.rgb(24, 34, 74)
        window.navigationBarColor = AndroidColor.rgb(247, 248, 252)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR

        setContent {
            NextRouteTheme {
                NextRouteNavHost()
            }
        }
    }
}
