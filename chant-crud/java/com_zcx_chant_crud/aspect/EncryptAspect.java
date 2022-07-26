package com_zcx_chant_crud.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class EncryptAspect {


    /**
     * Encrypt 作用于所有使用了该注解得方法
     */
    @Pointcut("@annotation(com_zcx_chant_crud.annotation.Encrypt)")
    public void testAdvice(){}

    @Around("testAdvice()")
    public Object Interceptor(ProceedingJoinPoint point) {


        return null;
    }


}
