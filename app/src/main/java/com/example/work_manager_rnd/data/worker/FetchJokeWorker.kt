package com.example.work_manager_rnd.data.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.work_manager_rnd.domain.usecase.GetJokeUseCase
import kotlinx.serialization.json.Json
import org.koin.java.KoinJavaComponent.inject

class FetchJokeWorker(
    context: Context,
    params: WorkerParameters
): CoroutineWorker(context, params) {

    private val getJokeUseCase: GetJokeUseCase by inject(GetJokeUseCase::class.java)

    override suspend fun doWork(): Result {
        return try {
            val joke = getJokeUseCase.invoke()
            val json = Json.encodeToString(joke)
            val outputData = Data.Builder()
                .putString("joke_result", json)
                .build()

            Result.success(outputData)
        } catch(e: Exception) {
            val error = workDataOf("error" to e.localizedMessage)
            Result.failure(error)
        }
    }
}