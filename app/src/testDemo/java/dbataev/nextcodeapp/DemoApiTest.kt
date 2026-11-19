package dbataev.nextcodeapp

import dbataev.nextcodeapp.core.common.LessonState
import dbataev.nextcodeapp.core.common.TaskType
import dbataev.nextcodeapp.core.data.local.*
import dbataev.nextcodeapp.core.data.local.demo.DemoApi
import dbataev.nextcodeapp.core.data.local.demo.DemoContent
import dbataev.nextcodeapp.core.data.remote.dto.*
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import java.util.Calendar
import org.junit.*
import org.junit.Assert.*
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import retrofit2.HttpException

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
class DemoApiTest {
    private class Tokens : SessionTokenStore {
        var value: String? = null
        override fun getToken() = value
        override fun saveToken(token: String) { value = token }
        override fun clearToken() { value = null }
    }
    private val tokens = Tokens()
    private val session = SessionManager(tokens)
    private lateinit var api: DemoApi
    @Before fun setup() {
        RuntimeEnvironment.getApplication().deleteDatabase("nextcode_demo.db")
        api = DemoApi(RuntimeEnvironment.getApplication(), session)
    }
    @After fun close() { api.close() }
    private suspend fun register(name: String = "judge") {
        session.saveToken(api.register(CreateUserDto(name, "Demo2026", "Судья")).jwt)
        api.setCurrentCourse(1)
    }
    private suspend fun solve(lesson: DemoContent.Lesson) {
        lesson.exercises.forEach { exercise ->
            val answer = if (exercise.task.type == TaskType.MULTIPLE_CHOICE)
                TaskAnswerDto(lesson.id, exercise.answer.split(',').map(String::toLong))
            else TaskAnswerDto(lesson.id, answer = exercise.answer)
            assertTrue(api.check(exercise.task.id, answer).correct)
        }
    }
    @Test fun entireCourseIsPlayableAndPersistsAfterRestart() = runBlocking {
        register()
        var awards = 0
        DemoContent.lessons.forEach { lesson ->
            assertEquals(LessonState.CURRENT, api.getModulesByCourseId(1).flatMap { it.lessons }.first { it.id == lesson.id }.state)
            assertEquals(3, api.getTasksByLesson(lesson.id).size)
            assertEquals(409, api.lessonCompleted(lesson.id).code())
            solve(lesson)
            awards += api.lessonCompleted(lesson.id).body()!!.size
            assertTrue(api.lessonCompleted(lesson.id).body()!!.isEmpty())
        }
        val expected = api.getCurrentUser()
        assertEquals(6L, expected.completedLessons)
        assertEquals(250L, expected.totalXp)
        assertEquals(3, expected.level)
        assertEquals(0, expected.xp)
        assertEquals(1, expected.streak)
        assertEquals(5, awards)
        assertTrue(api.getModulesByCourseId(1).flatMap { it.lessons }.all { it.state == LessonState.COMPLETED })
        assertTrue(api.getRandomQuote().text.isNotBlank())
        assertEquals(6, api.getAllAchievements().size)
        api.close()
        api = DemoApi(RuntimeEnvironment.getApplication(), session)
        assertEquals(expected, api.getCurrentUser())
        session.logout()
        session.saveToken(api.login(LoginUserDto("JUDGE", "Demo2026")).jwt)
        assertEquals(expected, api.getCurrentUser())
    }
    @Test fun accountsAreIsolatedAndProfileEditsPersist() = runBlocking {
        register()
        solve(DemoContent.lessons.first())
        api.lessonCompleted(1)
        api.updateUser(UpdateUserDto(username = "renamed", nickname = "Эксперт", password = "NewPass2026"))
        session.logout()
        register("second")
        assertEquals(0L, api.getCurrentUser().totalXp)
        assertEquals(LessonState.CURRENT, api.getModulesByCourseId(1).first().lessons.first().state)
        session.logout()
        session.saveToken(api.login(LoginUserDto("renamed", "NewPass2026")).jwt)
        assertEquals("Эксперт", api.getCurrentUser().nickname)
        assertEquals(25L, api.getCurrentUser().totalXp)
    }
    @Test fun rejectsWrongAnswersLockedLessonsAndDuplicateNames() = runBlocking {
        register()
        assertFalse(api.check(1, TaskAnswerDto(1, listOf(11))).correct)
        assertFalse(api.check(1, TaskAnswerDto(1, listOf(10,10))).correct)
        assertFalse(api.check(2, TaskAnswerDto(1, answer = "print")).correct)
        assertEquals(409, api.lessonCompleted(1).code())
        try { api.getTasksByLesson(2); fail("Locked lesson accessible") } catch (e: HttpException) { assertEquals(403, e.code()) }
        try { api.check(4, TaskAnswerDto(1, listOf(40,41))); fail("Foreign task accepted") } catch (e: HttpException) { assertEquals(404, e.code()) }
        try { api.register(CreateUserDto("JUDGE", "Demo2026", "Other")); fail("Duplicate registered") } catch (e: HttpException) { assertEquals(409, e.code()) }
        session.logout()
        try { api.login(LoginUserDto("judge", "wrong")); fail("Wrong password accepted") } catch (e: HttpException) { assertEquals(401, e.code()) }
    }
    @Test fun concurrentCompletionAwardsOnlyOnce() = runBlocking {
        register()
        solve(DemoContent.lessons.first())
        val results = List(5) { async { api.lessonCompleted(1) } }.awaitAll()
        assertEquals(1, results.sumOf { it.body()!!.size })
        assertEquals(25L, api.getCurrentUser().totalXp)
        assertEquals(1L, api.getCurrentUser().completedLessons)
    }
    @Test fun streakTracksConsecutiveDaysAndKeepsBestAfterGap() = runBlocking {
        register()
        suspend fun complete(index: Int) { solve(DemoContent.lessons[index]); api.lessonCompleted(index + 1L) }
        fun lastDay(offset: Int) {
            val date = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, offset) }
            val value = "${date.get(Calendar.YEAR)}-${date.get(Calendar.MONTH) + 1}-${date.get(Calendar.DAY_OF_MONTH)}"
            RuntimeEnvironment.getApplication().openOrCreateDatabase("nextcode_demo.db", 0, null).use {
                it.execSQL("UPDATE users SET last_day=?", arrayOf(value))
            }
        }
        complete(0)
        lastDay(-1); complete(1)
        assertEquals(2, api.getCurrentUser().streak)
        lastDay(-1); complete(2)
        assertEquals(3, api.getCurrentUser().streak)
        lastDay(-3); complete(3)
        assertEquals(1, api.getCurrentUser().streak)
        assertEquals(3L, api.getCurrentUser().bestStreak)
    }
    @Test fun invalidSessionIsCleared() = runBlocking {
        session.saveToken("missing-token")
        try { api.getCurrentUser(); fail("Invalid session accepted") } catch (e: HttpException) { assertEquals(401, e.code()) }
        assertNull(session.token())
        assertTrue(session.ended.value!!.expired)
    }
}
