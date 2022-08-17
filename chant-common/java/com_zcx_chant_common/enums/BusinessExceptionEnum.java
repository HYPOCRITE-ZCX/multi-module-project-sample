package com_zcx_chant_common.enums;

import com_zcx_chant_common.exception.BusinessException;

public enum BusinessExceptionEnum {


    ACCOUNT_OR_PASSWORD_ERROR(5001, "账号或密码错误"),

    LOGIN_INFORMATION_OUTDATED(5002, "登录信息过期，请从新登录");


    /**
     * 错误码
     */
    public final Integer code;

    /**
     * 错误信息
     */
    public final String message;

    BusinessExceptionEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public BusinessException newInstance() {
        BusinessException businessException = new BusinessException();
        businessException.setCode(this.code);
        businessException.setMessage(this.message);
        return businessException;
    }

    /**
     * 如果需要在异常消息中添加程序中变量值，那么就不方便定义枚举，可手动拼接异常信息传入
     *
     * @param code    错误码
     * @param message 错误信息
     * @return 业务异常实例
     */
    public static BusinessException from(Integer code, String message) {
        BusinessException businessException = new BusinessException();
        businessException.setCode(code);
        businessException.setMessage(message);
        return businessException;
    }

}
