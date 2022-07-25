package com_zcx_chant_crud.config;

public class SessionLocal {

    /**
     * 保存每个请求中携带的用户信息
     */
    private static final ThreadLocal<Object> LOCAL = new ThreadLocal<>();

    public static Object getObject() {
        return LOCAL.get();
    }

    public static void setObject(Object obj) {
        LOCAL.set(obj);
    }

    public static void clear(){
        LOCAL.remove();
    }
}
