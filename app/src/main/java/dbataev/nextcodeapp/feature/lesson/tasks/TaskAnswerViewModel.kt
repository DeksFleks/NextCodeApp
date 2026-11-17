package dbataev.nextcodeapp.feature.lesson.tasks
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dbataev.nextcodeapp.core.data.remote.RetrofitClient
import dbataev.nextcodeapp.core.data.remote.dto.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

class TaskAnswerViewModel : ViewModel() {
    var selected by mutableStateOf<Set<Long>>(emptySet())
        private set
    var input by mutableStateOf("")
    var loading by mutableStateOf(false)
        private set
    var feedback by mutableStateOf<String?>(null)
        private set
    var error by mutableStateOf<String?>(null)
        private set
    private var initialized = false
    private var completed = false
    fun initialize(task: TaskDto) {
        if (!initialized) { input = task.starterCode.orEmpty(); initialized = true }
    }
    fun toggle(id: Long, checked: Boolean, multiple: Boolean) {
        if (loading || completed) return
        selected = if (!checked) selected - id else if (multiple) selected + id else setOf(id)
    }
    fun dismiss() { feedback = null }
    fun submit(task: TaskDto, lessonId: Long, onComplete: () -> Unit) {
        if (loading || completed) return
        if (lessonId <= 0) { error = "Не удалось определить урок. Откройте его заново."; return }
        if (task.type == dbataev.nextcodeapp.core.common.TaskType.MULTIPLE_CHOICE && selected.isEmpty()) {
            error = "Выберите ответ"; return
        }
        if (task.type != dbataev.nextcodeapp.core.common.TaskType.MULTIPLE_CHOICE && input.isBlank()) {
            error = "Введите ответ"; return
        }
        loading = true; error = null
        viewModelScope.launch {
            try {
                val result = RetrofitClient.taskApi.check(task.id, TaskAnswerDto(lessonId, selected.toList(), input))
                if (result.correct) { completed = true; onComplete() }
                else feedback = result.explanation?.takeIf { it.isNotBlank() } ?: "Ответ неверный. Попробуйте ещё раз."
            } catch (e: CancellationException) { throw e }
            catch (e: Exception) { error = "Не удалось проверить ответ. Проверьте соединение и повторите." }
            finally { loading = false }
        }
    }
}
