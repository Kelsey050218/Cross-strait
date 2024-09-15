package com.supply.controller;

import com.supply.dto.DrugInputDTO;
import com.supply.dto.RequestDTO;
import com.supply.dto.UserInformationDTO;
import com.supply.result.Result;
import com.supply.service.MedicalService;
import com.supply.vo.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medical")
@Tag(name = "医护端部分接口")
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
    @Operation(summary = "搜素供应商信息接口")
    public Result<List<SupplyVO>> searchSupplyInformation(@RequestParam String firmName,@RequestParam String drugName){
        List<SupplyVO> list = medicalService.searchSupplyInformation(firmName,drugName);
        return Result.success(list);
    }

    @GetMapping("/request")
    @Operation(summary = "药品请求信息查询接口")
    public Result<List<RequestVO>> getDrugRequestInformation() {
        List<RequestVO> list = medicalService.getDrugRequestInformation();
        return Result.success(list);
    }

    @GetMapping("/shortage")
    @Operation(summary = "药品不足提示接口")
    public Result<List<DrugShortageVO>> shortageWarning(){
        List<DrugShortageVO> list = medicalService.shortageWarning();
        return Result.success(list);
    }

    @GetMapping("/statistic")
    @Operation(summary = "药品出入量统计接口")
    public Result<List<DrugStatisticVO>> drugStatistic(){
        List<DrugStatisticVO> list = medicalService.drugStatistic();
        return Result.success(list);
    }

    @GetMapping("/drugs/search")
    @Operation(summary = "搜索药品信息接口")
    public Result<DrugInformationVO> searchDrugInformation(@RequestParam String drugName){
        DrugInformationVO drugInformationVO = medicalService.searchDrugInformation(drugName);
        return Result.success(drugInformationVO);
    }

    @PutMapping("/drugs/output/{id}")
    @Operation(summary = "药品出库接口")
    public Result<Object> drugsOutput(@PathVariable Long id){
        medicalService.output(id);
        return Result.success();
    }

//    @PutMapping("/drugs/input/{id}")
//    @Operation(summary = "药品入库接口")
//    public Result<Object> drugsOutput(@RequestBody DrugInputDTO drugInputDTO){
//        medicalService.input(drugInputDTO);
//        return Result.success();
//    }

    @PostMapping("/request/send")
    @Operation(summary = "发送药品请求接口")
    public Result<Object> sendRequest(@RequestBody RequestDTO requestDTO){
        medicalService.sendRequset(requestDTO);
        return Result.success();
    }

    @GetMapping("/drugs")
    @Operation(summary = "药品信息查询接口")
    public Result<List<DrugInformationVO>> getDrugsInformation() {
        List<DrugInformationVO> list = medicalService.getDrugsInformation();
        return Result.success(list);
    }







    
}
