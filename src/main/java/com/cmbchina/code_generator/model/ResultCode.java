package com.cmbchina.code_generator.model;

import lombok.Getter;

public enum ResultCode {

    /** 成功 */
    Success(200, "success"),

    /** 失败 */
    Fail(201, "fail")
    ;


    @Getter
    int code;

    String status;

    ResultCode(int code, String status) {
        this.code = code;
        this.status = status;
    }
}
