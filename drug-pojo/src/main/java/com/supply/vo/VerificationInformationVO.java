package com.supply.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VerificationInformationVO implements Serializable {

    private Long id; //验证id

    private String username; //用户名

    private String firmName; //公司名

    private String applicationTime; //注册时间

    private List<String> images; //头像


}
