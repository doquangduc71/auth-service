package com.project.authservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(999,"Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_EXIST(204,"User already exist",HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(205,"Username must be at least {min} characters",HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(206,"Password must be at least {min} characters",HttpStatus.BAD_REQUEST),
    KEY_INVALID(207,"Invalid message key",HttpStatus.BAD_REQUEST),
    USER_NOT_EXIST(208,"User not exist",HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(209,"Unauthenticated",HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(210,"You do not have permission",HttpStatus.FORBIDDEN),
    INVALID_DOB(211,"Your age must be at least {min}",HttpStatus.BAD_REQUEST),

    ;

    ErrorCode(int code, String message,HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;

}
