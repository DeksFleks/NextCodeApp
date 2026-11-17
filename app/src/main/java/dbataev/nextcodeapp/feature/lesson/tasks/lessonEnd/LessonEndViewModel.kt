package dbataev.nextcodeapp.feature.lesson.tasks.lessonEnd

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dbataev.nextcodeapp.core.data.remote.RetrofitClient
import dbataev.nextcodeapp.core.data.remote.dto.AchievementDto
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

class LessonEndViewModel(
    private val completeLesson: suspend (Long) -> Response<List<AchievementDto>> = {
        RetrofitClient.lessonApi.lessonCompleted(it)
    }
) : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()
    private val _isCompleted = MutableStateFlow(false)
    val isCompleted = _isCompleted.asStateFlow()
    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()
    private val _newAchievements = MutableStateFlow<List<AchievementDto>>(emptyList())
    val newAchievements = _newAchievements.asStateFlow()

    fun lessonCompleted(lessonId: Long) {
        if (_isLoading.value || _isCompleted.value) return
        if (lessonId <= 0L) { _error.value = "Некорректный ID урока. Откройте урок заново."; return }
        _isLoading.value = true
        _error.value = null
        viewModelScope.launch {
            try {
                val response = completeLesson(lessonId)
                if (response.isSuccessful) {
                    _newAchievements.value = response.body().orEmpty()
                    _isCompleted.value = true
                } else {
                    _error.value = when (response.code()) {
                        401, 403 -> "Не удалось подтвердить сессию."
                        409 -> "Не все задания подтверждены сервером. Вернитесь к заданиям."
                        else -> "Не удалось сохранить результат. Повторите попытку."
                    }
                }
            } catch (e: CancellationException) { throw e }
            catch (e: Exception) { _error.value = "Не удалось сохранить результат. Проверьте соединение и повторите." }
            finally { _isLoading.value = false }
        }
    }
}
