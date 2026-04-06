package com.pulsemind.schema.workout.model

import kotlinx.serialization.Serializable

@Serializable
data class ExerciseDto(
    val id: String? = null,
    val name: String,
    val reps: Int,
    val weight: Double
)

