package com.syntiaro_pos_system.advice.v2;

import com.syntiaro_pos_system.entity.v2.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApplicationExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleValidationException(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        String errorMessage = result.getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .reduce("", (msg, error) -> msg + error + "\n");
        ApiResponse response = new ApiResponse(null, false, errorMessage, 400);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(ClassCastException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiResponse> handleClassCastException(ClassCastException ex) {
        ex.printStackTrace();
        ApiResponse response = new ApiResponse(null, false, "Invalid request format", 500);
        return ResponseEntity.badRequest().body(response);
    }

}
