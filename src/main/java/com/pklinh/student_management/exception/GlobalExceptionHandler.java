package com.pklinh.student_management.exception;

import com.pklinh.student_management.dto.response.ApiResponse;
import jakarta.validation.ConstraintViolation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;
import java.util.Objects;


@ControllerAdvice // Khai báo class này để spring biết là khi có exception xảy ra thì class này sẽ chịu trách nhiệm
public class GlobalExceptionHandler {
    @ExceptionHandler(value = RuntimeException.class)  // Khai báo class exception muốn bắt
    ResponseEntity<String> handlingRuntimeException(RuntimeException exception){
        return ResponseEntity.badRequest().body(exception.getMessage());  // nội dung mà chúng ta muốn trả về cho người dùng
    }
//    @ExceptionHandler(value = MethodArgumentNotValidException.class)
//    ResponseEntity<String> handlingMethodArgymentNotValidException(MethodArgumentNotValidException e){
//        return ResponseEntity.badRequest().body(e.getFieldError().getDefaultMessage());
//    }

//    Buổi 4: exception tối ưu
//    Bắt lỗi nghiệp vụ
    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handlingAppException(AppException e){
        ErrorCode errorCode = e.getErrorCode();
        ApiResponse apiResponse = ApiResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();
        return ResponseEntity.status(errorCode.getHttpStatusCode()).body(apiResponse);
    }
// Bắt lỗi validate trong các dto
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handlingMethodArgymentNotValidException(MethodArgumentNotValidException e){
//        Vì trong dto đã dùng enumKey thay cho message valid nên bây giờ cần lấy nó ra để xem là enum nào
        String enumKey = e.getFieldError().getDefaultMessage();
        ErrorCode errorCode = ErrorCode.INVALID_KEY;
        Map<String, Object> attributes = null;

        // Lấy errorcode từ enumkey
        try{
            errorCode = ErrorCode.valueOf(enumKey);

            // Trích xuất thông tin từ validate để thông báo chuẩn xác và dễ hơn
            var constraintViolation = e.getBindingResult()
                    .getAllErrors()
                    .getFirst()
                    .unwrap(ConstraintViolation.class);

            attributes = constraintViolation.getConstraintDescriptor().getAttributes();

        }
        catch(IllegalArgumentException e1){
        }
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(Objects.nonNull(attributes) ?
                mapAttribute(errorCode.getMessage(), attributes)
                : errorCode.getMessage());

        return ResponseEntity
                .status(errorCode.getHttpStatusCode())
                .body(apiResponse);
    }
    private static final String MIN_ATTRIBUTE = "min";

    private String mapAttribute(String message, Map<String, Object> attributes){
        String minValue = String.valueOf(attributes.get(MIN_ATTRIBUTE));
        return message.replace(MIN_ATTRIBUTE, minValue);
    }

//    Bắt lỗi access dinied
    @ExceptionHandler(value = org.springframework.security.access.AccessDeniedException.class)
    ResponseEntity<ApiResponse> handlingAccessDeniedException(AccessDeniedException e){
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;
        ApiResponse apiResponse = ApiResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();
        return ResponseEntity.status(errorCode.getHttpStatusCode()).body(apiResponse);
    }
}
