package dbataev.nextcodeapp.core.data.remote

import dbataev.nextcodeapp.core.data.local.TokenStorage
import dbataev.nextcodeapp.core.data.remote.api.AchievementApi
import dbataev.nextcodeapp.core.data.remote.api.AuthApi
import dbataev.nextcodeapp.core.data.remote.api.CourseApi
import dbataev.nextcodeapp.core.data.remote.api.LessonApi
import dbataev.nextcodeapp.core.data.remote.api.ModuleApi
import dbataev.nextcodeapp.core.data.remote.api.QuoteApi
import dbataev.nextcodeapp.core.data.remote.api.TaskApi
import dbataev.nextcodeapp.core.data.remote.api.UserApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    const val BASEURL = "http://192.168.0.10:8080/"     // Дом
    // const val BASEURL = "http://10.12.107.81:8080/"  // Мобильная точка доступа
    // const val BASEURL = "172.24.112.1:8080/"         // Обшага
    lateinit var session: dbataev.nextcodeapp.core.data.local.SessionManager
        private set

    fun init(tokenStorage: TokenStorage, context: android.content.Context? = null) {
        if (!::session.isInitialized) session = dbataev.nextcodeapp.core.data.local.SessionManager(tokenStorage)
        if (dbataev.nextcodeapp.BuildConfig.OFFLINE_DEMO && !::offline.isInitialized) {
            offline = dbataev.nextcodeapp.core.data.local.demo.DemoApi(requireNotNull(context).applicationContext, session)
        }
    }

    private lateinit var offline: dbataev.nextcodeapp.core.data.local.demo.DemoApi

    private fun offlineApi(): dbataev.nextcodeapp.core.data.local.demo.DemoApi = offline

    val client = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor(tokenProvider = { session.token() }, onUnauthorized = { session.expire(it) }))
        .build()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASEURL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val quoteApi: QuoteApi by lazy {
        if (dbataev.nextcodeapp.BuildConfig.OFFLINE_DEMO) offlineApi() else retrofit.create(QuoteApi::class.java)
    }

    val courseApi: CourseApi by lazy {
        if (dbataev.nextcodeapp.BuildConfig.OFFLINE_DEMO) offlineApi() else retrofit.create(CourseApi::class.java)
    }

    val moduleApi: ModuleApi by lazy {
        if (dbataev.nextcodeapp.BuildConfig.OFFLINE_DEMO) offlineApi() else retrofit.create(ModuleApi::class.java)
    }

    val authApi: AuthApi by lazy {
        if (dbataev.nextcodeapp.BuildConfig.OFFLINE_DEMO) offlineApi() else retrofit.create(AuthApi::class.java)
    }

    val taskApi: TaskApi by lazy{
        if (dbataev.nextcodeapp.BuildConfig.OFFLINE_DEMO) offlineApi() else retrofit.create(TaskApi::class.java)
    }

    val userApi: UserApi by lazy {
        if (dbataev.nextcodeapp.BuildConfig.OFFLINE_DEMO) offlineApi() else retrofit.create(UserApi::class.java)
    }

    val lessonApi: LessonApi by lazy {
        if (dbataev.nextcodeapp.BuildConfig.OFFLINE_DEMO) offlineApi() else retrofit.create(LessonApi::class.java)
    }

    val achievementApi: AchievementApi by lazy {
        if (dbataev.nextcodeapp.BuildConfig.OFFLINE_DEMO) offlineApi() else retrofit.create(AchievementApi::class.java)
    }
}
