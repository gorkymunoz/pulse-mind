package com.pulsemind.application.workout

import com.pulsemind.schema.workout.request.CreateWorkoutSessionRequestDto
import com.pulsemind.schema.workout.model.ExerciseDto
import com.pulsemind.schema.workout.request.UpdateWorkoutSessionRequestDto
import com.pulsemind.domain.workout.Exercise
import com.pulsemind.domain.workout.MoodEntry
import com.pulsemind.schema.workout.model.MoodEntryDto
import com.pulsemind.domain.workout.WorkoutSession
import com.pulsemind.infrastructure.persistence.WorkoutSessionRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import jakarta.persistence.EntityNotFoundException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.Optional
import java.util.UUID

class WorkoutSessionServiceTest {
    private val repository = mockk<WorkoutSessionRepository>()
    private val service = WorkoutSessionService(repository)

    @Test
    fun `create should persist session and return DTO with exercises`() {
        val now = LocalDateTime.now()
        val request = CreateWorkoutSessionRequestDto(
            userId = "user-123",
            sessionDate = now.toString(),
            mood = MoodEntryDto.GREAT,
            exercises = listOf(ExerciseDto(name = "Bench Press", reps = 10, weight = 80.0))
        )

        every { repository.save(any()) } answers {
            val input = firstArg<WorkoutSession>()
            val persisted = WorkoutSession(
                id = UUID.randomUUID(),
                userId = input.userId,
                sessionDate = input.sessionDate,
                mood = input.mood
            )
            val persistedExercises = input.exercises.map {
                Exercise(
                    id = UUID.randomUUID(),
                    name = it.name,
                    reps = it.reps,
                    weight = it.weight,
                    workoutSession = persisted
                )
            }
            persisted.replaceExercises(persistedExercises)
            persisted
        }

        val result = service.create(request)

        assertEquals("user-123", result.userId)
        assertEquals(MoodEntryDto.GREAT, result.mood)
        assertEquals(1, result.exercises.size)
        verify(exactly = 1) { repository.save(any()) }
    }

    @Test
    fun `findAll should return list of DTOs`() {
        val session = WorkoutSession(
            id = UUID.randomUUID(),
            userId = "user-123",
            sessionDate = LocalDateTime.now(),
            mood = MoodEntry.TIRED
        )
        val exercise = Exercise(
            id = UUID.randomUUID(),
            name = "Squat",
            reps = 8,
            weight = 100.0,
            workoutSession = session
        )
        session.replaceExercises(listOf(exercise))

        every { repository.findAll() } returns listOf(session)

        val result = service.findAll()

        assertEquals(1, result.size)
        assertEquals("Squat", result.first().exercises.first().name)
    }

    @Test
    fun `update should replace exercises`() {
        val existing = WorkoutSession(
            id = UUID.randomUUID(),
            userId = "user-123",
            sessionDate = LocalDateTime.now(),
            mood = MoodEntry.SORE
        )
        existing.replaceExercises(
            listOf(
                Exercise(
                    id = UUID.randomUUID(),
                    name = "Deadlift",
                    reps = 5,
                    weight = 120.0,
                    workoutSession = existing
                )
            )
        )

        every { repository.findById(existing.id!!) } returns Optional.of(existing)
        every { repository.save(any()) } answers {
            val input = firstArg<WorkoutSession>()
            val persisted = WorkoutSession(
                id = input.id ?: UUID.randomUUID(),
                userId = input.userId,
                sessionDate = input.sessionDate,
                mood = input.mood
            )
            val persistedExercises = input.exercises.map {
                Exercise(
                    id = UUID.randomUUID(),
                    name = it.name,
                    reps = it.reps,
                    weight = it.weight,
                    workoutSession = persisted
                )
            }
            persisted.replaceExercises(persistedExercises)
            persisted
        }

        val updated = service.update(
            existing.id!!,
            UpdateWorkoutSessionRequestDto(
                sessionDate = existing.sessionDate.plusDays(1).toString(),
                mood = MoodEntryDto.GREAT,
                exercises = listOf(ExerciseDto(name = "Pull Ups", reps = 12, weight = 0.0))
            )
        )

        assertEquals(MoodEntryDto.GREAT, updated.mood)
        assertEquals(1, updated.exercises.size)
        assertEquals("Pull Ups", updated.exercises.first().name)
    }

    @Test
    fun `findById should throw when missing`() {
        val id = UUID.randomUUID()
        every { repository.findById(id) } returns Optional.empty()

        assertThrows(EntityNotFoundException::class.java) {
            service.findById(id)
        }
    }

    @Test
    fun `delete should throw when missing`() {
        val id = UUID.randomUUID()
        every { repository.existsById(id) } returns false

        assertThrows(EntityNotFoundException::class.java) {
            service.delete(id)
        }
    }
}
