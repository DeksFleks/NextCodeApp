package dbataev.nextcodeapp.feature.lesson.tasks.write
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dbataev.nextcodeapp.core.common.TaskType
import dbataev.nextcodeapp.core.data.remote.dto.TaskDto
import dbataev.nextcodeapp.core.designsystem.component.*
import dbataev.nextcodeapp.feature.lesson.tasks.*

@Composable
fun WriteScreen(task: TaskDto, lessonId: Long, modifier: Modifier = Modifier,
    viewModel: TaskAnswerViewModel = viewModel(key = "task-${task.id}"), onTaskComplete: () -> Unit) {
    LaunchedEffect(task.id) { viewModel.initialize(task) }
    Column(modifier.fillMaxSize()) {
        NcNullTopBar(modifier)
        Column(Modifier.weight(1f).fillMaxWidth().verticalScroll(rememberScrollState())) {
            NextCodeMessageCard(text = task.question)
            if (task.type == TaskType.CODING) {
                Text("Язык: ${task.programmingLanguage.orEmpty()}. Проверка по эталону: код должен совпадать, включая регистр и пробелы внутри. Код не запускается.", Modifier.padding(16.dp))
            } else {
                Text(task.sentenceBefore.orEmpty(), Modifier.padding(horizontal = 20.dp))
            }
            NextCodeTextFieldCode(value = viewModel.input, onValueChange = { if (!viewModel.loading) viewModel.input = it },
                modifier = Modifier.fillMaxWidth().heightIn(min = 160.dp).padding(20.dp),
                placeholder = if (task.type == TaskType.FILL_BLANK) "Заполните пропуск…" else "Введите код…",
                singleLine = false)
            if (task.type == TaskType.FILL_BLANK) Text(task.sentenceAfter.orEmpty(), Modifier.padding(horizontal = 20.dp))
            viewModel.error?.let { Text(it, Modifier.padding(16.dp)) }
        }
        NextCodeButton(text = if (viewModel.loading) "Проверяем…" else "Проверить",
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            onClick = { viewModel.submit(task, lessonId, onTaskComplete) }) { }
    }
    viewModel.feedback?.let { message ->
        ExplanationBottomSheet(explanation = message, onDismiss = viewModel::dismiss, onRetryClick = viewModel::dismiss)
    }
}
