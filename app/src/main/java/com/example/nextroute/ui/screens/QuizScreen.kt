package com.example.nextroute.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Work
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material3.Card
import androidx.compose.material3.IconButton
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.nextroute.ui.components.NextRouteButton
import com.example.nextroute.ui.components.QuizProgress
import com.example.nextroute.ui.theme.NextRouteBlue
import com.example.nextroute.ui.theme.NextRouteGray
import com.example.nextroute.ui.theme.NextRouteLightBlue
import com.example.nextroute.ui.theme.NextRouteLightGray
import com.example.nextroute.ui.theme.NextRoutePurple
import com.example.nextroute.ui.theme.NextRouteText
import com.example.nextroute.ui.theme.NextRouteYellow
import com.example.nextroute.viewmodel.QuizViewModel
import androidx.compose.foundation.layout.navigationBarsPadding


@Composable
fun QuizScreen(
    viewModel: QuizViewModel,
    onBack: () -> Unit,
    onFinish: () -> Unit
) {

    LaunchedEffect(viewModel.currentStep) {
        if (viewModel.currentStep == 6) onFinish()
    }

    if (viewModel.currentStep == 6) return

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                start = 28.dp,
                end = 28.dp,
                bottom = 35.dp
            )
    ){

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    if (viewModel.currentStep > 1) viewModel.previousStep() else onBack()
                }
            ) {
                Icon(
                    imageVector = Icons.Outlined.ArrowBack,
                    contentDescription = "Voltar",
                    tint = NextRoutePurple
                )
            }
            Text(
                text = "Vamos planejar sua viagem",
                style = MaterialTheme.typography.titleMedium,
                color = NextRouteText
            )
        }

        Spacer(Modifier.height(14.dp))

        if (viewModel.currentStep in 1..4) {
            QuizProgress(viewModel.currentStep)
            Spacer(Modifier.height(24.dp))
        }

        AnimatedContent(
            targetState = viewModel.currentStep,
            transitionSpec = {
                (fadeIn(tween(260)) + slideInVertically(tween(260)) { it / 16 }) togetherWith
                    (fadeOut(tween(180)) + slideOutVertically(tween(180)) { -it / 18 })
            },
            label = "quizStepTransition"
        ) { step ->
            when (step) {
                1 -> TravelTypeQuestion(viewModel = viewModel)
                2 -> SceneryQuestion(viewModel = viewModel)
                3 -> BudgetQuestion(viewModel = viewModel)
                4 -> CompanionsQuestion(viewModel = viewModel)
                5 -> ResultQuestion(
                    viewModel = viewModel,
                    onReset = { viewModel.resetQuiz() },
                    onContinue = { viewModel.continueToApp() }
                )
            }
        }
    }
}


/* =========================================================
   BOAS-VINDAS
   ========================================================= */

@Composable
private fun WelcomeInsideQuiz(
    onNext: () -> Unit
) {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Column {

            Text(
                text = "Vamos encontrar seu próximo destino",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            Text(
                text = "Responda algumas perguntas sobre suas preferências para receber uma recomendação de viagem.",
                fontSize = 14.sp,
                lineHeight = 22.sp
            )
        }

        NextRouteButton(
            text = "Começar",
            onClick = onNext
        )
    }
}


/* =========================================================
   PERGUNTA 1
   ========================================================= */

@Composable
private fun TravelTypeQuestion(
    viewModel: QuizViewModel
) {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Column {

            Text(
                text = "Qual tipo de viagem você prefere?",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(
                modifier = Modifier.height(25.dp)
            )

            val options = listOf(
                "Praia e relaxamento",
                "Aventura e natureza",
                "Cultura e história",
                "Gastronomia e vida noturna"
            )

            options.forEach { option ->

                QuizOption(
                    text = option,
                    selected = viewModel.travelType == option,
                    onClick = {
                        viewModel.selectTravelType(option)
                    }
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )
            }
        }

        NextRouteButton(
            text = "Continuar",
            onClick = {

                if (viewModel.travelType != null) {
                    viewModel.nextStep()
                }
            }
        )
    }
}


/* =========================================================
   OPÇÃO DA PERGUNTA 1
   ========================================================= */

@Composable
private fun QuizOption(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .clickable(
                onClick = onClick
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (selected) NextRouteLightBlue else Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(
            width = 1.dp,
            color = if (selected) NextRouteBlue else NextRouteLightBlue
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                text = text,
                fontSize = 13.sp,
                fontWeight = if (selected) {
                    FontWeight.SemiBold
                } else {
                    FontWeight.Normal
                }
            )

            if (selected) {

                Icon(
                    imageVector = Icons.Outlined.CheckCircle,
                    contentDescription = "Selecionado",
                    tint = NextRouteBlue
                )
            }
        }
    }
}


/* =========================================================
   PERGUNTA 2 - CENÁRIO
   ========================================================= */

@Composable
private fun SceneryQuestion(
    viewModel: QuizViewModel
) {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Column {

            Text(
                text = "Qual cenário te atrai mais?",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(
                modifier = Modifier.height(22.dp)
            )

            val destinations = listOf(
                "Maldivas" to "https://images.unsplash.com/photo-1514282401047-d79a71a590e8",
                "Patagônia" to "https://images.unsplash.com/photo-1526392060635-9d6019884377",
                "Toscana" to "https://images.unsplash.com/photo-1523906834658-6e24ef2386f9",
                "Tóquio" to "https://images.unsplash.com/photo-1540959733332-eab4deabeeaf"
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.height(390.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                items(
                    items = destinations
                ) { item ->

                    DestinationCard(
                        name = item.first,
                        imageUrl = item.second,
                        selected = viewModel.scenery == item.first,
                        onClick = {
                            viewModel.selectScenery(item.first)
                        }
                    )
                }
            }
        }

        NextRouteButton(
            text = "Continuar",
            onClick = {

                if (viewModel.scenery != null) {
                    viewModel.nextStep()
                }
            }
        )
    }
}


/* =========================================================
   CARD DE DESTINO
   ========================================================= */

@Composable
private fun DestinationCard(
    name: String,
    imageUrl: String,
    selected: Boolean,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(190.dp)
            .clickable(
                onClick = onClick
            ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(
            width = 1.dp,
            color = if (selected) NextRouteBlue else NextRouteLightBlue
        )
    ) {

        Box(
            modifier = Modifier.fillMaxSize()
        ) {

            AsyncImage(
                model = imageUrl,
                contentDescription = name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Surface(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth(),
                color = Color.Black.copy(alpha = 0.48f)
            ) {

                Text(
                    text = name,
                    modifier = Modifier.padding(10.dp),
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            if (selected) {

                Icon(
                    imageVector = Icons.Outlined.CheckCircle,
                    contentDescription = "Selecionado",
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp),
                    tint = NextRouteBlue
                )
            }
        }
    }
}


/* =========================================================
   PERGUNTA 3 - ORÇAMENTO
   ========================================================= */

@Composable
private fun BudgetQuestion(
    viewModel: QuizViewModel
) {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Column {

            Text(
                text = "Qual seu orçamento para a viagem?",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(
                modifier = Modifier.height(25.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                border = BorderStroke(1.dp, NextRouteLightBlue)
            ) {

                Column(
                    modifier = Modifier.padding(22.dp)
                ) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        Text(
                            text = "Min: R$ 2.000",
                            fontSize = 11.sp
                        )

                        Text(
                            text = "Max: R$ 15.000",
                            fontSize = 11.sp
                        )
                    }

                    Spacer(
                        modifier = Modifier.height(25.dp)
                    )

                    Text(
                        text = "R$ ${viewModel.budget.toInt()}",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = NextRoutePurple
                    )

                    Text(
                        text = "Orçamento estimado por pessoa",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontSize = 11.sp,
                        color = NextRouteGray
                    )

                    Slider(
                        value = viewModel.budget,
                        onValueChange = {
                            viewModel.updateBudget(it)
                        },
                        valueRange = 2000f..15000f
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(25.dp)
            )

            Text(
                text = "Duração aproximada",
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            listOf(
                "3–5 dias",
                "1 semana",
                "2 semanas",
                "+2 semanas"
            ).forEach { duration ->

                FilterChip(
                    selected = viewModel.duration == duration,
                    onClick = {
                        viewModel.selectDuration(duration)
                    },
                    label = {
                        Text(
                            text = duration
                        )
                    }
                )

                Spacer(
                    modifier = Modifier.height(7.dp)
                )
            }
        }

        NextRouteButton(
            text = "Continuar",
            onClick = {

                if (viewModel.duration != null) {
                    viewModel.nextStep()
                }
            }
        )
    }
}


/* =========================================================
   PERGUNTA 4 - COMPANHIA
   ========================================================= */

@Composable
private fun CompanionsQuestion(
    viewModel: QuizViewModel
) {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Column {

            Text(
                text = "Com quem você vai viajar?",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(
                modifier = Modifier.height(25.dp)
            )

            val options = listOf(
                Icons.Outlined.Person to "Sozinho(a)",
                Icons.Outlined.Group to "Com Amigos",
                Icons.Outlined.Home to "Com Família",
                Icons.Outlined.Work to "A Trabalho"
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.height(350.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                items(
                    items = options
                ) { item ->

                    CompanionCard(
                        icon = item.first,
                        text = item.second,
                        selected = viewModel.companions == item.second,
                        onClick = {
                            viewModel.selectCompanions(item.second)
                        }
                    )
                }
            }
        }

        NextRouteButton(
            text = "Ver recomendação",
            onClick = {

                if (viewModel.companions != null) {
                    viewModel.finishQuiz()
                }
            }
        )
    }
}


/* =========================================================
   CARD DE COMPANHIA
   ========================================================= */

@Composable
private fun CompanionCard(
    icon: ImageVector,
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .clickable(
                onClick = onClick
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (selected) NextRouteLightBlue else Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(
            width = 1.dp,
            color = if (selected) NextRouteBlue else NextRouteLightBlue
        )
    ) {

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(26.dp),
                tint = NextRoutePurple
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Text(
                text = text,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}


/* =========================================================
   RESULTADO
   ========================================================= */

@Composable
private fun ResultQuestion(
    viewModel: QuizViewModel,
    onReset: () -> Unit,
    onContinue: () -> Unit
) {

    val destination = viewModel.result ?: return

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 20.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Column {

            AsyncImage(
                model = destination.imageUrl,
                contentDescription = destination.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .clip(
                        RoundedCornerShape(14.dp)
                    ),
                contentScale = ContentScale.Crop
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            Text(
                text = "SEU DESTINO IDEAL É...",
                color = NextRouteBlue,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(5.dp)
            )

            Text(
                text = destination.name,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = NextRouteText
            )

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            Text(
                text = destination.description,
                fontSize = 13.sp,
                lineHeight = 20.sp
            )

            Spacer(
                modifier = Modifier.height(15.dp)
            )

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = NextRouteYellow.copy(
                        alpha = 0.25f
                    )
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = NextRouteYellow
                )
            ) {

                Column(
                    modifier = Modifier.padding(15.dp)
                ) {

                    Text(
                        text = "Pacotes promocionais!",
                        fontSize = 11.sp
                    )

                    Text(
                        text = "A partir de R$ 3.200 /pessoa",
                        fontWeight = FontWeight.Bold,
                        color = NextRoutePurple
                    )
                }
            }
        }

        Column {

            NextRouteButton(
                text = "Continuar",
                onClick = onContinue
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            OutlinedButton(
                onClick = onReset,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(9.dp)
            ) {

                Text(
                    text = "Refazer quiz"
                )
            }
        }
    }
}