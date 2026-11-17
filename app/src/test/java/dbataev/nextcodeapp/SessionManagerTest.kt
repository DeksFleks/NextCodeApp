package dbataev.nextcodeapp
import dbataev.nextcodeapp.core.data.local.*
import org.junit.Assert.*
import org.junit.Test
class SessionManagerTest {
    private class Store(var storedToken: String? = "old") : SessionTokenStore {
        override fun getToken() = storedToken
        override fun saveToken(token: String) { this.storedToken = token }
        override fun clearToken() { storedToken = null }
    }
    @Test fun expiredSessionIsClearedAndOnlyReportedOnce() {
        val store = Store(); val session = SessionManager(store)
        session.expire("old")
        val event = session.ended.value!!
        assertNull(session.token()); assertTrue(event.expired)
        session.expire("old")
        assertEquals(event,session.ended.value)
        session.acknowledge(event)
        assertNull(session.ended.value)
    }
    @Test fun lateUnauthorizedResponseDoesNotLogOutNewAccount() {
        val session = SessionManager(Store())
        session.saveToken("new")
        session.expire("old")
        assertEquals("new",session.token()); assertNull(session.ended.value)
    }
    @Test fun manualLogoutIsDifferentFromExpiryAndLoginResetsNotice() {
        val session = SessionManager(Store())
        session.logout()
        assertFalse(session.ended.value!!.expired)
        session.saveToken("new")
        assertNull(session.ended.value)
    }
}
