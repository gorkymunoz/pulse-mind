package com.pulsemind.schema.workout.response

import com.pulsemind.schema.workout.model.ExerciseDto
import com.pulsemind.schema.workout.model.MoodEntryDto
import kotlinx.serialization.Serializable

@Serializable
data class WorkoutSessionResponseDto(
    val id: String,
    val userId: String,
    val sessionDate: String,
    val mood: MoodEntryDto,
    val exercises: List<ExerciseDto>
)

