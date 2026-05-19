package dbataev.nextcodeapp.core.common.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dbataev.nextcodeapp.core.data.remote.RetrofitClient
import dbataev.nextcodeapp.core.data.remote.dto.UserDto
import dbataev.nextcodeapp.core.data.remote.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {

    private val _user = MutableStateFlow<UserDto?>(null)
    val user = _user

    init {
        loadUser()
    }

    fun loadUser() {
        viewModelScope.launch {
            try {
                _user.value = RetrofitClient.userApi.getCurrentUser()
            } catch (e: Exception) {
                Log.e("UserViewModel", "loadUser failed", e)
            }
        }
    }
}