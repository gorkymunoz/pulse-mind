package com.pulsemind.application.workout.dto

import com.pulsemind.domain.workout.MoodEntry
import jakarta.validation.Valid
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime

data class UpdateWorkoutSessionRequest(
    @field:NotNull(message = "Session date is required")
    var sessionDate: LocalDateTime,

    @field:NotNull(message = "Mood is required")
    var mood: MoodEntry,

    @field:Valid
    @field:NotEmpty(message = "At least one exercise is required")
    val exercises: List<ExerciseRequest>
)

