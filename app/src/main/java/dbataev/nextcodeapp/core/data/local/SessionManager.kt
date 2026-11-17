package dbataev.nextcodeapp.core.data.local

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

interface SessionTokenStore {
    fun getToken(): String?
    fun saveToken(token: String)
    fun clearToken()
}

data class SessionEnd(val id: Long, val expired: Boolean)

/** Token changes and invalidation share one lock: a late response cannot clear a new login. */
class SessionManager(private val storage: SessionTokenStore) {
    private val _ended = MutableStateFlow<SessionEnd?>(null)
    val ended = _ended.asStateFlow()
    private var eventId = 0L

    @Synchronized fun token(): String? = storage.getToken()
    @Synchronized fun saveToken(token: String) {
        storage.saveToken(token)
        _ended.value = null
    }
    @Synchronized fun expire(expectedToken: String) {
        if (storage.getToken() != expectedToken) return
        storage.clearToken()
        _ended.value = SessionEnd(++eventId, expired = true)
    }
    @Synchronized fun logout() {
        storage.clearToken()
        _ended.value = SessionEnd(++eventId, expired = false)
    }
    @Synchronized fun acknowledge(event: SessionEnd) {
        if (_ended.value == event) _ended.value = null
    }
}
