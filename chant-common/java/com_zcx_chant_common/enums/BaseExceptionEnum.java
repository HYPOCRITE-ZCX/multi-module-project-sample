package com_zcx_chant_common.enums;

import com_zcx_chant_common.exception.BaseException;

public enum BaseExceptionEnum {

    LOGIN_INFORMATION_OUTDATED(5002, "登录信息过期，请从新登录");

    /**
     * 错误码
     */
    public final Integer code;

    /**
     * 错误信息
     */
    public final String message;

    BaseExceptionEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public BaseException newInstance() {
        return new BaseException(this.code, this.message);
    }

    /**
     * 如果需要在异常消息中添加程序中变量值，那么就不方便定义枚举，可手动拼接异常信息传入
     *
     * @param code    错误码
     * @param message 错误信息
     * @return 业务异常实例
     */
    public static BaseException from(Integer code, String message) {
        return new BaseException(code, message);
    }
}
