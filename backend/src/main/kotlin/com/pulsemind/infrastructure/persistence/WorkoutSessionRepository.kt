package com.pulsemind.infrastructure.persistence

import com.pulsemind.domain.workout.WorkoutSession
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface WorkoutSessionRepository : JpaRepository<WorkoutSession, UUID>

