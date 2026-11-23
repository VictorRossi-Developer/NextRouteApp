package com.example.nextroute.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val NextRouteTypography = Typography(
    headlineLarge = TextStyle(
        fontSize = 29.sp,
        lineHeight = 36.sp,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = (-0.35).sp,
        color = NextRouteText
    ),
    headlineMedium = TextStyle(
        fontSize = 24.sp,
        lineHeight = 31.sp,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = (-0.2).sp,
        color = NextRouteText
    ),
    headlineSmall = TextStyle(
        fontSize = 21.sp,
        lineHeight = 28.sp,
        fontWeight = FontWeight.SemiBold,
        color = NextRouteText
    ),
    titleLarge = TextStyle(
        fontSize = 18.sp,
        lineHeight = 25.sp,
        fontWeight = FontWeight.SemiBold,
        color = NextRouteText
    ),
    titleMedium = TextStyle(
        fontSize = 15.sp,
        lineHeight = 22.sp,
        fontWeight = FontWeight.Medium,
        color = NextRouteText
    ),
    bodyLarge = TextStyle(
        fontSize = 15.sp,
        lineHeight = 23.sp
    ),
    bodyMedium = TextStyle(
        fontSize = 14.sp,
        lineHeight = 21.sp
    ),
    labelLarge = TextStyle(
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium,
        letterSpacing = 0.1.sp
    )
)
