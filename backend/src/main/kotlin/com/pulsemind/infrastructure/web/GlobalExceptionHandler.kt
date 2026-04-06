package com.pulsemind.infrastructure.web

import jakarta.persistence.EntityNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

data class ApiError(
    val status: Int,
    val message: String,
    val errors: List<String> = emptyList()
)

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(EntityNotFoundException::class)
    fun handleNotFound(ex: EntityNotFoundException): ResponseEntity<ApiError> =
        ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ApiError(status = HttpStatus.NOT_FOUND.value(), message = ex.message ?: "Not Found"))

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidation(ex: MethodArgumentNotValidException): ResponseEntity<ApiError> {
        val errors = ex.bindingResult.allErrors.mapNotNull {
            when (it) {
                is FieldError -> "${it.field}: ${it.defaultMessage}"
                else -> it.defaultMessage
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ApiError(status = HttpStatus.BAD_REQUEST.value(), message = "Validation failed", errors = errors))
    }
}

