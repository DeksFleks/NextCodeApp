package dbataev.nextcodeapp.feature.lesson.theory

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dbataev.nextcodeapp.core.data.remote.RetrofitClient
import dbataev.nextcodeapp.core.data.remote.dto.TaskDto
import kotlinx.coroutines.launch


class TheoryViewModel : ViewModel() {
    var tasks by mutableStateOf<List<TaskDto>>(emptyList())
        private set

    var error by mutableStateOf<String?>(null)
        private set
    var loading by mutableStateOf(false)
        private set
    fun loadTasks(lessonId: Long){
        tasks = emptyList(); error = null; loading = true
        Log.e("TaskViewModel", "loadTasks CALLED, lessonId = $lessonId")

        viewModelScope.launch {
            try {
                Log.e("TaskViewModel", "Retrofit request START")
                val result = RetrofitClient.taskApi.getTasksByLesson(lessonId)
                Log.e("TaskViewModel", "Retrofit request SUCCESS, size = ${result.size}")

                tasks = result
                if (result.isEmpty()) error = "В уроке пока нет заданий"
            } catch (e: kotlinx.coroutines.CancellationException) { throw e
            } catch (e: Exception) {
                error = "Не удалось загрузить задания. Повторите попытку."
                Log.e("TaskViewModel", "loadTasks failed", e)
            } finally { loading = false }
        }
    }
}