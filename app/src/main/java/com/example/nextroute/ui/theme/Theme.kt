package com.example.nextroute.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val NextRouteColors = lightColorScheme(
    primary = NextRoutePurple,
    onPrimary = NextRouteSurface,
    secondary = NextRouteBlue,
    onSecondary = NextRouteSurface,
    background = NextRouteBackground,
    onBackground = NextRouteText,
    surface = NextRouteSurface,
    onSurface = NextRouteText,
    surfaceVariant = NextRouteLightGray,
    onSurfaceVariant = NextRouteGray,
    secondaryContainer = NextRouteLightBlue,
    onSecondaryContainer = NextRoutePurple,
    outline = NextRouteBorder,
    outlineVariant = NextRouteBorder,
    tertiary = NextRouteYellow,
    onTertiary = NextRoutePurple
)

@Composable
fun NextRouteTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = NextRouteColors,
        typography = NextRouteTypography,
        content = content
    )
}
