package dbataev.nextcodeapp.core.data.remote

import dbataev.nextcodeapp.core.data.local.TokenStorage
import dbataev.nextcodeapp.core.data.remote.api.AuthApi
import dbataev.nextcodeapp.core.data.remote.api.CourseApi
import dbataev.nextcodeapp.core.data.remote.api.ModuleApi
import dbataev.nextcodeapp.core.data.remote.api.QuoteApi
import dbataev.nextcodeapp.core.data.remote.api.TaskApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    const val BASEURL = "http://192.168.31.249:8080/"
    private lateinit var tokenStorage: TokenStorage

    fun init(tokenStorage: TokenStorage) {
        this.tokenStorage = tokenStorage
    }

    val client = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor { tokenStorage.getToken() })
        .build()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASEURL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val quoteApi: QuoteApi by lazy {
        retrofit.create(QuoteApi::class.java)
    }

    val courseApi: CourseApi by lazy {
        retrofit.create(CourseApi::class.java)
    }

    val moduleApi: ModuleApi by lazy {
        retrofit.create(ModuleApi::class.java)
    }

    val authApi: AuthApi by lazy {
        retrofit.create(AuthApi::class.java)
    }

    val taskApi: TaskApi by lazy{
        retrofit.create(TaskApi::class.java)
    }
}
