package com.example.nextroute.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nextroute.ui.components.NextRouteLogo
import com.example.nextroute.ui.theme.NextRouteBackground
import com.example.nextroute.ui.theme.NextRouteBlue
import com.example.nextroute.ui.theme.NextRouteGray
import com.example.nextroute.ui.theme.NextRouteLightBlue
import com.example.nextroute.ui.theme.NextRouteLightPurple
import com.example.nextroute.ui.theme.NextRoutePurple
import com.example.nextroute.ui.theme.NextRouteText
import com.example.nextroute.ui.theme.NextRouteYellow
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onTimeout: () -> Unit) {
    var visible by remember { mutableStateOf(false) }
    val contentAlpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = 420),
        label = "splashContentAlpha"
    )
    val contentScale by animateFloatAsState(
        targetValue = if (visible) 1f else 0.97f,
        animationSpec = tween(durationMillis = 520),
        label = "splashContentScale"
    )
    val progress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        visible = true
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1450)
        )
        delay(220)
        onTimeout()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(NextRouteBackground),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 34.dp)
                .alpha(contentAlpha)
                .graphicsLayer {
                    scaleX = contentScale
                    scaleY = contentScale
                },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                modifier = Modifier.size(164.dp),
                shape = CircleShape,
                color = NextRouteLightBlue,
                border = BorderStroke(1.dp, NextRouteBlue.copy(alpha = 0.16f)),
                shadowElevation = 0.dp
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(13.dp),
                    contentAlignment = Alignment.Center
                ) {
                    NextRouteLogo(
                        contentDescription = "Logotipo NextRoute",
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            Spacer(Modifier.height(27.dp))

            Text(
                text = "NextRoute",
                color = NextRoutePurple,
                fontSize = 32.sp,
                lineHeight = 38.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.6).sp
            )

            Spacer(Modifier.height(9.dp))

            Text(
                text = "Sua próxima viagem começa aqui.",
                color = NextRouteText,
                fontSize = 14.sp,
                lineHeight = 21.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(30.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.54f)
                    .height(4.dp)
                    .background(NextRouteLightPurple, RoundedCornerShape(50))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progress.value)
                        .height(4.dp)
                        .background(NextRouteBlue, RoundedCornerShape(50))
                )
            }

            Spacer(Modifier.height(18.dp))

            Box(
                modifier = Modifier
                    .size(width = 28.dp, height = 3.dp)
                    .background(NextRouteYellow, RoundedCornerShape(50))
            )

            Spacer(Modifier.height(11.dp))

            Text(
                text = "PLANEJE MELHOR. EXPLORE MAIS.",
                color = NextRouteGray,
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.35.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

