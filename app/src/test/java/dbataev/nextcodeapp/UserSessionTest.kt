package dbataev.nextcodeapp
import dbataev.nextcodeapp.app.navigation.learningRoute
import dbataev.nextcodeapp.core.common.viewModel.UserViewModel
import dbataev.nextcodeapp.core.data.local.*
import dbataev.nextcodeapp.core.data.remote.dto.UserDto
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
@OptIn(ExperimentalCoroutinesApi::class)
class UserSessionTest {
    @get:Rule val main = MainDispatcherRule()
    private class Store(var stored: String? = "token") : SessionTokenStore {
        override fun getToken() = stored
        override fun saveToken(token: String) { stored = token }
        override fun clearToken() { stored = null }
    }
    private fun user(course: Long?) = UserDto(id=2, username="tester", nickname="dev", xp=0,
        level=1, streak=0, xpForNextLevel=100, currentCourseId=course, totalXp=0, bestStreak=0, completedLessons=0)
    @Test fun startupRestoresCourseFromServerAndHandlesNoCourse() = runTest {
        val session = SessionManager(Store())
        val first = UserViewModel({ user(42) }, { session })
        assertEquals("home/42",learningRoute(first.refreshUser().currentCourseId))
        val second = UserViewModel({ user(null) }, { session })
        assertEquals("course",learningRoute(second.refreshUser().currentCourseId))
    }
    @Test fun oldProfileResponseCannotRepopulateAfterLogoutAndNewLogin() = runTest {
        val session = SessionManager(Store())
        val pending = CompletableDeferred<UserDto>()
        val vm = UserViewModel({ pending.await() }, { session })
        val request = async { vm.refreshUser() }
        runCurrent()
        session.logout(); vm.clearUser(); session.saveToken("new-account")
        pending.complete(user(42)); advanceUntilIdle()
        assertTrue(request.isCancelled); assertNull(vm.user.value)
    }
    @Test fun pendingProfileRefreshCannotOverwriteNewCourseSelection() = runTest {
        val session = SessionManager(Store())
        val pending = CompletableDeferred<UserDto>()
        var calls = 0
        val vm = UserViewModel({ if (++calls == 1) user(42) else pending.await() }, { session })
        vm.refreshUser()
        vm.loadUser(); runCurrent()
        vm.selectCourse(77)
        pending.complete(user(42)); advanceUntilIdle()
        assertEquals("home/77",learningRoute(vm.user.value!!.currentCourseId))
    }
}
