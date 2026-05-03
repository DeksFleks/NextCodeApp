package dbataev.nextcodeapp.feature.lesson.tasks.test

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class TestViewModel : ViewModel() {
    var selectedAnswers by mutableStateOf<List<String>>(emptyList())
        private set

    fun toggleAnswer(answer: String, checked: Boolean) {
        selectedAnswers =
            if (checked) selectedAnswers + answer
            else selectedAnswers - answer
    }
}