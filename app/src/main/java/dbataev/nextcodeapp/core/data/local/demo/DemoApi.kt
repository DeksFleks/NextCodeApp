package dbataev.nextcodeapp.core.data.local.demo

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Base64
import com.google.gson.Gson
import dbataev.nextcodeapp.core.common.*
import dbataev.nextcodeapp.core.data.local.SessionManager
import dbataev.nextcodeapp.core.data.remote.api.*
import dbataev.nextcodeapp.core.data.remote.dto.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.HttpException
import retrofit2.Response
import java.security.MessageDigest
import java.security.SecureRandom
import java.util.Calendar
import java.util.UUID
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import kotlin.math.pow
import kotlin.math.roundToInt

/** Local implementation of the application's API contracts. All writes are atomic and off the UI thread. */
class DemoApi(context: Context, private val session: SessionManager) : AuthApi, UserApi, CourseApi,
    ModuleApi, TaskApi, LessonApi, QuoteApi, AchievementApi {
    private val gson = Gson()
    private val helper = DemoDatabase(context.applicationContext)
    private val mutex = Mutex()
    private suspend fun <T> transaction(block: (SQLiteDatabase) -> T): T = withContext(Dispatchers.IO) {
        mutex.withLock {
            val db = helper.writableDatabase
            db.beginTransaction()
            try { block(db).also { db.setTransactionSuccessful() } } finally { db.endTransaction() }
        }
    }
    private fun fail(code: Int, message: String): Nothing = throw HttpException(Response.error<Any>(code,
        gson.toJson(mapOf("message" to message)).toResponseBody("application/json".toMediaType())))
    private fun user(db: SQLiteDatabase): UserDto {
        val token = session.token() ?: fail(401, "Войди в локальный аккаунт")
        db.rawQuery("SELECT u.profile FROM users u JOIN sessions s ON s.user_id=u.id WHERE s.token=?", arrayOf(token)).use {
            if (it.moveToFirst()) return gson.fromJson(it.getString(0), UserDto::class.java)
        }
        session.expire(token)
        fail(401, "Локальная сессия завершена")
    }
    private fun save(db: SQLiteDatabase, user: UserDto) {
        db.update("users", ContentValues().apply { put("username", user.username); put("profile", gson.toJson(user)) }, "id=?", arrayOf(user.id.toString()))
    }
    private fun issueToken(db: SQLiteDatabase, id: Long): JwtDto {
        db.delete("sessions", "user_id=?", arrayOf(id.toString()))
        val token = UUID.randomUUID().toString()
        db.execSQL("INSERT INTO sessions(token,user_id) VALUES(?,?)", arrayOf(token, id))
        return JwtDto(token)
    }
    private fun salt() = ByteArray(16).also { SecureRandom().nextBytes(it) }.let { Base64.encodeToString(it, Base64.NO_WRAP) }
    private fun hash(password: String, salt: String): String {
        val spec = PBEKeySpec(password.toCharArray(), Base64.decode(salt, Base64.NO_WRAP), 120_000, 256)
        return try { Base64.encodeToString(SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1").generateSecret(spec).encoded, Base64.NO_WRAP) }
        finally { spec.clearPassword() }
    }
    override suspend fun register(dto: CreateUserDto): JwtDto = transaction { db ->
        val name = dto.username.trim()
        if (name.length !in 4..20 || dto.password.length !in 8..64) fail(400, "Заполни имя, никнейм и пароль от 8 символов")
        if (exists(db, "SELECT 1 FROM users WHERE username=? COLLATE NOCASE", name)) fail(409, "Имя уже занято на этом устройстве")
        val salt = salt()
        val id = db.insertOrThrow("users", null, ContentValues().apply {
            put("username", name); put("salt", salt); put("password_hash", hash(dto.password, salt)); put("profile", "{}")
        })
        save(db, UserDto(1, id, name, dto.nickname.trim().ifBlank { name }, 0, 1, 0, 100, null, 0, 0, 0))
        issueToken(db, id)
    }
    override suspend fun login(dto: LoginUserDto): JwtDto = transaction { db ->
        db.rawQuery("SELECT id,salt,password_hash FROM users WHERE username=? COLLATE NOCASE", arrayOf(dto.username.trim())).use {
            if (!it.moveToFirst() || !MessageDigest.isEqual(hash(dto.password, it.getString(1)).toByteArray(), it.getString(2).toByteArray())) fail(401, "Неверное имя или пароль")
            issueToken(db, it.getLong(0))
        }
    }
    override suspend fun getCurrentUser() = transaction { user(it) }
    override suspend fun getLanguages() = listOf(LanguageDto(1, "ru", "Русский", true))
    override suspend fun setLanguage(languageId: Long) { transaction { db ->
        val u = user(db); if (languageId != 1L) fail(400, "В демо доступен русский язык"); save(db, u.copy(languageId = languageId))
    } }
    override suspend fun setCurrentCourse(courseId: Long) { transaction { db ->
        val u = user(db); if (courseId != 1L) fail(404, "Курс недоступен"); save(db, u.copy(currentCourseId = courseId))
    } }
    override suspend fun updateUser(userDto: UpdateUserDto) { transaction { db ->
        val u = user(db)
        val name = userDto.username?.trim() ?: u.username
        val nick = userDto.nickname?.trim() ?: u.nickname
        if (name.isBlank() || nick.isBlank()) fail(400, "Имя не может быть пустым")
        if (name != u.username && exists(db, "SELECT 1 FROM users WHERE username=? COLLATE NOCASE AND id<>?", name, u.id.toString())) fail(409, "Имя уже занято")
        userDto.password?.let { password ->
            if (password.length < 8) fail(400, "Пароль должен содержать от 8 символов")
            val salt = salt()
            db.execSQL("UPDATE users SET salt=?,password_hash=? WHERE id=?", arrayOf(salt, hash(password, salt), u.id))
        }
        save(db, u.copy(username = name, nickname = nick))
    } }
    override suspend fun getAllCourses(): List<CourseDto> = transaction { db ->
        user(db); listOf(CourseDto(1, "Java", true), CourseDto(2, "Python", false), CourseDto(3, "C#", false), CourseDto(4, "Go", false))
    }
    private fun catalog(db: SQLiteDatabase): List<DemoContent.Lesson> = db.rawQuery("SELECT content FROM lessons ORDER BY id", null).use {
        buildList { while (it.moveToNext()) add(gson.fromJson(it.getString(0), DemoContent.Lesson::class.java)) }
    }
    private fun completed(db: SQLiteDatabase, id: Long): Set<Long> = db.rawQuery("SELECT lesson_id FROM progress WHERE user_id=?", arrayOf(id.toString())).use {
        buildSet { while (it.moveToNext()) add(it.getLong(0)) }
    }
    private fun accessible(db: SQLiteDatabase, u: UserDto, lessonId: Long): DemoContent.Lesson {
        val lessons = catalog(db); val done = completed(db, u.id)
        val lesson = lessons.find { it.id == lessonId } ?: fail(404, "Урок не найден")
        if (u.currentCourseId != 1L || (lessonId !in done && lessons.firstOrNull { it.id !in done }?.id != lessonId)) fail(403, "Сначала пройди предыдущий урок")
        return lesson
    }
    override suspend fun getModulesByCourseId(courseId: Long): List<ModuleDto> = transaction { db ->
        val u = user(db); if (courseId != 1L) fail(404, "Курс недоступен")
        val done = completed(db, u.id); val lessons = catalog(db); val current = lessons.firstOrNull { it.id !in done }?.id
        lessons.groupBy { it.module }.map { (module, list) -> ModuleDto(module.toLong(), 1, if (module == 1) "Первые строки кода" else "Управляем программой", module,
            list.map { LessonDto(it.id, it.title, it.xp, it.theory, when (it.id) { in done -> LessonState.COMPLETED; current -> LessonState.CURRENT; else -> LessonState.BLOCKED }) }) }
    }
    override suspend fun getTasksByLesson(lessonId: Long): List<TaskDto> = transaction { db -> accessible(db, user(db), lessonId).exercises.map { it.task } }
    override suspend fun check(taskId: Long, answer: TaskAnswerDto): TaskResultDto = transaction { db ->
        val u = user(db)
        val exercise = accessible(db, u, answer.lessonId).exercises.find { it.task.id == taskId } ?: fail(404, "Задание не относится к уроку")
        val correct = when (exercise.task.type) {
            TaskType.MULTIPLE_CHOICE -> {
                val ids = answer.optionIds.orEmpty()
                ids.isNotEmpty() && ids.size == ids.toSet().size && ids.toSet() == exercise.answer.split(',').map { it.toLong() }.toSet()
            }
            else -> answer.answer?.replace("\r\n", "\n")?.trim() == exercise.answer
        }
        if (correct) db.execSQL("INSERT OR IGNORE INTO attempts(user_id,lesson_id,task_id) VALUES(?,?,?)", arrayOf(u.id, answer.lessonId, taskId))
        TaskResultDto(correct, exercise.explanation, if (exercise.task.type == TaskType.CODING) "EXACT_MATCH" else "ANSWER_MATCH")
    }
    override suspend fun getRandomQuote(): QuoteDto = transaction { db ->
        user(db); db.rawQuery("SELECT content FROM quotes ORDER BY RANDOM() LIMIT 1", null).use { it.moveToFirst(); gson.fromJson(it.getString(0), QuoteDto::class.java) }
    }
    private fun achievements(db: SQLiteDatabase): List<AchievementDto> = db.rawQuery("SELECT content FROM achievements ORDER BY id", null).use {
        buildList { while (it.moveToNext()) add(gson.fromJson(it.getString(0), AchievementDto::class.java)) }
    }
    override suspend fun getAllAchievements(): List<AchievementDto> = transaction { db -> user(db); achievements(db) }
    override suspend fun lessonCompleted(lessonId: Long): Response<List<AchievementDto>> = transaction { db ->
        val u = user(db); val lesson = accessible(db, u, lessonId)
        if (lessonId in completed(db, u.id)) return@transaction Response.success(emptyList())
        if (lesson.exercises.any { !exists(db, "SELECT 1 FROM attempts WHERE user_id=? AND lesson_id=? AND task_id=?", u.id.toString(), lessonId.toString(), it.task.id.toString()) }) {
            return@transaction Response.error(409, "Сначала реши все задания урока".toResponseBody())
        }
        val today = dayKey()
        val lastDay = db.rawQuery("SELECT last_day FROM users WHERE id=?", arrayOf(u.id.toString())).use { it.moveToFirst(); it.getString(0) }
        val streak = when (lastDay) { today -> u.streak; dayKey(-1) -> u.streak + 1; else -> 1 }
        var xp = u.xp + lesson.xp; var level = u.level; var next = u.xpForNextLevel
        while (xp >= next) { xp -= next; level++; next = (100 * 1.5.pow(level - 1)).roundToInt() }
        val updated = u.copy(xp = xp, level = level, xpForNextLevel = next, totalXp = u.totalXp + lesson.xp,
            completedLessons = u.completedLessons + 1, streak = streak, bestStreak = maxOf(u.bestStreak, streak.toLong()))
        save(db, updated)
        db.execSQL("UPDATE users SET last_day=? WHERE id=?", arrayOf(today, u.id))
        db.execSQL("INSERT INTO progress(user_id,lesson_id) VALUES(?,?)", arrayOf(u.id, lessonId))
        val awarded = achievements(db).mapIndexedNotNull { index, achievement ->
            val value = when (achievement.type) {
                AchievementType.LESSONS_COMPLETED -> updated.completedLessons
                AchievementType.LEVEL_REACHED -> updated.level.toLong()
                AchievementType.XP_EARNED -> updated.totalXp
                AchievementType.STREAK_DAYS -> updated.bestStreak
                else -> 0L
            }
            if (value >= achievement.conditionValue && !exists(db, "SELECT 1 FROM awards WHERE user_id=? AND achievement_id=?", u.id.toString(), index.toString())) {
                db.execSQL("INSERT INTO awards(user_id,achievement_id) VALUES(?,?)", arrayOf(u.id, index)); achievement
            } else null
        }
        Response.success(awarded)
    }
    private fun dayKey(offset: Int = 0): String = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, offset) }.let {
        "${it.get(Calendar.YEAR)}-${it.get(Calendar.MONTH) + 1}-${it.get(Calendar.DAY_OF_MONTH)}"
    }
    private fun exists(db: SQLiteDatabase, sql: String, vararg args: String) = db.rawQuery(sql, args).use { it.moveToFirst() }
    fun close() = helper.close()
}

private class DemoDatabase(context: Context) : SQLiteOpenHelper(context, "nextcode_demo.db", null, 1) {
    override fun onConfigure(db: SQLiteDatabase) { db.setForeignKeyConstraintsEnabled(true) }
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE users(id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT NOT NULL UNIQUE COLLATE NOCASE, salt TEXT NOT NULL, password_hash TEXT NOT NULL, profile TEXT NOT NULL, last_day TEXT)")
        db.execSQL("CREATE TABLE sessions(token TEXT PRIMARY KEY, user_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE)")
        db.execSQL("CREATE TABLE lessons(id INTEGER PRIMARY KEY, content TEXT NOT NULL)")
        db.execSQL("CREATE TABLE quotes(id INTEGER PRIMARY KEY, content TEXT NOT NULL)")
        db.execSQL("CREATE TABLE achievements(id INTEGER PRIMARY KEY, content TEXT NOT NULL)")
        db.execSQL("CREATE TABLE progress(user_id INTEGER NOT NULL REFERENCES users(id), lesson_id INTEGER NOT NULL REFERENCES lessons(id), PRIMARY KEY(user_id,lesson_id))")
        db.execSQL("CREATE TABLE attempts(user_id INTEGER NOT NULL REFERENCES users(id), lesson_id INTEGER NOT NULL REFERENCES lessons(id), task_id INTEGER NOT NULL, PRIMARY KEY(user_id,lesson_id,task_id))")
        db.execSQL("CREATE TABLE awards(user_id INTEGER NOT NULL REFERENCES users(id), achievement_id INTEGER NOT NULL REFERENCES achievements(id), PRIMARY KEY(user_id,achievement_id))")
        val gson = Gson()
        DemoContent.lessons.forEach { db.execSQL("INSERT INTO lessons VALUES(?,?)", arrayOf(it.id, gson.toJson(it.copy(theory = formatTheory(it.theory))))) }
        DemoContent.quotes.forEachIndexed { index, quote -> db.execSQL("INSERT INTO quotes VALUES(?,?)", arrayOf(index, gson.toJson(quote))) }
        DemoContent.achievements.forEachIndexed { index, achievement -> db.execSQL("INSERT INTO achievements VALUES(?,?)", arrayOf(index, gson.toJson(achievement))) }
    }
    private fun formatTheory(text: String): String {
        val split = text.indexOf("\n\n")
        if (split < 0) return text
        val remainder = text.substring(split + 2)
        val end = remainder.indexOf("\n\n").let { if (it < 0) remainder.length else it }
        return text.substring(0, split) + "\n\n```java\n" + remainder.substring(0, end) + "\n```" + remainder.substring(end)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Future demo versions must migrate accounts and progress rather than deleting them.
        error("Missing demo database migration from $oldVersion to $newVersion")
    }
}
