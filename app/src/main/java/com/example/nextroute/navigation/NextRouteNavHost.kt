package com.example.nextroute.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.nextroute.ui.screens.HomeScreen
import com.example.nextroute.ui.screens.QuizScreen
import com.example.nextroute.ui.screens.SplashScreen
import com.example.nextroute.ui.screens.WelcomeScreen
import com.example.nextroute.viewmodel.QuizViewModel

@Composable
fun NextRouteNavHost() {
    val navController = rememberNavController()
    val quizViewModel: QuizViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable("splash") {
            SplashScreen {
                navController.navigate("welcome") {
                    popUpTo("splash") { inclusive = true }
                    launchSingleTop = true
                }
            }
        }

        composable("welcome") {
            WelcomeScreen {
                quizViewModel.resetQuiz()
                navController.navigate("quiz")
            }
        }

        composable("quiz") {
            QuizScreen(
                viewModel = quizViewModel,
                onBack = { navController.popBackStack() },
                onFinish = {
                    if (navController.previousBackStackEntry?.destination?.route == "home") {
                        navController.popBackStack("home", inclusive = false)
                    } else {
                        navController.navigate("home") {
                            popUpTo("welcome") { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                }
            )
        }

        composable("home") {
            HomeScreen(
                onAccountCreated = {
                    quizViewModel.resetQuiz()
                    navController.navigate("quiz")
                }
            )
        }
    }
}
