package com_zcx_chant_common.config;


import com_zcx_chant_common.BaseException;
import com_zcx_chant_common.pojo.Response;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 统一异常处理
 */
@RestControllerAdvice
public class UnifiedExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Response methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        // 从异常对象中拿到ObjectError对象
        ObjectError objectError = e.getBindingResult().getAllErrors().get(0);
        // 然后提取错误提示信息进行返回
        return Response.error(5000, objectError.getDefaultMessage());
    }

    /**
     * 自定义异常
     * @param e
     * @return
     */
    @ExceptionHandler(BaseException.class)
    public Response baseExceptionHandler(BaseException e){
        return Response.error(5000, "请求异常");
    }

    /**
     *
     * @param e
     * @return
     */
    public Response exceptionHandler(Exception e) {
        return Response.error(5000, "系统异常");
    }
}
