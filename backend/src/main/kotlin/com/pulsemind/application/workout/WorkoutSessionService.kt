package com.pulsemind.application.workout

import com.pulsemind.application.workout.dto.CreateWorkoutSessionRequest
import com.pulsemind.application.workout.dto.ExerciseResponse
import com.pulsemind.application.workout.dto.UpdateWorkoutSessionRequest
import com.pulsemind.application.workout.dto.WorkoutSessionResponse
import com.pulsemind.domain.workout.Exercise
import com.pulsemind.domain.workout.WorkoutSession
import com.pulsemind.infrastructure.persistence.WorkoutSessionRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional
class WorkoutSessionService(
    private val workoutSessionRepository: WorkoutSessionRepository
) {
    fun create(request: CreateWorkoutSessionRequest): WorkoutSessionResponse {
        val session = WorkoutSession(
            userId = request.userId,
            sessionDate = request.sessionDate,
            mood = request.mood
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

        return workoutSessionRepository.save(session).toResponse()
    }

    @Transactional(readOnly = true)
    fun findById(id: UUID): WorkoutSessionResponse {
        val session = workoutSessionRepository.findById(id)
            .orElseThrow { EntityNotFoundException("WorkoutSession not found for id: $id") }
        return session.toResponse()
    }

    @Transactional(readOnly = true)
    fun findAll(): List<WorkoutSessionResponse> =
        workoutSessionRepository.findAll().map { it.toResponse() }

    fun update(id: UUID, request: UpdateWorkoutSessionRequest): WorkoutSessionResponse {
        val session = workoutSessionRepository.findById(id)
            .orElseThrow { EntityNotFoundException("WorkoutSession not found for id: $id") }

        session.sessionDate = request.sessionDate
        session.mood = request.mood
        val exercises = request.exercises.map {
            Exercise(
                name = it.name,
                reps = it.reps,
                weight = it.weight,
                workoutSession = session
            )
        }
        session.replaceExercises(exercises)

        return workoutSessionRepository.save(session).toResponse()
    }

    fun delete(id: UUID) {
        if (!workoutSessionRepository.existsById(id)) {
            throw EntityNotFoundException("WorkoutSession not found for id: $id")
        }
        workoutSessionRepository.deleteById(id)
    }

    private fun WorkoutSession.toResponse(): WorkoutSessionResponse = WorkoutSessionResponse(
        id = requireNotNull(id),
        userId = userId,
        sessionDate = sessionDate,
        mood = mood,
        exercises = exercises.map { exercise ->
            ExerciseResponse(
                id = requireNotNull(exercise.id),
                name = exercise.name,
                reps = exercise.reps,
                weight = exercise.weight
            )
        }
    )
}

