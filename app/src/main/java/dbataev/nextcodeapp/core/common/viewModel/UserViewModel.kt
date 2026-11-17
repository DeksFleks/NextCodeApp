package dbataev.nextcodeapp.core.common.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dbataev.nextcodeapp.core.data.remote.RetrofitClient
import dbataev.nextcodeapp.core.data.remote.dto.UserDto
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserViewModel(
    private val fetchUser: suspend () -> UserDto = { RetrofitClient.userApi.getCurrentUser() },
    private val sessionProvider: () -> dbataev.nextcodeapp.core.data.local.SessionManager = { RetrofitClient.session }
) : ViewModel() {
    private val _user = MutableStateFlow<UserDto?>(null)
    val user = _user.asStateFlow()
    private var loadJob: Job? = null

    suspend fun refreshUser(): UserDto {
        val token = sessionProvider().token() ?: throw CancellationException("Signed out")
        val user = fetchUser()
        if (sessionProvider().token() != token) throw CancellationException("Session changed")
        _user.value = user
        return user
    }
    fun loadUser() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            try { refreshUser() }
            catch (e: CancellationException) { throw e }
            catch (e: Exception) { Log.e("UserViewModel", "loadUser failed", e) }
        }
    }
    fun selectCourse(courseId: Long) {
        require(courseId > 0)
        loadJob?.cancel()
        _user.value = _user.value?.copy(currentCourseId = courseId)
        if (_user.value == null) loadUser()
    }
    fun clearUser() {
        loadJob?.cancel()
        _user.value = null
    }
}
