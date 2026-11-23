package com.example.nextroute.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.nextroute.data.Destination

class QuizViewModel : ViewModel() {

    var currentStep by mutableStateOf(1)
        private set

    var travelType by mutableStateOf<String?>(null)
        private set

    var scenery by mutableStateOf<String?>(null)
        private set

    var budget by mutableStateOf(7500f)
        private set

    var duration by mutableStateOf<String?>(null)
        private set

    var companions by mutableStateOf<String?>(null)
        private set

    var result by mutableStateOf<Destination?>(null)
        private set


    fun selectTravelType(value: String) {
        travelType = value
    }

    fun selectScenery(value: String) {
        scenery = value
    }

    fun updateBudget(value: Float) {
        budget = value
    }

    fun selectDuration(value: String) {
        duration = value
    }

    fun selectCompanions(value: String) {
        companions = value
    }


    fun previousStep() {
        if (currentStep > 1 && currentStep <= 5) {
            currentStep--
        }
    }

    fun nextStep() {
        if (currentStep < 4) {
            currentStep++
        }
    }


    fun finishQuiz() {

        val destinations = listOf(

            Destination(
                name = "Fernando de Noronha",
                description = "Um destino perfeito para quem busca praias paradisíacas, natureza preservada e momentos inesquecíveis junto ao mar.",
                imageUrl = "https://images.unsplash.com/photo-1548013146-72479768bada",
                price = 3200.0,
                travelType = "Praia e relaxamento",
                scenery = "Maldivas"
            ),

            Destination(
                name = "Patagônia",
                description = "Uma experiência de aventura cercada por montanhas, paisagens impressionantes e natureza selvagem.",
                imageUrl = "https://images.unsplash.com/photo-1526392060635-9d6019884377",
                price = 6800.0,
                travelType = "Aventura e natureza",
                scenery = "Patagônia"
            ),

            Destination(
                name = "Toscana",
                description = "Uma viagem marcada por história, arquitetura, paisagens rurais, cultura e experiências gastronômicas.",
                imageUrl = "https://images.unsplash.com/photo-1523906834658-6e24ef2386f9",
                price = 7200.0,
                travelType = "Cultura e história",
                scenery = "Toscana"
            ),

            Destination(
                name = "Tóquio",
                description = "Uma experiência vibrante para descobrir gastronomia, tecnologia, cultura, entretenimento e a vida noturna japonesa.",
                imageUrl = "https://images.unsplash.com/photo-1540959733332-eab4deabeeaf",
                price = 8500.0,
                travelType = "Gastronomia e vida noturna",
                scenery = "Tóquio"
            )
        )


        fun score(destination: Destination): Int {

            var points = 0

            // Tipo de viagem
            if (destination.travelType == travelType) {
                points += 50
            }

            // Cenário
            if (destination.scenery == scenery) {
                points += 40
            }

            // Orçamento
            val difference = kotlin.math.abs(budget.toDouble() - destination.price)

            points += when {
                difference <= 1500 -> 25
                difference <= 3000 -> 15
                difference <= 5000 -> 5
                else -> 0
            }

            // Duração
            when (destination.name) {

                "Fernando de Noronha" -> {
                    if (
                        duration == "3–5 dias" ||
                        duration == "1 semana"
                    ) {
                        points += 15
                    }
                }

                "Patagônia" -> {
                    if (
                        duration == "1 semana" ||
                        duration == "2 semanas"
                    ) {
                        points += 15
                    }
                }

                "Toscana" -> {
                    if (
                        duration == "1 semana" ||
                        duration == "2 semanas"
                    ) {
                        points += 15
                    }
                }

                "Tóquio" -> {
                    if (
                        duration == "2 semanas" ||
                        duration == "+2 semanas"
                    ) {
                        points += 15
                    }
                }
            }

            // Companhia
            when (companions) {

                "Sozinho(a)" -> {
                    if (
                        destination.name == "Patagônia" ||
                        destination.name == "Tóquio"
                    ) {
                        points += 8
                    }
                }

                "Com Amigos" -> {
                    if (
                        destination.name == "Patagônia" ||
                        destination.name == "Tóquio"
                    ) {
                        points += 8
                    }
                }

                "Com Família" -> {
                    if (
                        destination.name == "Fernando de Noronha" ||
                        destination.name == "Toscana"
                    ) {
                        points += 8
                    }
                }

                "A Trabalho" -> {
                    if (destination.name == "Tóquio") {
                        points += 8
                    }
                }
            }

            return points
        }


        result = destinations.maxByOrNull {
            score(it)
        }

        currentStep = 5
    }


    fun continueToApp() {
        currentStep = 6
    }


    fun resetQuiz() {
        currentStep = 1
        travelType = null
        scenery = null
        budget = 7500f
        duration = null
        companions = null
        result = null
    }
}