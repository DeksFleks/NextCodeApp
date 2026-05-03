package dbataev.nextcodeapp.feature.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dbataev.nextcodeapp.core.data.remote.dto.LessonDto

class HomeViewModel : ViewModel() {
    var selectedLesson: LessonDto? by mutableStateOf(null)
}
