package com.example.work_manager_rnd.domain.repository

import com.example.work_manager_rnd.domain.WorkResult
import kotlinx.coroutines.flow.Flow

fun interface WorkerRepository {
    fun executeWork(schedule: Long): Flow<WorkResult>
}