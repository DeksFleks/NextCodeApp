package dbataev.nextcodeapp.core.data.local.viewModel

import androidx.lifecycle.ViewModel
import dbataev.nextcodeapp.core.data.remote.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel()