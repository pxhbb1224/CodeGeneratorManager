package com.cmbchina.code_generator.model;

import lombok.Data;

@Data
public class Result {
    int code;

    Object data;

    String message; // 当 code = ResultCode

    /**
     * 隐藏
     */
    private Result() {

    }

    /**
     * 不允许外部自定义 Result 的 样式
     */
    private Result(int code, Object data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public static Result build(ResultCode code, Object data, String message) {
        return new Result(code.getCode(), data, message);
    }

    public static Result success() {
        return Result.build(ResultCode.Success, null, null);
    }

    public static Result success(Object data) {
        return Result.build(ResultCode.Success, data, null);
    }

    public static Result fail() {
        return Result.build(ResultCode.Fail, null, null);
    }

    public static Result fail(String errorMsg) {
        return Result.build(ResultCode.Fail, null, errorMsg);
    }

    public static Result fail(Exception exception) {
        return Result.build(ResultCode.Fail, null, exception.getMessage());
    }
}
