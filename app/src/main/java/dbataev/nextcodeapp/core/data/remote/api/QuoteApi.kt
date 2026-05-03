package dbataev.nextcodeapp.core.data.remote.api

import dbataev.nextcodeapp.core.data.remote.dto.QuoteDto
import retrofit2.http.GET

interface QuoteApi {
    @GET("api/quotes/random")
    suspend fun getRandomQuote(): QuoteDto
}