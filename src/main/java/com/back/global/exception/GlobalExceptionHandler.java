package com.back.global.exception;

import com.back.global.rsdata.RsData;
import com.back.todo.exception.TodoException;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TodoException.class)
    public ResponseEntity<RsData<Void>> handleTodoException(TodoException exception) {
        RsData<Void> response = RsData.of(
                exception.getResultCode(),
                exception.getMsg(),
                null
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RsData<Map<String, String>>> handleValidation(
            MethodArgumentNotValidException exception
    ) {
        Map<String, String> errors = new LinkedHashMap<>();

        exception.getBindingResult()
                .getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        RsData<Map<String, String>> response = RsData.of(
                "400-1",
                "입력값이 올바르지 않습니다.",
                errors
        );

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler({
            HttpMessageNotReadableException.class,
            MethodArgumentTypeMismatchException.class
    })
    public ResponseEntity<RsData<Void>> handleBadRequest(Exception exception) {
        RsData<Void> response = RsData.of(
                "400-2",
                "요청 형식이 올바르지 않습니다.",
                null
        );

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<RsData<Void>> handleException(Exception exception) {
        RsData<Void> response = RsData.of(
                "500-1",
                "서버 내부 오류가 발생했습니다.",
                null
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
