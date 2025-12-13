package com.pklinh.student_management.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
//    Lỗi nghiệp vụ
    USERNAME_EXISTED(1002, "User existed", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(1003, "User not found", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1004, "Username IS NULL", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1005, "Password NEED AT LEAST 2 CHARACTERS", HttpStatus.BAD_REQUEST),

//    Lỗi chưa xác định được
    INVALID_KEY(1001, "Invalid key", HttpStatus.BAD_REQUEST)
    ;

    private int code;
    private String message;
    private HttpStatusCode httpStatusCode;

    ErrorCode(int code, String message, HttpStatusCode httpStatusCode) {
        this.code = code;
        this.message = message;
        this.httpStatusCode = httpStatusCode;
    }

}
