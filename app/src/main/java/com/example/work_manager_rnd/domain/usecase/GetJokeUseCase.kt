package com.example.work_manager_rnd.domain.usecase

import com.example.work_manager_rnd.domain.models.JokesDomain
import com.example.work_manager_rnd.domain.repository.JokeRepository

class GetJokeUseCase(
    private val repository: JokeRepository
) {
    suspend operator fun invoke(): JokesDomain {
        return repository.fetchJoke()
    }
}