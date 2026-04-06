# PulseMind Backend

Single-module Spring Boot backend in Kotlin with clean-layer packages:
- `domain`: JPA entities (`WorkoutSession`, `Exercise`, `MoodEntry`)
- `application`: use-case service + DTOs
- `infrastructure`: repositories + REST controllers

## Run locally

```zsh
cd backend
docker-compose up -d
./gradlew bootRun
```

## Verify bootRun with PostgreSQL

1. Start PostgreSQL and run the app.
2. Confirm startup logs include Flyway + datasource initialization.
3. Create a session and fetch all sessions.

```zsh
cd backend
docker-compose up -d
./gradlew bootRun
```

In a second terminal:

```zsh
curl -sS -X POST "http://localhost:8080/api/v1/workout-sessions" \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "user-001",
    "sessionDate": "2026-04-03T10:30:00",
    "mood": "GREAT",
    "exercises": [
      {"name": "Bench Press", "reps": 10, "weight": 80.0},
      {"name": "Squat", "reps": 8, "weight": 100.0}
    ]
  }'

curl -sS "http://localhost:8080/api/v1/workout-sessions"
```

Optional cleanup:

```zsh
cd backend
docker-compose down
```

## Run tests

```zsh
cd backend
./gradlew test
```

## API endpoints
- `POST /api/v1/workout-sessions`
- `GET /api/v1/workout-sessions`
- `GET /api/v1/workout-sessions/{id}`
- `PUT /api/v1/workout-sessions/{id}`
- `DELETE /api/v1/workout-sessions/{id}`
