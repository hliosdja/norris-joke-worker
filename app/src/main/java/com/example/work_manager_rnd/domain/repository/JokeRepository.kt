package com.example.work_manager_rnd.domain.repository

import com.example.work_manager_rnd.domain.models.JokesDomain

fun interface JokeRepository {
    suspend fun fetchJoke(): JokesDomain
}