package dbataev.nextcodeapp.feature.lesson.theory

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dbataev.nextcodeapp.core.common.TaskType
import dbataev.nextcodeapp.core.data.remote.dto.TaskDto
import dbataev.nextcodeapp.core.designsystem.component.NcTopProgressPanel
import dbataev.nextcodeapp.core.designsystem.component.NextCodeButton
import dbataev.nextcodeapp.core.designsystem.component.NextCodeMessageCard
import dbataev.nextcodeapp.core.designsystem.theme.NcSecondColor

@Composable
fun TheoryScreen(
    theoryText: String?,
    id: Long,
    modifier: Modifier = Modifier,
    viewModel: TheoryViewModel = viewModel(),
    onStartTest: (TaskDto) -> Unit
) {
    val buttonTexts = listOf("Принято", "Запомню", "Окей", "Продолжить")
    val scrollState = rememberScrollState()

    LaunchedEffect(id) {
        viewModel.loadTasks(id)
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Box {
            NcTopProgressPanel()
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .padding(bottom = 16.dp)
        ) {
            NextCodeMessageCard(
                text = theoryText ?: "Здесь будет текст теории"
            )
        }

        Box(Modifier
            .fillMaxWidth()
            .background(NcSecondColor)
            .align(Alignment.CenterHorizontally)) {
            NextCodeButton(
                text = buttonTexts.random(),
                onClick = {
                    val firstTask = viewModel.tasks.firstOrNull { task ->
                        task.type == TaskType.TEST
                    }

                    if (firstTask != null) {
                        onStartTest(firstTask)
                    }
                },
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(bottom = 12.dp, top = 12.dp)
                    .fillMaxWidth(0.9f),
            ) {}
        }
    }
}