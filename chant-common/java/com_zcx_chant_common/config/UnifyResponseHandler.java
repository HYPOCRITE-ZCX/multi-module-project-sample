package com_zcx_chant_common.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com_zcx_chant_common.exception.BaseException;
import com_zcx_chant_common.pojo.Response;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.reflect.Type;

/**
 * 统一响应处理
 */
@RestControllerAdvice
public class UnifyResponseHandler implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        // 如果接口返回的类型本身就是 Response 那就没有必要进行额外的操作，返回false
        return !(methodParameter.getParameterType().equals(Response.class));
    }

    @Override
    public Object beforeBodyWrite(Object data, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        // String类型不能直接包装，所以要进行些特别的处理
        if (methodParameter.getGenericParameterType().equals(String.class)) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                // 将数据包装在Response里后，再转换为json字符串响应给前端
                return objectMapper.writeValueAsString(new Response<>(data));
            } catch (JsonProcessingException e) {
                throw new BaseException(4000, "返回String类型错误");
            }
        }
        // 将原本的数据包装在Response里
        return Response.success(data);
    }
}
