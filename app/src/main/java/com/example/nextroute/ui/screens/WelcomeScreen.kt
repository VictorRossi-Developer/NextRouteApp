package com.example.nextroute.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.nextroute.ui.components.NextRouteButton
import com.example.nextroute.ui.components.NextRouteLogo
import com.example.nextroute.ui.theme.NextRouteBackground
import com.example.nextroute.ui.theme.NextRouteBorder
import com.example.nextroute.ui.theme.NextRouteGray
import com.example.nextroute.ui.theme.NextRoutePurple

private const val WelcomeTravelPhoto =
    "https://images.unsplash.com/photo-1771237740000-ba11e92b6734?auto=format&fit=crop&w=1800&q=85"

@Composable
fun WelcomeScreen(onStart: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NextRouteBackground)
            .padding(horizontal = 24.dp, vertical = 22.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 12.dp)
        ) {
            NextRouteLogo(
                contentDescription = "Logotipo NextRoute",
                modifier = Modifier.size(42.dp)
            )
            Text(
                text = "NextRoute",
                modifier = Modifier.padding(start = 11.dp),
                style = MaterialTheme.typography.titleLarge,
                color = NextRoutePurple
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center
        ) {
            AsyncImage(
                model = WelcomeTravelPhoto,
                contentDescription = "Vista da asa de um avião acima das nuvens, fotografada pela janela",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(238.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, NextRouteBorder, RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.height(28.dp))
            Text(
                text = "Sua próxima viagem começa aqui.",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.headlineLarge
            )
            Spacer(Modifier.height(10.dp))
            Text(
                text = "Conte um pouco sobre o que você procura e descubra destinos que combinam com seu estilo, seu tempo e seu orçamento.",
                color = NextRouteGray,
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Column {
            NextRouteButton(
                text = "Encontrar meu destino",
                onClick = onStart
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text = "Planeje melhor. Explore mais.",
                modifier = Modifier.fillMaxWidth(),
                color = NextRouteGray,
                style = MaterialTheme.typography.bodySmall,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}
