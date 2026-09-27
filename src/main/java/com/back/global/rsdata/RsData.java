package com.back.global.rsdata;

public record RsData<T>(String resultCode, String msg, T data) {

    public static <T> RsData<T> of(String resultCode, String msg, T data) {
        return new RsData<>(resultCode, msg, data);
    }

    public static <T> RsData<T> success(String msg, T data) {
        return new RsData<>("200-1", msg, data);
    }
}
