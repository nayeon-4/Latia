package pe.edu.upc.latia_lati.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import pe.edu.upc.latia_lati.dtos.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private ResponseEntity<ErrorResponse> response(int status, String message, HttpServletRequest request) {
        return ResponseEntity.status(status).body(new ErrorResponse(status, message, request.getRequestURI()));
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        return response(404, ex.getMessage(), request);
    }
    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<ErrorResponse> handleBusinessRule(BusinessRuleException ex, HttpServletRequest request) {
        return response(400, ex.getMessage(), request);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage()).sorted()
                .collect(java.util.stream.Collectors.joining("; "));
        return response(400, message, request);
    }
    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ErrorResponse> handleInvalidInput(Exception ex, HttpServletRequest request) {
        return response(400, "Invalid JSON, date or path parameter", request);
    }
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleConflict(DataIntegrityViolationException ex, HttpServletRequest request) {
        return response(409, "Duplicate value or operation conflicts with related records", request);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleExplicitConflict(ConflictException ex, HttpServletRequest request) {
        return response(409, ex.getMessage(), request);
    }
    @ExceptionHandler(org.springframework.security.core.AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthentication(Exception ex, HttpServletRequest request) {
        return response(401, "Credenciales inválidas o sesión no vigente", request);
    }
    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(Exception ex, HttpServletRequest request) {
        return response(403, "No tienes permiso para esta operación", request);
    }
    @ExceptionHandler(org.springframework.web.method.annotation.HandlerMethodValidationException.class)
    public ResponseEntity<ErrorResponse> handleMethodValidation(Exception ex, HttpServletRequest request) {
        return response(400, "El ID y los parámetros deben ser válidos y positivos", request);
    }
    @ExceptionHandler(jakarta.validation.ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleServiceValidation(Exception ex, HttpServletRequest request) {
        return response(400, "Los datos no cumplen las validaciones requeridas", request);
    }
}
