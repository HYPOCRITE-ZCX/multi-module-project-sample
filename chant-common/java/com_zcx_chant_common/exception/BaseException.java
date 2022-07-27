package com_zcx_chant_common.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseException extends RuntimeException{

    /**
     * 错误码
     */
    private Integer code;

    /**
     * 错误信息
     */
    private String message;

}
