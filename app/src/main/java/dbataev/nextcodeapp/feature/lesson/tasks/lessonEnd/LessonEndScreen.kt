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
import dbataev.nextcodeapp.core.common.viewModel.UserViewModel
import dbataev.nextcodeapp.core.datastore.ContextRepository
import dbataev.nextcodeapp.core.designsystem.component.NcBackgroundText
import dbataev.nextcodeapp.core.designsystem.component.NcNullBottomBar
import dbataev.nextcodeapp.core.designsystem.component.NcNullTopBar
import dbataev.nextcodeapp.core.designsystem.component.NextCodeButton
import dbataev.nextcodeapp.core.designsystem.theme.DefaultAppTextStyles
import dbataev.nextcodeapp.core.designsystem.theme.NcAccentColor
import dbataev.nextcodeapp.core.designsystem.theme.NcBackgroundColor
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import dbataev.nextcodeapp.core.designsystem.component.NextCodeNewAchievementCard

@Composable
fun LessonEndScreen(
    navController: NavController,
    lessonId: Long,
    onCompleted: () -> Unit,
    viewModel: LessonEndViewModel = viewModel(),
    userViewModel: UserViewModel,
) {
    val context = LocalContext.current
    val repo = remember { ContextRepository(context.applicationContext) }
    val courseId by repo.courseIdFlow.collectAsState(initial = -1)

    val isCompleted by viewModel.isCompleted.collectAsState()
    val error by viewModel.error.collectAsState()
    val newAchievements by viewModel.newAchievements.collectAsState()


    LaunchedEffect(Unit) {
        viewModel.lessonCompleted(lessonId)
    }

    LaunchedEffect(isCompleted) {
        if (isCompleted) {
            userViewModel.loadUser()
            onCompleted()
        }
    }

    Box(Modifier
        .fillMaxSize()
        .background(NcBackgroundColor)
    ) {
        NcBackgroundText()

        val achievementsScrollState = rememberScrollState()

        if (newAchievements.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .heightIn(max = 280.dp)
                    .padding(
                        start = 16.dp,
                        top = 10.dp,
                        end = 16.dp
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(achievementsScrollState)
                        .padding(vertical = 28.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    newAchievements.forEach { achievement ->
                        NextCodeNewAchievementCard(
                            achievement = achievement,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                if (achievementsScrollState.canScrollBackward) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .fillMaxWidth()
                            .height(42.dp)
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        NcBackgroundColor,
                                        NcBackgroundColor.copy(alpha = 0.75f),
                                        Color.Transparent
                                    )
                                )
                            )
                    )
                }

                if (achievementsScrollState.canScrollForward) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .height(42.dp)
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        NcBackgroundColor.copy(alpha = 0.75f),
                                        NcBackgroundColor
                                    )
                                )
                            )
                    )
                }
            }
        }

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
                    popUpTo("lesson_end") {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 80.dp)
        ) {}

        NcNullBottomBar(
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }

}