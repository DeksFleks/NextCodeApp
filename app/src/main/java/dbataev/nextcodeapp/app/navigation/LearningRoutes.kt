package dbataev.nextcodeapp.app.navigation

/** A missing course always leads to selection, never a fabricated numeric ID. */
fun learningRoute(courseId: Long?): String =
    courseId?.takeIf { it > 0 }?.let { Screen.Home.createRoute(it) } ?: Screen.Course.route
