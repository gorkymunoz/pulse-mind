package com.pulsemind.infrastructure.persistence

import com.pulsemind.domain.workout.Exercise
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface ExerciseRepository : JpaRepository<Exercise, UUID>
