package com.supply.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DrugInputDTO implements Serializable {

    // 药品追溯码
    private String drugCode;

    // 药品名称
    private String drugName;

    // 药品供应商名称
    private String firmName;

    // 药品图片
    private String drugImage;

}
