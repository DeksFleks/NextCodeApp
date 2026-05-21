package dbataev.nextcodeapp.feature.lesson.tasks.write

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dbataev.nextcodeapp.core.data.remote.dto.TaskDto
import dbataev.nextcodeapp.core.designsystem.component.NcNullTopBar
import dbataev.nextcodeapp.core.designsystem.component.NextCodeButton
import dbataev.nextcodeapp.core.designsystem.component.NextCodeMessageCard
import dbataev.nextcodeapp.core.designsystem.component.NextCodeTextFieldCode
import dbataev.nextcodeapp.feature.lesson.tasks.ExplanationBottomSheet

@Composable
fun WriteScreen(
    task: TaskDto,
    modifier: Modifier = Modifier,
    viewModel: WriteViewModel = viewModel(),
    onTaskComplete: () -> Unit
) {
    val buttonTexts = listOf("Закомитить", "Ответил", "Продолжить", "Готово")
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Box {
            NcNullTopBar(modifier)
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .padding(bottom = 16.dp)
        ) {
            NextCodeMessageCard(
                text = task.question
            )

            NextCodeTextFieldCode(
                value = viewModel.userInput,
                onValueChange = viewModel::updateInput,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 20.dp, vertical = 10.dp),
                placeholder = "Введите ответ...",
                singleLine = false
            )

            NextCodeButton(
                text = buttonTexts.random(),
                onClick = {
                    val isCorrect = viewModel.checkAnswer(task.correctAnswer)

                    if (isCorrect) {
                        viewModel.clearInput()
                        onTaskComplete()
                    } else {
                        viewModel.showExplanation()
                    }
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 12.dp, top = 12.dp)
                    .fillMaxWidth(0.9f),
            ) { }
        }
    }

    if (viewModel.showExplanation) {
        ExplanationBottomSheet(
            explanation = task.explanation,
            onDismiss = {
                viewModel.hideExplanation()
            },
            onRetryClick = {
                viewModel.hideExplanation()
            }
        )
    }
}