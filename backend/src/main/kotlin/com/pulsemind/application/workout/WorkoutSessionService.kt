package com.pulsemind.application.workout

import com.pulsemind.schema.workout.request.CreateWorkoutSessionRequestDto
import com.pulsemind.schema.workout.request.UpdateWorkoutSessionRequestDto
import com.pulsemind.schema.workout.response.WorkoutSessionResponseDto
import com.pulsemind.schema.workout.model.ExerciseDto
import com.pulsemind.schema.workout.model.MoodEntryDto
import com.pulsemind.domain.workout.Exercise
import com.pulsemind.domain.workout.MoodEntry
import com.pulsemind.domain.workout.WorkoutSession
import com.pulsemind.infrastructure.persistence.WorkoutSessionRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.UUID

@Service
@Transactional
class WorkoutSessionService(
    private val workoutSessionRepository: WorkoutSessionRepository
) {
    fun create(request: CreateWorkoutSessionRequestDto): WorkoutSessionResponseDto {
        val session = WorkoutSession(
            userId = request.userId,
            sessionDate = LocalDateTime.parse(request.sessionDate),
            mood = request.mood.toDomain()
        )

        val exercises = request.exercises.map {
            Exercise(
                name = it.name,
                reps = it.reps,
                weight = it.weight,
                workoutSession = session
            )
        }
        session.replaceExercises(exercises)

        return workoutSessionRepository.save(session).toResponseDto()
    }

    @Transactional(readOnly = true)
    fun findById(id: UUID): WorkoutSessionResponseDto {
        val session = workoutSessionRepository.findById(id)
            .orElseThrow { EntityNotFoundException("WorkoutSession not found for id: $id") }
        return session.toResponseDto()
    }

    @Transactional(readOnly = true)
    fun findAll(): List<WorkoutSessionResponseDto> =
        workoutSessionRepository.findAll().map { it.toResponseDto() }

    fun update(id: UUID, request: UpdateWorkoutSessionRequestDto): WorkoutSessionResponseDto {
        val session = workoutSessionRepository.findById(id)
            .orElseThrow { EntityNotFoundException("WorkoutSession not found for id: $id") }

        session.sessionDate = LocalDateTime.parse(request.sessionDate)
        session.mood = request.mood.toDomain()
        val exercises = request.exercises.map {
            Exercise(
                name = it.name,
                reps = it.reps,
                weight = it.weight,
                workoutSession = session
            )
        }
        session.replaceExercises(exercises)

        return workoutSessionRepository.save(session).toResponseDto()
    }

    fun delete(id: UUID) {
        if (!workoutSessionRepository.existsById(id)) {
            throw EntityNotFoundException("WorkoutSession not found for id: $id")
        }
        workoutSessionRepository.deleteById(id)
    }

    private fun WorkoutSession.toResponseDto(): WorkoutSessionResponseDto = WorkoutSessionResponseDto(
        id = requireNotNull(id).toString(),
        userId = userId,
        sessionDate = sessionDate.toString(),
        mood = mood.toDto(),
        exercises = exercises.map { exercise ->
            ExerciseDto(
                id = requireNotNull(exercise.id).toString(),
                name = exercise.name,
                reps = exercise.reps,
                weight = exercise.weight
            )
        }
    )

    private fun MoodEntryDto.toDomain(): MoodEntry = MoodEntry.valueOf(this.name)

    private fun MoodEntry.toDto(): MoodEntryDto = MoodEntryDto.valueOf(this.name)
}

