package com.example.work_manager_rnd.data

import com.example.work_manager_rnd.domain.models.JokesDomain
import com.example.work_manager_rnd.domain.repository.JokeRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class JokeRepositoryImpl(
    private val client: HttpClient
) : JokeRepository {
    override suspend fun fetchJoke(): JokesDomain {
        val response: JokesDomain = client.get("https://api.chucknorris.io/jokes/random").body()
        return response
    }
}