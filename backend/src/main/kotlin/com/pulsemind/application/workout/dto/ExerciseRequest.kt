package com.pulsemind.application.workout.dto

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.PositiveOrZero

data class ExerciseRequest(
    @field:NotBlank(message = "Exercise name is required")
    val name: String,

    @field:Min(1, message = "Reps must be at least 1")
    val reps: Int,

    @field:PositiveOrZero(message = "Weight must be zero or positive")
    val weight: Double
)
