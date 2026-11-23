package com.example.nextroute.ui.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.nextroute.R

@Composable
fun NextRouteLogo(
    modifier: Modifier = Modifier,
    contentDescription: String? = "NextRoute"
) {
    Image(
        painter = painterResource(id = R.drawable.next_route_logo),
        contentDescription = contentDescription,
        modifier = modifier,
        contentScale = ContentScale.Fit
    )
}
