package com_zcx_chant_crud.exception;

import com_zcx_chant_common.exception.BaseException;

public class SampleException extends BaseException {

    public SampleException(){
        super(5001, "xxx异常");
    }

    public SampleException(Integer code, String message) {
        super(code, message);
    }
}
