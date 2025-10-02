package com.example.work_manager_rnd.domain.usecase

import com.example.work_manager_rnd.domain.WorkResult
import com.example.work_manager_rnd.domain.repository.WorkerRepository
import kotlinx.coroutines.flow.Flow

class StartJokeWorker(
    private val repository: WorkerRepository
) {
     operator fun invoke(schedule: Long): Flow<WorkResult> {
        return repository.executeWork(schedule)
    }
}