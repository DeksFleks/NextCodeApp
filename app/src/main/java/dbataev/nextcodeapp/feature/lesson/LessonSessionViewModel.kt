package dbataev.nextcodeapp.feature.lesson

import androidx.lifecycle.ViewModel

class LessonSessionViewModel : ViewModel() {

    var lessonId: Long = 0L
        private set

    fun setLessonId(id: Long) {
        lessonId = id
    }

    fun clear() {
        lessonId = 0L
    }
}