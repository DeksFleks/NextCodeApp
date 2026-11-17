package dbataev.nextcodeapp.app.navigation

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import dbataev.nextcodeapp.core.data.remote.RetrofitClient
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import dbataev.nextcodeapp.core.common.TaskType
import dbataev.nextcodeapp.core.common.viewModel.UserViewModel
import dbataev.nextcodeapp.core.data.remote.dto.TaskDto
import dbataev.nextcodeapp.core.designsystem.component.NextCodeTopBar
import dbataev.nextcodeapp.core.designsystem.theme.NcBackgroundColor
import dbataev.nextcodeapp.feature.auth.reg.RegisterScreen
import dbataev.nextcodeapp.feature.course.CourseScreen
import dbataev.nextcodeapp.feature.auth.SplashScreen
import dbataev.nextcodeapp.feature.auth.log.LoginScreen
import dbataev.nextcodeapp.feature.compiler.CompilerScreen
import dbataev.nextcodeapp.feature.lesson.tasks.lessonEnd.LessonEndScreen
import dbataev.nextcodeapp.feature.lesson.tasks.test.TestScreen
import dbataev.nextcodeapp.feature.lesson.theory.TheoryScreen
import dbataev.nextcodeapp.feature.lesson.tasks.write.WriteScreen
import dbataev.nextcodeapp.feature.setting.SettingScreen


@Composable
fun MainScreen(userViewModel: UserViewModel = viewModel()) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val shouldShowBars = currentRoute in listOf(
        Screen.Home.route,
        Screen.Leaderboard.route,
        Screen.Compiler.route,
        Screen.Achievements.route,
        Screen.Profile.route
    )

    val user by userViewModel.user.collectAsState()
    var currentLessonId by rememberSaveable {
        mutableLongStateOf(0L)
    }

    var sessionExpiredNotice by rememberSaveable { mutableStateOf(false) }
    val sessionEnd by RetrofitClient.session.ended.collectAsState()
    LaunchedEffect(sessionEnd) {
        val event = sessionEnd ?: return@LaunchedEffect
        userViewModel.clearUser()
        currentLessonId = 0L
        sessionExpiredNotice = event.expired
        navController.navigate(Screen.Login.route) {
            popUpTo(navController.graph.id) { inclusive = true }
            launchSingleTop = true
        }
        RetrofitClient.session.acknowledge(event)
    }

    // Android can restore a protected destination directly after process recreation.
    LaunchedEffect(currentRoute) {
        if (currentRoute != null && currentRoute !in listOf(Screen.Splash.route, Screen.Login.route, Screen.Register.route)
            && userViewModel.user.value == null) {
            if (RetrofitClient.session.token() == null) RetrofitClient.session.logout()
            else userViewModel.loadUser()
        }
    }

    val selectedIndex = when (currentRoute) {
        Screen.Home.route -> 0
        Screen.Leaderboard.route -> 1
        Screen.Compiler.route -> 2
        Screen.Achievements.route -> 3
        Screen.Profile.route -> 4
        else -> 0
    }

    fun navigateToTask(task: TaskDto, remainingTasks: ArrayList<TaskDto>) {
        navController.currentBackStackEntry
            ?.savedStateHandle
            ?.set("task", task)

        navController.currentBackStackEntry
            ?.savedStateHandle
            ?.set("tasks", remainingTasks)

        when (task.type) {
            TaskType.MULTIPLE_CHOICE -> {
                navController.navigate("test")
            }

            TaskType.CODING, TaskType.FILL_BLANK -> {
                navController.navigate("write")
            }

            else -> {
                Log.e("NAVIGATE_TO_TASK_DEBUG", "NAVIGATE_TO_TASK_DEBUG")
            }
        }
    }

    Scaffold(
        containerColor = NcBackgroundColor,
        topBar = {
            if (shouldShowBars) {
                NextCodeTopBar(
                    streak = user?.streak ?: 0,
                    level = user?.level ?: 1,
                    nickname = user?.nickname ?: "null",
                    currentXp = user?.xp ?: 0,
                    maxXp = user?.xpForNextLevel ?: 2_147_483_647,
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
                            0 -> learningRoute(user?.currentCourseId)
                            1 -> Screen.Leaderboard.route
                            2 -> Screen.Compiler.route
                            3 -> Screen.Achievements.route
                            4 -> Screen.Profile.route
                            else -> Screen.Home.route
                        }

                        navController.navigate(route) {
                            popUpTo(Screen.Home.route)
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
                    }
                )
            ) { backStackEntry ->
                val courseId = backStackEntry.arguments?.getLong("courseId") ?: 0L
                if (courseId <= 0L) {
                    LaunchedEffect(courseId) { navController.navigate(Screen.Course.route) }
                    return@composable
                }
                HomeScreen(
                    courseId = courseId,
                    navController = navController
                )
            }
            composable(Screen.Leaderboard.route) { LeaderboardScreen() }
            composable(Screen.Compiler.route){ CompilerScreen() }
            composable(Screen.Achievements.route) { AchievementsScreen() }
            composable(Screen.Profile.route) { ProfileScreen(
                navController = navController,
                userViewModel = userViewModel,
                achievementViewModel = viewModel()
            ) }
            composable(Screen.Course.route) {
                CourseScreen(
                    onCourseClick = { courseId ->
                        userViewModel.selectCourse(courseId)

                        navController.navigate(learningRoute(courseId)) {
                            popUpTo(navController.graph.id) { inclusive = true }
                        }
                    }
                )
            }

            composable(Screen.Splash.route) {
                SplashScreen(navController, userViewModel)
            }

            composable(Screen.Register.route) {
                RegisterScreen(
                    onRegisterSuccess = {
                        Log.d("REGISTER_DEBUG", "ON SUCCESS CALLED")

                        sessionExpiredNotice = false
                        userViewModel.clearUser()
                        userViewModel.loadUser()

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
                    sessionExpired = sessionExpiredNotice,
                    onLoginSuccess = {
                        sessionExpiredNotice = false
                        userViewModel.clearUser()
                        navController.navigate(Screen.Splash.route) {
                            popUpTo(navController.graph.id) { inclusive = true }
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

                if (lessonId != 0L) {
                    currentLessonId = lessonId
                    Log.d("LESSON_ID_DEBUG", "saved currentLessonId = $currentLessonId")
                } else {
                    Log.e("LESSON_ID_DEBUG", "lessonId is 0, currentLessonId not overwritten")
                }

                val theoryText = navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.get<String>("theoryText")

                Log.e("THEORY_DEBUG", "lessonId = $lessonId, theoryText = $theoryText")

                TheoryScreen(
                    theoryText = theoryText,
                    id = if (lessonId != 0L) lessonId else currentLessonId,
                    onStartTask = { task, remainingTasks ->
                        navigateToTask(task, remainingTasks)
                    }
                )
            }
            composable("test") {
                val task = navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.get<TaskDto>("task")

                val tasks = navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.get<ArrayList<TaskDto>>("tasks")
                    ?: arrayListOf()

                if (task != null) {
                    TestScreen(
                        task = task,
                        lessonId = currentLessonId,
                        onTaskComplete = {
                            val nextTask = tasks.firstOrNull()

                            if (nextTask == null) {
                                navController.navigate("lesson_end") {
                                    popUpTo("test") {
                                        inclusive = true
                                    }
                                    launchSingleTop = true
                                }
                            } else {
                                val remainingTasks = ArrayList(tasks.drop(1))
                                navigateToTask(nextTask, remainingTasks)
                            }
                        }
                    )
                } else {
                    Log.e("TEST_DEBUG", "task is null")
                }
            }

            composable("write") {
                val task = navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.get<TaskDto>("task")

                val tasks = navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.get<ArrayList<TaskDto>>("tasks")
                    ?: arrayListOf()

                if (task != null) {
                    WriteScreen(
                        task = task,
                        lessonId = currentLessonId,
                        onTaskComplete = {
                            val nextTask = tasks.firstOrNull()

                            if (nextTask == null) {
                                navController.navigate("lesson_end") {
                                    popUpTo("write") {
                                        inclusive = true
                                    }
                                    launchSingleTop = true
                                }
                            } else {
                                val remainingTasks = ArrayList(tasks.drop(1))
                                navigateToTask(nextTask, remainingTasks)
                            }
                        }
                    )
                } else {
                    Log.e("WRITE_DEBUG", "task is null")
                }
            }

            composable("lesson_end") {
                Log.d("LESSON_ID_DEBUG", "lesson_end currentLessonId = $currentLessonId")

                LessonEndScreen(
                    lessonId = currentLessonId,
                    userViewModel = userViewModel,
                    onContinue = {
                        currentLessonId = 0L
                        navController.navigate(learningRoute(user?.currentCourseId)) {
                            popUpTo(navController.graph.id) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable("settings") {
                SettingScreen(
                    navController = navController,
                    userViewModel = userViewModel
                )
            }
        }
    }
}