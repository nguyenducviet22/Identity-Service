package com.ndv.identity_service.exception;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
public enum ErrorCode {

    USERNAME_EXISTED(101, "Username existed!"),
    USER_NOT_EXISTED(102, "User is not existed!")
    ;

    @Getter
    private int code;
    @Getter
    private String message;
}
