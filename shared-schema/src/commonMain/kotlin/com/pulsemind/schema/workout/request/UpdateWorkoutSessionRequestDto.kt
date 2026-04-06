package com.pulsemind.schema.workout.request

import com.pulsemind.schema.workout.model.ExerciseDto
import com.pulsemind.schema.workout.model.MoodEntryDto
import kotlinx.serialization.Serializable

@Serializable
data class UpdateWorkoutSessionRequestDto(
    val sessionDate: String,
    val mood: MoodEntryDto,
    val exercises: List<ExerciseDto>
)

