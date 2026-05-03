package dbataev.nextcodeapp.feature.lesson.tasks.test

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dbataev.nextcodeapp.core.data.remote.dto.TaskDto
import dbataev.nextcodeapp.core.designsystem.component.NcNullTopBar
import dbataev.nextcodeapp.core.designsystem.component.NcTopProgressPanel
import dbataev.nextcodeapp.core.designsystem.component.NextCodeButton
import dbataev.nextcodeapp.core.designsystem.component.NextCodeMessageCard
import dbataev.nextcodeapp.core.designsystem.component.NextCodeTestButton
import dbataev.nextcodeapp.core.designsystem.theme.NcSecondColor

@Composable
fun TestScreen(
    task: TaskDto,
    modifier: Modifier = Modifier,
    viewModel: TestViewModel = viewModel()
    ){
    val buttonTexts = listOf("Закомитить", "Сделаль", "Ответил", "Продолжить", "Готово")
    val scrollState = rememberScrollState()
    val options = task.options ?: listOf("Кажется", "Что-то", "Сломалось", ":(")
    var selectedAnswers = viewModel.selectedAnswers

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

            options.forEach { option ->
                NextCodeTestButton(
                    text = option,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                    checked = selectedAnswers.contains(option),
                    onCheckedChange = { checked ->
                        viewModel.toggleAnswer(option, checked)
                    }
                ) { }
            }

        }

        Box(Modifier
            .fillMaxWidth()
            .background(NcSecondColor)
            .align(Alignment.CenterHorizontally)) {
            NextCodeButton(
                text = buttonTexts.random(),
                onClick = {

                },
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(bottom = 12.dp, top = 12.dp)
                    .fillMaxWidth(0.9f),
            ) { }
        }
    }

}