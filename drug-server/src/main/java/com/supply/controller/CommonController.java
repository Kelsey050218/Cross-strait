package com.supply.controller;

import com.supply.constant.MessageConstant;
import com.supply.result.Result;
import com.supply.utils.AliOssUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;

@RestController
@RequestMapping("/user")
@Tag(name = "文件上传")
@Slf4j
@RequiredArgsConstructor
public class CommonController {

    private final AliOssUtil aliOssUtil;

    @PostMapping("/upload")
    @Operation(summary = "文件上传接口")
    public Result<String> upload(MultipartFile file){
        log.info("文件上传：{}",file);
        try {
            String upload = aliOssUtil.upload(file);
            return Result.success(upload);
        } catch (IOException e) {
            log.error("文件上传失败:{}",e);
            e.printStackTrace();
        }
        return Result.error(MessageConstant.UPLOAD_FAILED);
    }
}
