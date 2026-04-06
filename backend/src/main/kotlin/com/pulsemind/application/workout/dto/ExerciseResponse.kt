package com.pulsemind.application.workout.dto

import java.util.UUID

data class ExerciseResponse(
    val id: UUID,
    val name: String,
    val reps: Int,
    val weight: Double
)

