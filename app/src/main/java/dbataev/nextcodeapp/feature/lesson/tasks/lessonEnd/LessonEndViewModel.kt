package dbataev.nextcodeapp.feature.lesson.tasks.lessonEnd

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dbataev.nextcodeapp.core.data.remote.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LessonEndViewModel : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _isCompleted = MutableStateFlow(false)
    val isCompleted = _isCompleted.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    fun lessonCompleted(lessonId: Long) {
        Log.d("LESSON_END_VM", "called, lessonId=$lessonId")

        if (lessonId == 0L) {
            _error.value = "Некорректный id урока"
            Log.e("LESSON_END_VM", "lessonId == 0")
            return
        }

        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null

                Log.d("LESSON_END_VM", "sending request")

                val response = RetrofitClient.lessonApi.lessonCompleted(lessonId)

                Log.d(
                    "LESSON_END_VM",
                    "response code=${response.code()}, success=${response.isSuccessful}"
                )

                if (response.isSuccessful) {
                    _isCompleted.value = true
                } else {
                    _error.value = "Ошибка завершения урока: ${response.code()}"
                }

            } catch (e: Exception) {
                Log.e("LESSON_END_VM", "request failed", e)
                _error.value = e.message ?: "Ошибка соединения"
            } finally {
                _isLoading.value = false
            }
        }
    }
}