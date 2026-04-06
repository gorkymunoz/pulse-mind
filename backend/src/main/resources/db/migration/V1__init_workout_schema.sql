CREATE TABLE workout_sessions (
    id UUID PRIMARY KEY,
    user_id VARCHAR(255) NOT NULL,
    session_date TIMESTAMP NOT NULL,
    mood VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE exercises (
    id UUID PRIMARY KEY,
    workout_session_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    reps INTEGER NOT NULL,
    weight DOUBLE PRECISION NOT NULL,
    CONSTRAINT fk_exercises_workout_session
        FOREIGN KEY (workout_session_id)
        REFERENCES workout_sessions(id)
        ON DELETE CASCADE
);

CREATE INDEX idx_workout_sessions_user_id ON workout_sessions(user_id);
CREATE INDEX idx_exercises_workout_session_id ON exercises(workout_session_id);

