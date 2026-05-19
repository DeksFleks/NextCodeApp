package dbataev.nextcodeapp.feature.lesson.tasks.write

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class WriteViewModel : ViewModel() {

    var userInput by mutableStateOf("")
        private set

    var showExplanation by mutableStateOf(false)
        private set

    fun updateInput(value: String) {
        userInput = value
    }

    fun clearInput() {
        userInput = ""
    }

    fun showExplanation() {
        showExplanation = true
    }

    fun hideExplanation() {
        showExplanation = false
    }

    fun checkAnswer(correctAnswer: String?): Boolean {
        return userInput.trim().equals(
            correctAnswer.orEmpty().trim(),
            ignoreCase = true
        )
    }
}

