package dbataev.nextcodeapp.core.data.remote

import java.io.IOException
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

class AuthInterceptor(
    private val tokenProvider: () -> String?,
    private val onUnauthorized: (String) -> Unit = {}
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val authEndpoint = request.url.encodedPath in setOf("/api/auth/login", "/api/auth/register")
        val token = if (authEndpoint) null else tokenProvider()
        val authenticated = request.newBuilder().removeHeader("Authorization").apply {
            if (token != null) header("Authorization", "Bearer $token")
        }.build()
        var response = chain.proceed(authenticated)
        if (token == null) return response

        val invalidSession = when (response.code) {
            401 -> true
            // The current Spring backend can answer 403 for an invalid JWT.
            // Check /me before treating a resource-level denial as a session failure.
            403 -> if (request.url.encodedPath == "/api/users/me") true else {
                val probe = authenticated.newBuilder()
                    .url(request.url.newBuilder().encodedPath("/api/users/me").query(null).build())
                    .get().build()
                // Release the first exchange before a second proceed(), retaining its error payload.
                val body = response.body
                val buffered = body?.bytes()?.toResponseBody(body.contentType())
                response.close()
                response = response.newBuilder().body(buffered).build()
                try {
                    chain.proceed(probe).use { it.code == 401 || it.code == 403 }
                } catch (_: IOException) { false }
            }
            else -> false
        }
        if (invalidSession) onUnauthorized(token)
        return response
    }
}
