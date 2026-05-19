package dbataev.nextcodeapp.feature.lesson.tasks.test

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class TestViewModel : ViewModel() {

    var selectedAnswers by mutableStateOf<List<String>>(emptyList())
        private set

    var showExplanation by mutableStateOf(false)
        private set

    fun toggleAnswer(answer: String, checked: Boolean) {
        selectedAnswers =
            if (checked) {
                listOf(answer)
            } else {
                emptyList()
            }
    }

    fun checkAnswer(correctAnswer: String): Boolean {
        return selectedAnswers.size == 1 && selectedAnswers.first() == correctAnswer
    }

    fun showExplanation() {
        showExplanation = true
    }

    fun hideExplanation() {
        showExplanation = false
        selectedAnswers = emptyList()
    }
}