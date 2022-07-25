package com_zcx_chant_crud;

import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

// 使用的是在每个 repository 接口添加 @Mapper 注解的方式，暂时不用 @MapperScan 的方式
// @MapperScan("root.module.*.repository")
@SpringBootApplication
public class SampleApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(SampleApplication.class)
                .sources(SampleApplication.class)
                .bannerMode(Banner.Mode.OFF) //关闭banner
                .run(args);
    }

}
