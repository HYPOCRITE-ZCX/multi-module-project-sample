package com_zcx_chant_common.utils;

import javax.servlet.http.HttpServletRequest;

public class RequestUtil {

    /**
     * 获取真实ip地址,不返回内网地址
     *
     * @param request 请求体对象
     * @return 真实ip地址的字符串
     */
    public static String getRealIp(HttpServletRequest request) {
        //目前则是网关ip
        String ip = request.getHeader("X-Real-IP");
        if (ip != null && !"".equals(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }
        ip = request.getHeader("X-Forwarded-For");
        if (ip != null && !"".equals(ip) && !"unknown".equalsIgnoreCase(ip)) {
            int index = ip.indexOf(',');
            if (index != -1) {
                //只获取第一个值
                return ip.substring(0, index);
            } else {
                return ip;
            }
        } else {
            //取不到真实ip则返回空，不能返回内网地址。
            return "";
        }
    }

}
