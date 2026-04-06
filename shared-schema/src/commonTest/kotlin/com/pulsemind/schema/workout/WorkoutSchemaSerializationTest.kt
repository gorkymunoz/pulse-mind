package com.pulsemind.schema.workout

import com.pulsemind.schema.workout.model.ExerciseDto
import com.pulsemind.schema.workout.model.MoodEntryDto
import com.pulsemind.schema.workout.response.WorkoutSessionResponseDto
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.serialization.json.Json

class WorkoutSchemaSerializationTest {
    private val json = Json { encodeDefaults = true }

    @Test
    fun `serialize and deserialize workout response dto`() {
        val payload = WorkoutSessionResponseDto(
            id = "ws-1",
            userId = "user-1",
            sessionDate = "2026-04-06T10:30:00",
            mood = MoodEntryDto.GREAT,
            exercises = listOf(
                ExerciseDto(id = "ex-1", name = "Bench Press", reps = 10, weight = 80.0)
            )
        )

        val encoded = json.encodeToString(WorkoutSessionResponseDto.serializer(), payload)
        val decoded = json.decodeFromString(WorkoutSessionResponseDto.serializer(), encoded)

        assertEquals(payload, decoded)
    }
}

