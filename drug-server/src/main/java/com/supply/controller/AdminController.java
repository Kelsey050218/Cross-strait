package com.supply.controller;

import com.supply.result.Result;
import com.supply.service.AdminService;
import com.supply.vo.ReportInformationVO;
import com.supply.vo.UserInformationVO;
import com.supply.vo.VerificationInformationVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@Tag(name = "管理员部分接口")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/information")
    @Operation(summary = "个人信息回显接口")
    public Result<UserInformationVO> getInformation() {
        UserInformationVO userInformationVO = adminService.getInformation();
        return Result.success(userInformationVO);
    }

    @GetMapping("/verify/{type}")
    @Operation(summary = "认证信息查询接口")
    public Result<List<VerificationInformationVO>> getVerificationInformation(@PathVariable Long type) {
        List<VerificationInformationVO> list = adminService.getVerificationInformation(type);
        return Result.success(list);
    }

    @PostMapping("/verify/{id}")
    @Operation(summary = "信息认证审核接口")
    public Result<Object> checkVerificationInformation(@PathVariable Long id, @RequestParam @Min(1) @Max(2) Long isAgree) {
        adminService.checkVerificationInformation(id, isAgree);
        return Result.success();
    }

    @GetMapping("/inform")
    @Operation(summary = "举报信息查询接口")
    public Result<List<ReportInformationVO>> getReportInformation() {
        List<ReportInformationVO> list = adminService.getReportInformation();
        return Result.success(list);
    }

    @PutMapping("/inform/{id}")
    @Operation(summary = "举报信息处理接口")
    public Result<Object> dealReport(@PathVariable Long id, @Min(1) @Max(2) Integer isIllegal) {
        adminService.dealReport(id, isIllegal);
        return Result.success();
    }
}
