package com_zcx_chant_common.pojo;

import lombok.Data;
import java.io.Serializable;


@Data
public class Response<T> implements Serializable {

    /**
     * 响应码
     */
    private Integer code;

    /**
     * 响应信息
     */
    private String message;

    /**
     * 响应数据对象
     */
    private T data;

    public Response() {
    }

    public Response(T data) {
        this.code = 2000;
        this.message = "请求成功";
        this.data = data;
    }

    public Response(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public static <T> Response success(T data) {
        return new Response<>(data);
    }

    public static Response error(Integer code, String message) {
        return new Response<>(code, message);
    }
}
