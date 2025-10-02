package com.example.work_manager_rnd.di

import com.example.work_manager_rnd.data.JokeRepositoryImpl
import com.example.work_manager_rnd.data.WorkerRepositoryImpl
import com.example.work_manager_rnd.domain.repository.JokeRepository
import com.example.work_manager_rnd.domain.repository.WorkerRepository
import com.example.work_manager_rnd.domain.usecase.GetJokeUseCase
import com.example.work_manager_rnd.domain.usecase.StartJokeWorker
import com.example.work_manager_rnd.presentation.viewmodel.JokeViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {

    single {
        Json {
            isLenient = true
            ignoreUnknownKeys = true
        }
    }

    single {
        HttpClient(CIO) {
            defaultRequest {
                url.protocol = URLProtocol.HTTPS
            }

            install(ContentNegotiation) {
                json(get())
            }

            install(HttpTimeout) {
                requestTimeoutMillis = 120_000L
                connectTimeoutMillis = 120_000L
                socketTimeoutMillis = 120_000L
            }

            install(Logging) {
                logger = Logger.SIMPLE
                level = LogLevel.ALL
            }
        }
    }

    // use case
    singleOf(::GetJokeUseCase)
    singleOf(::StartJokeWorker)

    // repository
    singleOf(::JokeRepositoryImpl).bind<JokeRepository>()
    singleOf(::WorkerRepositoryImpl).bind<WorkerRepository>()

    // viewmodel
    viewModelOf(::JokeViewModel)
}