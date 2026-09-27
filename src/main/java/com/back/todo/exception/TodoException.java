package com.back.todo.exception;

import lombok.Getter;

@Getter
public class TodoException extends RuntimeException {

    private final String resultCode;
    private final String msg;

    public TodoException(String resultCode, String msg) {
        super(resultCode + " : " + msg);
        this.resultCode = resultCode;
        this.msg = msg;
    }
}
