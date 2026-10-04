package pe.edu.upc.latia_lati.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import pe.edu.upc.latia_lati.dtos.ErrorResponse;
import java.util.stream.Collectors;

/** Errores que Swagger y el frontend reciben como JSON: status, message y path. */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);
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
                .map(error -> error.getField() + ": " + error.getDefaultMessage()).sorted().distinct()
                .collect(Collectors.joining("; "));
        return response(400, message, request);
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleInvalidJson(Exception ex, HttpServletRequest request) {
        return response(400, "El cuerpo JSON falta o tiene datos inválidos. Revisa tipos, comillas y fechas (AAAA-MM-DD)", request);
    }
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleInvalidParameter(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        return response(400, "El parámetro '" + ex.getName() + "' tiene un formato inválido. Usa un ID numérico o true/false para active", request);
    }
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingParameter(MissingServletRequestParameterException ex, HttpServletRequest request) {
        return response(400, "Falta el parámetro obligatorio: " + ex.getParameterName(), request);
    }
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleConflict(DataIntegrityViolationException ex, HttpServletRequest request) {
        return response(409, "La operación entra en conflicto con los datos existentes: hay un valor duplicado o registros relacionados que impiden eliminar. Revisa las dependencias antes de reintentar", request);
    }
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleExplicitConflict(ConflictException ex, HttpServletRequest request) {
        return response(409, ex.getMessage(), request);
    }
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthentication(AuthenticationException ex, HttpServletRequest request) {
        return response(401, "Credenciales inválidas o sesión no vigente. Vuelve a iniciar sesión", request);
    }
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex, HttpServletRequest request) {
        return response(403, ex.getMessage(), request);
    }
    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ErrorResponse> handleMethodValidation(HandlerMethodValidationException ex, HttpServletRequest request) {
        String message = ex.getParameterValidationResults().stream()
                .flatMap(result -> result.getResolvableErrors().stream())
                .map(error -> error.getDefaultMessage()).distinct().sorted().collect(Collectors.joining("; "));
        return response(ex.getStatusCode().value(), message, request);
    }
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleServiceValidation(ConstraintViolationException ex, HttpServletRequest request) {
        String message = ex.getConstraintViolations().stream()
                .map(error -> error.getPropertyPath() + ": " + error.getMessage()).sorted()
                .collect(Collectors.joining("; "));
        return response(400, message, request);
    }
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethod(HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        return response(405, "El método HTTP no está permitido para esta ruta. Para actualizar usa PUT /api/users/{id} o /api/healthprofiles/{id}", request);
    }
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMedia(Exception ex, HttpServletRequest request) {
        return response(415, "Envía el cuerpo como JSON con Content-Type: application/json", request);
    }
    @ExceptionHandler(DataAccessResourceFailureException.class)
    public ResponseEntity<ErrorResponse> handleDatabase(Exception ex, HttpServletRequest request) {
        LOGGER.error("No se pudo acceder a la base de datos en {}", request.getRequestURI(), ex);
        return response(503, "La base de datos no está disponible. Inténtalo más tarde o avisa al responsable", request);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(Exception ex, HttpServletRequest request) {
        LOGGER.error("Error inesperado en {}", request.getRequestURI(), ex);
        return response(500, "Ocurrió un error interno. Avisa al responsable indicando la ruta y la hora del error", request);
    }
}
