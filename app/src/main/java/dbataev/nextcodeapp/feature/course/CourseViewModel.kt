package dbataev.nextcodeapp.feature.course

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dbataev.nextcodeapp.core.data.remote.RetrofitClient
import dbataev.nextcodeapp.core.data.remote.dto.CourseDto
import kotlinx.coroutines.launch

class CourseViewModel : ViewModel() {
    var courses by mutableStateOf<List<CourseDto>>(emptyList())
        private set

    fun loadCourses() {
        viewModelScope.launch {
            try {
                Log.d("CourseViewModel", "loadCourses started")

                val result = RetrofitClient.courseApi.getAllCourses()

                Log.d("CourseViewModel", "loadCourses success: $result")

                courses = result
            } catch (e: Exception) {
                Log.e("CourseViewModel", "loadCourses failed", e)
            }
        }
    }
}