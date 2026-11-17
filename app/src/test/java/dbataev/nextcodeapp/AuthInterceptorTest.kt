package dbataev.nextcodeapp
import dbataev.nextcodeapp.core.data.remote.AuthInterceptor
import okhttp3.*
import okhttp3.mockwebserver.*
import org.junit.Assert.*
import org.junit.Test
class AuthInterceptorTest {
    private fun scenario(path: String, codes: List<Int>, expectedExpired: Int, expectedRequests: Int) {
        MockWebServer().use { server ->
            server.start()
            codes.forEach { server.enqueue(MockResponse().setResponseCode(it).setBody("{}")) }
            var expired = 0
            val client = OkHttpClient.Builder().addInterceptor(AuthInterceptor({ "jwt" }, { expired++ })).build()
            client.newCall(Request.Builder().url(server.url(path)).build()).execute().use {
                assertEquals(codes.first(), it.code)
                assertEquals("{}",it.body!!.string())
            }
            assertEquals(expectedExpired,expired)
            assertEquals(expectedRequests,server.requestCount)
            val request = server.takeRequest()
            if (path.startsWith("/api/auth/")) assertNull(request.getHeader("Authorization"))
            else assertEquals("Bearer jwt",request.getHeader("Authorization"))
            if (expectedRequests == 2) assertEquals("/api/users/me",server.takeRequest().path)
        }
    }
    @Test fun unauthorizedProtectedCallExpiresSession() = scenario("/api/tasks/bylesson/2",listOf(401),1,1)
    @Test fun backend403ExpiresOnlyWhenMeAlsoRejectsToken() = scenario("/api/tasks/bylesson/2",listOf(403,403),1,2)
    @Test fun ordinaryForbiddenResourceDoesNotExpireValidSession() = scenario("/api/tasks/bylesson/2",listOf(403,200),0,2)
    @Test fun failedSessionProbeDoesNotDiscardToken() = scenario("/api/tasks/bylesson/2",listOf(403,500),0,2)
    @Test fun loginFailureDoesNotTriggerGlobalLogout() = scenario("/api/auth/login",listOf(401),0,1)
    @Test fun forbiddenMeExpiresWithoutRecursiveProbe() = scenario("/api/users/me",listOf(403),1,1)
}
