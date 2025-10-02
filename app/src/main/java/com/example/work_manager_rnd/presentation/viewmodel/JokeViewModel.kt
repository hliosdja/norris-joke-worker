package com.example.work_manager_rnd.presentation.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.work_manager_rnd.domain.WorkResult
import com.example.work_manager_rnd.domain.usecase.StartJokeWorker
import com.example.work_manager_rnd.presentation.state.UIState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime

class JokeViewModel(
    private val startJokeWorker: StartJokeWorker
): ViewModel() {
    private val _uiState = MutableStateFlow(UIState())
    val uiState: StateFlow<UIState> = _uiState.asStateFlow()

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateSchedule(
        hour: Int,
        minute: Int
    ) {
        val schedule = calculateWorkSchedule(hour, minute)

        Log.d("JokeSchedule", "updateSchedule: $schedule")

        viewModelScope.launch(Dispatchers.IO) {
            startJokeWorker(schedule).collect { response ->
                when (response) {
                    is WorkResult.Loading -> { /* no-op */ }
                    is WorkResult.Success -> {
                        _uiState.value = _uiState.value.copy(joke = response.joke.value)
                    }
                    is WorkResult.Error -> {
                        _uiState.value = _uiState.value.copy(joke = response.errorMessage)
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun calculateWorkSchedule(
        hour: Int,
        minute: Int
    ): Long {
        val now = LocalDateTime.now()
        var scheduledTime = LocalDateTime.of(
            now.toLocalDate(),
            LocalTime.of(hour, minute)
        )

        if (scheduledTime.isBefore(now)) {
            scheduledTime = scheduledTime.plusDays(1)
        }

        return Duration.between(now, scheduledTime).toMillis()
    }
}