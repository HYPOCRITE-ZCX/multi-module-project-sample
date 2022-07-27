package com_zcx_chant_common.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.text.SimpleDateFormat;
import java.util.List;

@Configuration
public class CommonMvcConfig implements WebMvcConfigurer {

    /**
     * 解决跨域问题
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*") //放行哪些原始域
                .allowedMethods("PUT", "DELETE", "POST", "GET") //放行哪些请求方式
                .allowCredentials(false) //是否发送 Cookie
                .maxAge(3600);
    }


    /**
     * 配置jackson数据转换
     * @param converters
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {

        converters.removeIf(converter -> converter instanceof MappingJackson2HttpMessageConverter);

        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();

        ObjectMapper objectMapper = new Jackson2ObjectMapperBuilder()
                .deserializers()
                .serializationInclusion(JsonInclude.Include.NON_NULL) //序列化时过滤掉null值字段
                .serializationInclusion(JsonInclude.Include.NON_EMPTY) //序列化时过滤掉空值字段
                .dateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")) //格式化日期
                .build();

        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); //json串转对象时忽略掉对象中不存在的字段

        // 序列化时将long类型的数据转为String类型
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
        simpleModule.addSerializer(long.class, ToStringSerializer.instance);
        objectMapper.registerModule(simpleModule);

        converter.setObjectMapper(objectMapper);

        converters.add(converter);
    }


    /**
     * 配置下划线转驼峰的配置
     * 前端传参时使用下划线风格，后端使用驼峰风格
     * 后端响应时将驼峰字段再转为下划线风格
     * @param resolvers
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new UnderlineToHumpArgumentResolver());
    }


}
