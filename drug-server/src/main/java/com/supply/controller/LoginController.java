package com.supply.controller;

import com.supply.constant.MessageConstant;
import com.supply.dto.UserInformationDTO;
import com.supply.dto.UserLoginDTO;
import com.supply.result.Result;
import com.supply.service.LoginService;
import com.supply.vo.UserLoginVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Tag(name = "登录部分接口")
@Slf4j
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/register")
    @Operation(summary = "用户注册接口")
    public Result<Object> register(@RequestBody UserInformationDTO userInformationDTO) {
        loginService.register(userInformationDTO);
        return Result.success();
    }

    @PostMapping("/verifyCode/send")
    @Operation(summary = "验证码发送接口")
    public Result<Object> sendCode(@RequestParam String email) {
        loginService.sendCode(email);
        return Result.success();
    }

    @PutMapping("/resetPassword")
    @Operation(summary = "重置密码接口")
    public Result<Object> resetPassword(@RequestBody UserInformationDTO userInformationDTO) {
        loginService.resetPassword(userInformationDTO);
        return Result.success();
    }

    @PostMapping("/login")
    @Operation(summary = "用户登录接口")
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO) {
        UserLoginVO userLoginVO = loginService.login(userLoginDTO);
        if(userLoginVO != null){
            return Result.success(userLoginVO);
        }
        else {
            return Result.error(MessageConstant.INFORMATION_ERROR);
        }
    }
}
