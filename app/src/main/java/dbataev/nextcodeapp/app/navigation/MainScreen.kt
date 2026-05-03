package dbataev.nextcodeapp.app.navigation

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dbataev.nextcodeapp.core.designsystem.component.NextCodeBottomMenu
import dbataev.nextcodeapp.feature.achievements.AchievementsScreen
import dbataev.nextcodeapp.feature.home.HomeScreen
import dbataev.nextcodeapp.feature.leaderboard.LeaderboardScreen
import dbataev.nextcodeapp.feature.profile.ProfileScreen
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import dbataev.nextcodeapp.core.data.remote.dto.TaskDto
import dbataev.nextcodeapp.core.designsystem.component.NextCodeTopBar
import dbataev.nextcodeapp.core.designsystem.theme.NcBackgroundColor
import dbataev.nextcodeapp.feature.auth.reg.RegisterScreen
import dbataev.nextcodeapp.feature.course.CourseScreen
import dbataev.nextcodeapp.feature.auth.SplashScreen
import dbataev.nextcodeapp.feature.auth.log.LoginScreen
import dbataev.nextcodeapp.feature.home.HomeViewModel
import dbataev.nextcodeapp.feature.lesson.tasks.test.TestScreen
import dbataev.nextcodeapp.feature.lesson.theory.TheoryScreen

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val shouldShowBars = currentRoute in listOf(
        Screen.Home.route,
        Screen.Leaderboard.route,
        Screen.Achievements.route,
        Screen.Profile.route
    )

    val selectedIndex = when (currentRoute) {
        Screen.Home.route -> 0
        Screen.Leaderboard.route -> 1
        Screen.Achievements.route -> 2
        Screen.Profile.route -> 3
        else -> 0
    }

    Scaffold(
        containerColor = NcBackgroundColor,
        topBar = {
            if (shouldShowBars) {
                NextCodeTopBar(
                    streak = 3650,
                    level = 50,
                    nickname = "developer",
                    currentXp = 500,
                    maxXp = 600,
                    onCourseClick = { navController.navigate("course") }
                )
            }
        },
        bottomBar = {
            if (shouldShowBars) {
                NextCodeBottomMenu(
                    selectedIndex = selectedIndex,
                    onItemClick = { index ->
                        val route = when (index) {
                            // 0 -> Screen.Home.route
                            0 -> Screen.Home.createRoute(1L)
                            1 -> Screen.Leaderboard.route
                            2 -> Screen.Achievements.route
                            3 -> Screen.Profile.route
                            else -> Screen.Home.route
                        }

                        navController.navigate(route) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Splash.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(
                route = Screen.Home.route,
                arguments = listOf(
                    navArgument("courseId") {
                        type = NavType.LongType
                        defaultValue = 1L
                    }
                )
            ) { backStackEntry ->
                val courseId = backStackEntry.arguments?.getLong("courseId") ?: 1L
                HomeScreen(
                    courseId = courseId,
                    navController = navController
                )
            }
            composable(Screen.Leaderboard.route) { LeaderboardScreen() }
            composable(Screen.Achievements.route) { AchievementsScreen() }
            composable(Screen.Profile.route) { ProfileScreen() }
            composable(Screen.Course.route) {
                CourseScreen(
                    onCourseClick = { courseId ->
                        navController.navigate(Screen.Home.createRoute(courseId)) {
                            popUpTo(Screen.Course.route) {
                                inclusive = true
                            }
                        }
                    }
                )
            }

            composable(Screen.Splash.route) {
                SplashScreen(navController)
            }

            composable(Screen.Register.route) {
                RegisterScreen(
                    onRegisterSuccess = {
                        Log.d("REGISTER_DEBUG", "ON SUCCESS CALLED")

                        navController.navigate(Screen.Course.route) {
                            popUpTo(Screen.Register.route) {
                                inclusive = true
                            }
                        }
                    },
                    onLoginClick = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Register.route) {
                                inclusive = true
                            }
                        }
                    }
                )
            }
            composable("login") {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate(Screen.Home.createRoute(1L)) {
                            popUpTo("login") {
                                inclusive = true
                            }
                        }
                    },
                    onRegisterClick = {
                        navController.navigate("register") {
                            popUpTo("login") {
                                inclusive = true
                            }
                        }
                    }
                )
            }
            composable("theory") {
                Log.e("THEORY_DEBUG", "NAVIGATION TO THEORY")

                val lessonId = navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.get<Long>("lessonId") ?: 0L

                val theoryText = navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.get<String>("theoryText")

                Log.e("THEORY_DEBUG", "lessonId = $lessonId, theoryText = $theoryText")

                TheoryScreen(
                    theoryText = theoryText,
                    id = lessonId,
                    onStartTest = { task ->
                        navController.currentBackStackEntry
                            ?.savedStateHandle
                            ?.set("task", task)

                        navController.navigate("test")
                    }
                )
            }
            composable("test") {
                val task = navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.get<TaskDto>("task")

                if (task != null) {
                    TestScreen(task = task)
                } else {
                    Log.e("TEST_DEBUG", "task is null")
                }
            }
        }
    }
}