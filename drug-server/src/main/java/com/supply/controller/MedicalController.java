package com.supply.controller;

import com.supply.result.Result;
import com.supply.service.MedicalService;
import com.supply.vo.SupplyInformationVO;
import com.supply.vo.UserInformationVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/medical")
@Tag(name = "医护端部分接口")
@Slf4j
@RequiredArgsConstructor
public class MedicalController {

    private final MedicalService medicalService;

    @GetMapping("/information")
    @Operation(summary = "个人信息回显接口")
    public Result<UserInformationVO> getInformation() {
        UserInformationVO userInformationVO = medicalService.getInformation();
        return Result.success(userInformationVO);
    }

    @GetMapping("/supply/search")
    @Operation(summary = "搜索供应商信息接口")
    public Result<SupplyInformationVO>  searchSupplyInformation(){


    }

    



}
