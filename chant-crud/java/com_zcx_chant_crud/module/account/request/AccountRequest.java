package com_zcx_chant_crud.module.account.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class AccountRequest {

    @NotNull(message = "用户名不能为空")
    private String username;

    @NotNull(message = "密码不能为空")
    private String password;

    @NotNull(message = "邮箱不能为空")
    @Email(regexp = "", message = "邮箱格式不正确")
    private String email;

}
