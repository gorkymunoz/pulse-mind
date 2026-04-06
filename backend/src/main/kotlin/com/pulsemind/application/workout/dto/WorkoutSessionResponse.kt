package com.pulsemind.application.workout.dto

import com.pulsemind.domain.workout.MoodEntry
import java.time.LocalDateTime
import java.util.UUID

data class WorkoutSessionResponse(
    val id: UUID,
    val userId: String,
    val sessionDate: LocalDateTime,
    val mood: MoodEntry,
    val exercises: List<ExerciseResponse>
)

