package com_zcx_chant_common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {

    private JsonUtil() {
    }

    private static class InnerClass {
        private static final ObjectMapper INSTANCE = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private static ObjectMapper getInstance() {
        return InnerClass.INSTANCE;
    }


    /**
     * 将对象转换为json字符串
     *
     * @param obj 要转为json的对象
     * @return json串
     */
    public static String toJson(Object obj) {
        String str = null;
        try {
            str = JsonUtil.getInstance().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 可根据传入的类型返回相应的类型
     *
     * @param str json字符串
     * @param c   目标对象的class对象
     * @param <T> 泛型对象
     * @return 目标对象
     */
    public static <T> T toObject(String str, Class<T> c) {
        T obj = null;
        try {
            obj = new ObjectMapper().readValue(str, c);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public static <T> T convert(Object object, Class<T> c) {
        T obj;
        obj = new ObjectMapper().convertValue(object, c);
        return obj;
    }

}
