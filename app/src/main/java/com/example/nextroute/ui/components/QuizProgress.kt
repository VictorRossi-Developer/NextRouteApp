package com.example.nextroute.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nextroute.ui.theme.NextRouteLightGray
import com.example.nextroute.ui.theme.NextRouteBlue
import com.example.nextroute.ui.theme.NextRouteText

@Composable
fun QuizProgress(step: Int) {
    Column {
        Text(
            text = "ETAPA $step DE 4",
            fontSize = 10.sp,
            color = NextRouteText,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.8.sp
        )
        Spacer(Modifier.height(8.dp))
        Box(
            Modifier
                .fillMaxWidth()
                .height(4.dp)
                .background(NextRouteLightGray, RoundedCornerShape(8.dp))
        ) {
            Box(
                Modifier
                    .fillMaxWidth((step / 4f).coerceIn(0f, 1f))
                    .fillMaxHeight()
                    .background(NextRouteBlue, RoundedCornerShape(8.dp))
            )
        }
    }
}
