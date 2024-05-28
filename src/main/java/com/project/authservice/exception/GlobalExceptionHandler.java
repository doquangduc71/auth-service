package com.project.authservice.exception;

import com.project.authservice.dto.request.ApiResponse;
import jakarta.validation.ConstraintViolation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private static final String MIN_ATTRIBUTE = "min";

    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse> handlingRuntimeException(Exception exception){
        return ResponseEntity.badRequest().body(ApiResponse
                .builder()
                .code(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode())
                .message(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage())
                .build());
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handlingRuntimeException(AppException exception){
        return ResponseEntity
                .status(exception.getErrorCode().getStatusCode())
                .body(ApiResponse
                        .builder()
                        .code(exception.getErrorCode().getCode())
                        .message(exception.getErrorCode().getMessage())
                        .build());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handlingValidation(MethodArgumentNotValidException exception){
        String enumKey = exception.getFieldError().getDefaultMessage();

        ErrorCode errorCode = ErrorCode.KEY_INVALID;
        Map attributes = null;
        try {
            errorCode = ErrorCode.valueOf(enumKey);

            var constrainViolation = exception.getBindingResult()
                    .getAllErrors().get(0).unwrap(ConstraintViolation.class);
            attributes = constrainViolation.getConstraintDescriptor().getAttributes();
            log.info(attributes.toString());
        }catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
        }

        return ResponseEntity.badRequest().body(ApiResponse
                .builder()
                .code(errorCode.getCode())
                .message(Objects.nonNull(attributes)
                        ? mapAttribute(errorCode.getMessage(), attributes)
                        : errorCode.getMessage())
                .build());
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<ApiResponse> handlingAccessDeniedException(AccessDeniedException exception){
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;
        return ResponseEntity.status(errorCode.getStatusCode()).body(ApiResponse
                .builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build());
    }

    private String mapAttribute(String message, Map<String, Object> attributes){
        String minValue = attributes.get(MIN_ATTRIBUTE).toString();
        return message.replace("{"+MIN_ATTRIBUTE+"}", minValue);
    }
}
