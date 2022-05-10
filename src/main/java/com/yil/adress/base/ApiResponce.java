package com.yil.adress.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponce<T> {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime timestamp;
    private Integer status;
    private String message;
    private T data;

    public static <T> ApiResponce<T> ok() {
        return status(ApiStatus.Ok);
    }

    public static <T> ApiResponce<T> ok(T data) {
        return status(data, ApiStatus.Ok);
    }

    public static <T> ApiResponce<T> status(ApiStatus status) {
        return status(status.getCode(), status.getMessage());
    }

    public static <T> ApiResponce<T> status(Integer code, String message) {
        return new ApiResponceBuilder<T>()
                .timestamp(LocalDateTime.now())
                .status(code)
                .message(message)
                .build();
    }

    public static <T> ApiResponce<T> status(T data, ApiStatus status) {
        return status(data, status.getCode(), status.getMessage());
    }

    public static <T> ApiResponce<T> status(T data, Integer code, String message) {
        return new ApiResponceBuilder<T>()
                .timestamp(LocalDateTime.now())
                .data(data)
                .status(code)
                .message(message)
                .build();
    }


}
