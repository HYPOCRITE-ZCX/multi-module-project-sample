package com_zcx_chant_crud.config;

import com_zcx_chant_common.utils.JsonUtil;
import org.springframework.messaging.handler.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class InterceptorConfig implements WebMvcConfigurer {

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
     * 一下路径不走拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SessionInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/account/login",
                        "/account/register",
                        "/account/retrieve"
                );
    }

    class SessionInterceptor implements HandlerInterceptor {

        /**
         * 将每次请求头携带的用户信息保存，这样就不用在各个接口中在传用户相关的信息了，比用用户id
         *
         * @param request 请求对象
         * @param response 响应对象
         * @param handler
         * @return
         * @throws Exception
         */
        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
            if (!(handler instanceof HandlerMethod)){
                return true;
            }
            String userInfo = request.getHeader("userInfo");
            Object obj = JsonUtil.toObject(userInfo, Object.class);
            SessionLocal.setObject(obj);
            return true;
        }


        @Override
        public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

        }

        /**
         * 因为tomcat是线程复用的，所以当每个请求处理完之后，就移除掉该数据
         *
         * @param request
         * @param response
         * @param handler
         * @param ex
         * @throws Exception
         */
        @Override
        public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
            SessionLocal.clear();
        }
    }

}
