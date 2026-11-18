package dbataev.nextcodeapp.feature.achievements

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dbataev.nextcodeapp.core.data.remote.RetrofitClient
import dbataev.nextcodeapp.core.data.remote.dto.AchievementDto
import kotlinx.coroutines.launch

class AchievementViewModel(
    private val fetchAchievements: suspend () -> List<AchievementDto> = { RetrofitClient.achievementApi.getAllAchievements() }
) : ViewModel() {
    var achievements by mutableStateOf<List<AchievementDto>>(emptyList())
        private set

    fun loadAchievement() {
        viewModelScope.launch {
            try {
                Log.d("AchievementViewModel", "loadAchievement started")

                val result = fetchAchievements()

                Log.d("AchievementViewModel", "loadAchievement success: $result")

                achievements = result
            } catch (e: kotlinx.coroutines.CancellationException) { throw e
            } catch (e: Exception) {
                Log.e("AchievementViewModel", "loadAchievement failed", e)
            }
        }
    }
}