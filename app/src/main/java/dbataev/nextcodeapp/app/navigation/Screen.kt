package dbataev.nextcodeapp.app.navigation

sealed class Screen(val route: String){
    object Home : Screen("home/{courseId}") {
        fun createRoute(courseId: Long) = "home/$courseId"
    }
    data object Leaderboard : Screen("leaderboard")
    data object Achievements : Screen("achievements")
    data object Profile : Screen("profile")
    data object Course : Screen("course")
    data object Splash : Screen("splash")
    data object Register : Screen("register")
    data object Login : Screen("login")
    data object Theory : Screen("theory")
    data object Test: Screen("test")
}

