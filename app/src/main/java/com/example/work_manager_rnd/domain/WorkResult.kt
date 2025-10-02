package com.example.work_manager_rnd.domain

import com.example.work_manager_rnd.domain.models.JokesDomain

sealed class WorkResult {
    data object Loading : WorkResult()
    data class Success(val joke: JokesDomain) : WorkResult()
    data class Error(val errorMessage: String) : WorkResult()
}