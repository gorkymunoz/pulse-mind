package com.pulsemind.schema.workout.model

import kotlinx.serialization.Serializable

@Serializable
enum class MoodEntryDto {
    GREAT,
    TIRED,
    SORE,
    STRESSED,
    NEUTRAL
}

