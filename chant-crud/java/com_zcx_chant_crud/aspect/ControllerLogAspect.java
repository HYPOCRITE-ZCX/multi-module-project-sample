package com_zcx_chant_crud.aspect;


import com_zcx_chant_common.utils.JsonUtil;
import com_zcx_chant_common.utils.RequestUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller 层记录入参和出参得日志
 */
@Component
@Aspect
public class ControllerLogAspect {

    private static final Logger logger = LoggerFactory.getLogger(ControllerLogAspect.class);

    /**
     * 解析：
     * public： 所有用 public 修饰的方法
     * 第一个 * ：代表返回类型，* 代表任意类型
     * com_zcx_chant_crud.module 包
     * com_zcx_chant_crud.module 后面的两个点(..) 代表当前包和包下的所有子包
     * *Controller 代表所哟以Controller结尾的类
     * *(..)，* 表示类中的所有方法，(..) 表示任意参数
     *
     * 总结，就是 com_zcx_chant_crud.module下一级子包下的所有以Controller结尾的类中所有的public方法
     */
    @Pointcut("execution(public * com_zcx_chant_crud.module..*Controller.*(..))")
    public void logAdvice(){}

    @Around("logAdvice()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        // 执行控制器目标方法获取返回结果
        Object result = proceedingJoinPoint.proceed();

        Map<String, Object> requestInfo = getRequestInfo(proceedingJoinPoint);

        requestInfo.put("result", result);

        requestInfo.put("timeCost", System.currentTimeMillis() - start);

        logger.info("Request Info: {}", JsonUtil.toJson(requestInfo));

        return result;
    }

    @AfterThrowing(pointcut = "logAdvice()", throwing = "exception")
    public void doAfterThrow(JoinPoint joinPoint, RuntimeException exception) {

        Map<String, Object> requestInfo = getRequestInfo(joinPoint);

        requestInfo.put("exception", exception.getMessage());

        logger.info("Error Request Info: {}", JsonUtil.toJson(requestInfo));

    }

    /**
     * 获取基本的请求信息
     * @param joinPoint 连接点
     * @return map
     */
    private Map<String, Object> getRequestInfo(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        Map<String, Object> requestInfoMap = new HashMap<>();

        if (null != attributes) {
            HttpServletRequest request = attributes.getRequest();
            requestInfoMap.put("ip", RequestUtil.getRealIp(request));
            requestInfoMap.put("url", request.getRequestURL().toString());
            requestInfoMap.put("method", request.getMethod());
        }
        requestInfoMap.put("method", String.format("%s.%s", joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName()));
        requestInfoMap.put("params", getRequestParamsByJoinPoint(joinPoint));

        return requestInfoMap;
    }


    //获取入参
    private Map<String, Object> getRequestParamsByJoinPoint(JoinPoint joinPoint) {
        //参数名
        String[] paramNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
        //参数值
        Object[] paramValues = joinPoint.getArgs();

        Map<String, Object> requestParamsMap = new HashMap<>();
        for (int i = 0; i < paramNames.length; i++) {
            Object value = paramValues[i];

            //如果是文件对象
            if (value instanceof MultipartFile) {
                MultipartFile file = (MultipartFile) value;
                value = file.getOriginalFilename();  //获取文件名
            } else if (value instanceof MultipartFile[]) {
                MultipartFile[] files = (MultipartFile[]) value;
                StringBuilder fileNames = new StringBuilder();
                for (MultipartFile file : files) {
                    fileNames.append(file.getOriginalFilename()).append(";");
                }
                value = fileNames.toString();
            } else if (value instanceof BindingResult || "response".equals(paramNames[i]) || "request".equals(paramNames[i])) {
                continue;
            }

            requestParamsMap.put(paramNames[i], value);
        }
        return requestParamsMap;
    }

}
