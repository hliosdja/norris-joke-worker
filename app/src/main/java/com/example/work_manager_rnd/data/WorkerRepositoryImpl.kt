package com.example.work_manager_rnd.data

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.work_manager_rnd.data.worker.FetchJokeWorker
import com.example.work_manager_rnd.domain.WorkResult
import com.example.work_manager_rnd.domain.models.JokesDomain
import com.example.work_manager_rnd.domain.repository.WorkerRepository
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json
import java.util.concurrent.TimeUnit

class WorkerRepositoryImpl(
    context: Context
) : WorkerRepository {
    private val workManager = WorkManager.getInstance(context)

    override fun executeWork(schedule: Long) = flow {

        val workRequest = OneTimeWorkRequestBuilder<FetchJokeWorker>()
            .setInitialDelay(schedule, TimeUnit.MILLISECONDS)
            .build()

        workManager.enqueue(workRequest)

        workManager.getWorkInfoByIdFlow(workRequest.id)
            .collect { workInfo ->
                if (workInfo != null) {
                    when (workInfo.state) {
                        WorkInfo.State.ENQUEUED -> {
                            emit(WorkResult.Loading)
                        }
                        WorkInfo.State.SUCCEEDED -> {
                            val data = workInfo.outputData.getString("joke_result")
                            val jokeResult = Json.decodeFromString<JokesDomain>(data!!)
                            emit(
                                WorkResult.Success(jokeResult)
                            )
                        }
                        WorkInfo.State.FAILED -> {
                            val error = workInfo.outputData.getString("error")
                            emit(
                                WorkResult.Error(
                                    error ?: "Something went wrong"
                                )
                            )
                        }
                        else -> { /* no-op */ }
                    }
                }
            }
    }
}