package dbataev.nextcodeapp.feature.module

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dbataev.nextcodeapp.core.data.remote.RetrofitClient
import dbataev.nextcodeapp.core.data.remote.dto.LessonDto
import dbataev.nextcodeapp.core.data.remote.dto.ModuleDto
import kotlinx.coroutines.launch

class ModuleViewModel : ViewModel() {
    var selectedLesson: LessonDto? by mutableStateOf(null)

    var modules by mutableStateOf<List<ModuleDto>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun loadModules(courseId: Long) {
        viewModelScope.launch {
            try {
                isLoading = true
                errorMessage = null

                modules = RetrofitClient.moduleApi.getModulesByCourseId(courseId = courseId)

            } catch (e: Exception) {
                errorMessage = e.message
            } finally {
                isLoading = false
            }
        }
    }
}