package com.supply.controller;

import com.supply.service.SupplyService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/supply")
@Tag(name = "供应端部分接口")
@Slf4j
@RequiredArgsConstructor
public class SupplyController {

    private final SupplyService supplyService;


}
