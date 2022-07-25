package com_zcx_chant_crud.module.account;

import com_zcx_chant_crud.module.account.request.AccountRequest;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


/**
 * 账号接口，注册、登录、登出、找回密码
 */
@RestController
@RequestMapping("/account")
public class AccountController {


    @PostMapping("login")
    public Object login(@RequestBody @Valid AccountRequest accountRequest) {
        return new AccountEntity();
    }
}
