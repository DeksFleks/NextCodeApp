package dbataev.nextcodeapp.feature.lesson.tasks.lessonEnd

import android.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import dbataev.nextcodeapp.app.navigation.Screen
import dbataev.nextcodeapp.core.datastore.ContextRepository
import dbataev.nextcodeapp.core.designsystem.component.NcBackgroundText
import dbataev.nextcodeapp.core.designsystem.component.NcNullBottomBar
import dbataev.nextcodeapp.core.designsystem.component.NcNullTopBar
import dbataev.nextcodeapp.core.designsystem.component.NextCodeButton
import dbataev.nextcodeapp.core.designsystem.theme.DefaultAppTextStyles
import dbataev.nextcodeapp.core.designsystem.theme.NcAccentColor
import dbataev.nextcodeapp.core.designsystem.theme.NcBackgroundColor

@Composable
fun LessonEndScreen(
    navController: NavController,
    lessonId: Long,
    onCompleted: () -> Unit,
    viewModel: LessonEndViewModel = viewModel()
) {
    val context = LocalContext.current
    val repo = remember { ContextRepository(context.applicationContext) }
    val courseId by repo.courseIdFlow.collectAsState(initial = -1)

    val isCompleted by viewModel.isCompleted.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(lessonId) {
        viewModel.lessonCompleted(lessonId)
    }

    LaunchedEffect(isCompleted) {
        if (isCompleted) {
            onCompleted()
        }
    }

    Box(Modifier
        .fillMaxSize()
        .background(NcBackgroundColor)
    ) {
        NcBackgroundText()

        NcNullTopBar( modifier = Modifier )

        Text(
            text = "Урок пройден",
            color = NcAccentColor,
            style = DefaultAppTextStyles.hero,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
        )

        NextCodeButton(
            text = "git push",
            onClick = {
                navController.navigate(Screen.Home.createRoute(courseId.toLong())) {
                    popUpTo(Screen.Home.route) {
                        inclusive = true
                    }
                }
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 80.dp)
        ) {

        }

        NcNullBottomBar(
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }

}